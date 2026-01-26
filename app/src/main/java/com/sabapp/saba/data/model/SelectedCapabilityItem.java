package com.sabapp.saba.data.model;

import java.io.Serializable;

public class SelectedCapabilityItem implements Serializable {
    private String code;
    private String name;

    private double valuefinal;
    private double availablebudget;

    private int selectedValue; // value from SeekBar
    private double allocatedAmount;   // FINAL money value

    public SelectedCapabilityItem(String code, String name) {
        this.code = code;
        this.name = name;
        this.selectedValue = 0;
        this.allocatedAmount = 0;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public int getSelectedValue() { return selectedValue; }
    public void setSelectedValue(int selectedValue) {
        this.selectedValue = selectedValue;
    }

    public double getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(double allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }



    public double getCapabilityvalue() { return valuefinal; }
    public void setCapabilityvalue(double selectedValue) {
        this.valuefinal = selectedValue;
    }


}
