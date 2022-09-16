package com.student.hostel_application.models;

public class Complaints {
    String user_uid;
    String id;
    String complaint;
    String date;

    public  Complaints(){

    }

    public Complaints(String user_uid, String id, String complaint, String date) {
        this.user_uid = user_uid;
        this.id = id;
        this.complaint = complaint;
        this.date = date;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
