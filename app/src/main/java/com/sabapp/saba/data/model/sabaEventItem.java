package com.sabapp.saba.data.model;

import org.json.JSONObject;

import java.util.ArrayList;

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

    String capability_code;
    String capability_name;
    String capability_category;
    String capability_imageid;
    String capability_image_location;


    //values for vendor list
    String vendorbase_price;
    JSONObject vendorcapability_details;
    String vendorcapability_id;
    String vendorserviceimage_id;
    String vendorserviceimagelocation;
    String vendorid;

    String vendorname;
    String vendorcapabilityname;
    String vendorlocation;


    //for vendor assignments

    String event_idAssigned;
    String vendor_idAssigned;
    String capability_idAssigned;
    String statusAssigned;
    String agreed_priceAssigned;
    JSONObject contract_termsAssigned;
    String assigned_byAssigned;
    String time_assignedAssigned;
    String event_nameAssigned;

    String event_imagelocationAssigned;



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


    public String getcapability_code () {return capability_code;}

    public void setcapability_code(String name) {
        this.capability_code=name;
    }


    public String getcapability_name () {return capability_name;}

    public void setcapability_name(String name) {
        this.capability_name=name;
    }


    public String getcapability_category () {return capability_category;}

    public void setcapability_category(String name) {
        this.capability_category=name;
    }


    public String getcapability_imageid () {return capability_imageid;}

    public void setcapability_imageid(String name) {
        this.capability_imageid=name;
    }


    public String getcapability_image_location () {return capability_image_location;}
    public void setcapability_image_location(String name) {
        this.capability_image_location=name;
    }


    //FOR VENDOR VALUES

    public String getvendorbase_price () {return vendorbase_price;}

    public void setvendorbase_price(String name) {
        this.vendorbase_price=name;
    }

    public JSONObject getvendorcapability_details () {return vendorcapability_details;}

    public void setvendorcapability_details(JSONObject name) {
        this.vendorcapability_details=name;
    }

    public String getvendorcapability_id () {return vendorcapability_id;}

    public void setvendorcapability_id(String name) {
        this.vendorcapability_id=name;
    }


    public String getvendorserviceimage_id () {return vendorserviceimage_id;}

    public void setvendorserviceimage_id(String name) {
        this.vendorserviceimage_id=name;
    }

    public String getvendorserviceimagelocation () {return vendorserviceimagelocation;}

    public void setvendorserviceimagelocation(String name) {
        this.vendorserviceimagelocation=name;
    }

    public String getvendorid () {return vendorid;}

    public void setvendorid(String name) {
        this.vendorid=name;
    }

    public String getvendorname () {return vendorname;};
    public void setvendorname(String name) {
        this.vendorname=name;
    }
    public String getvendorcapabilityname () {return vendorcapabilityname;}
    public void setvendorcapabilityname(String name) {
        this.vendorcapabilityname=name;
    }

    public String getvendorlocation () {return vendorlocation;}
    public void setvendorlocation(String name) {
        this.vendorlocation=name;
    }



    //for vendor assignments

    public String getevent_idAssigned () {return event_idAssigned;}
    public void setevent_idAssigned(String name) {
        this.event_idAssigned=name;
    }

    public String getvendor_idAssigned () {return vendor_idAssigned;}
    public void setvendor_idAssigned(String name) {
        this.vendor_idAssigned=name;
    }

    public String getcapability_idAssigned () {return capability_idAssigned;}
    public void setcapability_idAssigned(String name) {
        this.capability_idAssigned=name;
    }

    public String getstatusAssigned () {return statusAssigned;}
    public void setstatusAssigned(String name) {
        this.statusAssigned=name;
    }

    public String getagreed_priceAssigned () {return agreed_priceAssigned;}
    public void setagreed_priceAssigned(String name) {
        this.agreed_priceAssigned=name;
    }

    public JSONObject getcontract_termsAssigned () {return contract_termsAssigned;}
    public void setcontract_termsAssigned(JSONObject name) {
        this.contract_termsAssigned=name;
    }

    public String getassigned_byAssigned () {return assigned_byAssigned;}
    public void setassigned_byAssigned(String name) {
        this.assigned_byAssigned=name;
    }

    public String gettime_assignedAssigned () {return time_assignedAssigned;}
    public void settime_assignedAssigned(String name) {
        this.time_assignedAssigned=name;
    }


    public String getevent_nameAssigned () {return event_nameAssigned;}

    public void setevent_nameAssigned(String name) {
        this.event_nameAssigned=name;
    }



    public String getevent_imagelocationAssigned () {return event_imagelocationAssigned;}

    public void setevent_imagelocationAssigned(String name) {
        this.event_imagelocationAssigned=name;
    }





}