package app.appl;

import spark.Request;
import spark.Response;
import spark.Route;

public class PostSignOutRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        request.session().attribute("currentUser", null);
        response.redirect("/");
        return null;
    }
}
