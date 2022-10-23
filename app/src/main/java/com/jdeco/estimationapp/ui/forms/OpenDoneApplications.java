package com.jdeco.estimationapp.ui.forms;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.adapters.EstimatedItemsListAdapter;
import com.jdeco.estimationapp.adapters.EstimatedTemplatesListAdapter;
import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.AttchmentType;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.objects.EstimationItem;
import com.jdeco.estimationapp.objects.Image;
import com.jdeco.estimationapp.objects.Item;
import com.jdeco.estimationapp.objects.NoteInfo;
import com.jdeco.estimationapp.objects.NoteLookUp;
import com.jdeco.estimationapp.objects.PriceList;
import com.jdeco.estimationapp.objects.ProjectType;
import com.jdeco.estimationapp.objects.Template;
import com.jdeco.estimationapp.objects.Warehouse;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.GeneralFunctions;
import com.jdeco.estimationapp.operations.Helper;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.operations.notesDialogFragment;
import com.jdeco.estimationapp.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OpenDoneApplications extends AppCompatActivity {

    TextView appID, appDate, customerName, customerAddress, branch, sbranch, appType, phoneTB, address, phase1Quntitiy, phase3Quntitiy;
    Spinner masterItemsDropList, subItemsDropList, itemsDropList, itemsDropList2, priceListSpinner1, wareHouseSpinner1, projectTypeSpinner1, priceListSpinner2, wareHouseSpinner2;
    Spinner itemsDropListDialog;
    Button resetApp,previewBtn,displayNotesBtn;//addItemToListBtn,addTemplateBtn;
    View mView;
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
    Helper helper;
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
    ArrayList<NoteLookUp> noteLookUpsArrayList = null;
    ArrayList<AttchmentType> imageLookupsArrayList = null;
    ArrayList<String> notesList ;
    ProgressDialog progress;
    String phase1txt = "0";
    String phase3txt = "0";

    String note = "";
    String appId = "";

    // Add image
    ImageView image1, image2, image3, image4, image5, image6;
    TextView imageText1, imageText2, imageText3, imageText4, imageText5, imageText6;
    //To show that this image belong to (new service) application
    private final String NEW_SERVICE = "_New_Service";

    // Blocks
    View templatesBlocks, itemsBlock, imagesBlocks, imagesBlock1, imagesBlock2, image1CardView, image2CardView, image3CardView, image4CardView, image5CardView, image6CardView;


    private String TAG = "OpenApplicationDetails";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_done_applications);
        //   Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

// Remove keyboard focus when start activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.open_done_application_details));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Add back arrow in action bar
        }


        initilize();
// Test code
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


        previewBtn = (Button) findViewById(R.id.previewBtn);
        displayNotesBtn= (Button) findViewById(R.id.displayNotesBtn);

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
        notesList = new ArrayList<>();



        progress = new ProgressDialog(OpenDoneApplications.this);
        progress.setTitle(getResources().getString(R.string.please_wait));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);



        //Add image
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);
        image6 = findViewById(R.id.image6);


        imageText1 = findViewById(R.id.imageText1);
        imageText2 = findViewById(R.id.imageText2);
        imageText3 = findViewById(R.id.imageText3);
        imageText4 = findViewById(R.id.imageText4);
        imageText5 = findViewById(R.id.imageText5);
        imageText6 = findViewById(R.id.imageText6);

        dbObject = new Database(this);
        session = new Session(this);
        helper = new Helper(this);
        applicationDetails = new ApplicationDetails();

        resetApp = (Button) findViewById(R.id.resetBtn);
        estimatedItemsListAdapter = new EstimatedItemsListAdapter(this, estimatedItems);
        estimatedTemplatesListAdapter = new EstimatedTemplatesListAdapter(this);


        appId=session.getValue("APP_ID");

        //get application details
        applicationDetails = dbObject.getApplications(session.getValue("APP_ID"), "D", session.getValue("username")).get(0);
        Log.d("phase3", ": " + applicationDetails.getPhase3Meter());
        assignAppDetails(applicationDetails);
        Log.d(TAG, "Items list size is " + dbObject.getItems("0").size());


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

        // change visibility of Blocks
        templatesBlocks = findViewById(R.id.templatesBlock);
        itemsBlock = findViewById(R.id.itemsBlock);
        imagesBlocks = findViewById(R.id.imagesBlocks);
        imagesBlock1 = findViewById(R.id.imagesBlock1);
        imagesBlock2 = findViewById(R.id.imagesBlock2);

        image1CardView = findViewById(R.id.image1CardView);
        image2CardView = findViewById(R.id.image2CardView);
        image3CardView = findViewById(R.id.image3CardView);
        image4CardView = findViewById(R.id.image4CardView);
        image5CardView = findViewById(R.id.image5CardView);
        image6CardView = findViewById(R.id.image6CardView);

        if (estimatedItemsListAdapter.getItemCount() != 0) {
            itemsBlock.setVisibility(View.VISIBLE);

        } else {
            itemsBlock.setVisibility(View.GONE);

        }
        if (estimatedTemplatesListAdapter.getItemCount() != 0) {
            templatesBlocks.setVisibility(View.VISIBLE);

        } else {
            templatesBlocks.setVisibility(View.GONE);

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
        if (!dbObject.tableIsEmpty(Database.ESTIMATED_ITEMS_TABLE)) {
            estimatedItems = dbObject.getEstimatedItems("0", session.getValue("APP_ID"));

            //add this item to recycler view and refresh list
            AddItemsToList(estimatedItems);
            Log.d("estimatedTemplates", "estimatedItems size : " + estimatedItems.size());
        }


        // if the temblate table is not empty add the templates to the list.
        if (!dbObject.tableIsEmpty(Database.ESTIMATED_TEMPLATES_TABLE)) {
            ArrayList<Template> estimatedTemplates = dbObject.getEstimatedTemplates(session.getValue("APP_ID"));
            AddTemplatesToList(estimatedTemplates);

            Log.d("estimatedTemplates", ": " + estimatedTemplates.size());
        }

        previewCapturedImage();



        // معاينة الطلب
        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PreviewData.class);
                startActivity(i);
            }
        });


        displayNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotes();
            }
        });




        resetApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbObject.updateApplicationStatus(session.getValue("APP_ID"), "N", "1");
                Intent goToMain = new Intent(OpenDoneApplications.this, OpenApplicationDetails.class);
                startActivity(goToMain);
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    private void warning(String text) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(text);
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


    private void AddItemsToList(ArrayList<Item> list) {

        //add item to the list
        //  estimatedTemplates.add(list);

        //Initialize our array adapter notice how it references the listitems
        estimatedItemsListAdapter = new EstimatedItemsListAdapter(this, list, "D");
        //its data has changed so that it updates the UI
        itemsList.setAdapter(estimatedItemsListAdapter);
        estimatedItemsListAdapter.notifyDataSetChanged();
        itemsBlock.setVisibility(View.VISIBLE);
    }

    void displayNotes(){

        if (helper.isInternetConnection()) {
            progress.show();
            getNotes();
        } else {
            GeneralFunctions.messageBox(getApplicationContext(), "لا يوجد أتصال", "أرجاء فحص الأتصال بالأنترنت");
        }

    }


    private void getNotes() {
        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<NoteInfo> notes = new ArrayList<NoteInfo>();

                Log.d("getApplicationServices", "Response: " + response);

                //create json object
                try {
                    JSONObject applicationResultObject = new JSONObject(response);
                    if(applicationResultObject.getString("count").equals("0")){

                        GeneralFunctions.messageBox(OpenDoneApplications.this,"لا يوجد ملاحظات","لا يوجد ملاحظات للطلب .");
                        progress.dismiss();
                        return;
                    }
                    //get application array according to items array object
                    JSONArray applicationJsonArr = applicationResultObject.getJSONArray("items");

                    Log.d("man1234", ":" + applicationJsonArr.length());
                    //loop on the array
                    for (int i = 0; i < applicationJsonArr.length(); i++) {
                        JSONObject applicationObject = applicationJsonArr.getJSONObject(i);

                        //Create application details object
                        NoteInfo noteInfo = new NoteInfo();

                        noteInfo.setAppId(applicationObject.getString("appl_id"));
                        noteInfo.setComments(applicationObject.getString("comments"));
                        noteInfo.setCreated_by(applicationObject.getString("created_by"));
                        noteInfo.setCreation_date(applicationObject.getString("creation_date"));

                        notes.add(noteInfo);
                    }


                    DialogFragment fragment = notesDialogFragment.newInstance(notes);
                    int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                    int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

                    fragment.show(getSupportFragmentManager(), "some tag");


                } catch (Exception ex) {
                    Log.d("error", ":" + ex);
                    GeneralFunctions.messageBox(OpenDoneApplications.this,"فشل طلب الملاحظات",ex.toString());
                    progress.dismiss();
                    ex.printStackTrace();
                }
                progress.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GeneralFunctions.messageBox(getApplicationContext(), "فشل طلب الخدمات", error.toString());
                progress.dismiss();
                //  progress.dismiss();
                //  Log.d("getItemsFromServer", "Error request applications :" + error.toString());
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                //parameters

                params.put("appId", appId);
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_GET_NOTES);

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


    private void AddTemplatesToList(ArrayList<Template> list) {

        //add item to the list
        //  estimatedTemplates.add(list);
        //Initialize our array adapter notice how it references the listitems
        estimatedTemplatesListAdapter = new EstimatedTemplatesListAdapter(this, list, "D");
        //its data has changed so that it updates the UI
        templatesList.setAdapter(estimatedTemplatesListAdapter);
        estimatedTemplatesListAdapter.notifyDataSetChanged();
        templatesBlocks.setVisibility(View.VISIBLE);
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
        bundle.putString("goTo", "done");
        back.putExtras(bundle); //Put your id to your next Intent
        startActivity(back);

    }


    private void previewCapturedImage() {
        try {

            ArrayList<Image> imagesArr = dbObject.getImages(appId);
            for(int i=0;i<imagesArr.size();i++){
                Image image = imagesArr.get(i);
                String path =   image.getFile();

//            String path1 = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appId + "_1.jpg";
//            String path2 = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appId + "_2.jpg";
//            String path3 = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appId + "_3.jpg";
//            String path4 = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appId + "_4.jpg";
//            String path5 = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appId + "_5.jpg";
//            String path6 = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appId + "_6.jpg";

                Log.d("Image Path 1 ", path);
                // bimatp factory
                BitmapFactory.Options options = new BitmapFactory.Options();

                // downsizing image as it throws OutOfMemory Exception for larger
                // images

                options.inSampleSize = 8;
                File pic = new File(path);
                String imageName = image.getFileName();
                if (pic.exists()) {
                    final Bitmap bitmap1 = BitmapFactory.decodeFile(path,
                            options);
                    if(imageName.equals(appId + "_1")){
                        image1.setImageBitmap(bitmap1);
                   }
                    else  if(imageName.equals(appId + "_2")){
                        image2.setImageBitmap(bitmap1);
                       }
                    else  if(imageName.equals(appId + "_3")){
                        image3.setImageBitmap(bitmap1);
                       }
                    else  if(imageName.equals(appId + "_4")){
                        image4.setImageBitmap(bitmap1);
                        }
                    else  if(imageName.equals(appId + "_5")){
                        image5.setImageBitmap(bitmap1);
                        }
                    else  if(imageName.equals(appId + "_6")){
                        image6.setImageBitmap(bitmap1);
                        }
                }

            }//END OF FOR LOOP

        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}