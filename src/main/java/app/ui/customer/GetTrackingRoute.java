package app.ui.customer;

import app.util.Util;
import spark.*;
import java.util.Map;
import java.util.Objects;

public class GetTrackingRoute implements Route {
    private final TemplateEngine templateEngine;

    public GetTrackingRoute(final TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    }

    @Override
    public Object handle(Request request, Response response) {
        Map<String, Object> vm = Util.getCurrentUser(request);
        vm.put("view" , request.host().split("\\.")[0]);

        if(request.host().startsWith("admin")){
            response.redirect("/");
            return null;
        }
        else if(request.host().startsWith("delivery")){
            response.redirect("/");
            return null;
        }
        else{
            return templateEngine.render(new ModelAndView(vm, "customer/tracking.ftl"));
        }
    }
}
