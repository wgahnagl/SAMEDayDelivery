package app.util.customer;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetPackageDataRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String packages = "packages";
        response.status(200);
        Gson gson = new Gson();
        return gson.toJson(packages);     }
}
