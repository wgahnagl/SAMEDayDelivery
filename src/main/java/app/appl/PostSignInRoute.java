package app.appl;

import app.util.User;
import spark.Request;
import spark.Response;
import spark.Route;

public class PostSignInRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String email = request.queryParams("email");
        String password = request.queryParams("password");
        //if the username matches the password stored in the DB
        if(true){
            request.session().attribute("currentUser", new User(email));
        }
        response.redirect("/");
        return null;
    }
}
