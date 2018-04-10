package academic.match.analyzer.tokenizier;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;

public class KeywordAnalyzer extends Analyzer {

    public KeywordAnalyzer() {
        super();
    }

    @Override
    protected TokenStreamComponents createComponents(String field) {
        Tokenizer tokenizer = new KeywordTokenizier();
        TokenStream filter = new LowerCaseFilter(tokenizer);
        return new TokenStreamComponents(tokenizer, filter);
    }
}