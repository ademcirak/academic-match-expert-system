package academic.match.web;
import academic.match.models.Person;
import academic.match.scraper.AmScraper;
import academic.match.scraper.Scraper;
import academic.match.web.controllers.IndexController;
import com.google.gson.Gson;

import static spark.Spark.*;

public class Routes {

    // see http://sparkjava.com/documentation#routes

    static Scraper scraper = new AmScraper();
    static Gson gson = new Gson();
    public static void registerRoutes() throws Exception {

        port(8282);
        staticFiles.location("/public"); // Static files
        staticFiles.expireTime(0);

        get("/test", IndexController.serveIndexPage);
        get("/hello", (req, res) -> "Hello World");


        // matches "GET /hello/foo" and "GET /hello/bar"
        // request.params(":name") is 'foo' or 'bar'
        get("/hello/:name", (request, response) -> {
            return "Hello: " + request.params(":name");
        });

        post("/person/create"  , (req, res) -> {

            Person person = new Person();
            person.name = req.queryParams("first_name");
            person.surname = req.queryParams("last_name");
            // TODO MURAT fill all fields
            person.papers = scraper.getPapers(person);
            return gson.toJson(person);
        });

        // Using Route
        notFound((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Custom 404\"}";
        });
    }



}
