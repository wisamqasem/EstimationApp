package com.jdeco.estimationapp.objects;
import androidx.annotation.NonNull;

public class EstimationItem {

    String itemCode;
    String itemDescription;
    String itemAmount;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
    }

    @NonNull
    @Override
    public String toString() {
        return this.itemDescription;
    }
}
