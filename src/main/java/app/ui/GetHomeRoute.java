package app.ui;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import java.util.Objects;

public class GetHomeRoute implements Route {
    private final TemplateEngine templateEngine;
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    public GetHomeRoute(final TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("Home is initialized");
    }

    @Override
    public Object handle(Request request, Response response){
        LOG.finer("Hmome is invoked");
        Map<String, Object> vm = new HashMap<>();

        vm.put("title", "hey");

        return templateEngine.render(new ModelAndView(vm, "home.ftl"));

    }

}
