package com.my.library.servlets;

import com.my.library.services.PasswordHash;import java.util.ResourceBundle;
public class MessageManager {
    private static PasswordHash.MessageManager instance;
    private ResourceBundle resourceBundle;
    private static final String BUNDLE_NAME = "messages";
    public static final String LOGIN_ERROR_MESSAGE = "LOGIN_ERROR_MESSAGE";
    public static final String SERVLET_EXCEPTION_ERROR_MESSAGE = "SERVLET_EXCEPTION_ERROR_MESSAGE";
    public static final String IO_EXCEPTION_ERROR_MESSAGE = "IO_EXCEPTION_ERROR_MESSAGE";

    public static PasswordHash.MessageManager getInstance() {
        if (instance == null) {
            instance = new PasswordHash.MessageManager();
            instance.resourceBundle =
                    ResourceBundle.getBundle(BUNDLE_NAME);
        }
        return instance;
    }

    public String getProperty(String key) {
        return (String) resourceBundle.getObject(key);
    }

    public static void main(String[] args){
        System.out.println(PasswordHash.MessageManager.getInstance().getProperty(LOGIN_ERROR_MESSAGE));
    } 
}