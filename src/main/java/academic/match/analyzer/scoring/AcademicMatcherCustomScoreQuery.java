package academic.match.analyzer.scoring;

import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Weight;

import java.io.IOException;

public class AcademicMatcherCustomScoreQuery extends Scorer {

    protected AcademicMatcherCustomScoreQuery(Weight weight) {
        super(weight);
    }

    @Override
    public int docID() {
        return 0;
    }

    @Override
    public float score() throws IOException {
        return 0;
    }

    @Override
    public DocIdSetIterator iterator() {
        return null;
    }
}
