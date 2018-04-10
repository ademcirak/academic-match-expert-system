package academic.match.analyzer;

import academic.match.analyzer.tokenizier.KeywordAnalyzer;
import academic.match.models.Paper;
import academic.match.models.Person;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Lucene {

    Directory dir;
    IndexWriter writer;
    IndexReader reader;
    IndexSearcher searcher;

    Lucene() throws Exception {
        this.dir = FSDirectory.open(new File(LuceneConstants.FILE_PATH).toPath());


        Map<String, Analyzer> analyzerMap = new HashMap<String, Analyzer>();
        analyzerMap.put(FieldConstants.KEYWORDS, new KeywordAnalyzer());
        PerFieldAnalyzerWrapper wrapper = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerMap);
        IndexWriterConfig config = new IndexWriterConfig(wrapper).setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND)

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

        doc.add(new TextField(FieldConstants.ID, person.id.toString(), Field.Store.YES));
        doc.add(new TextField(FieldConstants.NAME, person.name, Field.Store.YES));
        doc.add(new TextField(FieldConstants.SURNAME, person.surname, Field.Store.YES));
        doc.add(new TextField(FieldConstants.KEYWORDS, keywords, Field.Store.YES));
        doc.add(new TextField(FieldConstants.TITLES, titles, Field.Store.YES));
        doc.add(new TextField(FieldConstants.ABSTRACTS, abstracts, Field.Store.YES));
        return doc;
    }

    public void indexPerson(Person person) throws Exception {
        Document doc = this.personToDocument(person);
        this.writer.addDocument(doc);
        writer.commit();
    }

}
