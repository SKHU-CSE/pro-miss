package com.minsudongP.Singletone;

public class UserInfor {

    public static UserInfor shared= new UserInfor();

    String id_num;
    String ID;
    String Pwd;
    String Name;
    String profile_img;
    int money;
    int appoint_num;
    int success_appoint_num;

    String appintment_date;
    int appointment_status;
    String appointment_address;
    int appointment_id;
    String appintment_time;

    public String getAppintment_time() {
        return appintment_time;
    }

    public void setAppintment_time(String appintment_time) {
        this.appintment_time = appintment_time;
    }

    public int getAppointment_status() {
        return appointment_status;
    }

    public void setAppointment_status(int appointment_status) {
        this.appointment_status = appointment_status;
    }

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getAppintment_date() {
        return appintment_date;
    }

    public void setAppintment_date(String appintment_date) {
        this.appintment_date = appintment_date;
    }



    public String getAppointment_address() {
        return appointment_address;
    }

    public void setAppointment_address(String appointment_address) {
        this.appointment_address = appointment_address;
    }

    public String getId_num() {
        return id_num;
    }

    public void setId_num(String id_num) {
        this.id_num = id_num;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getAppoint_num() {
        return appoint_num;
    }

    public void setAppoint_num(int appoint_num) {
        this.appoint_num = appoint_num;
    }

    public int getSuccess_appoint_num() {
        return success_appoint_num;
    }

    public void setSuccess_appoint_num(int success_appoint_num) {
        this.success_appoint_num = success_appoint_num;
    }

    public String getNum() {
        return id_num;
    }

    public void setNum(String num) {
        id_num = num;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPwd() {
        return Pwd;
    }

    public void setPwd(String pwd) {
        Pwd = pwd;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
