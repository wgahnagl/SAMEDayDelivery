package app.ui;

import app.util.Util;
import spark.*;
import java.util.Map;
import java.util.Objects;

public class GetSignInRoute implements Route {
    private final TemplateEngine templateEngine;

    public GetSignInRoute(final TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    }

    @Override
    public Object handle(Request request, Response response) {
        Map<String, Object> vm = Util.getCurrentUser(request);
        vm.put("view" , request.host().split("\\.")[0]);
        String error = request.session().attribute("signInError");
        if(error != null){
            vm.put("error", error);
        }
        return templateEngine.render(new ModelAndView(vm, "login.ftl"));
    }
}
