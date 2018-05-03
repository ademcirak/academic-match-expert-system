package academic.match.web;
import academic.match.web.controllers.IndexController;

import static spark.Spark.*;

public class Routes {

    // see http://sparkjava.com/documentation#routes

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


        // Using Route
        notFound((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Custom 404\"}";
        });
    }



}
