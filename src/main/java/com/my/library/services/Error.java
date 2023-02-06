package com.my.library.services;

import java.util.HashMap;
import java.util.Map;

public class Error {
    public HashMap<String, String> getError() {
        return error;
    }

    public void setError(HashMap<String, String> error) {
        this.error = error;
    }

    private HashMap<String,String> error;
    
}
