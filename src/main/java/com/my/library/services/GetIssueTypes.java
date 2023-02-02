package com.my.library.services;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.IssueType;
import com.my.library.db.DAO.IssueTypeDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class GetIssueTypes {

    /**
     * Return map with book issue types that used for release book, mapped by issue name in english
     * Used in jsp pages
     * @see     IssueType
     * @see     com.my.library.db.entities.Entity
     * @see     IssueTypeDAO
     */

    public static Map<String, IssueType> get(IssueTypeDAO issueTypeDAO) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new IssueType().table);
        ArrayList<IssueType> it = issueTypeDAO.get(sq);
        return it.stream().collect(Collectors.toMap(x->x.getIssueType().get("en"), x->x));
    }

}
