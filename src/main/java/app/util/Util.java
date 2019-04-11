package app.util;

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
}
