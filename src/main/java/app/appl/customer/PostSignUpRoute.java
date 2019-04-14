package app.appl.customer;

import app.DBLiason;
import app.model.User;
import spark.Request;
import spark.Response;
import spark.Route;

public class PostSignUpRoute implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final String firstName = request.queryParams("firstName");
        final String lastName = request.queryParams("lastName");
        final String email = request.queryParams("email");
        final String password = request.queryParams("password");

        DBLiason.addCustomerByInfo(lastName, firstName, email, password);
        request.session().attribute("currentUser", new User(email, firstName, lastName));

        response.redirect("/account");
        return null;
    }
}
