package com.gtappdevelopers.instagram;

public class chat {

    String message,sendid,receiverid,time;

    public chat()
    {

    }
    public chat(String message, String sendid, String receiverid, String time) {
        this.message = message;
        this.sendid = sendid;
        this.receiverid = receiverid;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendid() {
        return sendid;
    }

    public void setSendid(String sendid) {
        this.sendid = sendid;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
