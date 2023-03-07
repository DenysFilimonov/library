package com.my.library.services;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BlackList {
    private static BlackList instance;
    private Map<String, Integer> blackListMap= new HashMap<>();
    private static final Object mutex = new Object();

    public Map<String, Integer> getBlackListMap() {
        return blackListMap;
    }

    public synchronized void add(String IPAddress) {
        if(blackListMap.get(IPAddress)==null)
            blackListMap.put(IPAddress, 1);
        else
            blackListMap.put(IPAddress, blackListMap.get(IPAddress)+1);
    }

    public static BlackList getInstance() {
        BlackList result;
            synchronized (mutex) {
                result = instance;
                if (result == null) {
                    instance = new BlackList();
                    result = instance;
                }
            }
        return result;
    }
}