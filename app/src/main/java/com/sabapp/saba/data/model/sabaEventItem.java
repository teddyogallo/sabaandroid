package com.sabapp.saba.data.model;

import android.graphics.Bitmap;

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
    String event_location,event_city,event_state,postal_code,event_country,longitude,latitude,deposit_percentage,deposit_balance_percentage,eventcapability_name,event_vibe,event_type,event_vendor_name,event_vendor_base_price,event_allocated_time;


    //for vendor proposals

    String event_idAssignedProposal;
    String vendor_idAssignedProposal;
    String capability_idAssignedProposal;
    String statusAssignedProposal;
    String agreed_priceAssignedProposal;
    JSONObject contract_termsAssignedProposal;
    String assigned_byAssignedProposal;
    String time_assignedAssignedProposal;
    String event_nameAssignedProposal;

    String event_imagelocationAssignedProposal;

    String event_locationProposal,event_cityProposal,event_stateProposal,postal_codeProposal,event_countryProposal,longitudeProposal,latitudeProposal,deposit_percentageProposal,deposit_balanceProposal_percentage,eventcapability_nameProposal,event_vibeProposal,event_typeProposal,event_vendor_nameProposal,event_vendor_base_priceProposal,event_allocated_timeProposal;

    //for conversations start
    String businessname,status,productprice,details;
    byte[] decodedString;

    Bitmap imagebitmap;

    Integer templateImg;

    //for messages

    String message,chatusertype,sentmessage,sentmessagetime,sentmessagedate,sentmessagesender,sendermessagetitle, receivedmessage,receivedmessagetime,receivedmessagedate,receivedmessagesender,receivedmessagetitle,encodedbase64,image_id,price_tier;


    //for eventtwin recommendations
    String eventtwin_notificationtitle, eventtwin_notificationcontent, eventtwin_notificationtype,eventtwin_notificationtypevalue;

    public void setMessage(String message) {
        this.message=message;
    }

    public String getMessage() {return message;}

    public void setChatusertype(String message) {
        this.chatusertype=message;
    }
    public void setSentmessage(String message) {
        this.sentmessage=message;
    }
    public void setSentmessagetime(String message) {
        this.sentmessagetime=message;
    }
    public void setSentmessagedate(String message) {
        this.sentmessagedate=message;
    }
    public void setSentmessagesender(String message) {
        this.sentmessagesender=message;
    }
    public void setSentmessagetitle(String message) {
        this.sendermessagetitle=message;
    }

    public String getChatusertype() {return chatusertype;}
    public String getSentmessage() {return sentmessage;}
    public String getSentmessagetime() {return sentmessagetime;}
    public String getSentmessagedate() {return sentmessagedate;}
    public String getSentmessagesender() {return sentmessagesender;}
    public String getSentmessagetitle() {return sendermessagetitle;}

    public void setReceivedmessage(String message) {
        this.receivedmessage=message;
    }
    public void setReceivedmessagetime(String message) {
        this.receivedmessagetime=message;
    }
    public void setReceivedmessagedate(String message) {
        this.receivedmessagedate=message;
    }
    public void setReceivedmessagesender(String message) {
        this.receivedmessagesender=message;
    }
    public void setReceivedmessagetitle(String message){this.receivedmessagetitle=message;}


    public String getReceivedmessage() {return receivedmessage;}
    public String getReceivedmessagetime() {return receivedmessagetime;}
    public String getReceivedmessagedate() {return receivedmessagedate;}
    public String getReceivedmessagesender() {return receivedmessagesender;}
    public String getReceivedmessagetitle() {return receivedmessagetitle;}

    public void setEncodedstring(String encodedbase64) {
        this.encodedbase64=encodedbase64;
    }

    public String getEncodedstring() {return encodedbase64;}

    public void setImage_id(String image_id) {
        this.image_id =image_id;
    }

    public String getImage_id() {return image_id;}
    public void setPrice_tier(String price_tier) {
        this.price_tier =price_tier;
    }

    public String getPrice_tier() {return price_tier;}


    //start for conversation start
    public void setBytes(byte[] decodedString) {
        this.decodedString=decodedString;
    }

    public byte[] getBytes() {return decodedString;}


    public void setBusinessname(String businessname) {
        this.businessname =businessname;
    }

    public String getBusinessname() {return businessname;}

    public void setProductprice(String productprice) {
        this.productprice=productprice;
    }

    public String getProductprice() {return productprice;}
    public void setStatus(String status) {
        this.status=status;
    }

    public String getStatus() {return status;}

    public void setDetails(String details) {
        this.details =details;
    }

    public String getDetails() {return details;}

    public void setImagebitmap(Bitmap imagebitmap) {
        this.imagebitmap=imagebitmap;
    }

    public Bitmap getImagebitmap() {return imagebitmap;}

    public void setTemplateImg(Integer templateImg) {
        this.templateImg =templateImg;
    }

    public Integer getTemplateImg() {return templateImg;}





    //end for conversation start

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


    public String getevent_locationAssigned () {return event_location;}

    public void setevent_locationAssigned(String name) {
        this.event_location=name;


    }

    public String getevent_cityAssigned () {return event_city;}

    public void setevent_cityAssigned(String name) {
        this.event_city=name;


    }


    public String getevent_stateAssigned () {return event_state;}

    public void setevent_stateAssigned(String name) {
        this.event_state=name;


    }

    public String getpostal_codeAssigned () {return postal_code;}

    public void setpostal_codeAssigned(String name) {
        this.postal_code=name;


    }




    public String getevent_countryAssigned () {return event_country;}

    public void setevent_countryAssigned(String name) {
        this.event_country=name;


    }


    public String getlongitudeAssigned () {return longitude;}

    public void setlongitudeAssigned(String name) {
        this.longitude=name;


    }

    public String getlatitudeAssigned () {return latitude;}

    public void setlatitudeAssigned(String name) {
        this.latitude=name;


    }



    public String getdeposit_percentageAssigned () {return deposit_percentage;}

    public void setdeposit_percentageAssigned(String name) {
        this.deposit_percentage=name;


    }



    public String getdeposit_balance_percentageAssigned () {return deposit_balance_percentage;}

    public void setdeposit_balance_percentageAssigned(String name) {
        this.deposit_balance_percentage=name;


    }



    public String geteventcapability_nameAssigned () {return eventcapability_name;}

    public void seteventcapability_nameAssigned(String name) {
        this.eventcapability_name=name;


    }


    public String getevent_vibeAssigned () {return event_vibe;}

    public void setevent_vibeAssigned(String name) {
        this.event_vibe=name;


    }



    public String getevent_typeAssigned () {return event_type;}

    public void setevent_typeAssigned(String name) {
        this.event_type=name;


    }


    public String getevent_vendor_nameAssigned () {return event_vendor_name;}

    public void setevent_vendor_nameAssigned(String name) {
        this.event_vendor_name=name;


    }


    public String getevent_vendor_base_priceAssigned () {return event_vendor_base_price;}

    public void setevent_vendor_base_priceAssigned(String name) {
        this.event_vendor_base_price=name;


    }


    public String getevent_allocated_timeAssigned () {return event_allocated_time;}

    public void setevent_allocated_timeAssigned(String name) {
        this.event_allocated_time=name;


    }


    //end of for vendor assignments
    //start of for vendor proposals

    public String getevent_idAssignedProposal () {return event_idAssignedProposal;}
    public void setevent_idAssignedProposal(String name) {
        this.event_idAssignedProposal=name;
    }

    public String getvendor_idAssignedProposal () {return vendor_idAssignedProposal;}
    public void setvendor_idAssignedProposal(String name) {
        this.vendor_idAssignedProposal=name;
    }

    public String getcapability_idAssignedProposal () {return capability_idAssignedProposal;}
    public void setcapability_idAssignedProposal(String name) {
        this.capability_idAssignedProposal=name;
    }

    public String getstatusAssignedProposal () {return statusAssignedProposal;}
    public void setstatusAssignedProposal(String name) {
        this.statusAssignedProposal=name;
    }

    public String getagreed_priceAssignedProposal () {return agreed_priceAssignedProposal;}
    public void setagreed_priceAssignedProposal(String name) {
        this.agreed_priceAssignedProposal=name;
    }

    public JSONObject getcontract_termsAssignedProposal () {return contract_termsAssignedProposal;}
    public void setcontract_termsAssignedProposal(JSONObject name) {
        this.contract_termsAssignedProposal=name;
    }

    public String getassigned_byAssignedProposal () {return assigned_byAssignedProposal;}
    public void setassigned_byAssignedProposal(String name) {
        this.assigned_byAssignedProposal=name;
    }

    public String gettime_assignedAssignedProposal () {return time_assignedAssignedProposal;}
    public void settime_assignedAssignedProposal(String name) {
        this.time_assignedAssignedProposal=name;
    }


    public String getevent_nameAssignedProposal () {return event_nameAssignedProposal;}

    public void setevent_nameAssignedProposal(String name) {
        this.event_nameAssignedProposal=name;
    }



    public String getevent_imagelocationAssignedProposal () {return event_imagelocationAssignedProposal;}

    public void setevent_imagelocationAssignedProposal(String name) {
        this.event_imagelocationAssignedProposal=name;


    }



    public String getevent_locationAssignedProposal () {return event_locationProposal;}

    public void setevent_locationAssignedProposal(String name) {
        this.event_locationProposal=name;


    }

    public String getevent_cityAssignedProposal () {return event_cityProposal;}

    public void setevent_cityAssignedProposal(String name) {
        this.event_cityProposal=name;


    }


    public String getevent_stateAssignedProposal () {return event_stateProposal;}

    public void setevent_stateAssignedProposal(String name) {
        this.event_stateProposal=name;


    }

    public String getpostal_codeAssignedProposal () {return postal_codeProposal;}

    public void setpostal_codeAssignedProposal(String name) {
        this.postal_codeProposal=name;


    }




    public String getevent_countryAssignedProposal () {return event_countryProposal;}

    public void setevent_countryAssignedProposal(String name) {
        this.event_countryProposal=name;


    }


    public String getlongitudeAssignedProposal () {return longitudeProposal;}

    public void setlongitudeAssignedProposal(String name) {
        this.longitudeProposal=name;


    }

    public String getlatitudeAssignedProposal () {return latitudeProposal;}

    public void setlatitudeAssignedProposal(String name) {
        this.latitudeProposal=name;


    }



    public String getdeposit_percentageAssignedProposal () {return deposit_percentageProposal;}

    public void setdeposit_percentageAssignedProposal(String name) {
        this.deposit_percentageProposal=name;


    }



    public String getdeposit_balance_percentageAssignedProposal () {return deposit_balanceProposal_percentage;}

    public void setdeposit_balance_percentageAssignedProposal(String name) {
        this.deposit_balanceProposal_percentage=name;


    }



    public String geteventcapability_nameAssignedProposal () {return eventcapability_nameProposal;}

    public void seteventcapability_nameAssignedProposal(String name) {
        this.eventcapability_nameProposal=name;


    }


    public String getevent_vibeAssignedProposal () {return event_vibeProposal;}

    public void setevent_vibeAssignedProposal(String name) {
        this.event_vibeProposal=name;


    }



    public String getevent_typeAssignedProposal () {return event_typeProposal;}

    public void setevent_typeAssignedProposal(String name) {
        this.event_typeProposal=name;


    }


    public String getevent_vendor_nameAssignedProposal () {return event_vendor_nameProposal;}

    public void setevent_vendor_nameAssignedProposal(String name) {
        this.event_vendor_nameProposal=name;


    }


    public String getevent_vendor_base_priceAssignedProposal () {return event_vendor_base_priceProposal;}

    public void setevent_vendor_base_priceAssignedProposal(String name) {
        this.event_vendor_base_priceProposal=name;


    }


    public String getevent_allocated_timeAssignedProposal () {return event_allocated_timeProposal;}

    public void setevent_allocated_timeAssignedProposal(String name) {
        this.event_allocated_timeProposal=name;


    }


    //end of vendor proposals


    //start for event twin notification values

    public String geteventtwin_notificationtitle () {return eventtwin_notificationtitle;}

    public void seteventtwin_notificationtitle(String name) {
        this.eventtwin_notificationtitle=name;


    }


    public String geteventtwin_notificationcontent () {return eventtwin_notificationcontent;}

    public void seteventtwin_notificationcontent(String name) {
        this.eventtwin_notificationcontent=name;


    }


    public String geteventtwin_notificationtype () {return eventtwin_notificationtype;}

    public void seteventtwin_notificationtype(String name) {
        this.eventtwin_notificationtype=name;


    }




    public String geteventtwin_notificationtypevalue () {return eventtwin_notificationtypevalue;}

    public void seteventtwin_notificationtypevalue(String name) {
        this.eventtwin_notificationtypevalue=name;


    }

    //end of for eventtwin notification values





}