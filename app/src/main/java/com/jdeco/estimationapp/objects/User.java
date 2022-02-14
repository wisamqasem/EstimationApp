package com.jdeco.estimationapp.objects;

public class User {
    int id;
    String username;
    String password;
    String token;
    String employeeNo;
    String safetySwitch;
    String fullName;

    public String getSafetySwitch() {
        return safetySwitch;
    }

    public void setSafetySwitch(String safetySwitch) {
        this.safetySwitch = safetySwitch;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }
}
