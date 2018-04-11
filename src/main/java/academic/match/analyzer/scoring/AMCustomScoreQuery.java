package academic.match.analyzer.scoring;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.Query;

import java.io.IOException;

public class AMCustomScoreQuery extends CustomScoreQuery {

    public AMCustomScoreQuery(Query subQuery) {
        super(subQuery);
    }

    protected CustomScoreProvider getCustomScoreProvider(
            LeafReaderContext context) throws IOException {
        return new AMCustomScoreProvider(context);
    }
}
