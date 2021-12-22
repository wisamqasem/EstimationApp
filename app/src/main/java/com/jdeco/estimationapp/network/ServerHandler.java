package com.jdeco.estimationapp.network;

import android.content.Context;
import android.provider.ContactsContract;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.ResultCode;
import com.jdeco.estimationapp.objects.TestData;
import com.jdeco.estimationapp.operations.Database;

import java.util.ArrayList;

public class ServerHandler {





    //get applications from the server
    public ResultCode getApplicationsFromServer(Context context)
    {
        ResultCode resultCode = new ResultCode();
        Database database = new Database(context);

        resultCode.setResultCode("1");
        resultCode.setResultMsg(context.getResources().getString(R.string.load_success_msg));

        //check if result successfull
        if(resultCode.getResultCode().equals("1"))
        {
            ArrayList<ApplicationDetails> list = TestData.getTestAppDetails();
            //loop on data and insert into applications table
            for(int i=0;i< list.size();i++)
            {
                //check record is exist in applications table
                if(!database.isItemExist(Database.APPLICATIONS_TABLE,"appId",list.get(i).getAppID())) {
                    //insert into database
                    database.insertNewApplication(list.get(i));
                }
            }
        }
        else
        {
            resultCode.setResultCode("1");
            resultCode.setResultMsg(context.getResources().getString(R.string.load_failed_msg));
        }

        return resultCode;
    }

}
