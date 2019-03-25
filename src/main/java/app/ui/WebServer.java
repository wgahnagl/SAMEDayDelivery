package app.ui;
import static spark.Spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import spark.TemplateEngine;

public class WebServer {
    private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

    public static final String HOME_URL = "/";

    private final TemplateEngine templateEngine;

    public WebServer(final TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
    }

    public void initialize() {
        staticFileLocation("/public");
        get(HOME_URL, new GetHomeRoute(templateEngine));
        LOG.config("WebServer is initialized.");
    }

}