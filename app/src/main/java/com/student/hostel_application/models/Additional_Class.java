package com.student.hostel_application.models;

public class Additional_Class {
    String attendance_data;
    boolean update;
public  Additional_Class(){

}
    public Additional_Class(String attendance_data, boolean update) {
        this.attendance_data = attendance_data;
        this.update = update;
    }

    public String getAttendance_data() {
        return attendance_data;
    }

    public void setAttendance_data(String attendance_data) {
        this.attendance_data = attendance_data;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
