package com.jdeco.estimationapp.operations;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.adapters.EstimatedItemsListAdapter;
import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.AttchmentType;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.objects.Item;
import com.jdeco.estimationapp.objects.NoteLookUp;
import com.jdeco.estimationapp.objects.PriceList;
import com.jdeco.estimationapp.objects.ProjectType;
import com.jdeco.estimationapp.objects.Template;
import com.jdeco.estimationapp.objects.Warehouse;
import com.jdeco.estimationapp.ui.MainActivity;
import com.jdeco.estimationapp.ui.forms.OpenApplicationDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetData {


    ArrayList<Item> materialsList = null ;
    ArrayList<Template> templateList ;
    ArrayList<Item> itemsArrayList = null ;
    ArrayList<PriceList> priceLists = null;
    ArrayList<NoteLookUp> noteLookUps = null;
    ProgressDialog progressDialog;

Database dbObject;

    //get login url
    RequestQueue mRequestQueue;
    StringRequest mStringRequest;
    StringRequest mStringRequest2;
    StringRequest mStringRequest3;
    StringRequest mStringRequest4;
    StringRequest mStringRequest5;
    StringRequest mStringRequest6;
    StringRequest mStringRequest7;
    StringRequest mStringRequest8;




    public  void loading(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public  void endLoading(){
        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {
                        Log.i("tag", "This'll run 10s later");
                        progressDialog.dismiss();
                    }
                },
                10000);

    }

    public  void getMaterialsFromServer(Context context) {

//RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context);




        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("getMaterialsFromServer", "Response: " + response);

                //create json object
                try {
                    materialsList = new ArrayList<>();
                    dbObject = new Database(context);
                    //parse string to json
                    JSONObject itemsResultObject = new JSONObject(response);

                    //get items array according to items array object
                    JSONArray itemsJsonArr = itemsResultObject.getJSONArray("items");

                    //loop on the array
                    for (int i = 0; i < itemsJsonArr.length(); i++) {
                        JSONObject itemObject = itemsJsonArr.getJSONObject(i);

                        //Create application details object
                        Item item = new Item();

                        String[] parts = itemObject.getString("item_name").split(" ");
                        item.setItemCode(parts[0]);
                        String itemName = "";
                        for (int j = 1; j < parts.length; j++) {
                            Log.d("qasem",itemName);
                            itemName += " "+parts[j];
                        }

                        //set item name
                        item.setItemName( itemObject.getString("item_name"));
                        item.setInventoryItemCode(itemObject.getString("inventory_item_id"));
                        item.setTemplateId("0");

                        //check item is already exist or not
                        if (!dbObject.isItemExist(Database.ITEMS_TABLE, "inventoryItemCode", item.getInventoryItemCode())) {
//                            //insert application in application table
                            dbObject.insertItem(item);
                        }

                        Log.d("getMaterialsFromServer", item.getItemName() + " " + item.getItemCode());

                        //add item to the list
                        materialsList.add(item);
                    }

                    Toast.makeText(context,"تم أضافة المواد بنجاح", Toast.LENGTH_LONG).show();


                } catch (Exception ex) {
                    endLoading();
                    Log.d("error", ":" + ex);
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                endLoading();
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

    private void getTemplatesFromServer(Context context) {
        templateList = new ArrayList<>();

//        //get login url
//        RequestQueue mRequestQueue;
//        StringRequest mStringRequest;

        //RequestQueue initialized
      //  mRequestQueue = Volley.newRequestQueue(context);

        //String Request initialized
        mStringRequest2 = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getItemsFromServer","Response: "+response);
                //create json object
                try
                {
                    dbObject = new Database(context);
                    JSONObject applicationResultObject = new JSONObject(response);

                    //get application array according to items array object
                    JSONArray templateJsonArr = applicationResultObject.getJSONArray("items");

                    Log.d("man1234",":" + templateJsonArr.length());
                    //loop on the array
                    for(int i=0;i<templateJsonArr.length();i++)
                    {
                        JSONObject templateObject = templateJsonArr.getJSONObject(i);

                        //Create application details object
                        Template templateDetails = new Template();

                        templateDetails.setTemplateId(String.valueOf(templateObject.getInt("template_id")));
                        templateDetails.setTemplateName(templateObject.getString("template_name"));
                        templateDetails.setTemplateDesc(templateObject.getString("status_desc"));

                        //check record is exist in applications table
                        if(!dbObject.isItemExist(Database.TEMPLATES_TABLE,"templateId",String.valueOf(templateObject.getInt("template_id")))) {
                            //insert application in application table
                            dbObject.insertNewTemplate(templateDetails);
                        }
String ti = templateDetails.getTemplateId();
                        Log.d("ti",":"+ti);
                        getItemsOfTemplate(context,templateDetails.getTemplateId());
                    }
                    Toast.makeText(context,"تم أضافة القوالب وعناصرها بنجاح", Toast.LENGTH_LONG).show();

                    endLoading();
                } catch (Exception ex) {
                    endLoading();
                    Log.d("error",":" + ex);
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                endLoading();
                Log.d("getItemsFromServer","Error Login Request :" + error.toString());
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                //parameters


                params.put("apiKey",CONSTANTS.API_KEY);
                params.put("action",CONSTANTS.ACTION_TEMPLATES_GET_ITEMS);

                return params;
            }
        };

        mRequestQueue.add(mStringRequest2);

//        templateList.add(new Template( "one", "wisam","A"));
//        templateList.add(new Template( "Two", "qasem","A"));
//        templateList.add(new Template( "three", "fuck","A"));
//        templateList.add(new Template( "four", "soa","A"));
//        templateList.add(new Template( "five", "leage","A"));
//        templateList.add(new Template( "six", "dude","A"));

    }

    private void getItemsOfTemplate(Context context ,String templateId) {
        itemsArrayList = new ArrayList<>();
Log.d("templateId",":"+templateId);






        //String Request initialized
        mStringRequest3 = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("getItemsFromServer","Response: "+response);

                //create json object
                try
                {
                    dbObject = new Database(context);
                    JSONObject applicationResultObject = new JSONObject(response);

                    //get application array according to items array object
                    JSONArray itemJsonArr = applicationResultObject.getJSONArray("items");

                    Log.d("man1234",":" + itemJsonArr.length());
                    //loop on the array
                    for(int i=0;i<itemJsonArr.length();i++)
                    {
                        JSONObject itemObject = itemJsonArr.getJSONObject(i);

                        //Create application details object
                        Item itemDetails = new Item();


                        itemDetails.setId(String.valueOf(itemObject.getInt("item_id")));
                        itemDetails.setItemName(itemObject.getString("item_name"));
                        itemDetails.setAllowDelete(itemObject.getString("delete_allowed"));
                        itemDetails.setAllowEdit(itemObject.getString("edit_allowed"));

                        if(!itemObject.getString("quantity").equals("null"))
                            itemDetails.setItemAmount(itemObject.getInt("quantity"));
                        else  itemDetails.setItemAmount(0);
                        itemDetails.setTemplateId(templateId);
                        itemDetails.setInventoryItemCode(itemObject.getString("item_code"));
                        itemDetails.setTemplateAmount(1);


                        //check record is exist in applications table
                        if(!dbObject.isItemAndTemplateExist(itemObject.getString("item_id"),templateId)) {
                            //insert application in application table
                            dbObject.insertItem(itemDetails);
                        }




                    }


                } catch (Exception ex) {
                    endLoading();
                    Log.d("error",":" + ex);
                    ex.printStackTrace();
                }





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                endLoading();
                Log.d("getItemsFromServer","Error Login Request :" + error.toString());
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                //parameters


                params.put("apiKey",CONSTANTS.API_KEY);
                params.put("action",CONSTANTS.ACTION_GET_ITEMS);
                params.put("templateId",templateId);

                return params;
            }
        };

        mRequestQueue.add(mStringRequest3);

//        itemsArrayList.add(new Template( "one", "wisam","A"));
//        itemsArrayList.add(new Template( "Two", "qasem","A"));
//        itemsArrayList.add(new Template( "three", "fuck","A"));
//        itemsArrayList.add(new Template( "four", "soa","A"));
//        itemsArrayList.add(new Template( "five", "leage","A"));
//        itemsArrayList.add(new Template( "six", "dude","A"));

    }

    // Ammar --> get priceList data from server
    private void requestPriceListFromServer(Context context) {
        //get login url

      //  RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        //StringRequest mStringRequest;

        //RequestQueue initialized
        // mRequestQueue = Volley.newRequestQueue(context);

//        Log.d("requestTemplates", "URL: " + CONSTANTS.API_LINK);

        priceLists = new ArrayList<>();


        //String Request initialized
        mStringRequest4 = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                //delete all exist item
             //   dbObject.deleteItemsFormTable(Database.PRICE_LIST_TABLE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray appsArr = jsonObject.getJSONArray("items");

                    Log.d("requestTemplates", "JSON Response test" + appsArr);


                    //loop on array
                    for (int i = 0; i < appsArr.length(); i++) {
                        JSONObject priceListObject = appsArr.getJSONObject(i);
                        PriceList priceList = new PriceList();
                        priceList.setPriceListId(priceListObject.getString("r"));
                        priceList.setPriceListName(priceListObject.getString("d"));
                        priceLists.add(priceList);

                        //check record is exist in template table
                        if (!dbObject.isItemExist(Database.PRICE_LIST_TABLE, "priceListId", priceList.getPriceListId())) {
                            //insert template in template table
                            dbObject.insertNewPriceList(priceList);
                        }

                    }

                    Toast.makeText(context,"تم أضافة قائمة الأسعار بنجاح", Toast.LENGTH_LONG).show();


                } catch (Exception ex) {
                    endLoading();
                    ex.printStackTrace();
                }

                //Toast.makeText(context,"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                endLoading();
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {

                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
//                Log.d("requestApplicationsEer", "Error Login Request :" + volleyError.toString());
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_GET_PRICE_LIST);
                return params;
            }
        };

        mRequestQueue.add(mStringRequest4);
//
    }
    // Ammar --> end get priceList data from server

    // Ammar --> get projectType data from server
    private void requestprojectTypeFromServer(Context context) {
        //get login url

        //RequestQueue mRequestQueue = Volley.newRequestQueue(this);
      //  StringRequest mStringRequest;

        ArrayList<ProjectType> projectTypes = new ArrayList<>();


        //String Request initialized
        mStringRequest5 = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                //delete all exist item
               // dbObject.deleteItemsFormTable(Database.PROJECT_TYPE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray appsArr = jsonObject.getJSONArray("items");

                    Log.d("requestTemplates", "JSON Response test" + appsArr);


                    //loop on array
                    for (int i = 0; i < appsArr.length(); i++) {
                        JSONObject projectTypeObject = appsArr.getJSONObject(i);
                        ProjectType projectType = new ProjectType();
                        projectType.setProjectTypeId(projectTypeObject.getString("type_id"));
                        projectType.setProjectTypeName(projectTypeObject.getString("type_desc_ar"));
                        projectTypes.add(projectType);

                        //check record is exist in projectType table
                        if (!dbObject.isItemExist(Database.PROJECT_TYPE, "typeId", projectType.getProjectTypeId())) {
                            //insert projectType in projectType table
                            dbObject.insertNewProjectType(projectType);
                        }

                    }
                    Toast.makeText(context,"تم أضافة أنواع المشاريع بنجاح", Toast.LENGTH_LONG).show();
//                    try {
//
//                        ArrayAdapter<ProjectType> dataAdapter = new ArrayAdapter<ProjectType>(getApplicationContext(), android.R.layout.simple_spinner_item, projectTypes);
//                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        projectTypeSpinner1.setAdapter(dataAdapter);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }

                } catch (Exception ex) {
                    endLoading();
                    ex.printStackTrace();
                }

                //Toast.makeText(context,"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                endLoading();
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {

                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
//                Log.d("requestApplicationsEer", "Error Login Request :" + volleyError.toString());
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_GET_PROJECT_TYPE);
                return params;
            }
        };

        mRequestQueue.add(mStringRequest5);
//
    }
    // Ammar --> end get projectType data from server

    // Ammar --> get Warehouse data from server
    private void requestWarehouseFromServer(Context context) {
        //get login url

      //  RequestQueue mRequestQueue = Volley.newRequestQueue(this);
       // StringRequest mStringRequest;

        //RequestQueue initialized
        // mRequestQueue = Volley.newRequestQueue(context);

//        Log.d("requestTemplates", "URL: " + CONSTANTS.API_LINK);

        ArrayList<Warehouse> warehouses = new ArrayList<>();


        //String Request initialized
        mStringRequest6 = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                //delete all exist item
            //    dbObject.deleteItemsFormTable(Database.WAREHOUSE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray appsArr = jsonObject.getJSONArray("items");

                    Log.d("requestTemplates", "JSON Response test" + appsArr);


                    //loop on array
                    for (int i = 0; i < appsArr.length(); i++) {
                        JSONObject warehouseObject = appsArr.getJSONObject(i);
                        Warehouse wareHouse = new Warehouse();
                        wareHouse.setWarehouseId(warehouseObject.getString("organization_id"));
                        wareHouse.setWarehouseName(warehouseObject.getString("organization_name"));
                        warehouses.add(wareHouse);

                        //check record is exist in template table
                        if (!dbObject.isItemExist(Database.WAREHOUSE, "orgId", wareHouse.getWarehouseId())) {
                            //insert template in template table
                            dbObject.insertNewWarehouse(wareHouse);
                        }

//                        Log.d("requestTemplates", "JSON Response " + templateObject.getString("template_id") + "," + templateObject.getString("template_name"));
                    }

                    Toast.makeText(context,"تم أضافة المستودع بنجاح", Toast.LENGTH_LONG).show();
//                    try {
//
//                        ArrayAdapter<Warehouse> dataAdapter = new ArrayAdapter<Warehouse>(getApplicationContext(), android.R.layout.simple_spinner_item, warehouses);
//                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        wareHouseSpinner1.setAdapter(dataAdapter);
//                        Intent i = new Intent(OpenApplicationDetails.this, EstimatedItemsListAdapter.class);
//                        i.putExtra("warehouseSpinner", warehouses);
////                        wareHouseSpinner2.setAdapter(dataAdapter);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
                    // Log.d("requestTemplates", "JSON Response " + dataAdapter);


                } catch (Exception ex) {
                    endLoading();
                    ex.printStackTrace();
                }

                //Toast.makeText(context,"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                endLoading();
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {

                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
//                Log.d("requestApplicationsEer", "Error Login Request :" + volleyError.toString());
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_GET_WAREHOUSE);
                return params;
            }
        };

        mRequestQueue.add(mStringRequest6);
//
    }
    // Ammar --> end get WareHouse data from server



    private void requestNoteLookUpsFromServer(Context context) {
        //get login url

        //  RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        // StringRequest mStringRequest;

        //RequestQueue initialized
        // mRequestQueue = Volley.newRequestQueue(context);

//        Log.d("requestTemplates", "URL: " + CONSTANTS.API_LINK);

        ArrayList<NoteLookUp> noteLookUps = new ArrayList<>();


        //String Request initialized
        mStringRequest7 = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                //delete all exist item
                //    dbObject.deleteItemsFormTable(Database.WAREHOUSE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray appsArr = jsonObject.getJSONArray("items");

                    Log.d("requestnotelookups", "JSON Response test" + appsArr);


                    //loop on array
                    for (int i = 0; i < appsArr.length(); i++) {
                        JSONObject noteLookUpObject = appsArr.getJSONObject(i);
                        NoteLookUp noteLookUp = new NoteLookUp();
                        noteLookUp.setName(noteLookUpObject.getString("d"));
                        noteLookUp.setCode(noteLookUpObject.getString("row_id"));
                        noteLookUps.add(noteLookUp);

                        //check record is exist in template table
                        if (!dbObject.isItemExist(Database.NOTE_LOOK_UP, "code", noteLookUp.getCode())) {
                            //insert template in template table
                            dbObject.insertNewNoteLookUp(noteLookUp);
                        }

//                        Log.d("requestTemplates", "JSON Response " + templateObject.getString("template_id") + "," + templateObject.getString("template_name"));
                    }
                    Toast.makeText(context,"تم أضافة أنواع الملاحظات بنجاح", Toast.LENGTH_LONG).show();
//                    try {
//
//                        ArrayAdapter<Warehouse> dataAdapter = new ArrayAdapter<Warehouse>(getApplicationContext(), android.R.layout.simple_spinner_item, warehouses);
//                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        wareHouseSpinner1.setAdapter(dataAdapter);
//                        Intent i = new Intent(OpenApplicationDetails.this, EstimatedItemsListAdapter.class);
//                        i.putExtra("warehouseSpinner", warehouses);
////                        wareHouseSpinner2.setAdapter(dataAdapter);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
                    // Log.d("requestTemplates", "JSON Response " + dataAdapter);


                } catch (Exception ex) {
                    endLoading();
                    ex.printStackTrace();
                }

                //Toast.makeText(context,"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                endLoading();
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {

                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
//                Log.d("requestApplicationsEer", "Error Login Request :" + volleyError.toString());
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_GET_NOTE_LOOK_UPS);
                return params;
            }
        };

        mRequestQueue.add(mStringRequest7);
//
    }

    private void requestAttchmentTypesFromServer(Context context) {
        ArrayList<AttchmentType> attchmentTypes = new ArrayList<>();
        //String Request initialized
        mStringRequest8 = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray appsArr = jsonObject.getJSONArray("items");

                    Log.d("requestattchmentTypess", "JSON Response test" + appsArr);


                    //loop on array
                    for (int i = 0; i < appsArr.length(); i++) {
                        JSONObject attchmentTypesObject = appsArr.getJSONObject(i);
                        AttchmentType attchmentType = new AttchmentType();
                        attchmentType.setText(attchmentTypesObject.getString("d"));
                        attchmentType.setCode(attchmentTypesObject.getString("r"));
                        attchmentTypes.add(attchmentType);

                        //check record is exist in template table
                        if (!dbObject.isItemExist(Database.ATTACHMENT_TYPE_TABLE, "code", attchmentType.getCode())) {
                            //insert template in template table
                            dbObject.insertNewAttchmentType(attchmentType);
                        }

//                        Log.d("requestTemplates", "JSON Response " + templateObject.getString("template_id") + "," + templateObject.getString("template_name"));
                    }
                    Toast.makeText(context,"تم أضافة أنواع الملحقات بنجاح", Toast.LENGTH_LONG).show();

                } catch (Exception ex) {
                    endLoading();
                    ex.printStackTrace();
                }

                //Toast.makeText(context,"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                endLoading();
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {

                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
//                Log.d("requestApplicationsEer", "Error Login Request :" + volleyError.toString());
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_ATTACHMENT_TYPE);
                return params;
            }
        };

        mRequestQueue.add(mStringRequest8);
//
    }

public void insertDataToDatabase(Context context){
    getMaterialsFromServer(context);
    requestPriceListFromServer(context);
    requestprojectTypeFromServer(context);
    requestWarehouseFromServer(context);
    requestNoteLookUpsFromServer(context);
    requestAttchmentTypesFromServer(context);
    getTemplatesFromServer(context);

  //  getItemsFromServer(context);
}


}
