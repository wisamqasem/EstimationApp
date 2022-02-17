package com.jdeco.estimationapp.ui.forms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.adapters.EstimatedItemsListAdapter;
import com.jdeco.estimationapp.adapters.EstimatedTemplatesListAdapter;
import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.EstimationItem;
import com.jdeco.estimationapp.objects.Item;
import com.jdeco.estimationapp.objects.PriceList;
import com.jdeco.estimationapp.objects.ProjectType;
import com.jdeco.estimationapp.objects.Template;
import com.jdeco.estimationapp.objects.Warehouse;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.GeneralFunctions;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.ui.MainActivity;

import java.util.ArrayList;
import java.util.Date;

public class OpenDoneApplications extends AppCompatActivity {

    TextView appID, appDate, customerName, customerAddress, branch, sbranch, appType, phoneTB,address,phase1Quntitiy,phase3Quntitiy;
    Spinner masterItemsDropList, subItemsDropList, itemsDropList, itemsDropList2, priceListSpinner1, wareHouseSpinner1, projectTypeSpinner1, priceListSpinner2, wareHouseSpinner2;
    Spinner  itemsDropListDialog;
    Button  resetApp;//addItemToListBtn,addTemplateBtn;
    View mView;
    ArrayList<EstimationItem> estimationItems = null ;
    ArrayList<Item> estimatedItems = null ;
    ArrayList<Item> submitEstimatedItems = null ;
    ArrayList<Template>  estimatedTemplates = null;
    EstimatedItemsListAdapter estimatedItemsListAdapter;
    EstimatedTemplatesListAdapter estimatedTemplatesListAdapter;
    Button submitBtn, cancelBtn;
    RecyclerView itemsList , templatesList;
    Database dbObject;
    Session session;
    EditText phase1, phase3;

    ApplicationDetails applicationDetails;
    private Session sessionManager;
    private RecyclerView.Adapter itemsListAdapter , templatesListAdapter;
    private String groupID;
    private Context context;
    ArrayList<Item> materialsList = null;
    ArrayList<PriceList> priceListArrayList = null;
    ArrayList<Warehouse> warehouseArrayList = null;
    ArrayList<ProjectType> projectTypeArrayList = null;

    private String TAG = "OpenApplicationDetails";












    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_done_applications);
     //   Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.open_done_application_details));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Add back arrow in action bar
        }




        initilize();

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
        phase1 = (EditText) findViewById(R.id.Phase_1);
        phase3 = (EditText) findViewById(R.id.Phase_3);

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

        resetApp= (Button) findViewById(R.id.resetBtn) ;


        //get application details
        applicationDetails = dbObject.getApplications(session.getValue("APP_ID"),"D",session.getValue("username")).get(0);
        Log.d("phase3",": "+applicationDetails.getPhase3Meter());
        assignAppDetails(applicationDetails);
        Log.d(TAG,"Items list size is "+dbObject.getItems("0").size());




        //        Ammar --> get priceList data
        if (dbObject.tableIsEmpty(Database.PRICE_LIST_TABLE)) {
            // requestPriceListFromServer();
            warning(getResources().getString(R.string.no_data_found));
        } else {
            priceListArrayList = dbObject.getPriceList();
          //  appendPriceListToSpinner(priceListSpinner1,priceListArrayList,null);

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

           // appendWareHouseListToSpinner(wareHouseSpinner1,warehouseArrayList,null);
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
         //   appendProjectTypeListToSpinner(projectTypeSpinner1,projectTypeArrayList,null);
//            ArrayAdapter<ProjectType> dataAdapter = new ArrayAdapter<ProjectType>(getApplicationContext(), android.R.layout.simple_spinner_item, projectTypeArrayList);
//
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//            projectTypeSpinner1.setAdapter(dataAdapter);
        }


        // if the estimated item table is not empty add the item to the list.
        if(!dbObject.tableIsEmpty(Database.ESTIMATED_ITEMS_TABLE)){
            estimatedItems =  dbObject.getEstimatedItems("0",session.getValue("APP_ID"));

                //add this item to recycler view and refresh list
                AdditemsToList(estimatedItems);
            Log.d("estimatedTemplates","estimatedItems size : "+estimatedItems.size());
        }









        // if the temblate table is not empty add the templates to the list.
        if(!dbObject.tableIsEmpty(Database.ESTIMATED_TEMPLATES_TABLE)){
            ArrayList<Template> estimatedTemplates =  dbObject.getEstimatedTemplates(session.getValue("APP_ID"));
            AddTemplatesToList(estimatedTemplates);

            Log.d("estimatedTemplates",": "+estimatedTemplates.size());
        }

         resetApp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        dbObject.updateApplicationStatus(session.getValue("APP_ID"),"N");
Intent goToMain = new Intent(OpenDoneApplications.this,OpenApplicationDetails.class);
startActivity(goToMain);
    }
});


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }





    private void warning (String text){
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



    private void AdditemsToList(ArrayList<Item> list) {

        //add item to the list
        //  estimatedTemplates.add(list);

        //Initialize our array adapter notice how it references the listitems
        estimatedItemsListAdapter = new EstimatedItemsListAdapter(this, list,"D");
        //its data has changed so that it updates the UI
        itemsList.setAdapter(estimatedItemsListAdapter);
        estimatedItemsListAdapter.notifyDataSetChanged();
    }


    private void AddTemplatesToList(ArrayList<Template> list) {

        //add item to the list
        //  estimatedTemplates.add(list);

        //Initialize our array adapter notice how it references the listitems
        estimatedTemplatesListAdapter = new EstimatedTemplatesListAdapter(this, list,"D");
        //its data has changed so that it updates the UI
        templatesList.setAdapter(estimatedTemplatesListAdapter);
        estimatedTemplatesListAdapter.notifyDataSetChanged();
    }


    //assign values to application
    private void assignAppDetails(ApplicationDetails app) {
        try {

            appID.setText(app.getAppID());
            appDate.setText((app.getAppDate().substring(0, 10)).trim());
            customerName.setText(app.getCustomerName());
            phase1Quntitiy.setText(app.getPhase1Meter());
            phase3Quntitiy.setText(app.getPhase3Meter());
            if (app.getCustomerAddress().equalsIgnoreCase("null"))
                customerAddress.setText(" ");
            else
                customerAddress.setText(app.getCustomerAddress());

            if (app.getPhone().equalsIgnoreCase("null"))
                phoneTB.setText(" ");
            else
                phoneTB.setText(app.getPhone());
          //  customerAddress.setText(app.getCustomerAddress());
           // phoneTB.setText(app.getPhone());
            branch.setText(app.getBranch());
            appType.setText(app.getAppType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        goBack();

    }

    public void goBack() {

        Intent back = new Intent(getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("goTo","done");
        back.putExtras(bundle); //Put your id to your next Intent
        startActivity(back);


    }







}