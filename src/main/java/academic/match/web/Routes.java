package academic.match.web;
import academic.match.analyzer.Lucene;
import academic.match.models.Paper;
import academic.match.models.Person;
import academic.match.scraper.AmScraper;
import academic.match.scraper.Scraper;
import academic.match.web.controllers.IndexController;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static spark.Spark.*;

public class Routes {

    // see http://sparkjava.com/documentation#routes

    static Scraper scraper = new AmScraper();
    static Lucene lucene = null;
    static Gson gson = new Gson();
    static Random rand = new Random();

    public static int randInt(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static void registerRoutes(Lucene luceneParam) throws Exception {

        lucene = luceneParam;

        port(8282);
        staticFiles.location("/public"); // Static files
        staticFiles.expireTime(0);

        post("/person/create"  , (req, res) -> {

            Person person = new Person();
            person.id = randInt(1, 2000000);
            person.title = req.queryParams("title");
            person.name = req.queryParams("first_name");
            person.surname = req.queryParams("last_name");
            person.availability = Double.parseDouble(req.queryParams("availability"));
            person.accuracy = Double.parseDouble(req.queryParams("accuracy"));
            person.acceptRate = Double.parseDouble(req.queryParams("accept_rate"));
            person.scopusAuthorId = req.queryParams("scopus_id");
            person.keywords = Arrays.asList(req.queryParams("keywords").split(","));
            // TODO MURAT fill all fields
            person.papers = scraper.getPapers(person);

            if(lucene.isPersonIndexed(person)) {
                System.out.println("already indexed");
            } else {
                lucene.indexPerson(person);

            }

            return gson.toJson(person);
        });

        post("/match"  , (req, res) -> {

            Paper paper = new Paper();
            paper.title = req.queryParams("title");
            paper.abstractText = req.queryParams("abstract");
            paper.keywords = Arrays.asList(req.queryParams("keywords").split(","));

            List<Person> list = lucene.search(paper);
            return gson.toJson(list);
        });


        // Using Route
        notFound((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Custom 404\"}";
        });
    }



}
