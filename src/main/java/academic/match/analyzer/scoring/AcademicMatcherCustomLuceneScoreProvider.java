package academic.match.analyzer.scoring;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;

import java.io.IOException;

public class AcademicMatcherCustomLuceneScoreProvider extends CustomScoreProvider {
    public AcademicMatcherCustomLuceneScoreProvider(LeafReaderContext context) {
        super(context);
    }

    @Override
    public float customScore(int doc, float subQueryScore, float[] valSrcScores) throws IOException {
        return super.customScore(doc, subQueryScore, valSrcScores);
    }
}
