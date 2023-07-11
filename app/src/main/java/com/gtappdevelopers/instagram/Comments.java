package com.gtappdevelopers.instagram;

public class Comments {

    String authourid,commnettext;

    public Comments(){

    }

    public Comments(String authourid, String commnettext) {
        this.authourid = authourid;

        this.commnettext = commnettext;
    }

    public String getAuthourid() {
        return authourid;
    }

    public void setAuthourid(String authourid) {
        this.authourid = authourid;
    }

    public String getCommnettext() {
        return commnettext;
    }

    public void setCommnettext(String commnettext) {
        this.commnettext = commnettext;
    }
}
