package app.appl.customer;

import app.DBLiason;
import app.model.User;
import app.util.Util;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;

public class PostCreateLabel implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        User user = request.session().attribute("currentUser");
        HashMap<String, String> addressData = Util.getAddressFromRequest(request);
        HashMap<String, String> labelData = Util.getLabelDataFromRequest(request);
        if(DBLiason.createLabel(user.getEmail(),
                addressData.get("address"),
                addressData.get("address2"),
                addressData.get("city"),
                addressData.get("state"),
                addressData.get("zip") ,
                addressData.get("country"),
                labelData.get("expediency"),
                labelData.get("packageType"),
                labelData.get("weight"))){
            response.redirect("/account");
            return null;
        }else{
            request.session().attribute("labelError", "Label add failed");
            response.redirect("/create_label");
            return null;
        }
    }
}
