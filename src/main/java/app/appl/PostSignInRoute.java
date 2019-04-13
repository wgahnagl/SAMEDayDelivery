package app.appl;

import app.DBLiason;
import app.model.User;
import app.util.Util;
import spark.*;

import java.util.Map;
import java.util.Objects;

public class PostSignInRoute implements Route {
    private final TemplateEngine templateEngine;

    public PostSignInRoute(TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String email = request.queryParams("email");
        String password = request.queryParams("password");

        if(DBLiason.checkPassword(email, password)){
            request.session().attribute("signInError", null);
            request.session().attribute("currentUser", new User(email));
        }else{
            request.session().attribute("signInError", "Invalid Password");
            response.redirect("/signin");
            return null;
        }
        response.redirect("/");
        return null;
    }
}
