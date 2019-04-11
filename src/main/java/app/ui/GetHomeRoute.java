package app.ui;

import app.util.User;
import app.util.Util;
import spark.*;

import java.util.HashMap;
import java.util.Map;

import java.util.Objects;

public class GetHomeRoute implements Route {
    private final TemplateEngine templateEngine;

    public GetHomeRoute(final TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    }

    @Override
    public Object handle(Request request, Response response){
        Map<String, Object> vm = Util.getCurrentUser(request);
        vm.put("view" , request.host().split("\\.")[0]);

        if(request.host().startsWith("admin")){
            return templateEngine.render(new ModelAndView(vm, "admin/home.ftl"));
        }
        else if(request.host().startsWith("delivery")){
            return templateEngine.render(new ModelAndView(vm, "delivery/home.ftl"));
        }
        else{
            return templateEngine.render(new ModelAndView(vm, "customer/home.ftl"));
        }
    }
}
