package app.appl.customer;

import app.DBLiason;
import app.model.User;
import spark.Request;
import spark.Response;
import spark.Route;

public class PostAddCreditCardRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        User user = request.session().attribute("currentUser");
        final String name = request.queryParams("cc-name");
        final String number = request.queryParams("cc-number");
        final String expiration = request.queryParams("cc-expiration");
        final String cvv = request.queryParams("cc-cvv");


        if(DBLiason.linkCreditCard(user.getEmail(), name, number, expiration, cvv)){
            response.redirect("/account");
            return null;
        }else{
            request.session().attribute("addressError", "Address add failed");
            response.redirect("/add_address");
            return null;
        }
    }
}
