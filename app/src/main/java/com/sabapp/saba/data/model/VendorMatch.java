package com.sabapp.saba.data.model;

public class VendorMatch {
    public String name;
    public String status;
    public String badgeColor;
    public String inventoryHex; // Add this! e.g., "#000080" for Navy

    public VendorMatch(String name, String status, String color, String invHex) {
        this.name = name;
        this.status = status;
        this.badgeColor = color;
        this.inventoryHex = invHex;
    }
}
