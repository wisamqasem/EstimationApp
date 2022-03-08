package com.jdeco.estimationapp.ui.forms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.jdeco.estimationapp.ui.forms.ApplicationsList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.adapters.ApplicationAdapter;
import com.jdeco.estimationapp.adapters.TemplateListAdapter;
import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.objects.RecyclerItemClickListener;
import com.jdeco.estimationapp.objects.Template;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.ui.MainActivity;
import com.jdeco.estimationapp.objects.RecyclerItemClickListener;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class templatesList extends AppCompatActivity {

    private ArrayList<Template> templateList;
    ArrayList<Template> filteredList;

    private Database dbObject;


    private RecyclerView mRecyclerView;
    private TemplateListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    EditText editText;
    private EditText templateAmount;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("TEMPLATES LIST");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Add back arrow in action bar
        }

        dbObject = new Database(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.templatesRV);


        filteredList = new ArrayList<>();

        if (dbObject.tableIsEmpty(Database.TEMPLATES_TABLE)) {
            // getData();
            warning();
        } else templateList = dbObject.getTemplates(null);
        buildRecyclerView();


        //handle list item click
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        if (!filteredList.isEmpty())
                            templateList = filteredList;

                        // get the selected ticket from list
                        final Template template = templateList.get(i);//here pro


                        // initilize the Fragment
                        // session.setValue("APP_ID",applicationDetails.getAppID());

                        //open application details
                        Intent intent = new Intent(templatesList.this, itemsList.class);//make new activity for the items inside the tempalte

                        //pass parameters to application details activity
                        Bundle bundle = new Bundle();
                        bundle.putString("templateId", template.getTemplateId()); //Your id
                        bundle.putString("templateName", template.getTemplateName()); //Your id
                        bundle.putString("templateAmount", String.valueOf(template.getTemplateAmount()));
                        bundle.putString("status", "N");
                        bundle.putString("action", "add");
                        intent.putExtras(bundle); //Put your id to your next Intent
                        startActivity(intent);
                    }
                }));


        editText = findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        /*
         * When you want that Keybord not shown untill user clicks on one of the EditText Field.
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


    }

    //to add the back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void filter(String text) {
        filteredList = new ArrayList<>();
        if (editText.getText().toString().matches("")) {
            templateList = dbObject.getTemplates(null);
        }


        for (Template item : templateList) {

            if (item.getTemplateName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        mAdapter.filterList(filteredList);
    }

    private void getData() {
        templateList = new ArrayList<>();

        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("getItemsFromServer", "Response: " + response);

                //create json object
                try {
                    JSONObject applicationResultObject = new JSONObject(response);

                    //get application array according to items array object
                    JSONArray templateJsonArr = applicationResultObject.getJSONArray("items");

                    Log.d("man1234", ":" + templateJsonArr.length());
                    //loop on the array
                    for (int i = 0; i < templateJsonArr.length(); i++) {
                        JSONObject templateObject = templateJsonArr.getJSONObject(i);

                        //Create application details object
                        Template templateDetails = new Template();


                        templateDetails.setTemplateId(String.valueOf(templateObject.getInt("template_id")));
                        templateDetails.setTemplateName(templateObject.getString("template_name"));
                        templateDetails.setTemplateDesc(templateObject.getString("status_desc"));


                        //check record is exist in applications table
                        if (!dbObject.isItemExist(Database.TEMPLATES_TABLE, "templateId", String.valueOf(templateObject.getInt("template_id")))) {
                            //insert application in application table
                            dbObject.insertNewTemplate(templateDetails);
                        }


                    }
                    if (!dbObject.tableIsEmpty(Database.TEMPLATES_TABLE)) BindItemsToList();


                } catch (Exception ex) {
                    Log.d("error", ":" + ex);
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


                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_TEMPLATES_GET_ITEMS);

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);

//        templateList.add(new Template( "one", "wisam","A"));
//        templateList.add(new Template( "Two", "qasem","A"));
//        templateList.add(new Template( "three", "fuck","A"));
//        templateList.add(new Template( "four", "soa","A"));
//        templateList.add(new Template( "five", "leage","A"));
//        templateList.add(new Template( "six", "dude","A"));

    }

    private void buildRecyclerView() {

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TemplateListAdapter(templateList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }


    private void BindItemsToList() {


        //get all templates
        templateList = dbObject.getTemplates(null);
        mAdapter.filterList(templateList);

        if (templateList.size() == 0) {
            Toast.makeText(this, "DATA NOT FOUND", Toast.LENGTH_LONG).show();

        }

//        //Initialize our array adapter notice how it references the listitems
//        appsAdapter = new ApplicationAdapter(context, applicationDetailsList);
//        //its data has changed so that it updates the UI
//        appsDropList.setAdapter(appsAdapter);
//        appsAdapter.notifyDataSetChanged();
    }


    private void warning() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(R.string.no_data_found);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getResources().getString(R.string.ok_lbl),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent goToApllicationsPage = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(goToApllicationsPage);
                        dialog.cancel();
                    }
                });

//        builder1.setNegativeButton(
//                "No",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}