package com.example.hostel_application.models;

public class Student {
    String Mobile_no;
    String Name;
    String floor_no;
    String Room_No;
    String Gender;
    String uid;
    String clg_name;
    boolean isBlocked;
    public  Student(){

    }
    public Student(String mobile_no, String name, String floor_no, String room_No, String gender, String uid, String clg_name,boolean isBlocked) {
        Mobile_no = mobile_no;
        Name = name;
        this.floor_no = floor_no;
        Room_No = room_No;
        Gender = gender;
        this.uid = uid;
        this.clg_name = clg_name;
        this.isBlocked=isBlocked;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getMobile_no() {
        return Mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        Mobile_no = mobile_no;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFloor_no() {
        return floor_no;
    }

    public void setFloor_no(String floor_no) {
        this.floor_no = floor_no;
    }

    public String getRoom_No() {
        return Room_No;
    }

    public void setRoom_No(String room_No) {
        Room_No = room_No;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getClg_name() {
        return clg_name;
    }

    public void setClg_name(String clg_name) {
        this.clg_name = clg_name;
    }
}
