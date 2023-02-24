package com.my.library.services;

import java.util.HashMap;

public class ErrorManager {



    private ErrorMap errors = new ErrorMap();

    /**
     * Add new <Key, Value> pair in error Map
     * @param  key          String name of <input> element that have to be checked by user
     * @param enMessage    String contained error description in English
     * @param uaMessage    String contained error description in Ukraine
     *
     */
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
