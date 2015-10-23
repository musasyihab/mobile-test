package com.musasyihab.timeline.model;

public class Report {
    String month;
    int completed;
    int snoozed;
    int overdue;

    public Report() {
    }

    public Report(String month, int completed, int snoozed, int overdue) {
        this.month = month;
        this.completed = completed;
        this.snoozed = snoozed;
        this.overdue = overdue;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getSnoozed() {
        return snoozed;
    }

    public void setSnoozed(int snoozed) {
        this.snoozed = snoozed;
    }

    public int getOverdue() {
        return overdue;
    }

    public void setOverdue(int overdue) {
        this.overdue = overdue;
    }

    public int getTotal(){
        return completed+snoozed+overdue;
    }

    public int getCompletedPercent(){
        return 10000*getCompleted()/getTotal();
    }

    public int getSnoozedPercent(){
        return 10000*(getCompleted()+getSnoozed())/getTotal();
    }
}
