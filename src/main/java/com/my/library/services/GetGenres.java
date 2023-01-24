package com.my.library.services;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.IssueType;
import com.my.library.db.entities.Status;
import com.my.library.db.repository.IssueTypeRepository;
import com.my.library.db.repository.StatusRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class GetStatuses {

    public static Map<String, Status> getTypes() throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Status().table);
        ArrayList<Status> st = StatusRepository.getInstance().get(sq);
        Map<String, Status> issueMap=st.stream().collect(Collectors.toMap(x->x.getStatus().get("en"), x->x));
        return issueMap;
    }

}
