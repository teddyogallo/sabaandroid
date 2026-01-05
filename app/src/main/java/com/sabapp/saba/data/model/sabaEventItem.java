package com.sabapp.saba.data.model;

public class sabaEventItem {

    private String product_name;
    private String product_price;
    private String product_id;
    private String product_numberavailable;
    private String product_paymentoptions;
    private String product_iamgestring;
    private String product_imageid;
    private String product_description;
    private String product_datestored;
    private String product_category;

    String eventUserid;
    String eventName;
    String eventTime;
    String eventLocation;

    String eventBudget;
    String budgetSpent;

    String setupStatus;
    String eventStatus;
    String plannerId;

    String eventimageId;
    String eventimageLocation;
    String eventimageEncoded;
    String time_Setup;
    String event_Id ;



    public String geteventUserid() {return eventUserid;}

    public void seteventUserid(String name) {
        this.eventUserid=name;
    }
    public String geteventName() {return eventName;}

    public void seteventName(String name) {
        this.eventName=name;
    }
    public String geteventTime() {return eventTime;}

    public void seteventTime(String name) {
        this.eventTime=name;
    }
    public String geteventLocation() {return eventLocation;}

    public void seteventLocation(String name) {
        this.eventLocation=name;
    }

    public String geteventBudget() {return eventBudget;}

    public void seteventBudget(String name) {
        this.eventBudget=name;
    }
    public String getbudgetSpent() {return budgetSpent;}

    public void setbudgetSpent(String name) {
        this.budgetSpent=name;
    }

    public String getsetupStatus() {return setupStatus;}

    public void setsetupStatus(String name) {
        this.setupStatus=name;
    }
    public String geteventStatus() {return eventStatus;}

    public void seteventStatus(String name) {
        this.eventStatus=name;
    }
    public String getplannerId() {return plannerId;}

    public void setplannerId(String name) {
        this.plannerId=name;
    }

    public String geteventimageId() {return eventimageId;}

    public void seteventimageId(String name) {
        this.eventimageId=name;
    }
    public String geteventimageLocation() {return eventimageLocation;}

    public void seteventimageLocation(String name) {
        this.eventimageLocation=name;
    }
    public String geteventimageEncoded() {return eventimageEncoded;}

    public void seteventimageEncoded(String name) {
        this.eventimageEncoded=name;
    }
    public String gettime_Setup() {return time_Setup;}

    public void settime_Setup(String name) {
        this.time_Setup=name;
    }
    public String getevent_Id () {return event_Id;}

    public void setevent_Id(String name) {
        this.event_Id=name;
    }



}