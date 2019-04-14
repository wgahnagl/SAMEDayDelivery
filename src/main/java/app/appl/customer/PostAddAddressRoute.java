package app.appl.customer;

import app.DBLiason;
import app.model.User;
import app.util.Util;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;

public class PostAddAddressRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        User user = request.session().attribute("currentUser");
        HashMap<String, String> addressData = Util.getAddressFromRequest(request);
        if (DBLiason.linkAddress(user.getEmail(),
                addressData.get("address"),
                addressData.get("address2"),
                addressData.get("city"),
                addressData.get("state"),
                addressData.get("zip"),
                addressData.get("country"))) {
            response.redirect("/account");
            return null;
        } else {
            request.session().attribute("addressError", "Address add failed");
            response.redirect("/add_address");
            return null;
        }
    }
}
