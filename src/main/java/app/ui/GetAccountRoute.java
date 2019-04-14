package app.ui;

import app.model.User;
import app.util.Util;
import spark.*;
import java.util.Map;
import java.util.Objects;

public class GetAccountRoute implements Route {
    private final TemplateEngine templateEngine;

    public GetAccountRoute(final TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    }

    @Override
    public Object handle(Request request, Response response) {
        Map<String, Object> vm = Util.getCurrentUser(request);
        User user = request.session().attribute("currentUser");
        vm.put("view" , request.host().split("\\.")[0]);
        vm.put("address", request.session().attribute("address"));
        vm.put("creditCard", request.session().attribute("creditCard"));
        vm.put("bankAccount", request.session().attribute("bankAccount"));
        vm.put("firstName", user.getFirstname());
        vm.put("lastName", user.getLastname());

        if(request.host().startsWith("admin")){
            return templateEngine.render(new ModelAndView(vm, "admin/accountView.ftl"));
        }
        else if(request.host().startsWith("delivery")){
            return templateEngine.render(new ModelAndView(vm, "delivery/accountView.ftl"));
        }
        else{
            return templateEngine.render(new ModelAndView(vm, "customer/accountView.ftl"));
        }
    }
}
