package com.my.library.services;

import java.util.ResourceBundle;
public class ConfigurationManager {
    private static ConfigurationManager instance;
    private ResourceBundle resourceBundle;
    private static final String BUNDLE_NAME = "config";
    public static final String ERROR_PAGE_PATH = "ERROR_PAGE_PATH";
    public static final String LOGIN_PAGE_PATH = "LOGIN_PAGE_PATH";

    public static final String REGISTER_PAGE_PATH = "REGISTER_PAGE_PATH";

    public static final String MAIN_PAGE_PATH = "MAIN_PAGE_PATH";
    public static final String CATALOG_PAGE_PATH = "CATALOG_PAGE_PATH";

    public static final String READERS_PAGE_PATH = "READERS_PAGE_PATH";

    public static final String RETURN_BOOK_PAGE_PATH = "RETURN_BOOK_PAGE_PATH";

    public static final String OK_RETURN = "OK_RETURN_PAGE_PATH";

    public static final String SUBSCRIPTIONS_PAGE_PATH = "SUBSCRIPTIONS_PAGE_PATH";
    public static final String ACCOUNT_PAGE_PATH = "ACCOUNT_PAGE_PATH";

    public static final String ORDERS_PAGE_PATH = "ORDERS_PAGE_PATH";

    public static final String BOOK_MANAGER_PAGE_PATH = "BOOK_MANAGER_PAGE_PATH";

    public static final String NEW_BOOK_PAGE_PATH = "NEW_BOOK_PAGE_PATH";

    public static final String LINES_ON_PAGE = "LINES_ON_PAGE";

    public static final String USER_MANAGER_PAGE_PATH = "USER_MANAGER_PAGE_PATH";

    public static final String ISSUE_ORDER_PAGE_PATH = "ISSUE_ORDER_PAGE_PATH";

    public static final String NO_RIGHTS_PAGE_PATH = "NO_RIGHTS_PAGE_PATH";
    public static final String COVER_PATH = "COVER_PATH";


    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
            instance.resourceBundle =
                    ResourceBundle.getBundle(BUNDLE_NAME);
        }
        return instance;
    }

    public String getProperty(String key) {
        return (String) resourceBundle.getObject(key);
    }

}