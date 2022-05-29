package com.arraykart.b2b.Retrofit.ModelClass;

import com.google.gson.annotations.Expose;

public class BugReport {

    @Expose
    private String bug_detail;

    public BugReport(String bug_detail) {
        this.bug_detail = bug_detail;
    }

    public String getBug_detail() {
        return bug_detail;
    }

    public void setBug_detail(String bug_detail) {
        this.bug_detail = bug_detail;
    }
}
