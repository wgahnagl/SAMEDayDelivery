package app.appl.customer;

import app.DBLiason;
import app.model.User;
import spark.Request;
import spark.Response;
import spark.Route;

public class PostAddAddressRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        User user = request.session().attribute("currentUser");
        final String address = request.queryParams("address");
        final String city = request.queryParams("city");
        final String country = request.queryParams("country");
        final String state = request.queryParams("state");
        final String zip = request.queryParams("zip");
        final String address2 = request.queryParams("address2");

        if(DBLiason.linkAddress(user.getEmail(), address, address2, city, state, zip, country)){
            response.redirect("/account");
            return null;
        }else{
            request.session().attribute("addressError", "Address add failed");
            response.redirect("/add_address");
            return null;
        }
    }
}
