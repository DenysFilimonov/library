package com.my.library.services;

import com.my.library.db.SQLSmartQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ErrorManager {

    /**
     * Add new <Key, Value> pair in error Map
     * @param  errors       Map with errors used for forms validation in *.jsp pages
     * @param  key          String name of <input> element that have to be checked by user
     * @param enMessage    String contained error description in English
     * @param uaMessage    String contained error description in Ukraine
     *
     */

    private ErrorMap errors = new ErrorMap();


    public void add(String key, String enMessage, String uaMessage){
        HashMap<String, String> element = new HashMap<>();
        element.put("en", enMessage);
        element.put("ua", uaMessage);
        errors.put(key, element);
    }

    public ErrorMap getErrors(){
        return errors;
    }

}
