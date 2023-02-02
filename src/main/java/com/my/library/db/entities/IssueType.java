package com.my.library.db.entities;

import java.util.Map;

public class IssueType extends Entity{

    {
        table = "issue_types";
    }
    private Map<String,String> issueType;

    public Float getPenalty() {
        return penalty;
    }

    public void setPenalty(Float penalty) {
        this.penalty = penalty;
    }

    private Float penalty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String,String> getIssueType() {
        return issueType;
    }

    public void setIssueType(Map<String,String> issueType) {
        this.issueType = issueType;
    }
}
