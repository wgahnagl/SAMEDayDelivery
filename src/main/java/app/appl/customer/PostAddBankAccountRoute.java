package app.appl.customer;

import spark.Request;
import spark.Response;
import spark.Route;

public class PostAddBankAccountRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final String bankName = request.queryParams("bankName");
        final String routingNumber = request.queryParams("routingNumber");
        final String accountNumber = request.queryParams("accountNumber");

        response.redirect("/account");
        return null;
    }
}
