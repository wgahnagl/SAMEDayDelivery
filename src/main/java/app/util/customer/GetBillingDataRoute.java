package app.util.customer;

import app.DBLiason;
import app.model.User;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.HashMap;

public class GetBillingDataRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        User user = request.session().attribute("currentUser");
        ArrayList<HashMap<String, String>> creditCards = DBLiason.getCreditCardsForCustomer(user.getEmail());
        request.session().attribute("creditCards", creditCards);
        response.status(200);
        Gson gson = new Gson();
        return gson.toJson(creditCards);
    }
}
