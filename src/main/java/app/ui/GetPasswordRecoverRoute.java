package app.ui;

import app.util.Util;
import spark.*;
import java.util.Map;
import java.util.Objects;

public class GetPasswordRecoverRoute implements Route {
    private final TemplateEngine templateEngine;

    public GetPasswordRecoverRoute(final TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    }

    @Override
    public Object handle(Request request, Response response) {
        Map<String, Object> vm = Util.getCurrentUser(request);
        vm.put("view" , request.host().split("\\.")[0]);

        return templateEngine.render(new ModelAndView(vm, "passwordRecover.ftl"));
    }
}
