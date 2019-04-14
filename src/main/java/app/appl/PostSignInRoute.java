package app.appl;

import app.DBLiason;
import app.model.User;
import spark.*;

import java.util.HashMap;
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
            HashMap<String, String> names = DBLiason.getNameForCustomer(email);
            request.session().attribute("signInError", null);
            request.session().attribute("currentUser", new User(email, names.get("first_name"), names.get("last_name")));
        }else{
            request.session().attribute("signInError", "Invalid Password");
            response.redirect("/signin");
            return null;
        }
        response.redirect("/");
        return null;
    }
}
