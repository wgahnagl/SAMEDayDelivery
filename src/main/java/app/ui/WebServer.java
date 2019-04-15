package app.ui;
import static spark.Spark.*;

import java.util.Objects;

import app.appl.*;
import app.appl.customer.*;
import app.ui.customer.*;
import app.util.customer.*;
import spark.TemplateEngine;

public class WebServer {

    public static final String HOME_URL = "/";
    public static final String SIGNIN_URL = "/signin";
    public static final String SIGNUP_URL = "/signup";
    public static final String SIGNOUT_URL = "/signout";
    public static final String FORGOT_PASSWORD_URL = "/password_recover";

    public static final String TRACKING_URL = "/tracking";
    public static final String ACCOUNT_URL = "/account";
    public static final String CREATE_LABEL_URL = "create_label";
    public static final String PREPAY_PACKAGE_URL = "prepay_package";
    public static final String GET_BILLING_URL = "/billing";

    public static final String GET_ADDRESS_DATA_URL = "/get_address_data";
    public static final String GET_CREDIT_CARD_DATA_URL = "/get_credit_card_data";
    public static final String GET_BANK_ACCOUNT_DATA_URL = "get_bank_account_data";
    public static final String GET_TRACKING_DATA_URL = "/get_tracking_data";
    public static final String GET_PACKAGE_DATA_URL = "/get_package_data";
    public static final String GET_BILLING_DATA_URL = "/get_billing_data";

    public static final String ADD_CREDIT_CARD_URL = "/add_credit_card";
    public static final String ADD_ADDRESS_URL  = "/add_address";
    public static final String ADD_BANK_ACCOUNT_URL  = "/add_bank_account";


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
        get(PREPAY_PACKAGE_URL, new GetPrepaidPackageRoute(templateEngine));
        get(CREATE_LABEL_URL, new GetCreateLabelroute(templateEngine));
        get(FORGOT_PASSWORD_URL, new GetPasswordRecoverRoute(templateEngine));
        get(GET_BILLING_URL, new GetBillingRoute(templateEngine));

        get(TRACKING_URL, new GetTrackingRoute(templateEngine));
        get(ACCOUNT_URL, new GetAccountRoute(templateEngine));
        get(ADD_CREDIT_CARD_URL, new GetAddCreditCardRoute(templateEngine));
        get(ADD_ADDRESS_URL, new GetAddAddressRoute(templateEngine));
        get(ADD_BANK_ACCOUNT_URL, new GetAddBankAccountRoute(templateEngine));

        get(GET_ADDRESS_DATA_URL, new GetAddressDataRoute());
        get(GET_BANK_ACCOUNT_DATA_URL, new GetBankAccountDataRoute());
        get(GET_CREDIT_CARD_DATA_URL, new GetCreditCardDataRoute());
        get(GET_TRACKING_DATA_URL, new GetTrackingDataRoute());
        get(GET_PACKAGE_DATA_URL, new GetPackageDataRoute());
        get(GET_BILLING_DATA_URL, new GetBillingDataRoute());

        post(SIGNIN_URL, new PostSignInRoute(templateEngine));
        post(SIGNUP_URL, new PostSignUpRoute());
        post(SIGNOUT_URL, new PostSignOutRoute());
        post(CREATE_LABEL_URL, new PostCreateLabel());
        post(PREPAY_PACKAGE_URL, new PostPrepaidPackageRoute());
        post(ADD_CREDIT_CARD_URL, new PostAddCreditCardRoute());
        post(ADD_ADDRESS_URL, new PostAddAddressRoute());
        post(ADD_BANK_ACCOUNT_URL, new PostAddBankAccountRoute());
    }

}