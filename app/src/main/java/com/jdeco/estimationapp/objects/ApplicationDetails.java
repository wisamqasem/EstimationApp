package com.jdeco.estimationapp.objects;

import android.util.Log;

import androidx.annotation.NonNull;

public class ApplicationDetails
{
    String customerName;
    String customerID;
    String customerAddress;
    String appID;
    String appDate;
    String appType;
    String statusCode;
    String status;
    String branch;
    String sBranch;
    String noOfPhases;
    String propertyType;
    String serviceClass;
    String accountNo;
    String username;
    String location;
    String phone;
    String rowId;
    String prjRowId;
    String ticketStatus;
    Warehouse warehouse ;
    PriceList priceList ;
    ProjectType projectType;
    String phase1Meter;
    String phase3Meter;
    String old_system_no;
    String  old_customer_name;
    String old_id_number;
    String id_number;
    String account_no;
    String appl_type_code;
    String status_code;
    String status_note;
    String service_status;
    String usage_type;
    String no_of_phase;
    String service_no;
    String property_type;
    String service_class;
    String to_user_id;
    String noofservices;
    String meter_type;
    String meter_no;
    String install_date;
    String last_read;
    String last_read_date;
    String last_qty;
    String notes;
    String noteLookUp;
    String sync = "0";
    String currentRead;
    String employeeNotes;
    String actionCode;
    String actionName;




    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }


    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getCurrentRead() {
        return currentRead;
    }

    public void setCurrentRead(String currentRead) {
        this.currentRead = currentRead;
    }

    public String getEmployeeNotes() {
        return employeeNotes;
    }

    public void setEmployeeNotes(String employeeNotes) {
        this.employeeNotes = employeeNotes;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getNoteLookUp() {
        return noteLookUp;
    }

    public void setNoteLookUp(String noteLookUp) {
        this.noteLookUp = noteLookUp;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public PriceList getPriceList() {
        return priceList;
    }

    public void setPriceList(PriceList priceList) {
        this.priceList = priceList;
    }

    public String getMeter_no() {
        return meter_no;
    }

    public void setMeter_no(String meter_no) {
        this.meter_no = meter_no;
    }

    public String getOld_system_no() {
        return old_system_no;
    }

    public void setOld_system_no(String old_system_no) {
        this.old_system_no = old_system_no;
    }

    public String getOld_customer_name() {
        return old_customer_name;
    }

    public void setOld_customer_name(String old_customer_name) {
        this.old_customer_name = old_customer_name;
    }

    public String getOld_id_number() {
        return old_id_number;
    }

    public void setOld_id_number(String old_id_number) {
        old_id_number = old_id_number;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getAppl_type_code() {
        return appl_type_code;
    }

    public void setAppl_type_code(String appl_type_code) {
        this.appl_type_code = appl_type_code;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus_note() {
        return status_note;
    }

    public void setStatus_note(String status_note) {
        this.status_note = status_note;
    }

    public String getService_status() {
        return service_status;
    }

    public void setService_status(String service_status) {
        this.service_status = service_status;
    }

    public String getUsage_type() {
        return usage_type;
    }

    public void setUsage_type(String usage_type) {
        this.usage_type = usage_type;
    }

    public String getNo_of_phase() {
        return no_of_phase;
    }

    public void setNo_of_phase(String no_of_phase) {
        this.no_of_phase = no_of_phase;
    }

    public String getService_no() {
        return service_no;
    }

    public void setService_no(String service_no) {
        this.service_no = service_no;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getService_class() {
        return service_class;
    }

    public void setService_class(String service_class) {
        this.service_class = service_class;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getNoofservices() {
        return noofservices;
    }

    public void setNoofservices(String noofservices) {
        this.noofservices = noofservices;
    }

    public String getMeter_type() {
        return meter_type;
    }

    public void setMeter_type(String meter_type) {
        this.meter_type = meter_type;
    }

    public String getInstall_date() {
        return install_date;
    }

    public void setInstall_date(String install_date) {
        this.install_date = install_date;
    }

    public String getLast_read() {
        return last_read;
    }

    public void setLast_read(String last_read) {
        this.last_read = last_read;
    }

    public String getLast_read_date() {
        return last_read_date;
    }

    public void setLast_read_date(String last_read_date) {
        this.last_read_date = last_read_date;
    }

    public String getLast_qty() {
        return last_qty;
    }

    public void setLast_qty(String last_qty) {
        this.last_qty = last_qty;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhase1Meter() {
        return phase1Meter;
    }

    public void setPhase1Meter(String phase1Meter) {
        this.phase1Meter = phase1Meter;
    }

    public String getPhase3Meter() {
        return phase3Meter;
    }

    public void setPhase3Meter(String phase3Meter) {
        this.phase3Meter = phase3Meter;
    }






    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getPrjRowId() {
        return prjRowId;
    }

    public void setPrjRowId(String prjRowId) {
        this.prjRowId = prjRowId;
    }

    int isSync;

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(String appDate) {

        this.appDate = appDate;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {

        this.branch = branch;
    }

    public String getsBranch() {
        return sBranch;
    }

    public void setsBranch(String sBranch) {
        this.sBranch = sBranch;
    }

    public String getNoOfPhases() {
        return noOfPhases;
    }

    public void setNoOfPhases(String noOfPhases) {
        this.noOfPhases = noOfPhases;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }




}
