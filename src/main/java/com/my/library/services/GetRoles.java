package com.my.library.services;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Genre;
import com.my.library.db.entities.Role;
import com.my.library.db.DAO.GenreDAO;
import com.my.library.db.DAO.RoleDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class GetRoles {

    /**
     * Return map with user roles mapped with role name in english
     * @see     Genre
     * @see     com.my.library.db.entities.Entity
     * @see     GenreDAO
     */

    public static Map<String, Role> get(RoleDAO roleDAO) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Role().table);
        ArrayList<Role> st = roleDAO.get(sq);
        return st.stream().collect(Collectors.toMap(x->x.getRoleName().get("en"), x->x));
    }

}
