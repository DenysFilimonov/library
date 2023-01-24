package com.my.library.services;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.IssueType;
import com.my.library.db.repository.IssueTypeRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class GetIssueTypes {

    public static Map<String, IssueType> getTypes() throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new IssueType().table);
        ArrayList<IssueType> it = IssueTypeRepository.getInstance().get(sq);
        Map<String, IssueType> issueMap=it.stream().collect(Collectors.toMap(x->x.getIssueType().get("en"), x->x));
        return issueMap;
    }

}
