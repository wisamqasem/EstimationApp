package com.jdeco.estimationapp.objects;

import java.util.ArrayList;

public class TestData {
    public static ArrayList<ApplicationDetails> getTestAppDetails()
    {
        ArrayList<ApplicationDetails> applicationDetailsArrayList = new ArrayList<>();
        ApplicationDetails app = new ApplicationDetails();
        app.setAppID("963258741");
        app.setCustomerName("مهند منير");
        app.setCustomerAddress("واد الجوز - امروؤ القيس");
      //  app.setBranch("1");
        app.setsBranch(10);
        app.setStatus("N");
        app.setAppDate("16-11-2021");
        app.setAppType("2");

        applicationDetailsArrayList.add(app);
        app = new ApplicationDetails();
        app.setAppID("985632165");
        app.setCustomerName("محمد عليان");
        app.setCustomerAddress("بيت صفافا- امروؤ القيس");
      //  app.setBranch("1");
        app.setsBranch(10);
        app.setStatus("N");
        app.setAppDate("19-11-2021");
        app.setAppType("3");

        applicationDetailsArrayList.add(app);

        return applicationDetailsArrayList;
    }
}
