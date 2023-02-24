package com.my.library.services;

import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.User;
import com.my.library.db.DAO.UserDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class GetUsers {

    /**
     * Return map with users that registered in system, mapped by user_id,
     * users password hiding while collecting map
     * @see     User
     * @see     com.my.library.db.entities.Entity
     * @see     UserDAO
     * @throws  SQLException can be thrown while retrieving users data
     */

    public static Map<Integer, User> get(UserDAO userDAO) throws SQLException {
        SQLBuilder sq = new SQLBuilder(new User().table).build();
        ArrayList<User> st = userDAO.get(sq);
        return st.stream().collect(Collectors.toMap(User::getId, x->{
            x.setPassword("");
            return x;
        }));
    }

}
