package com.sabapp.saba.data.model;

public class EventItem {
    public String time;
    public String title;
    public String vendorName;
    public String status; // "Booked", "Pending", "Action Required"
    public boolean isBooked;

    public EventItem(String time, String title, String vendorName, String status, boolean isBooked) {
        this.time = time;
        this.title = title;
        this.vendorName = vendorName;
        this.status = status;
        this.isBooked = isBooked;
    }
}
