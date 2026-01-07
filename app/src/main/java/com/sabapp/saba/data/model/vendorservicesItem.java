package com.sabapp.saba.data.model;

public class vendorservicesItem {

    private String id;
    private String name;

    public vendorservicesItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return name; // This is what the Spinner will display
    }
}


