package com.my.library.services;

import com.my.library.db.SQLSmartQuery;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public class SortManager {

    /**
     * Set up sort params for views
     * @param  req      HttpServletRequest request with form data
     * @param  query    SQLSmartQuery contains request string
     * @see             SQLSmartQuery
     */
    public static void SortManager(HttpServletRequest req, SQLSmartQuery query) throws SQLException {
        String orderParam = req.getParameter("order");
        String sortParam = req.getParameter("sort");
        if(sortParam!=null && !sortParam.equals("")) {
            query.order(sortParam,
                    (orderParam!=null && orderParam.equalsIgnoreCase("DESC") ?
                            SQLSmartQuery.SortOrder.DESC:
                            SQLSmartQuery.SortOrder.ASC));
        }

    }
}
