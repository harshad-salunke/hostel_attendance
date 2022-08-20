package com.example.hostel_application.models;

public class Attendance_data {
    int present;
    String description;
    String Time;
    String msg_id;
    public  Attendance_data(){

    }

    public Attendance_data(int present, String description, String time,String msg_id) {
        this.present = present;
        this.description = description;
        Time = time;
        this.msg_id=msg_id;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }




}
