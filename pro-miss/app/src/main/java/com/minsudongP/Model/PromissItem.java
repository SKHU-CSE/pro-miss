package com.minsudongP.Model;


public class PromissItem {
    private PromissType Type;
    private int user_id;
    private int Notification_id;
    private int Appointment_id;
    private int Notification_send;
    private String Notification_date;
    private String ProfileImageURl;
    private String Name;
    private String address;
    private String jibun;
    private String positionX;
    private String positionY;

    public int getAppointment_id() {
        return Appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        Appointment_id = appointment_id;
    }

    public int getNotification_send() {
        return Notification_send;
    }

    public void setNotification_send(int notification_send) {
        Notification_send = notification_send;
    }

    public String getNotification_date() {
        return Notification_date;
    }

    public void setNotification_date(String notification_date) {
        Notification_date = notification_date;
    }

    public int getNotification_id() {
        return Notification_id;
    }

    public void setNotification_id(int notification_id) {
        Notification_id = notification_id;
    }

    public PromissItem(PromissType Type, int id, String Profileimage, String Name) //FrendList
    {
        this.user_id=id;
        this.Type=Type;
        this.ProfileImageURl=Profileimage;
        this.Name=Name;
    }
    private String Date;
    private String Time;
    private String Money;
    private String Member; //지각한 멤버
    private String Place;

    public String getDate() {
        return Date;
    }



    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getMember() {
        return Member;
    }

    public void setMember(String member) {
        Member = member;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }


    public String getJibun() {
        return jibun;
    }

    public void setJibun(String jibun) {
        this.jibun = jibun;
    }

    public PromissType getType() {
        return Type;
    }

    public void setType(PromissType type) {
        Type = type;
    }

    public String getProfileImageURl() {
        return ProfileImageURl;
    }

    public void setProfileImageURl(String profileImageURl) {
        ProfileImageURl = profileImageURl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public PromissItem(PromissType type,String address,String jibun,String positionX,String positionY)
    {
        this.Type = type;
        this.jibun = jibun;
        this.address = address;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public PromissItem(PromissType type,int notification_id,int notification_send,String notification_date, String addressORdate, String jibunORtime, String positionXORPlace, String positionYORmoneyORnameORMember)
    {

        this.Notification_id=notification_id;
        this.Notification_send=notification_send;
        this.Notification_date=notification_date;
        if(type==PromissType.Time_Late)
        {
            this.Date=addressORdate;
            this.Time=jibunORtime;
            this.Place=positionXORPlace;
            this.Type=type;
            this.Money=positionYORmoneyORnameORMember;
        }
        else if(type==PromissType.Accept||type==PromissType.Cancel)
        {
            this.Name=positionYORmoneyORnameORMember;
            this.Date=addressORdate;
            this.Time=jibunORtime;
            this.Place=positionXORPlace;
            this.Type=type;
        }else if(type==PromissType.Late_Member)
        {
            this.Type=type;
            this.Date=addressORdate;
            this.Time=jibunORtime;
            this.Place=positionXORPlace;
            this.Member=positionYORmoneyORnameORMember;
        }
        else if(type==PromissType.Follow)
        {
            this.Type=type;
            this.Name=positionYORmoneyORnameORMember;
        }


    }

    public String getAddress() {
        return address;
    }

    public String getPositionX() {
        return positionX;
    }

    public void setPositionX(String positionX) {
        this.positionX = positionX;
    }

    public String getPositionY() {
        return positionY;
    }

    public void setPositionY(String positionY) {
        this.positionY = positionY;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public PromissItem(PromissType type,int notification_id,int notification_send,int appointment_id, String notification_date,String date,String time,String place) //New Appoint, Appoint_Start
    {
        this.Notification_date=notification_date;
        this.Notification_send=notification_send;
        this.Appointment_id=appointment_id;
        if(type==PromissType.New_Appoint) {
            this.Notification_id=notification_id;
            this.Date = date;
            this.Time = time;
            this.Place = place;
            this.Type = type;
        }
        else {
            this.Notification_id=notification_id;
            this.Date = date;
            this.Time = time;
            this.Place = place;
            this.Type = type;
        }
    }



}
