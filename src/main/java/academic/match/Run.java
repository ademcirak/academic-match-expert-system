package academic.match;

import academic.match.web.Routes;

public class Run {

    public static void main(String[] args) {

        // TODO: init scraper

        // TODO: init lucene index

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
