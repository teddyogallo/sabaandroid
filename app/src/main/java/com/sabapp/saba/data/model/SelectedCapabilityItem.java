package com.sabapp.saba.data.model;

import java.io.Serializable;

public class SelectedCapabilityItem implements Serializable {
    private String code;
    private String name;

    public SelectedCapabilityItem(String code, String name) {
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
