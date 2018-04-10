package academic.match.web;
import static spark.Spark.*;

public class Routes {

    // see http://sparkjava.com/documentation#routes

    public static void registerRoutes() throws Exception {
        get("/hello", (req, res) -> "Hello World");


        // matches "GET /hello/foo" and "GET /hello/bar"
        // request.params(":name") is 'foo' or 'bar'
        get("/hello/:name", (request, response) -> {
            return "Hello: " + request.params(":name");
        });

        staticFiles.location("/public"); // Static files

        // Using Route
        notFound((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Custom 404\"}";
        });
    }



}
