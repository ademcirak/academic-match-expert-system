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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

final public class Lucene {

    Directory dir;
    IndexWriter writer;
    IndexReader reader;
    IndexSearcher searcher;
    Analyzer analyzer;


    Lucene() throws Exception {
        this.dir = FSDirectory.open(new File(LuceneConstants.FILE_PATH).toPath());


        Map<String, Analyzer> analyzerMap = new HashMap<String, Analyzer>();
        analyzerMap.put(FieldConstants.KEYWORDS, new KeywordAnalyzer());
        analyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerMap);
        IndexWriterConfig config = new IndexWriterConfig(analyzer).setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

        this.writer = new IndexWriter(dir, config);
        this.reader = DirectoryReader.open(writer);
        this.searcher = new IndexSearcher(reader);
    }

    public static Lucene build() throws Exception {
        return new Lucene();
    }

    protected Document personToDocument(Person person) {
        Document doc = new Document();

        String keywords = person.keywords.stream()
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

        doc.add(new TextField(FieldConstants.ID, person.id.toString(), Field.Store.YES));
        doc.add(new TextField(FieldConstants.NAME, person.name, Field.Store.YES));
        doc.add(new TextField(FieldConstants.SURNAME, person.surname, Field.Store.YES));
        doc.add(keywordField);
        doc.add(new TextField(FieldConstants.TITLES, titles, Field.Store.YES));
        doc.add(new TextField(FieldConstants.ABSTRACTS, abstracts, Field.Store.YES));
        doc.add(new DoublePoint(FieldConstants.ACCEPT_RATE, person.acceptRate));
        doc.add(new DoublePoint(FieldConstants.AVAILABILITY, person.availability));
        doc.add(new DoublePoint(FieldConstants.ACCURACY, person.accuracy));
        return doc;
    }

    public void indexPerson(Person person) throws Exception {
        Document doc = this.personToDocument(person);
        this.writer.addDocument(doc);
        writer.commit();
    }

    public List<Person> search(Paper paper) throws Exception {

        String keywords = paper.keywords.stream()
                .collect(Collectors.joining(" OR "));

        Query titleTq = new QueryParser(FieldConstants.TITLES, analyzer).parse(paper.title);
        Query abstractTq = new QueryParser(FieldConstants.ABSTRACTS, analyzer).parse(paper.abstractText);
        Query keywordsTq = new QueryParser(FieldConstants.KEYWORDS, analyzer).parse(keywords);
        // Query keywordsTq = new TermQuery(new Term(FieldConstants.KEYWORDS, keywords));

        Query availability = DoublePoint.newRangeQuery(FieldConstants.AVAILABILITY, 1, Integer.MIN_VALUE);
        Query acceptRate = DoublePoint.newRangeQuery(FieldConstants.ACCEPT_RATE, 1, Integer.MIN_VALUE);
        Query accuracy = DoublePoint.newRangeQuery(FieldConstants.ACCURACY, 1, Integer.MIN_VALUE);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        Query query = builder
                .add(titleTq, BooleanClause.Occur.MUST)
                .add(abstractTq, BooleanClause.Occur.SHOULD)
                .add(keywordsTq, BooleanClause.Occur.SHOULD)
                .add(availability, BooleanClause.Occur.FILTER)
                .add(acceptRate, BooleanClause.Occur.FILTER)
                .add(accuracy, BooleanClause.Occur.FILTER)
                .build();

        CustomScoreQuery scoredQuery = new AMCustomScoreQuery(query);

        TopDocs hits = this.searcher.search(query, LuceneConstants.MAX_SEARCH);
        System.out.println(hits.totalHits + " docs found for the query \"" + query.toString() + "\"");

        ArrayList<Person> results = new ArrayList<>(LuceneConstants.MAX_SEARCH);

        for (ScoreDoc sd : hits.scoreDocs) {
            Document d = searcher.doc(sd.doc);
            Person p = new Person();
            p.id = Integer.parseInt(d.get(FieldConstants.ID));
            p.name = d.get(FieldConstants.NAME);
            p.surname = d.get(FieldConstants.SURNAME);
            results.add(p);
        }
        return results;
    }



}
