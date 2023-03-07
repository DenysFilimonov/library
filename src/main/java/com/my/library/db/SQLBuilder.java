package com.my.library.db;

import com.my.library.db.DAO.BookDAO;
import com.my.library.db.DAO.UserDAO;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.User;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLBuilder{

    public enum Operators {
        E("="),
        G(">"),
        GE(">="),
        LE("<="),
        L("<"),
        NE("<>"),
        LiKE("LIKE"),
        ILIKE("ILIKE");


        public final String label;


        Operators(String label) {
            this.label = label;
        }

    }

    public enum SortOrder {
        ASC("ASC"),
        DESC("DESC");

        public final String label;

        SortOrder(String label) {
            this.label = label;
        }

    }


    public enum GroupOperators{
        GROUP ("("),

        UNGROUP (")");

        public final String label;

        GroupOperators(String label) {
            this.label = label;
        }

    }

    public enum LogicOperators {
        AND("AND"),
        OR("OR"),
        NOT("NOT");


        public final String label;

        LogicOperators(String label) {
            this.label = label;
        }

    }

    private final String query = "SELECT ";

    private String table = "";

    private String filter="";
    private String order="";

    private String limit="";

    private String offset="";

    private String fields ="*";

    private String distinct="";

    private String SQLString;
    private String SQLStringCount;

    public String getSQLString() {
        return SQLString;
    }

    public String getSQLStringCount() {
        return SQLStringCount;
    }

    public SQLBuilder(String source){
        table = " FROM "+ source + " ";
    }

    public SQLBuilder source( String source) {
        table = " FROM "+ source + " ";
        return this;
    }

    public SQLBuilder filter(String fieldName, String fieldValue, Operators operator){
        if (filter.equals("")) filter += " WHERE";
        if (operator.equals(Operators.LiKE) || operator.equals(Operators.ILIKE)) filter +=" "+ fieldName+" "+ operator.label+" '%"+fieldValue+"%'";
        else filter +=" "+ fieldName+" "+ operator.label+" '"+fieldValue+"'";
        return this;
    }

    public SQLBuilder filter(String fieldName, Boolean fieldValue, Operators operator){
        if (filter.equals("")) filter += " WHERE";
        filter +=" "+ fieldName+" "+ operator.label+" "+fieldValue;
        return this;
    }

    public SQLBuilder filter(String fieldName, Integer fieldValue, Operators operator){
        if (filter.equals("")) filter += " WHERE";
        filter +=" "+ fieldName+" "+ operator.label+" "+fieldValue;
        return this;
    }

    public SQLBuilder field(String fieldName){
        if (fields=="*") fields=" "+fieldName+" ";
        else fields+=(" , "+fieldName+" ");
        return this;
    }

    public SQLBuilder logicOperator(LogicOperators operator){
        if (filter.equals("")) filter += " WHERE";
        filter+= " " + operator.label;
        return this;
    }

    public SQLBuilder groupOperator(GroupOperators operator){
        if (filter.equals("")) filter += " WHERE";
        filter+= " " + operator.label+" ";
        return this;
    }

    public SQLBuilder order(String fieldToOrder, SortOrder sortOrder){
        order = " ORDER BY " + fieldToOrder+" "+sortOrder.label;
        return this;
    }

    public SQLBuilder limit(int limit) {
        if (limit>0) this.limit = " LIMIT " + limit+" ";
        return this;
    }

    public SQLBuilder offset(int offset) {
        if (offset>=0) {this.offset = " OFFSET " + offset;}
        else this.offset = "";
        return this;
    }

    public SQLBuilder setDistinct(boolean distinct){
        if(distinct) this.distinct = " DISTINCT ";
        return this;
    }


    public SQLBuilder build(){
        SQLString = query+fields+table+filter+order+limit+offset;
        SQLStringCount = query+"COUNT ("+distinct+fields+") " + table + filter;
        return this;
    }

   public static void main(String[] args) throws SQLException {
        SQLBuilder sq = new SQLBuilder(new User().table)
                .filter("login", "admin' or 1=1 or 'a'='a", Operators.E);

       System.out.println(sq.build().SQLString);
       ArrayList<User> users = UserDAO.getInstance(ConnectionPool.dataSource).get(sq.build());
       for (User user : users) System.out.println(user.getLogin());


   }


}
