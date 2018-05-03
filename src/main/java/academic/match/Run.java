package academic.match;

import academic.match.analyzer.Lucene;
import academic.match.scraper.Scraper;
import academic.match.web.Routes;

public class Run {

    static Scraper scraper;
    static Lucene lucene;

    public static void main(String[] args) {

        // TODO: init scraper

         // TODO: init lucene index
        try {
            lucene = Lucene.build();
        }catch (Exception e) {
            System.out.println("Lucene initiation error: " + e.getMessage());
            e.printStackTrace();
        }

        // TODO: init rule engine

        // TODO: combine scraper & lucene index and rule engine together

        // register routes
        try {
            Routes.registerRoutes();
        } catch (Exception e) {
            System.out.println("Route registration failed:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
