package com.my.library.services;

import com.my.library.db.SQLBuilder;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public class SortManager {

    /**
     * Set up sort params for views
     * @param  req      HttpServletRequest request with form data
     * @param  query    SQLSmartQuery contains request string
     * @see             com.my.library.db.SQLBuilder
     */
    public static void SortManager(HttpServletRequest req, SQLBuilder query) throws SQLException {
        String orderParam = req.getParameter("order");
        String sortParam = req.getParameter("sort");
        if(sortParam!=null && !sortParam.equals("")) {
            query.order(sortParam,
                    (orderParam!=null && orderParam.equalsIgnoreCase("DESC") ?
                            SQLBuilder.SortOrder.DESC:
                            SQLBuilder.SortOrder.ASC));
        }

    }
}
