package app;

import app.ui.WebServer;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.Objects;

public final class Application {

    public static void main(String[] args) {
        final TemplateEngine templateEngine = new FreeMarkerEngine();
        final WebServer webServer = new WebServer(templateEngine);
        final Application app = new Application(webServer);

        app.initialize();
    }

    private final WebServer webServer;

    private Application(final WebServer webServer) {
        Objects.requireNonNull(webServer, "webServer must not be null");
        this.webServer = webServer;
    }

    private void initialize() {
        webServer.initialize();
    }
}