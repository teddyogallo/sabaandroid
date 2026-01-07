package com.sabapp.saba.data.model;

import java.io.Serializable;

public class SelectedVendorItem implements Serializable {
    private String code;
    private String name;

    public SelectedVendorItem(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
