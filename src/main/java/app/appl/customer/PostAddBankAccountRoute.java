package app.appl.customer;

import app.DBLiason;
import app.model.User;
import spark.Request;
import spark.Response;
import spark.Route;

public class PostAddBankAccountRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final User user = request.session().attribute("currentUser");
        final String bankName = request.queryParams("bankName");
        final String routingNumber = request.queryParams("routingNumber");
        final String accountNumber = request.queryParams("accountNumber");

        DBLiason.linkBankAccount(user.getEmail(), accountNumber, routingNumber);
        response.redirect("/account");
        return null;
    }
}
