package academic.match.analyzer.tokenizier;

import org.apache.lucene.analysis.util.CharTokenizer;

public class KeywordTokenizier extends CharTokenizer {

    public KeywordTokenizier() {
        super();
    }

    @Override
    protected boolean isTokenChar(final int character) {
        return ',' != character;
    }
}
