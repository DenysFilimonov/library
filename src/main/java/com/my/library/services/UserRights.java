package com.my.library.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UserRights {

    private static UserRights instance = null;

    public Map<String, ArrayList<String>> getUserRightsMap() {
        return userRightsMap;
    }

    private static final Map<String, ArrayList<String>> userRightsMap = new HashMap<>();
    private static Object mutex = new Object();

    private UserRights(){

    }

    public static UserRights getInstance(){
        UserRights result;
        synchronized (mutex){
            result = instance;
            if (result==null){
                result = instance = new UserRights();
            }
        }
        return result;
        }

    /**
     * Set up rights for different user roles: guest, user, librarian, administrator
     * @see             com.my.library.filters.SecurityFilter
     * @see             com.my.library.db.entities.User
     * @see             com.my.library.db.entities.Role
     */

    private static void setUserRights(){
        String[]  userRights = {"login", "catalog", "logout", "subscriptions", "account", "orderBook", "cancelOrder"};
        ArrayList<String> readerCommands = (ArrayList<String>) Arrays.stream(userRights).collect(Collectors.toList());
        userRightsMap.put("user", readerCommands);
        String[]  guestRights = {"login", "catalog", "register"};
        ArrayList<String> guestCommands = (ArrayList<String>) Arrays.stream(guestRights).collect(Collectors.toList());
        userRightsMap.put("guest", guestCommands);
        String[]  librarianRights = {"login", "catalog", "logout", "subscriptions", "account",
                "orders", "readers", "issueOrder", "returnBook", "orderBook", "cancelOrder"};
        ArrayList<String> librarianCommands = (ArrayList<String>) Arrays.stream(librarianRights).collect(Collectors.toList());
        userRightsMap.put("librarian", librarianCommands);
        String[]  adminRights = {"login", "catalog", "logout", "subscriptions", "account",
                "booksManager", "deleteBook", "editBook", "newBook", "userManager", "cancelOrder"};
        ArrayList<String> adminCommands = (ArrayList<String>) Arrays.stream(adminRights).collect(Collectors.toList());
        userRightsMap.put("administrator", adminCommands);
    }

}



