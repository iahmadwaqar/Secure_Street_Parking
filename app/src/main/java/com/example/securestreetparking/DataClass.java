package com.example.securestreetparking;

public class DataClass {

    private String date;
    private String time;
    private String url;

    public DataClass(){

    }
    public DataClass(String date, String time, String url) {
        this.date = date;
        this.time = time;
        this.url = url;
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

    public void setUrl(String url) {
        this.url = url;
    }
}
