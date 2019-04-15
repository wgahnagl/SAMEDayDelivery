package app.util;

import app.model.User;
import spark.Request;

import java.util.HashMap;
import java.util.Map;

public class Util {
    public static Map<String, Object> getCurrentUser(Request request) {
        Map<String, Object> vm = new HashMap<>();
        User currentUser = request.session().attribute("currentUser");
        if (currentUser != null) {
            vm.put("currentUser", currentUser);
        }
        return vm;
    }

    public static HashMap<String, String> getAddressFromRequest (Request request){
        HashMap<String, String> elements  = new HashMap<>();
        elements.put("address", request.queryParams("address"));
        elements.put("city",request.queryParams("city"));
        elements.put("country", request.queryParams("country"));
        elements.put("state", request.queryParams("state"));
        elements.put("zip", request.queryParams("zip"));
        elements.put("address2" , request.queryParams("address2"));
        return elements;
    }

    public static HashMap<String, String> getLabelDataFromRequest(Request request){
        HashMap<String, String> elements  = new HashMap<>();
        elements.put("expediency", request.queryParams("expediency"));
        elements.put("packageType", request.queryParams("packageType"));
        elements.put("weight", request.queryParams("weight"));
        elements.put("autoWithdraw", request.queryParams("autoWithdraw"));
        elements.put("recieverPays", request.queryParams("recieverPays"));
        elements.put("cc-name", request.queryParams("cc-name"));
        elements.put("cc-number", request.queryParams("cc-number"));
        elements.put("cc-expiration", request.queryParams("cc-expiration"));
        elements.put("cc-cvv", request.queryParams("cc-cvv"));

        return elements;
    }
}
