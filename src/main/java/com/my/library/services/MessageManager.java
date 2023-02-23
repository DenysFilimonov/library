package com.my.library.services;

import java.util.ResourceBundle;
public class MessageManager {
    private static MessageManager instance;
    private ResourceBundle resourceBundle;
    private static final String BUNDLE_NAME = "messages";
    public static final String LOGIN_ERROR_MESSAGE = "LOGIN_ERROR_MESSAGE";

    public static final String LOGIN_ERROR_MESSAGE_UA = "LOGIN_ERROR_MESSAGE_UA";
    public static final String USER_BLOCKED_MESSAGE = "USER_BLOCKED_MESSAGE";
    public static final String USER_BLOCKED_MESSAGE_UA = "USER_BLOCKED_MESSAGE_UA";
    public static final String SERVLET_EXCEPTION_ERROR_MESSAGE = "SERVLET_EXCEPTION_ERROR_MESSAGE";
    public static final String IO_EXCEPTION_ERROR_MESSAGE = "IO_EXCEPTION_ERROR_MESSAGE";
    public static final String SQL_EXCEPTION_ERROR_MESSAGE = "SQL_EXCEPTION_ERROR_MESSAGE";
    public static final String OTHER_EXCEPTION_ERROR_MESSAGE = "OTHER_EXCEPTION_ERROR_MESSAGE";

    public static Object mutex = new Object();

    public static MessageManager getInstance() {
        MessageManager result;
        synchronized (mutex){
            result = instance;
            if (result==null){
                result = instance = new MessageManager();
                instance.resourceBundle =
                        ResourceBundle.getBundle(BUNDLE_NAME);
            }
        }
        return result;
    }

    public String getProperty(String key) {
        return (String) resourceBundle.getObject(key);
    }

}