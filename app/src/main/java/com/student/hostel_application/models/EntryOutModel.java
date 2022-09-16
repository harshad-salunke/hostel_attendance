package com.student.hostel_application.models;

public class EntryOutModel {
    String id;
    String name;
    String user_id;
    String msg_token;
    String accepted;
    String to_date;
    String from_date;
    String resaon_msg;
    String msg_replay;
    public EntryOutModel(){

    }

    public EntryOutModel(String id, String name, String user_id, String msg_token, String accepted, String to_date, String from_date, String resaon_msg, String msg_replay) {
        this.id = id;
        this.name = name;
        this.user_id = user_id;
        this.msg_token = msg_token;
        this.accepted = accepted;
        this.to_date = to_date;
        this.from_date = from_date;
        this.resaon_msg = resaon_msg;
        this.msg_replay = msg_replay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMsg_token() {
        return msg_token;
    }

    public void setMsg_token(String msg_token) {
        this.msg_token = msg_token;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getResaon_msg() {
        return resaon_msg;
    }

    public void setResaon_msg(String resaon_msg) {
        this.resaon_msg = resaon_msg;
    }

    public String getMsg_replay() {
        return msg_replay;
    }

    public void setMsg_replay(String msg_replay) {
        this.msg_replay = msg_replay;
    }
}
