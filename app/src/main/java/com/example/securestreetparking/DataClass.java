package com.example.securestreetparking;

public class DataClass {

    private String date;
    private String time;
    private String url;
    private String threat="no";

    public DataClass() {

    }

    public DataClass(String date, String time, String url, String threat) {
        this.date = date;
        this.time = time;
        this.url = url;
        this.threat = threat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public String getThreat() {
        return threat;
    }

    public void setThreat(String threat) {
        this.threat = threat;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
