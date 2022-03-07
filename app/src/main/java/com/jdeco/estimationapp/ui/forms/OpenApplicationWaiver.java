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
import android.view.WindowManager;
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
import com.jdeco.estimationapp.operations.Helper;
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


    TextView appID, customerNameTB, branch, appType, phoneTB, addressTB, old_customer_nameTV, customer_nameTV, appl_date, status, service_status, sub_branch, service_no, service_class, meter_no, meter_type, install_date;
    TextView last_read, last_read_date, notes, safety_switch, meter_no_form, service_no_from;

    Button submitBtn;
    Spinner situationsSP;
    ProgressDialog progress;
    Session session;
    Database dbObject;
    Helper helper;

    EditText note, currentRead;

    ApplicationDetails applicationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_application_waiver);

        // Remove keyboard focus when start activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("APPLICATION DETAILS");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Add back arrow in action bar
        }


        notes = findViewById(R.id.notes);
        last_read_date = findViewById(R.id.last_read_date);
        last_read = findViewById(R.id.last_read);
        install_date = findViewById(R.id.install_date);
        meter_type = findViewById(R.id.meter_type);
        meter_no = findViewById(R.id.meter_no);
        service_class = findViewById(R.id.service_class);
        service_no = findViewById(R.id.service_no);
        sub_branch = findViewById(R.id.sub_branch);
        service_status = findViewById(R.id.service_status);
        status = findViewById(R.id.status);
        appl_date = findViewById(R.id.appl_date);
        customer_nameTV = findViewById(R.id.customer_nameTV);
        old_customer_nameTV = findViewById(R.id.old_customer_nameTV);
        addressTB = findViewById(R.id.addressTB);
        phoneTB = findViewById(R.id.phoneTB);

        appType = findViewById(R.id.appType);
        branch = findViewById(R.id.branch);
        customerNameTB = findViewById(R.id.customerNameTB);
        appID = findViewById(R.id.appID);


        note = (EditText) findViewById(R.id.note);
        currentRead = (EditText) findViewById(R.id.currentRead);

        safety_switch = findViewById(R.id.safety_switch);

        service_no_from = findViewById(R.id.service_no_form);
        meter_no_form = findViewById(R.id.meter_no_form);

        situationsSP = (Spinner) findViewById(R.id.situations);
        submitBtn = (Button) findViewById(R.id.submitBtn);

        dbObject = new Database(this);
        session = new Session(this);
        helper = new Helper(this);
        applicationDetails = new ApplicationDetails();

        //get application details
        applicationDetails = dbObject.getApplications(session.getValue("APP_ID"), "N", session.getValue("username")).get(0);


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
                if (currentRead.getText().toString().isEmpty() || currentRead.getText().toString().equalsIgnoreCase(" ")) {
                    progress.dismiss();
                    currentRead.requestFocus();
                    currentRead.setError("الرجاء تعبيئة الحقل");
                    Toast.makeText(OpenApplicationWaiver.this, "الرجاء تعبئة القراءة الحالية !", Toast.LENGTH_SHORT).show();
                } else if (note.getText().toString().isEmpty() || note.getText().toString().equalsIgnoreCase(" ")) {
                    progress.dismiss();
                    note.requestFocus();
                    note.setError("الرجاء تعبيئة الحقل");
                    Toast.makeText(OpenApplicationWaiver.this, "الرجاء تعبئة الملاحظات !", Toast.LENGTH_SHORT).show();
                } else {
                    String bodyData = "{\n" +
                            "\"application\": {\n" +
                            "\"applRowId\": " + applicationDetails.getRowId() + ",\n" +//applicationDetails.getAppID()
                            "\"actionCode\": " + 1 + ",\n" +//applicationDetails.getPrjRowId()
                            "\"employeeNo\": \"" + session.getValue("emp_id") + "\",\n" +
                            "\"applId\": " + applicationDetails.getAppID() + ",\n" +//applicationDetails.getAppID()
                            "\"safetySwitch\": " + session.getValue("saftey_switch") + ",\n" +
                            "\"lastRead\": " + currentRead.getText().toString() + ",\n" +
                            "\"notes\": '" + note.getText().toString() + "',\n" +
                            "\"username\": \"" + applicationDetails.getUsername() + "\",\n" +
                            "\"lastReadDate\": \"" + date + "\",\n" +
                            "}}\n";

                    Log.d("bodyData : ", bodyData);
                    submitMaterialsToServer(bodyData);
                }

            }


        });

        ArrayList<String> options = new ArrayList<String>();
        options.add("لا مانع");
        options.add("ملاحظة");



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
                        applicationDetails.setSync("1");
                         dbObject.updateApplicationStatus(applicationDetails.getAppID(), applicationDetails.getTicketStatus(),applicationDetails.getSync());
                        //dbObject.deleteِApplication(session.getValue("APP_ID"));
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
        helper.goBack(MainActivity.class);
       /* DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        helper.goBack(MainActivity.class);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(OpenApplicationWaiver.this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();*/
    }

    /*    public void goBack() {
            Class c = MainActivity.class;

            Intent back = new Intent(getApplicationContext(), c);
            back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(back);
        }
        */
    void assignData(ApplicationDetails task) {

        if (task.getPhone() != null && !task.getPhone().equalsIgnoreCase("null")) {
            phoneTB.setText(task.getPhone());
        } else {
            phoneTB.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getCustomerAddress() != null && !task.getCustomerAddress().equalsIgnoreCase("null")) {
            addressTB.setText(task.getCustomerAddress());
        } else {
            addressTB.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getOld_customer_name() != null && !task.getOld_customer_name().equalsIgnoreCase("null")) {
            old_customer_nameTV.setText(task.getOld_customer_name());
        } else {
            old_customer_nameTV.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getCustomerName() != null && !task.getCustomerName().equalsIgnoreCase("null")) {
            customer_nameTV.setText(task.getCustomerName());
        } else {
            customer_nameTV.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getAppDate() != null && !task.getAppDate().equalsIgnoreCase("null")) {
            appl_date.setText(task.getAppDate().substring(0, 10));
        } else {
            appl_date.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getStatus() != null && !task.getStatus().equalsIgnoreCase("null")) {
            status.setText(task.getStatus());
        } else {
            status.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getService_status() != null && !task.getService_status().equalsIgnoreCase("null")) {
            service_status.setText(task.getService_status());
        } else {
            service_status.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getService_no() != null && !task.getService_no().equalsIgnoreCase("null")) {
            service_no.setText(task.getService_no());
        } else {
            service_no.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getService_class() != null && !task.getService_class().equalsIgnoreCase("null")) {
            service_class.setText(task.getService_class());
        } else {
            service_class.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (String.valueOf(task.getMeter_no()) != null && !String.valueOf(task.getMeter_no()).equalsIgnoreCase("null")) {
            meter_no.setText(String.valueOf(task.getMeter_no()));
        } else {
            meter_no.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getMeter_type() != null && !task.getMeter_type().equalsIgnoreCase("null")) {
            meter_type.setText(task.getMeter_type());
        } else {
            meter_type.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getInstall_date() != null && !task.getInstall_date().equalsIgnoreCase("null")) {
            install_date.setText(task.getInstall_date().substring(0, 10));
        } else {
            install_date.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getLast_read() != null && !task.getLast_read().equalsIgnoreCase("null")) {
            last_read.setText(task.getLast_read());
        } else {
            last_read.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getLast_read_date() != null && !task.getLast_read_date().equalsIgnoreCase("null")) {
            last_read_date.setText(task.getLast_read_date().substring(0, 10));
        } else {
            last_read_date.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getAppID() != null && !task.getAppID().equalsIgnoreCase("null")) {
            appID.setText(task.getAppID());
        } else {
            appID.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getCustomerName() != null && !task.getCustomerName().equalsIgnoreCase("null")) {
            customerNameTB.setText(task.getCustomerName());
        } else {
            customerNameTB.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getBranch() != null && !task.getBranch().equalsIgnoreCase("null")) {
            branch.setText(task.getBranch());
        } else {
            branch.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getsBranch() != null && !task.getsBranch().equalsIgnoreCase("null")) {
            sub_branch.setText(task.getsBranch());
        } else {
            sub_branch.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getAppType() != null && !task.getAppType().equalsIgnoreCase("null")) {
            appType.setText(task.getAppType());
        } else {
            appType.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getNotes() != null && !task.getNotes().equalsIgnoreCase("null")) {
            notes.setText(task.getNotes());
        } else {
            notes.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (session.getValue("saftey_switch") != null && !session.getValue("saftey_switch").equalsIgnoreCase("null")) {
            safety_switch.setText(session.getValue("saftey_switch"));
        } else {
            safety_switch.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getService_no() != null && !task.getService_no().equalsIgnoreCase("null")) {
            service_no_from.setText(task.getService_no());
        } else {
            service_no_from.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (String.valueOf(task.getMeter_no()) != null && !String.valueOf(task.getMeter_no()).equalsIgnoreCase("null")) {
            meter_no_form.setText(String.valueOf(task.getMeter_no()));
        } else {
            meter_no_form.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }


    }

}