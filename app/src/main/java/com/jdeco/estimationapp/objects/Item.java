package com.jdeco.estimationapp.objects;

public class Item {
    String id;
    String itemCode;
    String inventoryItemCode;
    String itemName;
    int itemAmount;
    String templateId;
    String allowDelete;
    String allowEdit;
    PriceList pricList;
    Warehouse warehouse;
Boolean checked = true;
    String appId;
    int templateAmount;

    public int getTemplateAmount() {
        return templateAmount;
    }

    public void setTemplateAmount(int templateAmount) {
        this.templateAmount = templateAmount;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public PriceList getPricList() {
        return pricList;
    }

    public void setPricList(PriceList pricList) {
        this.pricList = pricList;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public String getAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(String allowDelete) {
        this.allowDelete = allowDelete;
    }

    public String getAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(String allowEdit) {
        this.allowEdit = allowEdit;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Item() {
        this.id = "0";
        this.itemName = "";
        this.itemAmount = 0;
    }

    public Item(String id, String itemName, int itemAmount) {
        this.id = id;
        this.itemName = itemName;
        this.itemAmount = itemAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getInventoryItemCode() {
        return inventoryItemCode;
    }

    public void setInventoryItemCode(String inventoryItemCode) {
        this.inventoryItemCode = inventoryItemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }



    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    public int incressAmount(){
        itemAmount+=1;
        return itemAmount;
    }

    public int incressAmount10(){
        itemAmount+=10;
        return itemAmount;
    }

    public int decressAmount(){
        itemAmount-=1;
        if(itemAmount<1)itemAmount= 1;
        return itemAmount;
    }

    public int decressAmount10(){
        itemAmount-=10;
        if(itemAmount<1)itemAmount= 1;
        return itemAmount;
    }

    @Override
    public String toString() {
        return  itemName + " , " + itemCode.substring(3,11);
    }


}


