package com.example.hostel_application.models;

public class DeleteAcount {
  String name;
  String room_no;
  String msg;
  String uid;
  String Resiteration_date;
  String Date;
  public   DeleteAcount(){

  }

    public DeleteAcount(String name, String room_no, String msg, String uid, String resiteration_date, String date) {
        this.name = name;
        this.room_no = room_no;
        this.msg = msg;
        this.uid = uid;
        Resiteration_date = resiteration_date;
        Date = date;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getResiteration_date() {
        return Resiteration_date;
    }

    public void setResiteration_date(String resiteration_date) {
        Resiteration_date = resiteration_date;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
