package academic.match.web.controllers;

import spark.*;

import java.util.HashMap;
import java.util.Map;

public class IndexController {
    public static Route serveIndexPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();

        return "Hello World";
        // return ViewUtil.render(request, model, Path.Template.INDEX);
    };
}
