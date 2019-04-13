package app.appl;

import app.DBLiason;
import app.model.User;
import spark.Request;
import spark.Response;
import spark.Route;

public class PostSignInRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String email = request.queryParams("email");
        String password = request.queryParams("password");

        if(DBLiason.checkPassword(email, password)){
            request.session().attribute("currentUser", new User(email));
        }else{
            response.header("error", "user not found");
        }
        return null;
    }
}
