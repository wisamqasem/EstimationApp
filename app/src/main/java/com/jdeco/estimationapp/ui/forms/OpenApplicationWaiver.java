package com.jdeco.estimationapp.ui.forms;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.objects.Item;
import com.jdeco.estimationapp.objects.ProjectType;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.GeneralFunctions;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OpenApplicationWaiver extends AppCompatActivity {



TextView appID,customerNameTB,branch,appType,phoneTB,addressTB,old_customer_nameTV,customer_nameTV,appl_date,status,service_status,sub_branch,service_no,service_class,meter_no,meter_type,install_date;
TextView last_read,last_read_date,notes;

    Button submitBtn;
Spinner situationsSP;
    ProgressDialog progress;
    Session session;
    Database dbObject;

    EditText note ;

    ApplicationDetails applicationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_application_waiver);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("APPLICATION DETAILS");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Add back arrow in action bar
        }



        notes = findViewById(R.id.notes);
        last_read_date = findViewById(R.id.last_read_date);
        last_read= findViewById(R.id.last_read);
        install_date= findViewById(R.id.install_date);
        meter_type= findViewById(R.id.meter_type);
        meter_no= findViewById(R.id.meter_no);
        service_class= findViewById(R.id.service_class);
        service_no= findViewById(R.id.service_no);
        sub_branch= findViewById(R.id.sub_branch);
        service_status= findViewById(R.id.service_status);
        status= findViewById(R.id.status);
        appl_date= findViewById(R.id.appl_date);
        customer_nameTV= findViewById(R.id.customer_nameTV);
        old_customer_nameTV= findViewById(R.id.old_customer_nameTV);
        addressTB= findViewById(R.id.addressTB);
        phoneTB= findViewById(R.id.phoneTB);

        appType= findViewById(R.id.appType);
        branch= findViewById(R.id.branch);
        customerNameTB= findViewById(R.id.customerNameTB);
        appID= findViewById(R.id.appID);


        note = (EditText)findViewById(R.id.note);

        situationsSP = (Spinner)findViewById(R.id.situations);
        submitBtn = (Button)findViewById(R.id.submitBtn) ;

        dbObject = new Database(this);
        session = new Session(this);
        applicationDetails = new ApplicationDetails();

        //get application details
        applicationDetails = dbObject.getApplications(session.getValue("APP_ID"), "N").get(0);




        assignData(applicationDetails);


        //submit data to the server
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                progress = new ProgressDialog(OpenApplicationWaiver.this);
                progress.setTitle(getResources().getString(R.string.please_wait));
                progress.setCancelable(true);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();



                    CharSequence date = DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date());
                    //edit.................................................................................
                    // check if the edit text null
                    String bodyData = "{\n" +
                            "\"application\": {\n" +
                            "\"applRowId\": " + applicationDetails.getRowId() + ",\n" +//applicationDetails.getAppID()
                            "\"actionCode\": " + 1 + ",\n" +//applicationDetails.getPrjRowId()
                            "\"employeeNo\": \"" + session.getValue("emp_id") + "\",\n" +
                            "\"applId\": " + applicationDetails.getAppID() + ",\n" +//applicationDetails.getAppID()
                            "\"safetySwitch\": "+session.getValue("saftey_switch")+",\n" +
                            "\"lastRead\": "+2500+",\n" +
                            "\"notes\": " +note.getText().toString()  + ",\n" +
                            "\"username\": \"" + applicationDetails.getUsername() + "\",\n" +
                            "\"lastReadDate\": \"" + date + "\",\n" +
                            "}}\n";

                    Log.d("bodyData : ",bodyData);
                    submitMaterialsToServer(bodyData);

                }





        });

        ArrayList<String> options=new ArrayList<String>();
        options.add("جيد");
        options.add("غير جيد");


        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, options);
        //add adapter to spinner
        situationsSP.setAdapter(adapter);


    }

    private void submitMaterialsToServer(String bodyData) {
        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;


        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("submitMaterialsToServer", "Response: " + response);
                try {
                    JSONObject submitData = new JSONObject(response);
                    Log.d("submitMaterialsToServer", "Response: " + (submitData.getString("request_response").equals("Success")));
                    if (submitData.getString("request_response").equals("Success...!!!!")) {

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();//display the response submit success
                        applicationDetails.setTicketStatus("D");
                        dbObject.updateApplicationStatus(applicationDetails.getAppID(), applicationDetails.getTicketStatus());
                        Intent i = new Intent(OpenApplicationWaiver.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_failed), Toast.LENGTH_LONG).show();//display the response submit failed
                        progress.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progress.setCancelable(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("getItemsFromServer", "Error Login Request :" + error.toString());
                progress.dismiss();
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                //parameters
                // params.put("username", "jd");
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_processChangeName);
                params.put("data", bodyData);

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    public void goBack() {

        Intent back = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(back);
    }

    void assignData(ApplicationDetails task){

        phoneTB.setText(task.getPhone());
        addressTB.setText(task.getCustomerAddress());
        old_customer_nameTV.setText(task.getOld_customer_name());
        customer_nameTV.setText(task.getCustomerName());
        appl_date.setText(task.getAppDate());
        status.setText(task.getStatus());
        service_status.setText(task.getService_status());
       // sub_branch.setText(task.gets);
        service_no.setText(task.getService_no());
        service_class.setText(task.getService_class());
        meter_no.setText(String.valueOf(task.getMeter_no()));
        meter_type.setText(task.getMeter_type());
        install_date.setText(task.getInstall_date());
        last_read.setText(task.getLast_read());
        last_read_date.setText(task.getLast_read_date());
        appID.setText(task.getAppID());
        customerNameTB.setText(task.getCustomerName());
        branch.setText(task.getBranch());
        sub_branch.setText(task.getsBranch());
        appType.setText(task.getAppType());


        note.setText(task.getNotes());

    }

}