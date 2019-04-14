package app.util.customer;

import app.DBLiason;
import app.model.User;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;

public class GetBankAccountDataRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        User user = request.session().attribute("currentUser");
        HashMap<String, String> bankAccount = DBLiason.getBankAccountForCustomer(user.getEmail());
        request.session().attribute("bankAccount", bankAccount);
        response.status(200);
        Gson gson = new Gson();
        return gson.toJson(bankAccount);
    }
}
