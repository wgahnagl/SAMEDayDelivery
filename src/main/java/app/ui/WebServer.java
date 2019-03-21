package app.ui;
import static spark.Spark.*;

import java.util.Objects;
import spark.TemplateEngine;

public class WebServer {

    public static final String HOME_URL = "/";
    public static final String SIGNIN_URL = "/signin";
    public static final String SIGNUP_URL = "/signup";
    public static final String TRACKING_URL = "/tracking";
    public static final String ACCOUNT_URL = "/account";
    public static final String FORGOT_PASSWORD_URL = "/password_recover";

    private final TemplateEngine templateEngine;

    public WebServer(final TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
    }

    public void initialize() {
        staticFileLocation("/public");
        get(HOME_URL, new GetHomeRoute(templateEngine));
        get(SIGNIN_URL, new GetSignInRoute(templateEngine));
        get(SIGNUP_URL, new GetSignUpRoute(templateEngine));
        get(TRACKING_URL, new GetTrackingRoute(templateEngine));
        get(ACCOUNT_URL, new GetAccountRoute(templateEngine));
        get(FORGOT_PASSWORD_URL, new GetPasswordRecoverRoute(templateEngine));
    }

}