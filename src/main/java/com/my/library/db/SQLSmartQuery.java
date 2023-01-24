package com.my.library.db;

import java.util.Objects;

public class SQLQuery {

    private String query = "SELECT ";

    private String table = "";

    private String filter="";
    private String order="";

    private String limit="";

    private String offset="";

    public SQLQuery(){

    }

    public void source( String source) {table = "FROM "+ source + " ";}

    public void filter(String condition){
        filter = "WHERE "+condition+" ";
    }
    public void order(String sortOrder){
        order = "ORDER BY " + sortOrder+" ";
    }
    public void limit(int limit) {
        if (limit>0) this.limit = "LIMIT " + limit+" ";
    }

    public void offset(int offset) {
        if (offset>=0) this.offset = "OFFSET " + offset;
    }


    public String build(){
        System.out.println(query+" * "+table+filter+order+limit+offset);
        return query+" * "+table+filter+order+limit+offset;
    }
}
