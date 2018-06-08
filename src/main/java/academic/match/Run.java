package academic.match;

import academic.match.analyzer.Lucene;
import academic.match.loader.MySQLLoader;
import academic.match.models.Person;
import academic.match.scraper.AmScraper;
import academic.match.scraper.Scraper;
import academic.match.web.Routes;

import java.util.ArrayList;

public class Run {

    static Scraper scraper;
    static Lucene lucene;

    public static void main(String[] args) {

        // TODO: init scraper
        try {
            scraper = new AmScraper();
        }catch (Exception e) {
            System.out.println("AmScraper initiation error: " + e.getMessage());
            e.printStackTrace();
        }

         // TODO: init lucene index
        try {
            lucene = Lucene.build();
        }catch (Exception e) {
            System.out.println("Lucene initiation error: " + e.getMessage());
            e.printStackTrace();
        }

        // load data
        try {
            MySQLLoader loader = new MySQLLoader();
            ArrayList<Person> persons = loader.getNames();

            for(Person p : persons) {
                System.out.println("Searching for " + p.name + " " + p.surname);
                p.papers = scraper.getPapers(p);
                System.out.println("Indexing... " + p.name + " " + p.surname);

                if(p.papers.size() == 0) {
                    System.out.println("No paper found for: " + p.name + " " + p.surname);
                } else {
                    lucene.indexPerson(p);
                    System.out.println("Indexed: " + p.name + " " + p.surname);
                }

            }
        } catch (Exception e) {
            System.out.println("Loader error  " + e.getMessage());
            e.printStackTrace();
        }


        // TODO: init rule engine

        // TODO: combine scraper & lucene index and rule engine together

        // register routes
        try {
            // Routes.registerRoutes(lucene);
        } catch (Exception e) {
            System.out.println("Route registration failed:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
