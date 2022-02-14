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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.adapters.ItemListAdapter;
import com.jdeco.estimationapp.adapters.TemplateListAdapter;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.objects.Item;
import com.jdeco.estimationapp.objects.PriceList;
import com.jdeco.estimationapp.objects.RecyclerItemClickListener;
import com.jdeco.estimationapp.objects.Template;
import com.jdeco.estimationapp.objects.Warehouse;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.GeneralFunctions;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class itemsList extends AppCompatActivity {

    private ArrayList<Item> itemsArrayList;
    private ArrayList<Item> estimatedItemsArrayList;

    private Database dbObject;


    private RecyclerView mRecyclerView;
    private ItemListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Button submitBtn, cancelBtn;

    EditText editText;
    EditText templateAmount;
    Context context;
    Session session;
    protected ImageButton templateMoreBtn;
    protected ImageButton templateLessBtn;


    String templateId;
    String templateName;
    String action;
    String templateAmountValue;
    String appId;
    String appStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        Bundle extras;
        Template template = new Template();


        extras = getIntent().getExtras();
        if (extras != null) {
            Log.d("templateid", extras.getString("templateId"));
            templateId = extras.getString("templateId");
            templateName = extras.getString("templateName");
            action = extras.getString("action");
            templateAmountValue = extras.getString("templateAmount");
            appStatus = extras.getString("status");

        }


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(templateName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Add back arrow in action bar
        }

        dbObject = new Database(this);
        session = new Session(this);
        mAdapter = new ItemListAdapter(itemsArrayList,appStatus);
        mRecyclerView = (RecyclerView) findViewById(R.id.itemsRV);
        //initilize buttons
        submitBtn = (Button) findViewById(R.id.itemListSubmitBtn);
        cancelBtn = (Button) findViewById(R.id.itemListCancelBtn);
        templateMoreBtn = (ImageButton) findViewById(R.id.template_item_more);
        templateLessBtn = (ImageButton) findViewById(R.id.template_item_less);
        templateAmount = (EditText) findViewById(R.id.template_amount);


        appId = session.getValue("APP_ID");


        Log.d("appStatus",":"+appStatus);
//if the application status is done
        if(appStatus.equals("D")){
            templateMoreBtn.setEnabled(false);
            templateLessBtn.setEnabled(false);
            templateAmount.setEnabled(false);
            submitBtn.setEnabled(false);
            cancelBtn.setEnabled(false);
        }


        if (dbObject.tableItemsOfTemplatesIsEmpty(templateId)) {
            // getData();
            warning("NO DATA FOUND PLEASE UPDATE THE DATA");
        } else itemsArrayList = dbObject.getItems(templateId);


        if (action.equals("add")) submitBtn.setText(getResources().getString(R.string.submit_form_lbl));
        else if (action.equals("update")) {
            template.setTemplateAmount(Integer.valueOf(templateAmountValue));

            submitBtn.setText(getResources().getString(R.string.update_lbl));
            estimatedItemsArrayList = dbObject.getEstimatedItems(templateId, appId);
            Log.d("estimatedItemsArrayList", " app_id : " + appId);
            Log.d("estimatedItemsArrayList", ":" + estimatedItemsArrayList);
            for (Item i : itemsArrayList) {
                for (Item e : estimatedItemsArrayList) {
                    Log.d("estimatedItemsArrayList", ":" + i.getId() + " " + e.getId());
                    if (i.getId().equals(e.getId())) {
                        i.setChecked(true);
                        i.setItemAmount(e.getItemAmount());
                        Log.d("estimatedItemsArrayList", ": true");

                        break;
                    } else i.setChecked(false);


                }

            }
        }

        buildRecyclerView();


        templateMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                templateAmount.setText(String.valueOf(template.incressAmount()));
                // notifyItemChanged(i);

            }
        });

        templateLessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                templateAmount.setText(String.valueOf(template.decressAmount()));
                // notifyItemChanged(i);

            }
        });


        editText = findViewById(R.id.edittext2);
        editText.addTextChangedListener(new TextWatcher() {// for search
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



        templateAmount = findViewById(R.id.template_amount);
        templateAmount.setText(templateAmountValue);
        templateAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    template.setTemplateAmount(Integer.valueOf(s.toString()));
                    mAdapter.setTemplateAmountNumber(template.getTemplateAmount());
                }
                if (s.toString().equals("0")) templateAmount.setText("1");
//               for(Item i : itemsArrayList ){
//
//                   i.setItemAmount(i.getItemAmount()*template.getTemplateAmount());
//
//               }
//                mAdapter.setItems(itemsArrayList) ;

            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Item> submitArray = new ArrayList<Item>();
                template.setTemplateId(templateId);
                template.setTemplateName(templateName);

                // template.setTemplateAmount();
                submitArray = mAdapter.getItemList();
                for (Item estimated_item : submitArray) {
                    Log.d("submitArray", ": " + submitArray.size());

                    estimated_item.setTemplateId(templateId);
                    estimated_item.setPricList(new PriceList());
                    estimated_item.setWarehouse(new Warehouse());
                   // estimated_item.setItemAmount(estimated_item.getItemAmount()*Integer.valueOf(templateAmountValue));
                    if (estimated_item.getChecked()) {
                        Log.d("estimated_item", estimated_item.getItemName() + " " + estimated_item.getChecked());
                        //    check record is exist in applications table
                        if (!dbObject.isEstimatedItemExist(Database.ESTIMATED_ITEMS_TABLE, "itemName", estimated_item.getItemName(),appId,templateId)) {
                            //insert estimaed items in estimaed items table
                            dbObject.insertEstimatedItem(estimated_item, true, appId);
                        } else {
                            //update estimaed items in estimaed items table
                            dbObject.updateEstimatedItem(estimated_item, true,appId);

                        }
                    } else {
                        dbObject.deleteEstimatedItem(estimated_item,appId);
                    }
                }


                if (action.equals("add"))
                    if (!dbObject.isTemplateExist(Database.ESTIMATED_TEMPLATES_TABLE, "templateName", templateName , appId)) {

                        Log.d("APP_ID", ": " + appId);
                        //insert estimated templates in estimated templates table
                        dbObject.insertEstimatedTemplate(template, appId);
                        Intent go = new Intent(getApplicationContext(), OpenApplicationDetails.class);
                        startActivity(go);

                    } else {
                        warning(getResources().getString(R.string.template_already_exist));
                    }
                else if (action.equals("update")) {

                    dbObject.upadteEstimatedTemplate(template, appId);
                    Intent go = new Intent(getApplicationContext(), OpenApplicationDetails.class);
                    startActivity(go);
                }

            }


        });


        //back to applications list
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle go back box
                goBack();
            }
        });


    }

    //to add the back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void filter(String text) {
        ArrayList<Item> filteredList = new ArrayList<>();

        for (Item item : itemsArrayList) {
            if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        mAdapter.filterList(filteredList);
    }

    public void goBack() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("");
        alertDialog.setMessage(getResources().getString(R.string.close_form_confirmation_msg));

        alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //close the activity
                        finish();
                    }
                });

        alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


//    private void getData() {
//        itemsArrayList = new ArrayList<>();
//
//        //get login url
//        RequestQueue mRequestQueue;
//        StringRequest mStringRequest;
//
//        //RequestQueue initialized
//        mRequestQueue = Volley.newRequestQueue(this);
//
//
//
//        //String Request initialized
//        mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.d("getItemsFromServer","Response: "+response);
//
//                //create json object
//                try
//                {
//                    JSONObject applicationResultObject = new JSONObject(response);
//
//                    //get application array according to items array object
//                    JSONArray itemJsonArr = applicationResultObject.getJSONArray("items");
//
//                    Log.d("man1234",":" + itemJsonArr.length());
//                    //loop on the array
//                    for(int i=0;i<itemJsonArr.length();i++)
//                    {
//                        JSONObject itemObject = itemJsonArr.getJSONObject(i);
//
//                        //Create application details object
//                        Item itemDetails = new Item();
//
//
//                        itemDetails.setId(String.valueOf(itemObject.getInt("item_id")));
//                        itemDetails.setItemName(itemObject.getString("item_name"));
//                        itemDetails.setAllowDelete(itemObject.getString("delete_allowed"));
//                        itemDetails.setAllowEdit(itemObject.getString("edit_allowed"));
//
//                        if(!itemObject.getString("quantity").equals("null"))
//                        itemDetails.setItemAmount(itemObject.getInt("quantity"));
//                        else  itemDetails.setItemAmount(0);
//                        itemDetails.setTemplateId(templateId);
//                        itemDetails.setInventoryItemCode(itemObject.getString("item_code"));
//
//
//                        //check record is exist in applications table
//                        if(!dbObject.isItemExist(Database.ITEMS_TABLE,"itemId",itemObject.getString("item_id"))) {
//                            //insert application in application table
//                            dbObject.insertItem(itemDetails);
//                        }
//
//
//
//
//                    }
//                    if(!dbObject.tableIsEmpty(Database.TEMPLATES_TABLE))BindItemsToList();
//
//                } catch (Exception ex) {
//                    Log.d("error",":" + ex);
//                    ex.printStackTrace();
//                }
//
//
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Log.d("getItemsFromServer","Error Login Request :" + error.toString());
//            }
//
//        }) {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> params = new HashMap<>();
//                //parameters
//
//
//                params.put("apiKey",CONSTANTS.API_KEY);
//                params.put("action",CONSTANTS.ACTION_GET_ITEMS);
//                params.put("templateId",templateId);
//
//                return params;
//            }
//        };
//
//        mRequestQueue.add(mStringRequest);
//
////        itemsArrayList.add(new Template( "one", "wisam","A"));
////        itemsArrayList.add(new Template( "Two", "qasem","A"));
////        itemsArrayList.add(new Template( "three", "fuck","A"));
////        itemsArrayList.add(new Template( "four", "soa","A"));
////        itemsArrayList.add(new Template( "five", "leage","A"));
////        itemsArrayList.add(new Template( "six", "dude","A"));
//
//    }

    private void buildRecyclerView() {

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ItemListAdapter(itemsArrayList,appStatus);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


//    private void BindItemsToList() {
//
//        //get all templates
//        itemsArrayList = dbObject.getItems(templateId);
//        mAdapter.filterList(itemsArrayList);
//
//        if (itemsArrayList.size() == 0) {
//            Toast.makeText(this, "DATA NOT FOUND", Toast.LENGTH_LONG).show();
//
//        }
//    }


    private void warning(String text) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(text);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

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