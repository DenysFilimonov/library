package com.my.library.db;

public class SQLSmartQuery extends SQLQuery{

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

    public SQLSmartQuery(){

    }

    public void source( String source) {table = " FROM "+ source + " ";}

    public void filter(String fieldName, String fieldValue, Operators operator){
        if (filter.equals("")) filter += " WHERE";
        if (operator.equals(Operators.LiKE) || operator.equals(Operators.ILIKE)) filter +=" "+ fieldName+" "+ operator.label+" '%"+fieldValue+"%'";
        else filter +=" "+ fieldName+" "+ operator.label+" '"+fieldValue+"'";
    }

    public void filter(String fieldName, Boolean fieldValue, Operators operator){
        if (filter.equals("")) filter += " WHERE";
        filter +=" "+ fieldName+" "+ operator.label+" "+fieldValue;
    }

    public void filter(String fieldName, Integer fieldValue, Operators operator){
        if (filter.equals("")) filter += " WHERE";
        filter +=" "+ fieldName+" "+ operator.label+" "+fieldValue;
    }

    public void field(String fieldName){
        if (fields=="*") fields=" "+fieldName+" ";
        else fields+=(" , "+fieldName+" ");
    }

    public void logicOperator(LogicOperators operator){
        if (filter.equals("")) filter += " WHERE";
        filter+= " " + operator.label;
    }

    public void groupOperator(GroupOperators operator){
        if (filter.equals("")) filter += " WHERE";
        filter+= " " + operator.label+" ";
    }

    public void order(String fieldToOrder, SortOrder sortOrder){

        order = " ORDER BY " + fieldToOrder+" "+sortOrder.label;
    }
    public void limit(int limit) {
        if (limit>0) this.limit = " LIMIT " + limit+" ";
    }
    public void offset(int offset) {
        if (offset>=0) {this.offset = " OFFSET " + offset;}
        else this.offset = "";
    }

    public void setDistinct(boolean distinct){
        if(distinct) this.distinct = " DISTINCT ";
    }


    public String build(){
        return query+fields+table+filter+order+limit+offset;
    }

    public String buildCount(){
        return query+"COUNT ("+distinct+fields+") " + table + filter;
    }
}
