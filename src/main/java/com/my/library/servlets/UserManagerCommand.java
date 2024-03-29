package com.my.library.servlets;

import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.Role;
import com.my.library.db.entities.User;
import com.my.library.services.*;
import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserManagerCommand extends ControllerCommand {

    /**
     * Implements Command method execute
     * Serve the requests to users accounts page, including editing users status and roles
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context AppContext
     * @throws SQLException     can be thrown during password validation
     * @throws ServletException throw to upper level, where it will be caught
     * @throws IOException      throw to upper level, where it will be caught
     * @see com.my.library.servlets.CommandMapper
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
            IOException, SQLException, NoSuchAlgorithmException, OperationNotSupportedException, CloneNotSupportedException {
        setContext(context);
        changeUserRole(req);
        changeUserStatus(req);
        setRoles(req);
        setUsers(req);
        String page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.USER_MANAGER_PAGE_PATH);
        SetWindowUrl.setUrl(page, req);
        return page;

    }

    /**
     * Set role HashMap for using at jsp page
     * @param  req      HttpServletRequest request
     * @throws          SQLException can be thrown during password validation
     */

    private void setRoles(HttpServletRequest req) throws SQLException {
        SQLBuilder sq = new SQLBuilder(new Role().table);
        ArrayList<Role> roles = roleDAO.get(sq.build());
        req.setAttribute("roles", roles);
    }

    /**
     * Set attribute "users" in request for using at jsp page regarding pagination, sort and search params
     * @param  req      HttpServletRequest request
     * @throws          SQLException can be thrown during password validation
     */
    private void setUsers(HttpServletRequest req) throws SQLException {
        SQLBuilder sq;
        if(req.getMethod().equals("POST")) {
            sq = prepareCatalogSQl(req);
            req.getSession().setAttribute(req.getParameter("command"), sq);
        }
        else{
            if (req.getSession().getAttribute(req.getParameter("command"))!=null)
                sq = (SQLBuilder) req.getSession().getAttribute(req.getParameter("command"));
            else {
                sq = prepareCatalogSQl(req);
            }
        }
        req.setAttribute("pagination", new PaginationManager(req, sq, userDAO));
        SortManager.SortManager(req, sq);
        ArrayList<User> users = userDAO.get(sq.build());
        req.setAttribute("users", users);
    }

    /**
     * Set role HashMap for using at jsp page
     * @param  req      HttpServletRequest request
     * @return          SQLSmartQuery class with data request params
     */
    private SQLBuilder prepareCatalogSQl(HttpServletRequest req) {
        SQLBuilder sq = new SQLBuilder(new User().table);
        String userSearchStr = req.getParameter("name");
        if (userSearchStr != null && !userSearchStr.equals("")) {
            sq.filter("first_name", userSearchStr, SQLBuilder.Operators.ILIKE).
                    filter("second_name", userSearchStr, SQLBuilder.Operators.ILIKE).
                    logicOperator(SQLBuilder.LogicOperators.OR).
                    filter("login", userSearchStr, SQLBuilder.Operators.ILIKE);
        }
        sq.order("login", SQLBuilder.SortOrder.ASC);
        return sq;
    }

    /**
     * Serve request for changing user role
     * @param  req      HttpServletRequest request
     * @throws          SQLException can be thrown during sql operations
     */
    private void changeUserRole(HttpServletRequest req) throws SQLException {
        if(req.getParameter("setRole")!=null && !req.getParameter("setRole").equals("")
                && req.getParameter("userId")!=null && !req.getParameter("userId").equals("")){
            User user;
            ErrorManager errorManager = new ErrorManager();
            Role role;
            user = userDAO.getOne(Integer.parseInt(req.getParameter("userId")));
            role = roleDAO.getOne(Integer.parseInt(req.getParameter("setRole")));
            if (user == null) errorManager.add("userId", "There isn't user with this id",
                    "Користувача з таким ID не існує");
            if (role ==null) errorManager.add("roleId", "There isn't role with this id",
                    "Ролі з таким ID не існує");

            if(!errorManager.getErrors().isEmpty()){
                req.setAttribute("errors", errorManager.getErrors());
                return;
            }
                user.setRole(role);
                userDAO.update(user);
            }
        }

    /**
     * Serve request for changing user status
     * @param  req      HttpServletRequest request
     * @throws          SQLException can be thrown during SQL operations
     */
    private void changeUserStatus(HttpServletRequest req) throws SQLException {
        if(req.getParameter("setActive")!=null && !req.getParameter("setActive").equals("")
                && req.getParameter("userId")!=null && !req.getParameter("userId").equals("")){
            User user;
            ErrorManager errorManager = new ErrorManager();
            user = userDAO.getOne(Integer.parseInt(req.getParameter("userId")));
            if (user==null) errorManager.add("userId", "There isn't user with this id",
                    "Користувача з таким ID не існує");
            if(!errorManager.getErrors().isEmpty()){
                req.setAttribute("errors", errorManager.getErrors());
                return;
            }
            user.setActive(req.getParameter("setActive").equals("true"));
            userDAO.update(user);
        }

    }

}