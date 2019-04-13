package app.util.customer;

import app.model.User;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetCreditCardDataRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        User user = request.session().attribute("currentUser");
        //HashMap<String, String> creditCard =  DBLiason.getCreditCardsForCustomer(user.getEmail());
        String creditCard = "";
        request.session().attribute("creditCard", creditCard);
        response.status(200);
        Gson gson = new Gson();
        return gson.toJson(creditCard);    }
}
