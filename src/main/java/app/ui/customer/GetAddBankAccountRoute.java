package app.ui.customer;

import app.util.Util;
import spark.*;

import java.util.Map;
import java.util.Objects;

public class GetAddBankAccountRoute implements Route {
    private final TemplateEngine templateEngine;

    public GetAddBankAccountRoute(final TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<String, Object> vm = Util.getCurrentUser(request);
        vm.put("view" , request.host().split("\\.")[0]);
        return templateEngine.render(new ModelAndView(vm, "customer/addBank.ftl"));
    }
}
