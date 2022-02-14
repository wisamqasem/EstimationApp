package com.jdeco.estimationapp.ui.forms;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.objects.Item;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.GeneralFunctions;
import com.jdeco.estimationapp.operations.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class submitApplication extends AppCompatActivity {


    EditText appid,customerName,address,branch,area,note,phone;
    Button submitBtn;
    Spinner priceList , projectType,wareHouse;
    ArrayList<Item> estimatedItems=null;
    ApplicationDetails appDetails;
    Database dbObject;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_application);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appid = (EditText) findViewById(R.id.submitApplicationPageId);
        address = (EditText) findViewById(R.id.submitApplicationPageAddress);
        area = (EditText) findViewById(R.id.submitApplicationPageArea);
        branch = (EditText) findViewById(R.id.submitApplicationPageBranch);
        note = (EditText) findViewById(R.id.submitApplicationPageNote);
        customerName = (EditText) findViewById(R.id.submitApplicationPageCustomerName);
        phone = (EditText) findViewById(R.id.submitApplicationPagePhone);

        submitBtn = (Button) findViewById(R.id.submitApplicationPageBtn);

        priceList = (Spinner) findViewById(R.id.priceListSpinner);
        wareHouse = (Spinner) findViewById(R.id.wareHouseSpinner);
        projectType = (Spinner) findViewById(R.id.projectTypeSpinner);

        appDetails = new ApplicationDetails();

        dbObject = new Database(this);

        session = new Session(this);

        //get application details
        appDetails = dbObject.getApplications(session.getValue("APP_ID"),"N").get(0);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assignAppDetails(appDetails);

                estimatedItems = new ArrayList<>();
                //get the size of the materials list
                int materials_count = estimatedItems.size();

                // no materials selected by the estimator
                if (materials_count <= 0) {
                    GeneralFunctions.populateMsg(getApplicationContext(), getResources().getString(R.string.empty_lbl), true);
                } else {
                    String estimatedItemsArray = "";
                    for (Item item : estimatedItems) {
                        // do something with object

                        estimatedItemsArray += "{\n" +
                                "\"itemId\": " + 123 + ",\n" +//item.getItemCode()
                                "\"quantity\": " + 11 + ",\n" +//item.getItemAmount()
                                "\"templateId\": null,\n" +
                                "\"warehouseId\": 85,\n" +
                                "\"priceListId\": 10033\n" +
                                "}";
                    }
                    String bodyData = "{\n" +
                            "\"application\": {\n" +
                            "\"applRowId\": " + 2309 + ",\n" +//applicationDetails.getAppID()
                            "\"prjRowId\": 142,\n" +
                            "\"customerName\": \"" + appDetails.getCustomerName() + "\",\n" +
                            "\"applId\": " + 2309 + ",\n" +//applicationDetails.getAppID()
                            "\"warehouseId\": 85,\n" +
                            "\"priceListId\": 10033,\n" +
                            "\"projectTypeId\": 6,\n" +
                            "\"username\": \"" + appDetails.getUsername() + "\",\n" +
                            "\"postingDate\": \"2021-10-18T17:17:42\",\n" +
                            "\"Items\": [" + estimatedItemsArray +
                            "],\n" +
                            "\"enclosure\": {\n" +
                            "\"phase1\": 4,\n" +
                            "\"phase3\": 3\n" +
                            "}\n" +
                            "}\n" +
                            "}\n";
                    // submitMaterialsToServer(bodyData);


                }
            }
        });








    }

    private void submitApplicationToServer(String bodyData) {
        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;


        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("submitApplicationToServ", "Response: " + response);
                try {
                    JSONObject submitData = new JSONObject(response);
                    Log.d("submitMaterialsToServ", "Response: " +(submitData.getString("request_response").equals("Success")));
                    if(submitData.getString("request_response").equals("Success"))
                    {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();//display the response submit success
                    }
                    else {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.submit_failed), Toast.LENGTH_LONG).show();//display the response submit failed

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //create json object
                try {


                    Log.d("bodyData1 : ",bodyData);
                } catch (Exception ex) {
                    Log.d("fuckingError", ":" + ex);
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("getItemsFromServer", "Error Login Request :" + error.toString());
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                //parameters
                // params.put("username", "jd");
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_APPLICATIONS_SUBMIT_ITEMS);
                Log.d("bodyData2 : ",bodyData);
                params.put("data",bodyData );

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }


        //assign values to application
    private void assignAppDetails(ApplicationDetails app) {
            try {

                customerName.setText(app.getCustomerName());
                address.setText(app.getCustomerAddress());
                branch.setText(app.getBranch());
                phone.setText(app.getPhone());
             //   area.setText(app.geta());


            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }




}


