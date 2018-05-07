package academic.match.analyzer.scoring;

import academic.match.analyzer.FieldConstants;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.CustomScoreProvider;

import java.io.IOException;

public class AMCustomScoreProvider extends CustomScoreProvider {

    private static float AVAILABILITY_MULTIPLIER = 0.1f;
    private static float ACCURACY_MULTIPLIER = 0.1f;
    private static float ACCEPT_RATE_MULTIPLIER = 0.1f;

    public AMCustomScoreProvider(LeafReaderContext context) {
        super(context);

    }

    @Override
    public float customScore(int doc, float subQueryScore, float[] valSrcScores) throws IOException {
        // return super.customScore(doc, subQueryScore, valSrcScores);

        Document document =context.reader().document(doc);
        IndexableField availabilityIf= document.getField(FieldConstants.AVAILABILITY);// the field I wanted to extract
        float availabilityScore = availabilityIf.numericValue().floatValue();

        IndexableField accuracyIf= document.getField(FieldConstants.AVAILABILITY);// the field I wanted to extract
        float accuracyScore = accuracyIf.numericValue().floatValue();

        IndexableField acceptRateIf= document.getField(FieldConstants.AVAILABILITY);// the field I wanted to extract
        float acceptRate = acceptRateIf.numericValue().floatValue();

        return (1 + availabilityScore * AVAILABILITY_MULTIPLIER
                + accuracyScore * ACCURACY_MULTIPLIER
                + ACCEPT_RATE_MULTIPLIER * acceptRate)
                * subQueryScore;

    }

}
