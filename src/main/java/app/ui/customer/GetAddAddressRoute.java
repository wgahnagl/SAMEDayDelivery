package app.ui.customer;

import app.model.User;
import app.util.Util;
import spark.*;

import java.util.Map;
import java.util.Objects;

public class GetAddAddressRoute implements Route {
    private final TemplateEngine templateEngine;

    public GetAddAddressRoute(final TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<String, Object> vm = Util.getCurrentUser(request);
        User user = request.session().attribute("currentUser");
        vm.put("firstName", user.getFirstname());
        vm.put("lastName", user.getLastname());

        vm.put("view" , request.host().split("\\.")[0]);
        String error = request.session().attribute("addressError");
        if(error != null){
            vm.put("error", error);
        }

        return templateEngine.render(new ModelAndView(vm, "customer/addAddress.ftl"));
    }
}
