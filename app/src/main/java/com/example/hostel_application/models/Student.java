package com.example.hostel_application.models;

public class
Student {
    String uid;
    String Mobile_no;
    String Name;
    String floor_no;
    String Room_No;
    String clg_name;
    String clg_year;
    String clg_branch;
    String Gender;
    boolean isBlocked;
    String Resiter_Date;
    String msg_token;
    String parent_name;
    String parent_mobile;
    String adress;
    String gfm_name;
    String gfm_mobile;
    public  Student(){

    }

    public String getGfm_name() {
        return gfm_name;
    }

    public void setGfm_name(String gfm_name) {
        this.gfm_name = gfm_name;
    }

    public String getGfm_mobile() {
        return gfm_mobile;
    }

    public void setGfm_mobile(String gfm_mobile) {
        this.gfm_mobile = gfm_mobile;
    }

    public Student(String mobile_no, String name, String floor_no, String room_No, String gender,
                   String uid, String clg_name, boolean isBlocked, String clg_year, String clg_branch,
                   String Resiter_Date , String parent_name, String parent_mobile, String adress,
                   String msg_token, String gfm_name, String gfm_mobile
                   )
    {
        Mobile_no = mobile_no;
        Name = name;
        this.floor_no = floor_no;
        Room_No = room_No;
        Gender = gender;
        this.uid = uid;
        this.clg_name = clg_name;
        this.isBlocked=isBlocked;
        this.clg_branch=clg_branch;
        this.clg_year=clg_year;
        this.Resiter_Date=Resiter_Date;
        this.adress=adress;
        this.msg_token=msg_token;
        this.parent_mobile=parent_mobile;
        this.parent_name=parent_name;
        this.gfm_mobile=gfm_mobile;
        this.gfm_name=gfm_name;
    }

    public String getClg_year() {
        return clg_year;
    }

    public void setClg_year(String clg_year) {
        this.clg_year = clg_year;
    }

    public String getClg_branch() {
        return clg_branch;
    }

    public void setClg_branch(String clg_branch) {
        this.clg_branch = clg_branch;
    }

    public String getResiter_Date() {
        return Resiter_Date;
    }

    public void setResiter_Date(String resiter_Date) {
        Resiter_Date = resiter_Date;
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

    public String getMsg_token() {
        return msg_token;
    }

    public void setMsg_token(String msg_token) {
        this.msg_token = msg_token;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getParent_mobile() {
        return parent_mobile;
    }

    public void setParent_mobile(String parent_mobile) {
        this.parent_mobile = parent_mobile;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
