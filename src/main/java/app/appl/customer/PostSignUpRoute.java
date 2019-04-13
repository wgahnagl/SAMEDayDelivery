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

        final String address = request.queryParams("address");
        final String address2 = request.queryParams("address2");
        final String city = request.queryParams("city");
        final String country = request.queryParams("country");
        final String state = request.queryParams("state");
        final String zip = request.queryParams("zip");
        final String cc_name = request.queryParams("cc-name");
        final String cc_number = request.queryParams("cc-number");
        final String cc_expiration = request.queryParams("cc-expiration");
        final String cc_cvv = request.queryParams("cc-cvv");

        request.session().attribute("currentUser", new User(email));

        response.redirect("/account");
        return null;
    }
}
