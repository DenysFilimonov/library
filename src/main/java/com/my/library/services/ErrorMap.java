package com.my.library.services;

import java.util.HashMap;
import java.util.Map;

public class ErrorMap extends HashMap {

    private final HashMap<String, Map<String, String>> map = new HashMap<>();

    public HashMap<String, String> get(String key){
        return (HashMap<String, String>) map.get(key);
    }

}
