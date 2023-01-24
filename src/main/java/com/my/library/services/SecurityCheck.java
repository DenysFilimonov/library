package com.my.library.services;

import com.my.library.db.SQLSmartQuery;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

public class AppContext {

    private static AppContext instance;

    private static final HashMap<String, SQLSmartQuery> context = new HashMap<>();

    private AppContext(){

    }



    public static AppContext getInstance(){
        if(instance==null) instance = new AppContext();
        return instance;
    }

    /**
     * Store data retrieving parameters between POST requests for multi-parametrized user views
     * @param  session Session object for separating data for different sessions
     * @param  command String name of current command executed by controller
     * @see         SQLSmartQuery
     */

    public void setContext(HttpSession session, String command, SQLSmartQuery query){
        context.put(session.getId()+command, query);
    }

    /**
     * Return data retrieving params for multi-parametrized user view
     * @param  session Session object for identifying target session
     * @param  command String name of current command, executing by controller
     * @return query   SQLSmartQuery contained current data retrieving params
     * @see         SQLSmartQuery
     */

    public SQLSmartQuery getContext(HttpSession session, String command){
        return context.get(session.getId()+command);
    }


}
