package com.my.library.services;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Status;
import com.my.library.db.DAO.StatusDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class GetStatuses {

    /**
     * Return map with book statuses mapped with english name
     * @see     Status
     * @see     com.my.library.db.entities.Entity
     * @see     StatusDAO
     */

    public static Map<String, Status> get(StatusDAO statusDAO) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Status().table);
        ArrayList<Status> st = statusDAO.get(sq);
        return st.stream().collect(Collectors.toMap(x->x.getStatus().get("en"), x->x));
    }

}
