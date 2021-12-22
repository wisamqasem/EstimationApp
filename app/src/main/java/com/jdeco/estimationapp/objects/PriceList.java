package com.jdeco.estimationapp.objects;

public class PriceList {

    String priceListName;
    String priceListId;



    public PriceList() {
    }

    public PriceList(String priceListId, String priceListName) {
        this.priceListId = priceListId;
        this.priceListName = priceListName;
    }



    public String getPriceListName() {
        return priceListName;
    }

    public void setPriceListName(String priceListName) {
        this.priceListName = priceListName;
    }

    public String getPriceListId() {
        return priceListId;
    }

    public void setPriceListId(String priceListId) {
        this.priceListId = priceListId;
    }

    @Override
    public String toString() {
        return priceListName ;
    }
}
