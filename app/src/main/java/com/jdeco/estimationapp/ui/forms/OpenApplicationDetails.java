package com.jdeco.estimationapp.ui.forms;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.internal.IResolveAccountCallbacks;
import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.adapters.EstimatedItemsListAdapter;
import com.jdeco.estimationapp.adapters.EstimatedTemplatesListAdapter;
import com.jdeco.estimationapp.network.ServerHandler;
import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.objects.EstimationItem;
import com.jdeco.estimationapp.objects.Item;
import com.jdeco.estimationapp.objects.PriceList;
import com.jdeco.estimationapp.objects.ProjectType;
import com.jdeco.estimationapp.objects.ResultCode;
import com.jdeco.estimationapp.objects.Template;
import com.jdeco.estimationapp.objects.Warehouse;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.GeneralFunctions;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.ui.LoginUI;
import com.jdeco.estimationapp.ui.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OpenApplicationDetails extends AppCompatActivity {
    TextView appID, appDate, customerName, customerAddress, branch, sbranch, appType, phoneTB, address,phase1Quntitiy,phase3Quntitiy;
    Spinner masterItemsDropList, subItemsDropList, itemsDropList, itemsDropList2, priceListSpinner1, wareHouseSpinner1, projectTypeSpinner1, priceListSpinner2, wareHouseSpinner2;
    Spinner itemsDropListDialog;
    Button addItemToListBtn, addTemplateBtn;
    View mView,promptsView;
    LayoutInflater li;
    ArrayList<EstimationItem> estimationItems = null;
    ArrayList<Item> estimatedItems = null;
    ArrayList<Item> submitEstimatedItems = null;
    ArrayList<Template> estimatedTemplates = null;
    EstimatedItemsListAdapter estimatedItemsListAdapter;
    EstimatedTemplatesListAdapter estimatedTemplatesListAdapter;
    Button submitBtn, cancelBtn;
    RecyclerView itemsList, templatesList;
    Database dbObject;
    Session session;
    EditText phase1, phase3;

    ApplicationDetails applicationDetails;
    private Session sessionManager;
    private RecyclerView.Adapter itemsListAdapter, templatesListAdapter;
    private String groupID;
    private Context context;
    ArrayList<Item> materialsList = null;
    ArrayList<PriceList> priceListArrayList = null;
    ArrayList<Warehouse> warehouseArrayList = null;
    ArrayList<ProjectType> projectTypeArrayList = null;
    ProgressDialog progress;
    String phase1txt = "0";
    String phase3txt ="0" ;


    private String TAG = "OpenApplicationDetails";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.open_application_details_ui);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("APPLICATION DETAILS");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Add back arrow in action bar
        }


        //initilize vars
        initilize();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.add_data_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addDataAction) {
            showMenuAdItem();
        }
        return super.onOptionsItemSelected(item);
    }


    //initilize Variables
    private void initilize() {
        appID = (TextView) findViewById(R.id.appID);
        appDate = (TextView) findViewById(R.id.appDate);
        customerName = (TextView) findViewById(R.id.customerNameTB);
        customerAddress = (TextView) findViewById(R.id.addressTB);
        phoneTB = (TextView) findViewById(R.id.phoneTB);

        branch = (TextView) findViewById(R.id.branch);
//        sbranch = (TextView) findViewById(R.id.sBranch);
        appType = (TextView) findViewById(R.id.appType);
//        phase1 = (EditText) findViewById(R.id.Phase_1);
//        phase3 = (EditText) findViewById(R.id.Phase_3);

        phase1Quntitiy = (TextView) findViewById(R.id.phase1Quntitiy);
        phase3Quntitiy = (TextView) findViewById(R.id.phase3Quntitiy);


        //initilize spinners
        estimationItems = new ArrayList<>();
//        masterItemsDropList = (Spinner) findViewById(R.id.masterItemsDropList);
        //  subItemsDropList = (Spinner) findViewById(R.id.subItemsDropList);
        //itemsDropList = (Spinner) findViewById(R.id.itemsDropList2);

        priceListSpinner1 = (Spinner) findViewById(R.id.priceListSpinner1);
        wareHouseSpinner1 = (Spinner) findViewById(R.id.warehouseSpinner1);
        projectTypeSpinner1 = (Spinner) findViewById(R.id.projectTypeSpinner1);
//        priceListSpinner2 = (Spinner) findViewById(R.id.priceListSpinner2);
//        wareHouseSpinner2 = (Spinner) findViewById(R.id.wareHouseSpinner2);
        itemsList = (RecyclerView) findViewById(R.id.itemsList);
        itemsList.setHasFixedSize(true);
        itemsList.setLayoutManager(new LinearLayoutManager(this));
        templatesList = (RecyclerView) findViewById(R.id.templatesList);
        templatesList.setHasFixedSize(true);
        templatesList.setLayoutManager(new LinearLayoutManager(this));
        estimatedItems = new ArrayList<Item>();
        submitEstimatedItems = new ArrayList<Item>();

        dbObject = new Database(this);
        session = new Session(this);
        applicationDetails = new ApplicationDetails();

        estimatedItemsListAdapter = new EstimatedItemsListAdapter(this, estimatedItems);

        //its data has changed so that it updates the UI
        itemsList.setAdapter(estimatedItemsListAdapter);


        //        Ammar --> get priceList data
        if (dbObject.tableIsEmpty(Database.PRICE_LIST_TABLE)) {
            // requestPriceListFromServer();
            warning(getResources().getString(R.string.no_data_found));
        } else {
            priceListArrayList = dbObject.getPriceList();
            appendPriceListToSpinner(priceListSpinner1, priceListArrayList, null);

//            ArrayAdapter<PriceList> dataAdapter = new ArrayAdapter<PriceList>(getApplicationContext(), android.R.layout.simple_spinner_item, priceListArrayList);
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            priceListSpinner1.setAdapter(dataAdapter);

        }
//        Ammar --> get Warehouse data
        if (dbObject.tableIsEmpty(Database.WAREHOUSE)) {
            //  requestWarehouseFromServer();
            warning(getResources().getString(R.string.no_data_found));
        } else {
            warehouseArrayList = dbObject.getWarehouse();

            appendWareHouseListToSpinner(wareHouseSpinner1, warehouseArrayList, null);
//            ArrayAdapter<Warehouse> dataAdapter = new ArrayAdapter<Warehouse>(getApplicationContext(), android.R.layout.simple_spinner_item, warehouseArrayList);
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            wareHouseSpinner1.setAdapter(dataAdapter);
        }


//        Ammar --> get ProjectType data
        if (dbObject.tableIsEmpty(Database.PROJECT_TYPE)) {
            //   requestprojectTypeFromServer();
            warning(getResources().getString(R.string.no_data_found));
        } else {
            projectTypeArrayList = dbObject.getProjectType();
            appendProjectTypeListToSpinner(projectTypeSpinner1, projectTypeArrayList, null);
//            ArrayAdapter<ProjectType> dataAdapter = new ArrayAdapter<ProjectType>(getApplicationContext(), android.R.layout.simple_spinner_item, projectTypeArrayList);
//
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//            projectTypeSpinner1.setAdapter(dataAdapter);
        }


        // if the estimated item table is not empty add the item to the list.
        if (!dbObject.tableIsEmpty(Database.ESTIMATED_ITEMS_TABLE)) {
            estimatedItems = dbObject.getEstimatedItems("0", session.getValue("APP_ID"));
            //add this item to recycler view and refresh list
            AddItemsToList(estimatedItems);
            Log.d("estimatedTemplates", "estimatedItems size : " + estimatedItems.size());
        }


        //get application details
        applicationDetails = dbObject.getApplications(session.getValue("APP_ID"), "N").get(0);

        assignAppDetails(applicationDetails);

        Log.d(TAG, "Items list size is " + dbObject.getItems("0").size());


        // if the temblate table is not empty add the templates to the list.
        if (!dbObject.tableIsEmpty(Database.ESTIMATED_TEMPLATES_TABLE)) {
            ArrayList<Template> estimatedTemplates = dbObject.getEstimatedTemplates(session.getValue("APP_ID"));
            AddTemplatesToList(estimatedTemplates);

            Log.d("estimatedTemplates", ": " + estimatedTemplates.size());
        }


        //if
//        if (dbObject.getItems("0").size() <= 0) {
//            getMaterialsFromServerTest();
//        }
//        else
//        {
//            materialsList = dbObject.getItems("0");
//
//            appendListToSpinner(itemsDropList2,materialsList,null);
//        }

        //initilize buttons
        submitBtn = (Button) findViewById(R.id.submitBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);


        //submit data to the server
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                progress = new ProgressDialog(OpenApplicationDetails.this);
                progress.setTitle(getResources().getString(R.string.please_wait));
                progress.setCancelable(true);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();




                if (!dbObject.tableIsEmpty(Database.ESTIMATED_ITEMS_TABLE)) {
                    submitEstimatedItems = dbObject.getEstimatedItems(null, session.getValue("APP_ID"));
                    Log.d("estimatedItems", ":" + estimatedItems.size());
                } else {
                    warning(getResources().getString(R.string.provide_data));
                }

                //get application details
//                applicationDetails = dbObject.getApplications(session.getValue("APP_ID")).get(0);
                // Toast.makeText(getApplicationContext(), "" + applicationDetails.toString(), Toast.LENGTH_LONG).show();
                Log.d("send", "onClick: " + applicationDetails.toString());
//                Intent i = new Intent(OpenApplicationDetails.this, submitApplication.class);
//                startActivity(i);

                //get the size of the materials list
                int materials_count = submitEstimatedItems.size();
                // no materials selected by the estimator
                if (materials_count <= 0) {
                    GeneralFunctions.populateMsg(getApplicationContext(), getResources().getString(R.string.empty_lbl), true);
                } else {
                    String estimatedItemsArray = "";
//                    for (Item item : estimatedItems) {
                    for (int i = 0; i < submitEstimatedItems.size(); i++) {
                        Item item = submitEstimatedItems.get(i);
                        // do something with object
                        if (0 < i && i < submitEstimatedItems.size()) {
                            estimatedItemsArray += ",";
                        }
                        estimatedItemsArray += "{\n" +
                                "\"itemId\": " + item.getId() + ",\n" +//item.getItemCode()
                                "\"quantity\": " + item.getItemAmount() + ",\n" +//item.getItemAmount()
                                "\"templateId\":" + item.getTemplateId() + ",\n" +
                                "\"warehouseId\": " + "85" + ",\n" +//item.getWarehouse().getWarehouseId()
                                "\"priceListId\": " + "10033" + "\n" + //item.getPricList().getPriceListId()
                                "}";

                    }
                    CharSequence date = DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date());

                    //edit.................................................................................
                    // check if the edit text null


                    if(phase1txt == null || phase1txt.equals(""))
                        phase1.setText("0");

                    if(phase3txt == null || phase3txt.equals(""))
                        phase3.setText("0");


                    String bodyData = "{\n" +
                            "\"application\": {\n" +
                            "\"applRowId\": " + applicationDetails.getRowId() + ",\n" +//applicationDetails.getAppID()
                            "\"prjRowId\": " + "138" + ",\n" +//applicationDetails.getPrjRowId()
                            "\"customerName\": \"" + applicationDetails.getCustomerName() + "\",\n" +
                            "\"applId\": " + applicationDetails.getAppID() + ",\n" +//applicationDetails.getAppID()
                            "\"warehouseId\": 85,\n" +
                            "\"priceListId\": 10033,\n" +
                            "\"projectTypeId\": " + ((ProjectType) projectTypeSpinner1.getSelectedItem()).getProjectTypeId() + ",\n" +
                            "\"username\": \"" + applicationDetails.getUsername() + "\",\n" +
                            "\"postingDate\": \"" + date + "\",\n" +
                            "\"Items\": [" + estimatedItemsArray +
                            "],\n" +
                            "\"enclosure\": {\n" +
                            "\"phase1\": " + phase1txt + ",\n" +
                            "\"phase3\": " + phase3txt + ",\n" +
                            "}\n" +
                            "}\n" +
                            "}\n";
                    submitMaterialsToServer(bodyData);
                    //handle send data to the server
                 /*   sendDataToServer task = new sendDataToServer();
                    task.execute();*/
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

        //load required lists
        loadListFromServer task = new loadListFromServer();
        task.execute();


        /**
         List<String> master_items = Arrays.asList(getResources().getStringArray(R.array.master_items));
         List<String> sub_master_items = Arrays.asList(getResources().getStringArray(R.array.sub_master_items));

         ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, master_items);
         dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         masterItemsDropList.setAdapter(dataAdapter);

         dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sub_master_items);
         dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         subItemsDropList.setAdapter(dataAdapter);
         **/


    }

    //assign values to application
    private void assignAppDetails(ApplicationDetails app) {
        try {

            appID.setText(app.getAppID());
            appDate.setText(app.getAppDate().substring(0, 10));
            customerName.setText(app.getCustomerName());
            if (app.getCustomerAddress().equalsIgnoreCase("null"))
                customerAddress.setText(" ");
            else
                customerAddress.setText(app.getCustomerAddress());

            if (app.getPhone().equalsIgnoreCase("null"))
                phoneTB.setText(" ");
            else
                phoneTB.setText(app.getPhone());
           // phoneTB.setText(app.getPhone());
            branch.setText(app.getBranch());
            appType.setText(app.getAppType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //alert dialog
    private void showMenuAdItem() {
        AlertDialog alert = null;
        final CharSequence[] items = {getResources().getString(R.string.add_item), getResources().getString(R.string.add_template_lbl),getResources().getString(R.string.enclouser_lbl), getResources().getString(R.string.exit)};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.choose_item_lbl));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    addItem();
                } else if (item == 1) {

                    addTemplate();
                } else if (item == 2) {

                    showEnclouser();
                } else
                    dialog.dismiss();
            }
        });
        alert = builder.create();
        alert.show();
    }
    //alert dialog
    private void showEnclouser() {
        AlertDialog alert = null;
        promptsView = getLayoutInflater().inflate(R.layout.add_enclouser_dialog, null);
//get the value of edit text
        phase1 = (EditText) promptsView.findViewById(R.id.Phase_1);
        phase3 = (EditText) promptsView.findViewById(R.id.Phase_3);
        //create new dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle(getResources().getString(R.string.choose_item_lbl));
        builder.setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        phase1txt = phase1.getText().toString();
                        phase3txt = phase3.getText().toString();
                        phase1Quntitiy.setText(phase1txt);
                        phase3Quntitiy.setText(phase3txt);
                        dbObject.submitEnclousers(session.getValue("APP_ID"),phase1txt,phase3txt);
                        // get user input and set it to result
                        // edit text
                        Toast.makeText(getApplicationContext(), "1Phase: "+phase1.getText().toString() + " ||  3Phase: "+phase3.getText().toString(), Toast.LENGTH_LONG).show();
                       // showMenuAdItem();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        //set view to alert dialog
        builder.setView(promptsView);
        alert = builder.create();
        alert.show();
    }

    private void addTemplate() {

        Intent i = new Intent(getApplicationContext(), templatesList.class);

        startActivity(i);
    }

    private void addItem() {
        //get selected item in the items list
        mView = getLayoutInflater().inflate(R.layout.add_item_dialog, null);
        itemsDropListDialog = (Spinner) mView.findViewById(R.id.itemsDropListDialog);

        if (dbObject.tableIsEmpty(Database.ITEMS_TABLE)) {
            //   getMaterialsFromServer();
            warning(getResources().getString(R.string.no_data_found));

            Log.d("data", "from server");
        } else {
            materialsList = dbObject.getItems("0");
            Log.d("data", "from database");
            appendListToSpinner(itemsDropListDialog, materialsList, null);

        }


        AlertDialog alertItem = null;
        final CharSequence[] items = {getResources().getString(R.string.add_item), getResources().getString(R.string.add_template_lbl), getResources().getString(R.string.exit)};
        final AlertDialog.Builder builderDialog = new AlertDialog.Builder(this);

        builderDialog.setTitle(getResources().getString(R.string.choose_item_lbl));


        builderDialog.setPositiveButton("ADD ITEM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Item selectedItem = (Item) itemsDropListDialog.getSelectedItem();
                selectedItem.setPricList((PriceList) priceListSpinner1.getSelectedItem());
                //remove the item from the materils list
                selectedItem.setWarehouse((Warehouse) wareHouseSpinner1.getSelectedItem());
                //check if the item already exist in the required items


                AddItemToList(selectedItem);


//                if (estimatedItems.size() > 0) {
//                    for (int i = 0; i < estimatedItems.size(); i++) {
//                        if (estimatedItems.get(i).toString().equals(selectedItem.toString())) {
//                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.material_already_exist), Toast.LENGTH_LONG).show();
//                        } else {
//                            selectedItem.setItemAmount(1);
//                            //add this item to recycler view and refresh list
//                            AddItemToList(selectedItem);
//                        }
//                    }
//                } else {
//                    selectedItem.setItemAmount(1);
//                    //add this item to recycler view and refresh list
//                   // estimatedItems.add(selectedItem);
//                    AddItemToList(selectedItem);
//                }


//                if( estimatedItems.contains(selectedItem)){
//                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.material_already_exist), Toast.LENGTH_LONG).show();
//                }
//                else {
//                    selectedItem.setItemAmount(1);
//                    //add this item to recycler view and refresh list
//                    AddItemToList(selectedItem);
//                }

                dialog.dismiss();

            }
        });
        builderDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderDialog.setView(mView);
        alertItem = builderDialog.create();
        alertItem.show();


    }

    private void warning(String text) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(text);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
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


    private void AddItemsToList(ArrayList<Item> list) {

        //add item to the list
        // estimatedItems.add(item);......................................................
        //add item to estimated database
//        if(!dbObject.isEstimatedItemExist(Database.ESTIMATED_ITEMS_TABLE,"itemName",item.getItemName(), session.getValue("APP_ID"), "0")) {
//            //insert item in estimation items table
//            dbObject.insertEstimatedItem(item,false,session.getValue("APP_ID"));
//            Log.d("AddItemToList",": yes");
//        }else  Log.d("AddItemToList",": no "+item.getInventoryItemCode());


        //Initialize our array adapter notice how it references the listitems


        estimatedItemsListAdapter.setItems(list);
        estimatedItemsListAdapter.notifyDataSetChanged();
    }


    // Recycle view append list
    private void AddItemToList(Item item) {

        //add item to the list
        // estimatedItems.add(item);......................................................


        //add item to estimated database
        if (!dbObject.isEstimatedItemExist(Database.ESTIMATED_ITEMS_TABLE, "itemName", item.getItemName(), session.getValue("APP_ID"), "0")) {
            item.setItemAmount(1);
            //insert item in estimation items table
            dbObject.insertEstimatedItem(item, false, session.getValue("APP_ID"));
            estimatedItemsListAdapter.setItem(item);
            Log.d("AddItemToList", ": yes");
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.material_already_exist), Toast.LENGTH_LONG).show();
        }


        //  estimatedItemsListAdapter = new EstimatedItemsListAdapter(this, estimatedItems);
        //Initialize our array adapter notice how it references the listitems

        //its data has changed so that it updates the UI
        // itemsList.setAdapter(estimatedItemsListAdapter);


    }

    private void AddTemplatesToList(ArrayList<Template> list) {

        //add item to the list
        //  estimatedTemplates.add(list);

        //Initialize our array adapter notice how it references the listitems
        estimatedTemplatesListAdapter = new EstimatedTemplatesListAdapter(this, list,"N");
        //its data has changed so that it updates the UI
        templatesList.setAdapter(estimatedTemplatesListAdapter);
        estimatedTemplatesListAdapter.notifyDataSetChanged();
    }

    private void getMaterialsFromServerTest() {
        materialsList = new ArrayList<>();
        materialsList.add(new Item("1", "www", 0));
        materialsList.add(new Item("1", "www", 0));
        materialsList.add(new Item("1", "www", 0));
        materialsList.add(new Item("1", "www", 0));
        materialsList.add(new Item("1", "www", 0));

    }

    //get items from server
    private void getMaterialsFromServer() {
        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("getMaterialsFromServer", "Response: " + response);

                //create json object
                try {
                    materialsList = new ArrayList<>();
                    //parse string to json
                    JSONObject itemsResultObject = new JSONObject(response);

                    //get items array according to items array object
                    JSONArray itemsJsonArr = itemsResultObject.getJSONArray("items");

                    //loop on the array
                    for (int i = 0; i < itemsJsonArr.length(); i++) {
                        JSONObject itemObject = itemsJsonArr.getJSONObject(i);

                        //Create application details object
                        Item item = new Item();


                        item.setItemCode(itemObject.getString("item_code"));
                        //set item name
                        item.setItemName(itemObject.getString("item_name"));
                        item.setInventoryItemCode(itemObject.getString("inventory_item_id"));
                        item.setTemplateId("0");

                        //check item is already exist or not
                        if (!dbObject.isItemExist(Database.ITEMS_TABLE, "itemCode", item.getItemCode())) {
//                            //insert application in application table
                            dbObject.insertItem(item);
                        }

                        Log.d("getMaterialsFromServer", item.getItemName() + " " + item.getItemCode());

                        //add item to the list
                        materialsList.add(item);
                    }


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
                params.put("username", "jd");
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_APPLICATIONS_GET_ITEMS);

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
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
                        Intent i = new Intent(OpenApplicationDetails.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_failed), Toast.LENGTH_LONG).show();//display the response submit failed

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //create json object
                try {
                    materialsList = new ArrayList<>();

                    Log.d("bodyData1 : ", bodyData);
                } catch (Exception ex) {
                    Log.d("fuckingError", ":" + ex);
                    ex.printStackTrace();
                }
                progress.setCancelable(false);

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
                Log.d("bodyData2 : ", bodyData);
                params.put("data", bodyData);

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }


    private class loadListFromServer extends AsyncTask<String, ResultCode, ResultCode> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ResultCode doInBackground(String... resultCodes) {
            ResultCode resultCode = new ResultCode();
            /**
             HttpHandler httpHandler = new HttpHandler(getApplicationContext(),dbObject);


             try
             {
             //load lookups
             estimationItems = httpHandler.loadEstimationLookup(getApplicationContext());
             resultCode.setResultCode("1");
             resultCode.setResultMsg(getResources().getString(R.string.load_success_msg));
             }
             catch (Exception ex)
             {
             ex.printStackTrace();
             resultCode.setResultCode("-1");
             resultCode.setResultMsg(getResources().getString(R.string.exception_msg));
             }
             **/
            return resultCode;
        }

        @Override
        protected void onPostExecute(ResultCode resultCode) {
            super.onPostExecute(resultCode);

//            // Creating adapter for spinner
//            ArrayAdapter<EstimationItem> dataAdapter = new ArrayAdapter<EstimationItem>(OpenApplicationDetails.this, android.R.layout.simple_spinner_item, estimationItems);
//            // Drop down layout style - list view with radio button
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            // attaching data adapter to spinner
//            itemsDropList.setAdapter(dataAdapter);
//
//            itemsDropList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    //check view in ar rotate the spinner selected item
//                    if (view != null) {
//                        //if (Locale.getDefault().getLanguage().equals("ar") && android.os.Build.MODEL.equals(getResources().getString(R.string.device_model)))
//                        //    view.setRotationY(180);// rotating the view (Selected Item) back
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
        }
    }

    //send data to the server
    private class sendDataToServer extends AsyncTask<ResultCode, ResultCode, ResultCode> {

        ResultCode resultCode = new ResultCode();
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OpenApplicationDetails.this);
            progressDialog.setMessage(getResources().getString(R.string.processing_lbl));
            progressDialog.show();
        }

        @Override
        protected ResultCode doInBackground(ResultCode... params) {

            ResultCode resultCode = new ResultCode();


            return resultCode;
        }

        @Override
        protected void onPostExecute(ResultCode resultCode) {
            super.onPostExecute(resultCode);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    public void goBack() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("");
        alertDialog.setMessage(getResources().getString(R.string.close_form_confirmation_msg));

        alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //close the activity
                        Intent back = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(back);
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

    private void populateDialogWithFiled() {

        GeneralFunctions.populateMsg(this, "Iam in dialog function", true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.material_amount_lbl));

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(getResources().getString(R.string.yes_lbl), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String x = input.getText().toString();
                Toast.makeText(getApplicationContext(), x, Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.cancel_lbl), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

        //GeneralFunctions.populateMsg(this, "Is dialog is show " + builder.show(), true);
    }


    //set a list in spinner
    private void appendListToSpinner(Spinner spinner, ArrayList<Item> list, String selectedValue) {


        try {
            //append items to activity
            ArrayAdapter<Item> adapter =
                    new ArrayAdapter<Item>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
            //add adapter to spinner
            spinner.setAdapter(adapter);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, ex.getMessage());
        }
    }

    //set a list in spinner
    private void appendPriceListToSpinner(Spinner spinner, ArrayList<PriceList> list, String selectedValue) {


        try {
            //append items to activity
            ArrayAdapter<PriceList> adapter =
                    new ArrayAdapter<PriceList>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
            //add adapter to spinner
            spinner.setAdapter(adapter);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, ex.getMessage());
        }
    }

    //set a list in spinner
    private void appendProjectTypeListToSpinner(Spinner spinner, ArrayList<ProjectType> list, String selectedValue) {


        try {
            //append items to activity
            ArrayAdapter<ProjectType> adapter =
                    new ArrayAdapter<ProjectType>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
            //add adapter to spinner
            spinner.setAdapter(adapter);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, ex.getMessage());
        }
    }

    //set a list in spinner
    private void appendWareHouseListToSpinner(Spinner spinner, ArrayList<Warehouse> list, String selectedValue) {


        try {
            //append items to activity
            ArrayAdapter<Warehouse> adapter =
                    new ArrayAdapter<Warehouse>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
            //add adapter to spinner
            spinner.setAdapter(adapter);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, ex.getMessage());
        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();


        //estimatedTemplatesListAdapter.notifyItemChanged(estimatedTemplatesListAdapter.getPos());

    }
}