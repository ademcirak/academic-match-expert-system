package academic.match.analyzer;

import academic.match.analyzer.scoring.AMCustomScoreQuery;
import academic.match.analyzer.tokenizier.KeywordAnalyzer;
import academic.match.models.Paper;
import academic.match.models.Person;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

final public class Lucene {

    Directory dir;
    IndexWriter writer;
    IndexReader reader;
    IndexSearcher searcher = null;
    Analyzer analyzer;
    static Lucene instance;


    Lucene(Directory dir) throws Exception {
        this.dir = dir;
        Map<String, Analyzer> analyzerMap = new HashMap<String, Analyzer>();
        analyzerMap.put(FieldConstants.KEYWORDS, new KeywordAnalyzer());
        analyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerMap);
        IndexWriterConfig config = new IndexWriterConfig(analyzer).setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

        this.writer = new IndexWriter(dir, config);
        this.reader = DirectoryReader.open(writer);
    }



    public static Lucene build() throws Exception {
        instance = new Lucene(FSDirectory.open(new File(LuceneConstants.FILE_PATH).toPath()));
        return instance;
    }

    public static Lucene tempBuild() throws Exception {
        instance = new Lucene(new RAMDirectory());
        return instance;
    }

    public void closeWriter() throws IOException {
        if(instance!=null) {
            writer.commit();
            writer.close();
        }
    }

    protected Document personToDocument(Person person) {
        Document doc = new Document();

        String keywords = "";
        if(person.keywords!=null && !person.keywords.isEmpty())
            keywords = person.keywords.stream()
                .collect(Collectors.joining(","));

        String titles = person.papers.stream()
                .map(new Function<Paper, String>() {
                    @Override
                    public String apply(Paper paper) {
                        return paper.title;
                    }
                })
                .collect(Collectors.joining("\n"));

        String abstracts = person.papers.stream()
                .map(new Function<Paper, String>() {
                    @Override
                    public String apply(Paper paper) {
                        return paper.abstractText;
                    }
                })
                .collect(Collectors.joining("\n"));

        String paperKeywords = person.papers.stream()
                .map(new Function<Paper, String>() {
                    @Override
                    public String apply(Paper paper) {
                        return paper.keywords.stream().collect(Collectors.joining(","));
                    }
                })
                .collect(Collectors.joining(","));

        keywords = keywords + (paperKeywords.length() > 0 ? ("," + paperKeywords) : "");

        TextField keywordField = new TextField(FieldConstants.KEYWORDS, keywords, Field.Store.YES);
        TextField titleField = new TextField(FieldConstants.TITLES, titles, Field.Store.YES);

        doc.add(new TextField(FieldConstants.ID, person.id.toString(), Field.Store.YES));
        doc.add(new TextField(FieldConstants.NAME, person.name, Field.Store.YES));
        doc.add(new TextField(FieldConstants.SURNAME, person.surname, Field.Store.YES));
        doc.add(keywordField);
        doc.add(titleField);
        doc.add(new TextField(FieldConstants.ABSTRACTS, abstracts, Field.Store.YES));
        doc.add(new TextField(FieldConstants.SCOPUS_ID, person.scopusAuthorId, Field.Store.YES));

        doc.add(new StoredField(FieldConstants.ACCEPT_RATE, person.acceptRate));
        doc.add(new StoredField(FieldConstants.AVAILABILITY, person.availability));
        doc.add(new StoredField(FieldConstants.ACCURACY, person.accuracy));

        // doc.add(new DoubleDocValuesField(FieldConstants.AVAILABILITY, person.availability));
        // doc.add(new DoubleDocValuesField(FieldConstants.ACCURACY, person.accuracy));
        return doc;
    }

    public void indexPerson(Person person) throws Exception {
        System.out.println("new person indexing: " + person);
        Document doc = this.personToDocument(person);
        this.writer.addDocument(doc);
        writer.commit();
        System.out.println("new person indexed");
    }

    public boolean isPersonIndexed(Person person) throws IOException {
        // init reader if not exist
        if(searcher==null)
            searcher = new IndexSearcher(reader);

        TopDocs docs = searcher.search(new TermQuery(new Term(FieldConstants.SCOPUS_ID, person.scopusAuthorId)), 1);

        if(docs == null)
            return false;

        return  docs.totalHits > 0;
    }

    public List<Person> search(Paper paper) throws Exception {

        // init reader if not exist
        if(searcher==null) {
            searcher = new IndexSearcher(reader);
        }
        String keywords = "";
        if(paper.keywords!=null && !paper.keywords.isEmpty())
            keywords = paper.keywords.stream()
                .collect(Collectors.joining(" OR "));

        Query titleTq = new QueryParser(FieldConstants.TITLES, analyzer).parse(paper.title);
        Query abstractTq = new QueryParser(FieldConstants.ABSTRACTS, analyzer).parse(paper.abstractText);
        Query keywordsTq = new QueryParser(FieldConstants.KEYWORDS, analyzer).parse(keywords);
        // Query keywordsTq = new TermQuery(new Term(FieldConstants.KEYWORDS, keywords));

        Query boostedtitleQuery = new BoostQuery(titleTq, 2);
        Query boostedkeywordsQuery = new BoostQuery(keywordsTq, 2);

        Query availability = DoublePoint.newRangeQuery(FieldConstants.AVAILABILITY, 1, Integer.MAX_VALUE);
        Query acceptRate = DoublePoint.newRangeQuery(FieldConstants.ACCEPT_RATE, 1, Integer.MAX_VALUE);
        Query accuracy = DoublePoint.newRangeQuery(FieldConstants.ACCURACY, 1, Integer.MAX_VALUE);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        Query query = builder
                .add(boostedtitleQuery, BooleanClause.Occur.SHOULD)
                .add(abstractTq, BooleanClause.Occur.SHOULD)
                .add(boostedkeywordsQuery, BooleanClause.Occur.SHOULD)
                // .add(availability, BooleanClause.Occur.FILTER)
                // .add(acceptRate, BooleanClause.Occur.FILTER)
                // .add(accuracy, BooleanClause.Occur.FILTER)
                .build();

        CustomScoreQuery scoredQuery = new AMCustomScoreQuery(query);

        TopDocs hits = this.searcher.search(scoredQuery, LuceneConstants.MAX_SEARCH);
        System.out.println(hits.totalHits + " docs found for the query \"" + query.toString() + "\"");

        ArrayList<Person> results = new ArrayList<>(LuceneConstants.MAX_SEARCH);

        for (ScoreDoc sd : hits.scoreDocs) {
            Document d = searcher.doc(sd.doc);
            Person p = new Person();
            p.id = Integer.parseInt(d.get(FieldConstants.ID));
            p.name = d.get(FieldConstants.NAME);
            p.surname = d.get(FieldConstants.SURNAME);
            p.scopusAuthorId = d.get(FieldConstants.SCOPUS_ID);
            p.acceptRate = d.getField(FieldConstants.ACCEPT_RATE).numericValue().doubleValue();
            p.availability = d.getField(FieldConstants.AVAILABILITY).numericValue().doubleValue();
            p.accuracy = d.getField(FieldConstants.ACCURACY).numericValue().doubleValue();
            p.score = sd.score;
            results.add(p);
        }
        return results;
    }



}
