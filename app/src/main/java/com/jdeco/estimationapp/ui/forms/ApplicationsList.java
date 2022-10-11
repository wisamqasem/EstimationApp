package com.jdeco.estimationapp.ui.forms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.adapters.ApplicationAdapter;
import com.jdeco.estimationapp.network.ServerHandler;
import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.objects.Item;
import com.jdeco.estimationapp.objects.PriceList;
import com.jdeco.estimationapp.objects.ProjectType;
import com.jdeco.estimationapp.objects.RecyclerItemClickListener;
import com.jdeco.estimationapp.objects.ResultCode;
import com.jdeco.estimationapp.objects.ServiceInfo;
import com.jdeco.estimationapp.objects.Warehouse;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.GeneralFunctions;
import com.jdeco.estimationapp.operations.Helper;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ApplicationsList extends Fragment {

    EditText searchTB;
    Button searchBtn, refreshBtn;
    private RecyclerView appsDropList;
    private RecyclerView.Adapter appsAdapter;
    public ArrayList<ApplicationDetails> applicationDetailsList;
    public ArrayList<String> areasList;
    private TextView empName, openTicketsCount, groupName;
    private RadioGroup filterByRadioGroup;
    private Session session;
    private String groupID;
    private Database dbObject;
    private Helper helper;
    private ProgressDialog pDialog;
    public Context context;
    private RadioButton radioButton;
    private ImageButton searchAreaBtn;
    private String searchBy;
    private String searchText = "";

    Spinner branchesSP;


    //change by ammar
    SwipeRefreshLayout swipeRefreshLayout;

    InputMethodManager imm;
    ProgressDialog progress;

    public ApplicationsList() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.applications_list_ui, container, false);
        try {
            context = view.getContext();
            // Set title bar
            ((MainActivity) getActivity()).setToolbarTitle(context.getResources().getString(R.string.applications_list_lbl));

            //initilize controls
            initialize(view);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return view;
    }

    private void initialize(View view) {

        searchTB = (EditText) view.findViewById(R.id.appid);
        searchBtn = (Button) view.findViewById(R.id.searchBtn);
        refreshBtn = (Button) view.findViewById(R.id.refreshList);
        // swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);


        empName = (TextView) view.findViewById(R.id.empName);
        groupName = (TextView) view.findViewById(R.id.empGroup);
        filterByRadioGroup = (RadioGroup) view.findViewById(R.id.radio);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        progress = new ProgressDialog(context);
        progress.setTitle(getResources().getString(R.string.please_wait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        openTicketsCount = (TextView) view.findViewById(R.id.open_tickets_count);

        // prepare tickets drop list
        appsDropList = (RecyclerView) view.findViewById(R.id.recycler_view);

        appsDropList.setHasFixedSize(true);
        appsDropList.setLayoutManager(new LinearLayoutManager(view.getContext()));

        searchAreaBtn = (ImageButton) view.findViewById(R.id.areaSearhBtn);

        branchesSP = (Spinner) view.findViewById(R.id.searchByAreaSP);

        session = new Session(view.getContext());
        //initiate the db object
        dbObject = new Database(view.getContext());
        helper = new Helper(view.getContext());
//        filterByRadioGroup = (RadioGroup) view.findViewById(R.id.choicGroup);

        //Initiate session manager
        final String empID = session.getUserDetails().getUsername();
        String empNameText = session.getUserDetails().getFullName();
        //String groupNameTxT = session.getGroup().getGroupName();

        empName.setText(empNameText);
        //groupName.setText(groupNameTxT);
        applicationDetailsList = new ArrayList<ApplicationDetails>();
        areasList = new ArrayList<String>();
        //earchTB.setInputType(InputType.TYPE_CLASS_NUMBER);

        areasList= dbObject.getAreas("N",session.getValue("username"));
        appendListToSpinner(branchesSP, areasList, null);// appened list of ares to the spinner

if(session.checkValue("searchTxt")) {
    searchText = session.getValue("searchTxt");
    searchTB.setText(session.getValue("searchTxt"));
    if(session.checkValue("searchAreaTxt"))
    branchesSP.setSelection(findIndexList(areasList, session.getValue("searchAreaTxt")));
   else  branchesSP.setSelection(0);


}

        BindItemsToList();


      //  appendListToSpinner(branchesSP, areasList, null);
        Log.d("internet Connection", "is connected : " + helper.isInternetConnection());


//        if(helper.isInternetConnection()){
//        //    progress.show();
//        //    getApplicationsFromServer(session.getUserDetails().getUsername(), null);//.equals(null) ? "":session.getUserDetails().getUsername()
//
//        }else {
//            BindItemsToList();
//        }


//        if (dbObject.tableIsEmpty(Database.APPLICATIONS_TABLE)){
//
//
//        }
//        else BindItemsToList();


        // change by Ammar add action to refresh recyclerView
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getApplicationsFromServer(session.getUserDetails().getUsername(), null);
//                appsAdapter.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);
////                openTicketsCount.setText(applicationDetailsList.size());
//            }
//        });
        //add action to refresh btn
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.show();
                searchText = "";
                session.setValue("searchTxt",searchText);
                session.setValue("searchAreaTxt","الجميع");
                searchTB.setText("");
                if (helper.isInternetConnection()) {
                    dbObject.deleteNewApplications();
                    getApplicationsFromServer(session.getUserDetails().getUsername(), null);//.equals(null) ? "":session.getUserDetails().getUsername()
                    appendListToSpinner(branchesSP, areasList, null);
                } else {
                    GeneralFunctions.messageBox(context, getResources().getString(R.string.check_internet_connection), getString(R.string.check_internet_saved_data));
                    BindItemsToList();

                    // Toast.makeText(context, getResources().getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
                }
                imm.hideSoftInputFromWindow(searchTB.getWindowToken(), 0);//to hide the keybored after press the button;

            }
        });

        // make enter key work
        searchTB.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    searchText = searchTB.getText().toString();
                    searchText = arabicToDecimal(searchTB.getText().toString());
                    if (!dbObject.tableIsEmpty("applications")) BindItemsToList();

                    applicationDetailsList = dbObject.getApplicationsBySearch(null, searchText, searchBy, "N", session.getValue("username"));


                    imm.hideSoftInputFromWindow(searchTB.getWindowToken(), 0);//to hide the keybored after press the button;
                    return true;
                }
                return false;
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int selectedId = filterByRadioGroup.getCheckedRadioButtonId();

//                radioButton = (RadioButton) view.findViewById(selectedId);
                searchText = searchTB.getText().toString();
                session.setValue("searchTxt",searchText );

//                if (filterByRadioGroup.getCheckedRadioButtonId() == view.findViewById(R.id.byAppID).getId()) {
//                    searchBy = "byAppID";
                searchText = arabicToDecimal(searchTB.getText().toString());
//
//                } else if (filterByRadioGroup.getCheckedRadioButtonId() == view.findViewById(R.id.byName).getId()) {
//
//                    searchBy = "byName";
//                }

//                if (filterByRadioGroup.getCheckedRadioButtonId() == view.findViewById(R.id.bySub_branch).getId()) {
//                    searchBy = "bySub_branch";
//                } else if (filterByRadioGroup.getCheckedRadioButtonId() == view.findViewById(R.id.byServiceNo).getId()) {
//                    searchBy = "byServiceNo";
//                    searchText = arabicToDecimal(searchTB.getText().toString());
//                }
                if (!dbObject.tableIsEmpty("applications")) BindItemsToList();

                applicationDetailsList = dbObject.getApplicationsBySearch(null, searchText, searchBy, "N", session.getValue("username"));

                imm.hideSoftInputFromWindow(searchTB.getWindowToken(), 0);//to hide the keybored after press the button;

            }
        });


        searchAreaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText = branchesSP.getSelectedItem().toString();
                session.setValue("searchTxt", searchText);
                session.setValue("searchAreaTxt", searchText);
                BindItemsToList();
                imm.hideSoftInputFromWindow(searchTB.getWindowToken(), 0);//to hide the keybored after press the button;
            }
        });



        //handle list item click
        appsDropList.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {

try{

    GeneralFunctions.startLoading(progress);

    Log.d("appsDropList",":"+applicationDetailsList.get(i));

    // get the selected ticket from list
    final ApplicationDetails applicationDetails = applicationDetailsList.get(i);


    // initilize the Fragment
    session.setValue("APP_ID", applicationDetails.getAppID());
    session.setValue("NO_OF_PHASE", applicationDetails.getNo_of_phase());
    Intent intent = new Intent();
    if (applicationDetails.getAppl_type_code().equals("04")) {
        //open application details waiver
        intent = new Intent(context, OpenApplicationWaiver.class);
    } else if (applicationDetails.getAppl_type_code().equals("01")) {
        //open application details
        intent = new Intent(context, OpenApplicationDetails.class);

    }else if (applicationDetails.getAppl_type_code().equals("03")) {
        //open application details
        intent = new Intent(context, OpenApplicationDetails.class);

    }

    //open application details
    //  Intent intent = new Intent(context, OpenApplicationDetails.class);

    //pass parameters to application details activity
    Bundle bundle = new Bundle();
    bundle.putString("APP_ID", applicationDetails.getAppID()); //Your id
    intent.putExtras(bundle); //Put your id to your next Intent
    startActivity(intent);
}catch (Exception ex){
    GeneralFunctions.messageBox(context,"حدث خطاء",ex.toString());
}
                    }
                }));

        //get application from the server
        // getApplicationsFromServer(session.getUserDetails().getUsername().equals(null) ? "":session.getUserDetails().getUsername(),null);
        //get materials from the server


    }


    private class getApplicationsListProcess extends AsyncTask<String, ResultCode, ResultCode> {
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //swipeRefreshLayout.setRefreshing(true);

            // Showing progress dialog
            pDialog = new ProgressDialog(context);
            pDialog.setMessage(getResources().getString(R.string.processing_lbl));
            pDialog.setCancelable(false);
            pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.cancel_lbl), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    BindItemsToList();
                    cancel(true);
                }
            });
            pDialog.show();
        }

        @Override
        protected ResultCode doInBackground(String... arg0) {
            ResultCode result = new ResultCode();
            try {
                // Creating service handler class instance
                ServerHandler serverHandler = new ServerHandler();

                //get data from server
                //   result = serverHandler.getApplicationsFromServer(context);
                getApplicationsFromServer(session.getUserDetails().getUsername(), null);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // result.setResultCode("1");
            // result.setResultMsg(context.getResources().getString(R.string.load_success_msg));

            return result;
        }

        @Override
        protected void onPostExecute(ResultCode result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();


            // stopping swipe refresh
            //swipeRefreshLayout.setRefreshing(false);
//            if (result.getResultCode() != null) {
//                if (result.getResultCode().equals("1")) // if the list updated or no internet connection or no items
//                {
//                  //  BindItemsToList();
//                }
//            }
        }
    }


    private void BindItemsToList() {

        if (searchText.matches("") || searchText.matches(" ") || searchText.matches("الجميع")) {
            //get all applications
            applicationDetailsList = dbObject.getApplications(null, "N", session.getValue("username"));

        } else {
            Log.d("BindItemsToList", searchText);
            //get all applications by search
            applicationDetailsList = dbObject.getApplicationsBySearch(null, searchText, searchBy, "N", session.getValue("username"));
        }
        if (applicationDetailsList.size() == 0) {
            Toast.makeText(context, "لا يوجد طلبات", Toast.LENGTH_LONG).show();
        }



        //Initialize our array adapter notice how it references the listitems
        appsAdapter = new ApplicationAdapter(context, applicationDetailsList);
        //its data has changed so that it updates the UI
        appsDropList.setAdapter(appsAdapter);
        openTicketsCount.setText(getResources().getString(R.string.number_of_items) + "  " + applicationDetailsList.size());
        progress.dismiss();
        appsAdapter.notifyDataSetChanged();


    }


    //send request items from server
    private void getApplicationsFromServer(String username, String date) {
        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context);


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d("getItemsFromServer", "Response: " + response);

                //create json object
                try {
                    JSONObject applicationResultObject = new JSONObject(response);

                    //get application array according to items array object
                    JSONArray applicationJsonArr = applicationResultObject.getJSONArray("items");

                    Log.d("man1234", ":" + applicationJsonArr.length());
                    //loop on the array
                    for (int i = 0; i < applicationJsonArr.length(); i++) {
                        JSONObject applicationObject = applicationJsonArr.getJSONObject(i);

                        //Create application details object
                        ApplicationDetails applicationDetails = new ApplicationDetails();

                        applicationDetails.setCustomerName(applicationObject.getString("customer_name"));
                        applicationDetails.setAppID(String.valueOf(applicationObject.getInt("appl_id")));

                        applicationDetails.setBranch(applicationObject.getString("branch"));
                        applicationDetails.setStatus(applicationObject.getString("status"));
                        applicationDetails.setPhone(applicationObject.getString("mobile"));
                        Log.d("fuckig phone 1 : ", applicationDetails.getPhone());
                        applicationDetails.setCustomerAddress(applicationObject.getString("address"));


                        applicationDetails.setAppDate(applicationObject.getString("appl_date"));
                        applicationDetails.setAppType(applicationObject.getString("appl_type"));
                        //Log.d("man123",":" + applicationDetails.getAppType()+" the  id : --> "+i);
                        applicationDetails.setUsername(applicationObject.getString("username"));


                        applicationDetails.setRowId(applicationObject.getString("row_id"));
                        applicationDetails.setPrjRowId(applicationObject.getString("prj_row_id"));
                        applicationDetails.setOld_system_no(applicationObject.getString("old_system_no"));
                        applicationDetails.setOld_customer_name(applicationObject.getString("old_customer_name"));
                        applicationDetails.setOld_id_number(applicationObject.getString("old_id_number"));
                        applicationDetails.setId_number(applicationObject.getString("id_number"));
                        applicationDetails.setAccount_no(applicationObject.getString("account_no"));
                        applicationDetails.setAppl_type_code(applicationObject.getString("appl_type_code"));
                        applicationDetails.setStatus_code(applicationObject.getString("status_code"));
                        applicationDetails.setStatus_note(applicationObject.getString("status"));
                        applicationDetails.setService_status(applicationObject.getString("service_status"));
                        applicationDetails.setUsage_type(applicationObject.getString("usage_type"));
                        applicationDetails.setNo_of_phase(applicationObject.getString("no_of_phase"));
                        applicationDetails.setService_no(applicationObject.getString("service_no"));
                        applicationDetails.setProperty_type(applicationObject.getString("property_type"));
                        applicationDetails.setService_class(applicationObject.getString("service_class"));
                        applicationDetails.setTo_user_id(applicationObject.getString("to_user_id"));
                        applicationDetails.setNoofservices(applicationObject.getString("noofservices"));
                        applicationDetails.setMeter_type(applicationObject.getString("meter_type"));
                        applicationDetails.setInstall_date(applicationObject.getString("install_date"));
                        applicationDetails.setLast_read(applicationObject.getString("last_read"));
                        applicationDetails.setLast_read_date(applicationObject.getString("last_read_date"));
                        applicationDetails.setLast_qty(applicationObject.getString("last_qty"));
                        applicationDetails.setNotes(applicationObject.getString("notes"));
                        applicationDetails.setMeter_no(applicationObject.getString("meter_no"));
                        applicationDetails.setsBranch(applicationObject.getString("sub_branch"));


                        //check record is exist in applications table
                        // if the the application in not exist insert new application.
                        if (!dbObject.isItemExist(Database.APPLICATIONS_TABLE, "appId", String.valueOf(applicationObject.getInt("appl_id")))) {


                            applicationDetails.setPhase1Meter("0");
                            applicationDetails.setPhase3Meter("0");
                            applicationDetails.setTicketStatus("N");
                            applicationDetails.setSync("2");//0 : not sync , 1 sync , 2 new
                            applicationDetails.setPriceList(new PriceList("10033", "JDECo PNA Price List"));
                            applicationDetails.setProjectType(new ProjectType("Account List", "6"));

                            if(applicationObject.getString("branch").equals("القدس"))
                            applicationDetails.setWarehouse(new Warehouse("121", "Jerusalem Warehouse"));
                            else if(applicationObject.getString("branch").equals("اريحا"))
                            applicationDetails.setWarehouse(new Warehouse("101", "Jericho Warehouse"));
                            else if(applicationObject.getString("branch").equals("رام الله"))
                                applicationDetails.setWarehouse(new Warehouse("93", "Ramallah Warehouse"));
                            else if(applicationObject.getString("branch").equals("بيت لحم"))
                                applicationDetails.setWarehouse(new Warehouse("88", "Bethlehem Warehouse"));

Log.d("project",":"+applicationDetails.getProjectType().getProjectTypeName());

                            //insert application in application table
                            dbObject.insertNewApplication(applicationDetails);
                        } else {

                            dbObject.updateNewApplication(applicationDetails, "N");

                        }


                        //if the application statue is done keep it done and check if the application sync (مرحل )  if no update to yes  .
//                        else if(dbObject.checkAppDone(String.valueOf(applicationObject.getInt("appl_id")))){
//                            if(dbObject.checkAppSync(String.valueOf(applicationObject.getInt("appl_id")))){
//                                //applicationDetails.setSync("1");
//                                dbObject.updateApplicationData(applicationDetails.getAppID(),"sync","1");//sync yes
//                            }else dbObject.updateNewApplication(applicationDetails,"D");//sync no
//
//                        }
//                        else {
//                            if(dbObject.checkAppSync(String.valueOf(applicationObject.getInt("appl_id")))){
//                                applicationDetails.setSync("1");
//                                dbObject.updateNewApplication(applicationDetails,"N");//sync yes
//                            }//else dbObject.updateNewApplication(applicationDetails,"N");//sync no
//                        }

//dbObject.showApplications();
                    }
                    //  if (!dbObject.tableIsEmpty(Database.APPLICATIONS_TABLE))
                    BindItemsToList();
                } catch (Exception ex) {
                    progress.dismiss();
                    GeneralFunctions.messageBox(getContext(),"فشل أستدعاء الطلبات",ex.toString());
                    Log.d("error", ":" + ex);
                    ex.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GeneralFunctions.messageBox(context, "فشل تحديث الطلبات", error.toString());
                progress.dismiss();
                Log.d("getItemsFromServer", "Error request applications :" + error.toString());
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                //parameters
                params.put("username", username);
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_APPLICATIONS);

                return params;
            }
        };


        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });


        mRequestQueue.add(mStringRequest);
    }

    private int findIndexList(ArrayList<String> array, String x) {
        int pos = 0;
        for (int ii = 0; ii < array.size(); ii++) {
            if (array.get(ii).equals(x)) {
                pos = ii;
                break;
            }
        }
        return pos;

    }

    private void appendListToSpinner(Spinner spinner, ArrayList<String> list, String selectedValue) {
        try {
           // list = dbObject.getAreas("N",session.getValue("username"));
            //append items to activity
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
            //add adapter to spinner
            spinner.setAdapter(adapter);
        } catch (Exception ex) {
            GeneralFunctions.messageBox(context,"تعذر جلب المناطق",ex.toString());
            ex.printStackTrace();

        }
    }









    // change by Ammar arabicNumbersToDecimal
    private String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }


}
