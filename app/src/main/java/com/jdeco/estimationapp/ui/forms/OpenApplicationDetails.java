package com.jdeco.estimationapp.ui.forms;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.jdeco.estimationapp.BuildConfig;
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
import com.jdeco.estimationapp.objects.ResultCode;
import com.jdeco.estimationapp.objects.ServiceInfo;
import com.jdeco.estimationapp.objects.Template;
import com.jdeco.estimationapp.objects.Warehouse;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.GeneralFunctions;
import com.jdeco.estimationapp.operations.Helper;
import com.jdeco.estimationapp.operations.MyDialogFragment;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.operations.notesDialogFragment;
import com.jdeco.estimationapp.ui.MainActivity;
import com.jdeco.estimationapp.ui.SuccessScreen;
import com.jdeco.estimationapp.ui.screens.serviceInfo;
import com.viethoa.DialogUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OpenApplicationDetails extends AppCompatActivity  implements  View.OnClickListener, View.OnLongClickListener{
    TextView appID, appDate, customerName, customerAddress, branch, sbranch, appType, phoneTB, address,
            phase1Quntitiy, phase3Quntitiy, noteTV, noOfServices, noOfPhase, propertyType,near_by_serviceTV,serviceNo;
    Spinner masterItemsDropList, subItemsDropList, itemsDropList, itemsDropList2, priceListSpinner1, wareHouseSpinner1, projectTypeSpinner1, noteLookUpSP, imageLookUpsSP, priceListSpinner2, wareHouseSpinner2;
    Spinner itemsDropListDialog;
    Button addItemToListBtn, addTemplateBtn,closeDialog,previewBtn,displayNotesBtn;
    ImageView  micBtn;
    View mView, promptsView;
    LayoutInflater li;
    ArrayList<EstimationItem> estimationItems = null;
    ArrayList<Item> estimatedItems = null;
    ArrayList<Item> submitEstimatedItems = null;
    ArrayList<Template> estimatedTemplates = null;
    EstimatedItemsListAdapter estimatedItemsListAdapter;
    EstimatedTemplatesListAdapter estimatedTemplatesListAdapter;
    Button submitBtn, cancelBtn;
    RecyclerView itemsList, templatesList;
    ListView notesListLV;
    Database dbObject;
    Session session;
    Helper helper;
    EditText phase1, phase3, noteET;
    ImageButton showServicesBtn,showServicesBtn2;
    String base64Image="";
    RequestQueue mRequestQueue;
    SpeechRecognizer speechRecognizer;


    View enclouserBlock, templatesBlock, itemsBlock, noteBlock;

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

    private Uri fileUri; // file url to store image/video

    private static final String IMAGE_DIRECTORY_NAME = "EstimationImages";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;


    String note = "";
    String appId = "";

    File file;

    // Add image
    ImageView image1, image2, image3, image4, image5, image6;
    TextView imageText1, imageText2, imageText3, imageText4, imageText5, imageText6;
    ImageView removeImageBtn1, removeImageBtn2, removeImageBtn3, removeImageBtn4, removeImageBtn5, removeImageBtn6,
            submitImageBtn1, submitImageBtn2, submitImageBtn3, submitImageBtn4, submitImageBtn5, submitImageBtn6;
    //To show that this image belong to (new service) application
    private final String NEW_SERVICE = "_New_Service";
    //    String base64;
    int imagesFlag = 0, image1Flag = 0, image2Flag = 0, image3Flag = 0, image4Flag = 0, image5Flag = 0, image6Flag = 0;
    ScrollView scrollView;
    Uri photoUri;

    private static final int REQUEST_CAMERA_CODE = 100;//12;


    private String TAG = "OpenApplicationDetails";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// Remove keyboard focus when start activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (ContextCompat.checkSelfPermission(OpenApplicationDetails.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OpenApplicationDetails.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        setContentView(R.layout.open_application_details_ui);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.application_details_lbl));
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
        sbranch = (TextView) findViewById(R.id.sub_branchTV);
        appType = (TextView) findViewById(R.id.appType);
        noOfServices = (TextView) findViewById(R.id.noofservicesTV);
        noOfPhase = (TextView) findViewById(R.id.no_of_phaseTV);
        propertyType = (TextView) findViewById(R.id.property_typeTV);
        near_by_serviceTV=(TextView) findViewById(R.id.near_by_serviceTV);
        serviceNo=(TextView) findViewById(R.id.serviceNo);
//        phase1 = (EditText) findViewById(R.id.Phase_1);
//        phase3 = (EditText) findViewById(R.id.Phase_3);

        phase1Quntitiy = (TextView) findViewById(R.id.phase1Quntitiy);
        phase3Quntitiy = (TextView) findViewById(R.id.phase3Quntitiy);

        showServicesBtn = (ImageButton) findViewById(R.id.showServicesBtn);//this for services
        showServicesBtn2 = (ImageButton) findViewById(R.id.showServicesBtn2);//this for the nearst service

        previewBtn = (Button) findViewById(R.id.previewBtn);
        displayNotesBtn= (Button) findViewById(R.id.displayNotesBtn);


        //initilize spinners
        estimationItems = new ArrayList<>();
//        masterItemsDropList = (Spinner) findViewById(R.id.masterItemsDropList);
        //  subItemsDropList = (Spinner) findViewById(R.id.subItemsDropList);
        //itemsDropList = (Spinner) findViewById(R.id.itemsDropList2);

        notesList = new ArrayList<>();
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
        context = getApplicationContext();
        helper = new Helper(this);
        applicationDetails = new ApplicationDetails();

        estimatedItemsListAdapter = new EstimatedItemsListAdapter(this, estimatedItems);
        estimatedTemplatesListAdapter = new EstimatedTemplatesListAdapter(this);


        speechRecognizer =   SpeechRecognizer.createSpeechRecognizer(this);


        appId = session.getValue("APP_ID");


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

        removeImageBtn1 = findViewById(R.id.removeImageBtn1);
        removeImageBtn2 = findViewById(R.id.removeImageBtn2);
        removeImageBtn3 = findViewById(R.id.removeImageBtn3);
        removeImageBtn4 = findViewById(R.id.removeImageBtn4);
        removeImageBtn5 = findViewById(R.id.removeImageBtn5);
        removeImageBtn6 = findViewById(R.id.removeImageBtn6);


        submitImageBtn1 = findViewById(R.id.submitImageBtn1);
        submitImageBtn2 = findViewById(R.id.submitImageBtn2);
        submitImageBtn3 = findViewById(R.id.submitImageBtn3);
        submitImageBtn4 = findViewById(R.id.submitImageBtn4);
        submitImageBtn5 = findViewById(R.id.submitImageBtn5);
        submitImageBtn6 = findViewById(R.id.submitImageBtn6);

        scrollView = findViewById(R.id.scroll);

try{
    file = createImageFile();
    photoUri = FileProvider.getUriForFile(OpenApplicationDetails.this, BuildConfig.APPLICATION_ID+".provider",file);
}catch (Exception e){
    e.printStackTrace();
}


        progress = new ProgressDialog(OpenApplicationDetails.this);
        progress.setTitle(getResources().getString(R.string.please_wait));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        // change visibility of Blocks
        enclouserBlock = findViewById(R.id.enclouserBlock);

        templatesBlock = findViewById(R.id.templatesBlock);
        itemsBlock = findViewById(R.id.itemsBlock);


        if (estimatedItemsListAdapter.getItemCount() != 0) {
            itemsBlock.setVisibility(View.VISIBLE);

        } else {
            itemsBlock.setVisibility(View.GONE);

        }
//        if(dbObject.getNotes(appId).size()==0){
//           noteBlock.setVisibility(View.GONE);
//        }


        if (estimatedTemplatesListAdapter.getItemCount() != 0) {
            templatesBlock.setVisibility(View.VISIBLE);

        } else {
            templatesBlock.setVisibility(View.GONE);

        }
        String testQuntitiy = phase1Quntitiy.getText().toString();
        String testQuntitiy1 = phase3Quntitiy.getText().toString();

        if (phase1Quntitiy.getText().toString().equalsIgnoreCase("0") || phase1Quntitiy.getText().toString().isEmpty() || phase1Quntitiy.getText().toString().equalsIgnoreCase("null") &&
                phase3Quntitiy.getText().toString().equalsIgnoreCase("0") || phase3Quntitiy.getText().toString().isEmpty() || phase3Quntitiy.getText().toString().equalsIgnoreCase("null")) {
            // enclouserBlock.setVisibility(View.GONE);

        } else {
            //  enclouserBlock.setVisibility(View.VISIBLE);

        }

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
        applicationDetails = dbObject.getApplications(session.getValue("APP_ID"), "N", session.getValue("username")).get(0);


        assignAppDetails(applicationDetails);



        Log.d(TAG, "Items list size is " + dbObject.getItems("0").size());


        // if the temblate table is not empty add the templates to the list.
        if (!dbObject.tableIsEmpty(Database.ESTIMATED_TEMPLATES_TABLE)) {
            ArrayList<Template> estimatedTemplates = dbObject.getEstimatedTemplates(session.getValue("APP_ID"));
            AddTemplatesToList(estimatedTemplates);

            Log.d("estimatedTemplates", ": " + estimatedTemplates.size());
        }

        // if image table is not empty
        if (!dbObject.tableIsEmpty(Database.IMAGES_TABLE)) {
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_1" + NEW_SERVICE)) {

                image1Flag = helper.setImageFromDatabase(session.getValue("APP_ID") + "_1" + NEW_SERVICE, image1, imageText1, removeImageBtn1);
                submitImageBtn1.setVisibility(View.VISIBLE);
                if (image1Flag == 3) {
                    submitImageBtn1.setBackground(getResources().getDrawable(R.drawable.upload_background));
                }
//                image1Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_2" + NEW_SERVICE)) {

                image2Flag = helper.setImageFromDatabase(session.getValue("APP_ID") + "_2" + NEW_SERVICE, image2, imageText2, removeImageBtn2);
                submitImageBtn2.setVisibility(View.VISIBLE);
                if (image2Flag == 3) {
                    submitImageBtn2.setBackground(getResources().getDrawable(R.drawable.upload_background));
                }
//                image2Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_3" + NEW_SERVICE)) {

                image3Flag = helper.setImageFromDatabase(session.getValue("APP_ID") + "_3" + NEW_SERVICE, image3, imageText3, removeImageBtn3);
                submitImageBtn3.setVisibility(View.VISIBLE);
                if (image3Flag == 3) {
                    submitImageBtn3.setBackground(getResources().getDrawable(R.drawable.upload_background));
                }
//                image3Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_4" + NEW_SERVICE)) {

                image4Flag = helper.setImageFromDatabase(session.getValue("APP_ID") + "_4" + NEW_SERVICE, image4, imageText4, removeImageBtn4);
                submitImageBtn4.setVisibility(View.VISIBLE);
                if (image4Flag == 3) {
                    submitImageBtn4.setBackground(getResources().getDrawable(R.drawable.upload_background));
                }
//                image4Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_5" + NEW_SERVICE)) {

                image5Flag = helper.setImageFromDatabase(session.getValue("APP_ID") + "_5" + NEW_SERVICE, image5, imageText5, removeImageBtn5);
                submitImageBtn5.setVisibility(View.VISIBLE);
                if (image5Flag == 3) {
                    submitImageBtn5.setBackground(getResources().getDrawable(R.drawable.upload_background));
                }
//                image5Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_6" + NEW_SERVICE)) {

                image6Flag = helper.setImageFromDatabase(session.getValue("APP_ID") + "_6" + NEW_SERVICE, image6, imageText6, removeImageBtn6);
                submitImageBtn6.setVisibility(View.VISIBLE);
                if (image6Flag == 3) {
                    submitImageBtn6.setBackground(getResources().getDrawable(R.drawable.upload_background));
                }
//                image6Flag = 1;
            }

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




        previewCapturedImage();






        //initilize buttons
        submitBtn = (Button) findViewById(R.id.submitBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);



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


        showServicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.isInternetConnection()) {
                    progress.show();
                    getApplicationServices();
                } else {
                    GeneralFunctions.messageBox(context, "لا يوجد أتصال", "أرجاء فحص الأتصال بالأنترنت");
                }

            }
        });
        showServicesBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.isInternetConnection()) {
                    Intent i = new Intent(getApplicationContext(), serviceInfo.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("serviceNo",applicationDetails.getNear_by_service() ); //Your id
                    i.putExtras(bundle);
                    startActivity(i);

                } else {
                    GeneralFunctions.messageBox(context, "لا يوجد أتصال", "أرجاء فحص الأتصال بالأنترنت");
                }

            }
        });


//        // Add Image
//        image1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imagesFlag = 1;
//                if (image1Flag == 0) {
//                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    camera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
//
//                } else {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
//                    alertDialog.setTitle("");
//                    alertDialog.setMessage(R.string.edit_image_confirm);
//                    alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
//                                }
//                            });
//                    alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//                    alertDialog.show();
//                }
//            }
//        });
//
//        image2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imagesFlag = 2;
//                if (image2Flag == 0) {
//
//                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    camera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
//
//                } else {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
//                    alertDialog.setTitle("");
//                    alertDialog.setMessage(R.string.edit_image_confirm);
//                    alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
//                                }
//                            });
//                    alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//                    alertDialog.show();
//                }
//            }
//        });
//
//        image3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imagesFlag = 3;
//                if (image3Flag == 0) {
//
//                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    camera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
//
//                } else {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
//                    alertDialog.setTitle("");
//                    alertDialog.setMessage(R.string.edit_image_confirm);
//                    alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
//                                }
//                            });
//                    alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//                    alertDialog.show();
//                }
//            }
//        });
//
//        image4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imagesFlag = 4;
//                if (image4Flag == 0) {
//
//                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    camera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
//
//                } else {
//
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
//                    alertDialog.setTitle("");
//                    alertDialog.setMessage(R.string.edit_image_confirm);
//                    alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
//                                }
//                            });
//                    alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//                    alertDialog.show();
//                }
//            }
//        });
//
//        image5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imagesFlag = 5;
//                if (image5Flag == 0) {
//
//                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    camera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
//
//                } else {
//
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
//                    alertDialog.setTitle("");
//                    alertDialog.setMessage(R.string.edit_image_confirm);
//                    alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
//                                }
//                            });
//                    alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//                    alertDialog.show();
//                }
//            }
//        });
//
//        image6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imagesFlag = 6;
//                if (image6Flag == 0) {
//
//                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    camera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
//
//                } else {
//
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
//                    alertDialog.setTitle("");
//                    alertDialog.setMessage(R.string.edit_image_confirm);
//                    alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
//                                }
//                            });
//                    alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//                    alertDialog.show();
//                }
//            }
//        });


        wareHouseSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Warehouse warehouse = ((Warehouse) wareHouseSpinner1.getSelectedItem());
                applicationDetails.setWarehouse(warehouse);
                dbObject.updateApplicationData(appId, "warehouseId", warehouse.getWarehouseId());
                dbObject.updateApplicationData(appId, "warehouseName", warehouse.getWarehouseName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        priceListSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                PriceList priceList = ((PriceList) priceListSpinner1.getSelectedItem());
                 applicationDetails.setPriceList(priceList);
                dbObject.updateApplicationData(appId, "priceListId", priceList.getPriceListId());
                dbObject.updateApplicationData(appId, "priceListName", priceList.getPriceListName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        projectTypeSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ProjectType projectType = ((ProjectType) projectTypeSpinner1.getSelectedItem());
                applicationDetails.setProjectType(projectType);
                dbObject.updateApplicationData(appId, "projectTypeId", projectType.getProjectTypeId());
                dbObject.updateApplicationData(appId, "projectTypeName", projectType.getProjectTypeName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

//        removeImageBtn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
//                alertDialog.setTitle("");
//                alertDialog.setMessage(R.string.delete_image_confirm);
//                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                removeImageBtn1.setVisibility(View.GONE);
//                                submitImageBtn1.setVisibility(View.GONE);
//                                submitImageBtn1.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
//                                image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
//                                image1Flag = 0;
//                                dbObject.deleteImage(session.getValue("APP_ID") + "_1" + NEW_SERVICE);
//                                imageText1.setText("");
//                            }
//                        });
//                alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                alertDialog.show();
//
//            }
//        });
//        removeImageBtn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
//                alertDialog.setTitle("");
//                alertDialog.setMessage(R.string.delete_image_confirm);
//                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                removeImageBtn2.setVisibility(View.GONE);
//                                submitImageBtn2.setVisibility(View.GONE);
//                                submitImageBtn2.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
//                                image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
//                                image2Flag = 0;
//                                dbObject.deleteImage(session.getValue("APP_ID") + "_2" + NEW_SERVICE);
//                                imageText2.setText("");
//                            }
//                        });
//                alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                alertDialog.show();
//            }
//        });
//        removeImageBtn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
//                alertDialog.setTitle("");
//                alertDialog.setMessage(R.string.delete_image_confirm);
//                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                removeImageBtn3.setVisibility(View.GONE);
//                                submitImageBtn3.setVisibility(View.GONE);
//                                submitImageBtn3.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
//                                image3.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
//                                image3Flag = 0;
//                                dbObject.deleteImage(session.getValue("APP_ID") + "_3" + NEW_SERVICE);
//                                imageText3.setText("");
//                            }
//                        });
//                alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                alertDialog.show();
//            }
//        });
//        removeImageBtn4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
//                alertDialog.setTitle("");
//                alertDialog.setMessage(R.string.delete_image_confirm);
//                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                removeImageBtn4.setVisibility(View.GONE);
//                                submitImageBtn4.setVisibility(View.GONE);
//                                submitImageBtn4.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
//                                image4.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
//                                image4Flag = 0;
//                                dbObject.deleteImage(session.getValue("APP_ID") + "_4" + NEW_SERVICE);
//                                imageText4.setText("");
//                            }
//                        });
//                alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                alertDialog.show();
//            }
//        });
//        removeImageBtn5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
//                alertDialog.setTitle("");
//                alertDialog.setMessage(R.string.delete_image_confirm);
//                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                removeImageBtn5.setVisibility(View.GONE);
//                                submitImageBtn5.setVisibility(View.GONE);
//                                submitImageBtn5.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
//                                image5.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
//                                image5Flag = 0;
//                                dbObject.deleteImage(session.getValue("APP_ID") + "_5" + NEW_SERVICE);
//                                imageText5.setText("");
//                            }
//                        });
//                alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                alertDialog.show();
//            }
//        });
//        removeImageBtn6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
//                alertDialog.setTitle("");
//                alertDialog.setMessage(R.string.delete_image_confirm);
//                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                removeImageBtn6.setVisibility(View.GONE);
//                                submitImageBtn6.setVisibility(View.GONE);
//                                submitImageBtn6.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
//                                image6.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
//                                image6Flag = 0;
//                                dbObject.deleteImage(session.getValue("APP_ID") + "_6" + NEW_SERVICE);
//                                imageText6.setText("");
//
//                            }
//                        });
//                alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                alertDialog.show();
//            }
//        });
//
//        submitImageBtn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    Image imageFromDatabase = dbObject.getImage(session.getValue("APP_ID") + "_1" + NEW_SERVICE);
//                    if (image1Flag == 1) {
//                        submitImage(imageFromDatabase,1);
//                    } else if (image1Flag == 3) {
//                        GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.upload_image), getString(R.string.upload_image_lbl));
//                    }
//                }catch(Exception e){ GeneralFunctions.messageBox(OpenApplicationDetails.this,"something went wrong" , e.toString());}
//            }
//        });
//        submitImageBtn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    Image imageFromDatabase = dbObject.getImage(session.getValue("APP_ID") + "_2" + NEW_SERVICE);
//                    if (image2Flag == 1) {
//                        submitImage(imageFromDatabase,2);
//                    } else if (image2Flag == 3) {
//                        GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.upload_image), getString(R.string.upload_image_lbl));
//                    }
//                }catch(Exception e){ GeneralFunctions.messageBox(OpenApplicationDetails.this,"something went wrong" , e.toString());}
//
//            }
//        });
//        submitImageBtn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    Image imageFromDatabase = dbObject.getImage(session.getValue("APP_ID") + "_3" + NEW_SERVICE);
//                    if (image3Flag == 1) {
//                        submitImage(imageFromDatabase,3);
//                    } else if (image3Flag == 3) {
//                        GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.upload_image), getString(R.string.upload_image_lbl));
//                    }
//                }catch(Exception e){ GeneralFunctions.messageBox(OpenApplicationDetails.this,"something went wrong" , e.toString());}
//
//            }
//        });
//        submitImageBtn4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    Image imageFromDatabase = dbObject.getImage(session.getValue("APP_ID") + "_4" + NEW_SERVICE);
//                    if (image4Flag == 1) {
//                        submitImage(imageFromDatabase,4);
//                    } else if (image4Flag == 3) {
//                        GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.upload_image), getString(R.string.upload_image_lbl));
//                    }
//                }catch(Exception e){ GeneralFunctions.messageBox(OpenApplicationDetails.this,"something went wrong" , e.toString());}
//            }
//        });
//        submitImageBtn5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    Image imageFromDatabase = dbObject.getImage(session.getValue("APP_ID") + "_5" + NEW_SERVICE);
//                    if (image5Flag == 1) {
//                        submitImage(imageFromDatabase,5);
//                    } else if (image5Flag == 3) {
//                        GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.upload_image), getString(R.string.upload_image_lbl));
//                    }
//                }catch(Exception e){ GeneralFunctions.messageBox(OpenApplicationDetails.this,"something went wrong" , e.toString());}
//            }
//        });
//        submitImageBtn6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    Image imageFromDatabase = dbObject.getImage(session.getValue("APP_ID") + "_6" + NEW_SERVICE);
//                    if (image6Flag == 1) {
//                        submitImage(imageFromDatabase,6);
//                    } else if (image6Flag == 3) {
//                        GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.upload_image), getString(R.string.upload_image_lbl));
//                    }
//                }catch(Exception e){ GeneralFunctions.messageBox(OpenApplicationDetails.this,"something went wrong" , e.toString());}
//            }
//        });






        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);
        image5.setOnClickListener(this);
        image6.setOnClickListener(this);

        removeImageBtn1.setOnClickListener(this);
        removeImageBtn2.setOnClickListener(this);
        removeImageBtn3.setOnClickListener(this);
        removeImageBtn4.setOnClickListener(this);
        removeImageBtn5.setOnClickListener(this);
        removeImageBtn6.setOnClickListener(this);

        image1.setOnLongClickListener(this);
        image2.setOnLongClickListener(this);
        image3.setOnLongClickListener(this);
        image4.setOnLongClickListener(this);
        image5.setOnLongClickListener(this);
        image6.setOnLongClickListener(this);











        //submit data to the server
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                applicationDetails = dbObject.getApplications(session.getValue("APP_ID"), "N", session.getValue("username")).get(0);


                if (!dbObject.tableIsEmpty(Database.ESTIMATED_ITEMS_TABLE)) {
                    submitEstimatedItems = dbObject.getEstimatedItems(null, session.getValue("APP_ID"));
                    Log.d("estimatedItems", ":" + estimatedItems.size());
                } else {
                    // warning(getResources().getString(R.string.provide_data));
                }

                //get the size of the materials list
                int materials_count = submitEstimatedItems.size();


                if (materials_count <= 0) {//check if there is no items
                    GeneralFunctions.messageBox(OpenApplicationDetails.this, "فشل الأعتماد", "لا يوجد أي عناصر .");
                } else if (helper.isInternetConnection()) {//check internet connection

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                    alertDialog.setTitle("");
                    alertDialog.setMessage(R.string.approve_data_confirm);
                    alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //-----------------------------------------------------------------------------------------------

                                    progress.show();



                                    String estimatedItemsArray = "";
                                    for (int i = 0; i < submitEstimatedItems.size(); i++) {
                                        Item item = submitEstimatedItems.get(i);
                                        // do something with object
                                        if (0 < i && i < submitEstimatedItems.size()) {
                                            estimatedItemsArray += ",";
                                        }
                                        estimatedItemsArray += "{\n" +
                                                "\"itemId\": " + item.getId() + ",\n" +//item.getItemCode()
                                                "\"quantity\": " + item.getItemAmount() * item.getTemplateAmount() + ",\n" +//item.getItemAmount()
                                                "\"templateId\":" + item.getTemplateId() + "\n" +
                                              //  "\"warehouseId\": " +85 /*(item.getTemplateId().equals("0") ? item.getWarehouse().getWarehouseId() : applicationDetails.getWarehouse().getWarehouseId()) */+ ",\n" +//item.getWarehouse().getWarehouseId()
                                           //     "\"priceListId\": " + 10033/* (item.getTemplateId().equals("0") ? item.getPricList().getPriceListId() : applicationDetails.getPriceList().getPriceListId())*/ + "\n" + //item.getPricList().getPriceListId()
                                                "}";

                                    }
                                    CharSequence date = DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date());
                                    //edit.................................................................................
                                    // check if the edit text null
                                    if (phase1txt == null || phase1txt.equals("")) {
                                        phase1.setText("0");
                                        phase1txt = "0";
                                    }
                                    if (phase3txt == null || phase3txt.equals("")) {
                                        phase3.setText("0");
                                        phase3txt = "0";
                                    }
                                    String bodyData = "{\n" +
                                            "\"application\": {\n" +
                                            "\"applRowId\": " + applicationDetails.getAppID() + ",\n" +//applicationDetails.getAppID()
                                            "\"prjRowId\": " + applicationDetails.getPrjRowId() + ",\n" +//applicationDetails.getPrjRowId()
                                            "\"customerName\": \"" + applicationDetails.getCustomerName() + "\",\n" +
                                            "\"applId\": " + applicationDetails.getAppID() + ",\n" +//applicationDetails.getAppID()
                                     //       "\"warehouseId\": " + 85 + ",\n" + //applicationDetails.getWarehouse().getWarehouseId()
                                      //      "\"priceListId\": " + 10033 + ",\n" + //applicationDetails.getPriceList().getPriceListId()
                                     //       "\"projectTypeId\": " + 6 + ",\n" + //applicationDetails.getProjectType().getProjectTypeId()
                                            "\"username\": \"" + applicationDetails.getUsername() + "\",\n" +
                                            "\"postingDate\": \"" + date + "\",\n" +
                                            "\"Items\": [" + estimatedItemsArray +
                                            "],\n" +
                                            "\"enclosure\": {\n" +
                                            "\"phase1\": " + applicationDetails.getPhase1Meter() + ",\n" +
                                            "\"phase3\": " + applicationDetails.getPhase3Meter() + ",\n" +
                                            "}\n" +
                                            "}\n" +
                                            "}\n";
                                    Log.d("bodyData", "bodyData : " + bodyData);

//submit images is inside submitMaterialsToServer .
                                    submitMaterialsToServer(bodyData);
                                    /*for (int i = 1; i < 7; i++) {
                                        if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_" + i + NEW_SERVICE)) {
                                            Image imageFromDatabase = dbObject.getImage(session.getValue("APP_ID") + "_" + i + NEW_SERVICE);
                                            try {
                                                submitImage(imageFromDatabase);
                                            } catch (Exception e) {
                                                String error = e.toString();
                                                e.printStackTrace();
                                            }
                                        }
                                    }*/


                                    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                }
                            });
                    alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();


                } else {
                    GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.check_internet_connection), getString(R.string.check_internet_saved_data));
                    dbObject.updateApplicationStatus(applicationDetails.getAppID(), applicationDetails.getTicketStatus(), "0");

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        boolean isCaptureImage = true;



        //insert new image
        String imagePath = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + session.getImageName() + ".jpg";
        String imageName = session.getImageName();
        if (requestCode == REQUEST_CAMERA_CODE) {
            if (resultCode == Activity.RESULT_OK) {

              //  File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
             //   Bitmap bitmap = decodeSampledBitmapFromFile(currentPhotoPath, 1500, 1920);

             //   Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                String base64 = "";
                if(imageName.equals(appId+"_1")){
                    DialogCustomView(true, imagePath, 1);
                }
                else if(imageName.equals(appId+"_2")){
                    DialogCustomView(true, imagePath, 2);
                }
                else if(imageName.equals(appId+"_3")){
                    DialogCustomView(true, imagePath, 3);
                }
                else if(imageName.equals(appId+"_4")){
                    DialogCustomView(true, imagePath, 4);
                }
                else if(imageName.equals(appId+"_5")){
                    DialogCustomView(true, imagePath, 5);
                }
                else if(imageName.equals(appId+"_6")){
                    DialogCustomView(true, imagePath, 6);
                }
                else GeneralFunctions.messageBox(this,"ERROR","SOME THING WRONG HAPPEND");


                if (resultCode == Activity.RESULT_CANCELED) {
GeneralFunctions.messageBox(this,"something wrong happiend","");
                }
            }






//            image1.setImageBitmap(bitmap);
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//            byte[] imageBytes = byteArrayOutputStream.toByteArray();
//            String base64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//            base64 = imageString;
        }

        try
        {
            //GeneralFunctions.populateToastMsg(getApplicationContext(),imagePath,true);
          //  if(isCaptureImage)
          //  previewCapturedImage();

        } catch (Exception ex) {
            Log.d("upload image", ex.getMessage() + "");
            ex.printStackTrace();
        }



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
            sbranch.setText(app.getsBranch());
            appType.setText(app.getAppType());
            noOfServices.setText(app.getNoofservices());
            noOfPhase.setText(app.getNo_of_phase() + " فاز ");
            propertyType.setText(app.getProperty_type());
            near_by_serviceTV.setText(app.getNear_by_service());
            serviceNo.setText(app.getService_no());
            phase1Quntitiy.setText(app.getPhase1Meter());
            phase3Quntitiy.setText(app.getPhase3Meter());


            Log.d("project",":"+applicationDetails.getProjectType().getProjectTypeName());
            Log.d("project",":"+applicationDetails.getPriceList().getPriceListName());

            priceListSpinner1.setSelection(findIndexPriceList(priceListArrayList, app.getPriceList().getPriceListName()));
            wareHouseSpinner1.setSelection(findIndexWarehouse(warehouseArrayList, app.getWarehouse().getWarehouseName()));
            projectTypeSpinner1.setSelection(findIndexProjectType(projectTypeArrayList, app.getProjectType().getProjectTypeName()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private int findIndexPriceList(ArrayList<PriceList> array, String x) {
        int pos = 0;
        for (int ii = 0; ii < array.size(); ii++) {
            if (array.get(ii).getPriceListName().equals(x)) {
                pos = ii;
                break;
            }
        }
        return pos;

    }





    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    {
        // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }




    private int findIndexWarehouse(ArrayList<Warehouse> array, String x) {
        int pos = 0;
        for (int ii = 0; ii < array.size(); ii++) {
            if (array.get(ii).getWarehouseName().equals(x)) {
                pos = ii;
                break;
            }
        }
        return pos;

    }

    private int findIndexProjectType(ArrayList<ProjectType> array, String x) {
        int pos = 0;
        for (int ii = 0; ii < array.size(); ii++) {
            if (array.get(ii).getProjectTypeName().equals(x)) {
                pos = ii;
                break;
            }
        }
        return pos;

    }

    //alert dialog
    private void showMenuAdItem() {
        AlertDialog alert = null;
        final CharSequence[] items = {getResources().getString(R.string.add_item), getResources().getString(R.string.add_template_lbl), getResources().getString(R.string.enclouser_lbl), getString(R.string.add_image), getString(R.string.add_note), getResources().getString(R.string.exit)};

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
                } else if (item == 3) {
                    try {
                        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //   showImageLookUps();


                } else if (item == 4) {
                    addNote();
                } else dialog.dismiss();
            }
        });
        alert = builder.create();
        alert.show();
    }

//delete this
//    void showImageLookUps(String imageId, String base64, TextView imageLookupText) {
//        AlertDialog alert = null;
//        promptsView = getLayoutInflater().inflate(R.layout.image_lookups, null);
//
//        imageLookUpsSP = (Spinner) promptsView.findViewById(R.id.imageLookUpsSP);
//        if (dbObject.tableIsEmpty(Database.ATTACHMENT_TYPE_TABLE)) {
//            warning(getResources().getString(R.string.no_data_found));
//        } else {
//            imageLookupsArrayList = dbObject.getAttchmentType();
//            // appendNoteLookUpsListToSpinner(noteLookUpSP, noteLookUpsArrayList, null);
//            appendImagesLookupsListToSpinner(imageLookUpsSP, imageLookupsArrayList, null);
//        }
//        //create new dialog
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getResources().getString(R.string.choose_item_lbl));
//        builder.setCancelable(false)
//                .setPositiveButton(getResources().getString(R.string.submit_form_lbl), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//                        AttchmentType imageLookUp = ((AttchmentType) imageLookUpsSP.getSelectedItem());
//                        Image image = new Image();
//
//                        //need work
//                        image.setAppRowId(appId);
//                        image.setAttachmentType(imageLookUp);
//                        image.setFile(base64);
//                        image.setFileName(appId + "_" + imageId + NEW_SERVICE);
//                        image.setUsername(session.getValue("username"));
//
//                       // dbObject.addImage(image);
//
//                        imageLookupText.setText(imageLookUp.toString());
//
//
//                        dialog.dismiss();
//                    }
//                });
//                /*.setNegativeButton(getResources().getString(R.string.cancel_lbl),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.dismiss();
//                            }
//                        });*/
//        //set view to alert dialog
//        builder.setView(promptsView);
//        alert = builder.create();
//        alert.show();
//    }


    void addNote() {

        AlertDialog alert = null;
        promptsView = getLayoutInflater().inflate(R.layout.add_note, null);
//get the value of edit text
        noteET = (EditText) promptsView.findViewById(R.id.note);
         micBtn = (ImageView)promptsView.findViewById(R.id.micBtn);
        noteLookUpSP = (Spinner) promptsView.findViewById(R.id.notesSpinner);
        if (dbObject.tableIsEmpty(Database.NOTE_LOOK_UP)) {
            warning(getResources().getString(R.string.no_data_found));
        } else {
            noteLookUpsArrayList = dbObject.getNoteLookUps();
            appendNoteLookUpsListToSpinner(noteLookUpSP, noteLookUpsArrayList, null);
        }
        //create new dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.choose_item_lbl));
        builder.setCancelable(false)
                .setPositiveButton("أعتماد الملاحظة", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String noteLookUp = ((NoteLookUp) noteLookUpSP.getSelectedItem()).getCode();
                        note = noteET.getText().toString().replace("\\", "/");





                        //  dbObject.submitNote(appId, note,noteLookUp);
                       // updateNotes();
//                    {
//                        "application": {
//                        "applRowId":"220060" ,
//                                "notes":"Test from server......",
//                                "noteLookupID":498 ,
//                                "username":"JZAYDAN"
//                    }
//                    }



                        String data = "{" +
                                "\"application\":{" +
                                "\"applRowId\":\"" + appId + "\" ," +
                                "\"notes\":\"" + note + "\"," +
                                "\"noteLookupID\":" + Integer.parseInt(noteLookUp) + " ," +
                                "\"username\":\"" + session.getValue("username") + "\"" +
                                "}" +
                                "}";
                        Log.d("sumbitNote", "data : " + data);
                        submitNote(data);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        //set view to alert dialog
        builder.setView(promptsView);
        alert = builder.create();
        alert.show();









        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
       /* intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);*/
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "ar-SA");
        speechRecognizerIntent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{"ar-SA"});
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ar-SA");
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "ar-SA");
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");





        micBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    micBtn.setImageResource(R.drawable.ic_baseline_mic_none_24);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });


        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                noteET.setText("");
                noteET.setHint("تكلم...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                micBtn.setImageResource(R.drawable.ic_baseline_mic_24);
                noteET.setHint("");
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                noteET.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });











    }


    private void submitNote(String bodyData) {
        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;
        progress.show();

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();//display the response submit success
                Log.d("sumbitNote", "Response: " + response);
                try {

                    JSONObject submitData = new JSONObject(response);
                    Log.d("submitMaterialsToServer", "Response: " + (submitData.getString("request_response").equals("Success")));
                    if (submitData.getString("request_response").equals("Success.")) {
                        GeneralFunctions.messageBox(OpenApplicationDetails.this, getString(R.string.success_lbl), getResources().getString(R.string.submit_success));
                        progress.dismiss();
                        // Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();//display the response submit success
                    } else {
                        GeneralFunctions.messageBox(OpenApplicationDetails.this, getString(R.string.failed), getResources().getString(R.string.submit_failed));
                        progress.dismiss();
                        //Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_failed), Toast.LENGTH_LONG).show();//display the response submit failed
                    }
                } catch (JSONException e) {
                    progress.dismiss();
                    GeneralFunctions.messageBox(OpenApplicationDetails.this, getString(R.string.failed), e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getItemsFromServer", "Error Login Request :" + error.toString());
                progress.dismiss();
                GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.submit_failed), error.toString());
                // Toast.makeText(getApplicationContext(), "Submit note failed !", Toast.LENGTH_LONG).show();
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                //parameters
                // params.put("username", "jd");
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_SUBMIT_NOTE);
                params.put("data", bodyData);
                params.put("appId", appId);

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }


    private void submitImages() {
        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;
        try {
progress.show();
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);
ArrayList<Image> imagesArr = dbObject.getImages(appId);
Helper helper = new Helper(this);
for(int i=0;i<imagesArr.size();i++){
Image image = imagesArr.get(i);

    BitmapFactory.Options options = new BitmapFactory.Options();
String imagePath = image.getFile();//imagePath
    options.inSampleSize = 8;
    File pic = new File(imagePath);
    Log.d("submitImages","imagePath : "+imagePath);
    if (pic.exists()) {
        final Bitmap bitmap = BitmapFactory.decodeFile(imagePath,
                options);
         base64Image = helper.toBase64(bitmap);

    }else { throw new Exception("صورة غير موجودة .");}

submitImage(imagePath,image.getFileName(), image.getAttachmentType().getCode());

}//end of for loop
            Intent i = new Intent(OpenApplicationDetails.this, SuccessScreen.class);
            startActivity(i);
        } catch (Exception e) {
            progress.dismiss();
            GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.submit_failed)+" صور ", e.toString());
            e.printStackTrace();
        }
    }//end of fun


    private void submitImage(String imagePath,String imageName ,String imageLookupsCode) {
        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;
        try {

            progress.show();
            //RequestQueue initialized
            mRequestQueue = Volley.newRequestQueue(this);

            Helper helper = new Helper(this);


                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inSampleSize = 8;
                File pic = new File(imagePath);
                Log.d("submitImages","imagePath : "+imagePath);
            Log.d("submitImages","imageName : "+ pic.getName());

                if (pic.exists()) {
                    final Bitmap bitmap = BitmapFactory.decodeFile(imagePath,
                            options);
                    base64Image = helper.toBase64(bitmap);

                }else { throw new Exception("صورة غير موجودة .");}

                //String Request initialized
                mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();//display the response submit success
                        Log.d("sumbitImage", "Response: " + response);
                        try {

                            JSONObject submitData = new JSONObject(response);
                            submitImageBtn1.setBackground(ContextCompat.getDrawable(context, R.drawable.upload_background));

                            //if the user try to upload again the app will show "u already uploaded"
//                if(imageFlag==1)image1Flag = 3;
//                else if(imageFlag==2)image2Flag = 3;
//                else if(imageFlag==3)image3Flag = 3;
//                else if(imageFlag==4)image4Flag = 3;
//                else if(imageFlag==5)image5Flag = 3;
//                else if(imageFlag==6)image6Flag = 3;


                            if (submitData.getString("message").equals("Created " + imageName)) {
                                //display the response submit success
                                //  Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG);
                                //   toast.setGravity(Gravity.CENTER, 0, 0);
                                //   toast.show();
                             //   image.setIsSync(1);
                               // dbObject.updateImageTable(image);
                                progress.dismiss();
                                Toast.makeText(OpenApplicationDetails.this, "تم أعتماد الصورة بنجاح", Toast.LENGTH_LONG).show();


                            } else {
                                progress.dismiss();
                                GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.submit_failed), submitData.getString("message"));
                                // Toast.makeText(getApplicationContext(), submitData.getString("message"), Toast.LENGTH_LONG).show();//display the response submit failed
                            }
                        }catch (JSONException ex ){
                            progress.dismiss();
                            GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.submit_failed), ex.toString());
                            ex.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Log.d("getItemsFromServer", "Error Login Request :" + error.toString());
                        GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.submit_failed), error.toString());
                        // Toast.makeText(getApplicationContext(), getString(R.string.submit_image_failed), Toast.LENGTH_LONG).show();
                    }

                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        //parameters
                        params.put("apiKey", CONSTANTS.API_KEY);
                        params.put("action", CONSTANTS.ACTION_SUBMIT_Image);
                        params.put("file", base64Image); // base64
                        params.put("appRowId", appId);
                        params.put("filename", imageName);//filename
                        params.put("content_type", "image/jpeg");
                        params.put("appId", appId);

                        /////////////// parse to int
                        params.put("attachmentType", imageLookupsCode); // attachement type code
                        params.put("username", session.getValue("username"));

                        return params;
                    }

                };

                mRequestQueue.add(mStringRequest);

        } catch (Exception e) {
            progress.dismiss();
            GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.submit_failed)+" صور ", e.toString());
            e.printStackTrace();
        }

    }//end of fun

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
                .setPositiveButton(getResources().getString(R.string.submit_form_lbl), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        phase1txt = phase1.getText().toString();
                        phase3txt = phase3.getText().toString();
                        if (phase1txt.equals("") || phase1txt.equals(null) || phase1txt.equals("0")) {
                            phase1txt = "0";
                            phase1Quntitiy.setText("0");
                        } else{ phase1Quntitiy.setText(phase1txt);}
                        if (phase3txt.equals("") || phase3txt.equals(null) || phase3txt.equals("0")) {
                            phase3txt = "0";
                            phase3Quntitiy.setText("0");
                        } else {phase3Quntitiy.setText(phase3txt);}
//                        phase1Quntitiy.setText(phase1txt);
//                        phase3Quntitiy.setText(phase3txt);
                        dbObject.submitEnclousers(session.getValue("APP_ID"), phase1txt, phase3txt);
                        // get user input and set it to result
                        // edit text
                        // Toast.makeText(getApplicationContext(), "1Phase: " + phase1.getText().toString() + " ||  3Phase: " + phase3.getText().toString(), Toast.LENGTH_LONG).show();
                        // showMenuAdItem();
                        dialog.dismiss();
//                        enclouserBlock.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel_lbl),
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

        Intent i = new Intent(getApplicationContext(), TemplatesGallry.class);
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


        builderDialog.setPositiveButton(R.string.add_item, new DialogInterface.OnClickListener() {
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
        builderDialog.setNegativeButton(getResources().getString(R.string.cancel_lbl), new DialogInterface.OnClickListener() {
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
        builder1.setCancelable(false);

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

    void displayNotes(){

        if (helper.isInternetConnection()) {
            progress.show();
            try{
                getNotes();
            }catch(Exception ex){
                progress.dismiss();
                GeneralFunctions.messageBox(getApplicationContext(), "لا يمكن أستعراض الملاحظات", ex.toString());
            }

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

             //   Log.d("getApplicationServices", "Response: " + response);

                //create json object
                try {
                    JSONObject applicationResultObject = new JSONObject(response);
                    if(applicationResultObject.getString("count").equals("0")){

                        GeneralFunctions.messageBox(OpenApplicationDetails.this,"لا يوجد ملاحظات","لا يوجد ملاحظات للطلب .");
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
                    GeneralFunctions.messageBox(OpenApplicationDetails.this,"فشل طلب الملاحظات",ex.toString());
                    progress.dismiss();
                  //  ex.printStackTrace();
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

        // Set item Block to visible
        if (!list.isEmpty()) {
            itemsBlock.setVisibility(View.VISIBLE);
        }

    }


    // Recycle view append list
    private void AddItemToList(Item item) {

        //add item to the list
        // estimatedItems.add(item);......................................................


        //add item to estimated database
        if (!dbObject.isEstimatedItemExist(Database.ESTIMATED_ITEMS_TABLE, "itemName", item.getItemName(), session.getValue("APP_ID"), "0")) {
            item.setItemAmount(1);
            item.setTemplateAmount(1);
            //insert item in estimation items table
            dbObject.insertEstimatedItem(item, false, session.getValue("APP_ID"));
            estimatedItemsListAdapter.setItem(item);
            itemsBlock.setVisibility(View.VISIBLE);
            Log.d("AddItemToList", ": yes");
        } else {
            GeneralFunctions.messageBox(OpenApplicationDetails.this, getString(R.string.warning), getResources().getString(R.string.material_already_exist));
            // Toast.makeText(getApplicationContext(), getResources().getString(R.string.material_already_exist), Toast.LENGTH_LONG).show();
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
        estimatedTemplatesListAdapter = new EstimatedTemplatesListAdapter(this, list, "N");
        //its data has changed so that it updates the UI
        templatesList.setAdapter(estimatedTemplatesListAdapter);
        estimatedTemplatesListAdapter.notifyDataSetChanged();

        templatesBlock.setVisibility(View.VISIBLE);
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
                        String doneDate = DateFormat.format("hh:mm:ss    yyyy-MM-dd  ", new Date()).toString();

//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();//display the response submit success
                        applicationDetails.setTicketStatus("D");
                        applicationDetails.setSync("1");
                        dbObject.updateApplicationData(applicationDetails.getAppID(), "doneDate", doneDate);
                        dbObject.updateApplicationStatus(applicationDetails.getAppID(), applicationDetails.getTicketStatus(), "1");
                        submitImages();// submit images to server after submiting the tamplates and items to server

//                        Intent i = new Intent(OpenApplicationDetails.this, MainActivity.class);
                        progress.dismiss();

                    } else {
                        progress.dismiss();
                        GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.failed), getResources().getString(R.string.submit_failed));
                        //Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_failed), Toast.LENGTH_LONG).show();//display the response submit failed

                    }
                } catch (JSONException e) {
                    progress.dismiss();
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
                progress.dismiss();
                GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.submit_failed), error.toString());
                dbObject.updateApplicationStatus(applicationDetails.getAppID(), applicationDetails.getTicketStatus(), "0");
                // Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();//display the response submit failed
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
                params.put("data", bodyData);
                params.put("appId", appId);

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


    private void getApplicationServices() {
        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context);


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<ServiceInfo> services = new ArrayList<ServiceInfo>();

                Log.d("getApplicationServices", "Response: " + response);

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
                        ServiceInfo serviceInfo = new ServiceInfo();

                        serviceInfo.setAppId(applicationObject.getString("appl_row_id"));
                        serviceInfo.setComp_id(String.valueOf(applicationObject.getInt("comp_id")));
                        serviceInfo.setMeter_type(applicationObject.getString("meter_type"));
                        serviceInfo.setPhase(applicationObject.getString("phase"));
                        serviceInfo.setPhase_type(applicationObject.getString("phase_type"));
                        serviceInfo.setService_class(applicationObject.getString("service_class"));
                        serviceInfo.setService_class_id(applicationObject.getString("service_class_id"));
                        serviceInfo.setUsage_type(applicationObject.getString("usage_type"));

                        services.add(serviceInfo);
                    }


                    DialogFragment fragment = MyDialogFragment.newInstance(services);
                    int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                    int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

                    fragment.show(getSupportFragmentManager(), "some tag");


                } catch (Exception ex) {
                    Log.d("error", ":" + ex);
                    GeneralFunctions.messageBox(OpenApplicationDetails.this,"فشل طلب الخدمات",ex.toString());
                    progress.dismiss();
                    ex.printStackTrace();
                }
                progress.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GeneralFunctions.messageBox(context, "فشل طلب الخدمات", error.toString());
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

                params.put("appId", session.getValue("APP_ID"));
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_Application_Agreements);

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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image1:
                selectImage(1);
                break;
            case R.id.image2:
                selectImage(2);
                break;
            case R.id.image3:
                selectImage(3);
                break;
            case R.id.image4:
                selectImage(4);
                break;
            case R.id.image5:
                selectImage(5);
                break;
            case R.id.image6:
                selectImage(6);
                break;
            case R.id.removeImageBtn1:
                deleteImage(1);
                break;
            case R.id.removeImageBtn2:
                deleteImage(2);
                break;
            case R.id.removeImageBtn3:
                deleteImage(3);
                break;
            case R.id.removeImageBtn4:
                deleteImage(4);
                break;
            case R.id.removeImageBtn5:
                deleteImage(5);
                break;
            case R.id.removeImageBtn6:
                deleteImage(6);
                break;

        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.image1:
                DialogCustomView(false, null, 1);
                break;
            case R.id.image2:
                DialogCustomView(false, null, 2);
                break;
            case R.id.image3:
                DialogCustomView(false, null, 3);
                break;
            case R.id.image4:
                DialogCustomView(false, null, 4);
                break;
            case R.id.image5:
                DialogCustomView(false, null, 5);
                break;
            case R.id.image6:
                DialogCustomView(false, null, 6);
                break;
        }
        return false;
    }



    //
    protected void DialogCustomView(boolean imageByPath, String path, final int index) {
        String title = "معاينة الصورة";
        String negativeButton = "متابعة";
        String positiveButton = "ألتقاط صورة مرة أخرى";

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.preview_image_dialog, null);
        ImageView imageView = (ImageView) customView.findViewById(R.id.imageView);
        imageLookUpsSP = (Spinner) customView.findViewById(R.id.imageLookUpsSP);

        imageView.setImageBitmap(getImage(imageByPath, path, appId, index));

        if (dbObject.tableIsEmpty(Database.ATTACHMENT_TYPE_TABLE)) {
            warning(getResources().getString(R.string.no_data_found));
        } else {
            imageLookupsArrayList = dbObject.getAttchmentType();
            // appendNoteLookUpsListToSpinner(noteLookUpSP, noteLookUpsArrayList, null);
            appendImagesLookupsListToSpinner(imageLookUpsSP, imageLookupsArrayList, null);
        }



        imageView.setOnTouchListener(new ImageMatrixTouchHandler(customView.getContext()));

        Dialog myDialog = DialogUtils.createCustomDialog(this, title, customView,
                negativeButton, positiveButton, true, new DialogUtils.DialogListener() {
                    @Override
                    public void onPositiveButton() {
                        selectImage(index);
                    }

                    @Override
                    public void onNegativeButton() { //عند ضغط على متابعة
                        AttchmentType imageLookUp = ((AttchmentType) imageLookUpsSP.getSelectedItem());
                        Image image = new Image();

                        //need work
                        image.setAppRowId(appId);
                        image.setAttachmentType(imageLookUp);
                        image.setImagePath(CONSTANTS.getImagePath("EstimationImages")+ appId + "_" + index + ".jpg");//insert into file column in database
                        image.setFileName(appId + "_" + index);//fileName column in database
                        image.setUsername(session.getValue("username"));
                        if (!dbObject.isItemExist(Database.IMAGES_TABLE, "filename", appId + "_" + index)) {
                            dbObject.addImage(image);
                        }else{ dbObject.updateImage(image);}
                        previewCapturedImage();
                        //  imageLookupText.setText(imageLookUp.toString());
                    }
                });

        if (myDialog != null && !myDialog.isShowing()) {
            myDialog.show();
        }

        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(myDialog.getWindow().getAttributes());

        // Set the alert dialog window width and height
        // Set alert dialog width equal to screen width 90%
        // int dialogWindowWidth = (int) (displayWidth * 0.9f);
        // Set alert dialog height equal to screen height 90%
        // int dialogWindowHeight = (int) (displayHeight * 0.9f);

        // Set alert dialog width equal to screen width 70%
        int dialogWindowWidth = (int) (displayWidth * 0.9f);
        // Set alert dialog height equal to screen height 70%
        int dialogWindowHeight = (int) (displayHeight * 0.95f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        myDialog.getWindow().setAttributes(layoutParams);
    }

    private void deleteImage(int index) {
        try {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
            alertDialog.setTitle("");
            alertDialog.setMessage(R.string.delete_image_confirm);
            alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //get the path
                            String imagePath = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appId + "_" + index + ".jpg";
                            Log.d("Delete Path", imagePath);
                            boolean isFileDeleted = deleteFile(imagePath);
                            String imageName = appId + "_" + index;
                            boolean isDBDelete = dbObject.deleteImage(imageName);

                            if (isFileDeleted && isDBDelete) {
                                switch (index){
                                    case 1 :
                                        removeImageBtn1.setVisibility(View.GONE);
                                        image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                        image1Flag = 0;
                                        imageText1.setText("");
                                        break;
                                    case 2 :
                                        removeImageBtn2.setVisibility(View.GONE);
                                        image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                        imageText1.setText("");
                                        break;
                                    case 3 :
                                        removeImageBtn3.setVisibility(View.GONE);
                                        image3.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                        imageText1.setText("");
                                        break;
                                    case 4 :
                                        removeImageBtn4.setVisibility(View.GONE);
                                        image4.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                        imageText1.setText("");
                                        break;
                                    case 5 :
                                        removeImageBtn5.setVisibility(View.GONE);
                                        image5.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                        imageText1.setText("");
                                        break;
                                    case 6 :
                                        removeImageBtn6.setVisibility(View.GONE);
                                        image6.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                        imageText1.setText("");
                                        break;
                                }

                            } else {
                                GeneralFunctions.messageBox(OpenApplicationDetails.this,"فشل مسح صورة","حاول مسح صورة مرة أخرى");
                            }






                        }
                    });
            alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();



        } catch (Exception ex) {
            ex.printStackTrace();
            GeneralFunctions.messageBox(this,"فشل مسح صورة","حاول مسح صورة مرة أخرى");
        }
    }




    private Bitmap getImage(boolean imageByPath, String path, String appId, int index) {

        Bitmap bitmap = null;
        try {
            if (!imageByPath) {
                path = CONSTANTS.getImagePath("EstimationImages")+ appId + "_" + index + ".jpg";

            }

            ImageView imageView = new ImageView(this);
            Log.d("getImage Path", path);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger
            // images

            options.inSampleSize = 4;
            File pic = new File(path);
            if (pic.exists()) {
                bitmap = BitmapFactory.decodeFile(path, options);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add_image);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bitmap;
    }


    private void selectImage(final int index) {
        final String name = appId + "_" + index;
        session.setImageName(name);
        imagesFlag=index;//which image was choosen
        captureImage(name);
    }

    /*
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private Uri captureImage(String name) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri(MEDIA_TYPE_IMAGE, name));
        } else {
            File file = new File(getOutputMediaFileUri(MEDIA_TYPE_IMAGE, name).getPath());
            Uri fileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }


        /**
         Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, name);
         Log.d("Directory ", fileUri.getPath());
         intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

         // start the image capture Intent
         startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
         **/
        return fileUri;
    }

    public Uri getOutputMediaFileUri(int type, String name) {
        return Uri.fromFile(getOutputMediaFile(type, name));
    }

    private File getOutputMediaFile(int type, String name) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/EstimationApp/" + IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + name + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public  boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            return  true;
        }

        return false;
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









//no need
    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + "wisamImage"/*timeStamp*/ + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }




    public void goBack() {
        Intent back = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(back);
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//
//        alertDialog.setTitle("");
//        alertDialog.setMessage(getResources().getString(R.string.close_form_confirmation_msg));
//
//        alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //close the activity
//
//                    }
//                });
//
//        alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//        alertDialog.show();
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

    // append Note Look Ups List To Spinner
    private void appendNoteLookUpsListToSpinner(Spinner spinner, ArrayList<NoteLookUp> list, String selectedValue) {

        try {
            //append items to activity
            ArrayAdapter<NoteLookUp> adapter =//
                    new ArrayAdapter<NoteLookUp>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
            //add adapter to spinner
            spinner.setAdapter(adapter);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, ex.getMessage());
        }

    }

    private void appendImagesLookupsListToSpinner(Spinner spinner, ArrayList<AttchmentType> list, String selectedValue) {

        try {
            //append items to activity
            ArrayAdapter<AttchmentType> adapter =//
                    new ArrayAdapter<AttchmentType>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
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







    /*
     * Display image from a path to ImageView
     */

//    private void previewCapturedImage() {
//        try {
//
//
//
//            String path1 = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appId + "_1.jpg";
//            String path2 = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appId + "_2.jpg";
//            String path3 = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appId + "_3.jpg";
//            String path4 = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appId + "_4.jpg";
//            String path5 = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appId + "_5.jpg";
//            String path6 = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appId + "_6.jpg";
//
//            Log.d("Image Path 1 ", path1);
//            // bimatp factory
//            BitmapFactory.Options options = new BitmapFactory.Options();
//
//            // downsizing image as it throws OutOfMemory Exception for larger
//            // images
//
//            options.inSampleSize = 8;
//            File pic = new File(path1);
//            if (pic.exists()) {
//                final Bitmap bitmap1 = BitmapFactory.decodeFile(path1,
//                        options);
//                image1.setImageBitmap(bitmap1);
//                removeImageBtn1.setVisibility(View.VISIBLE);
//            }
//
//            pic = new File(path2);
//            if (pic.exists()) {
//                final Bitmap bitmap2 = BitmapFactory.decodeFile(path2,
//                        options);
//                image2.setImageBitmap(bitmap2);
//                removeImageBtn2.setVisibility(View.VISIBLE);
//            }
//
//            pic = new File(path3);
//            if (pic.exists()) {
//                final Bitmap bitmap3 = BitmapFactory.decodeFile(path3,
//                        options);
//                image3.setImageBitmap(bitmap3);
//                removeImageBtn3.setVisibility(View.VISIBLE);
//            }
//
//            pic = new File(path4);
//            if (pic.exists()) {
//                final Bitmap bitmap4 = BitmapFactory.decodeFile(path4,
//                        options);
//                image4.setImageBitmap(bitmap4);
//                removeImageBtn4.setVisibility(View.VISIBLE);
//            }
//
//            pic = new File(path5);
//            if (pic.exists()) {
//                final Bitmap bitmap5 = BitmapFactory.decodeFile(path5,
//                        options);
//                image5.setImageBitmap(bitmap5);
//                removeImageBtn5.setVisibility(View.VISIBLE);
//            }
//
//            pic = new File(path6);
//            if (pic.exists()) {
//                final Bitmap bitmap6 = BitmapFactory.decodeFile(path6,
//                        options);
//                image6.setImageBitmap(bitmap6);
//                removeImageBtn6.setVisibility(View.VISIBLE);
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }







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
                    removeImageBtn1.setVisibility(View.VISIBLE);}
                  else  if(imageName.equals(appId + "_2")){
                        image2.setImageBitmap(bitmap1);
                        removeImageBtn2.setVisibility(View.VISIBLE);}
                  else  if(imageName.equals(appId + "_3")){
                        image3.setImageBitmap(bitmap1);
                        removeImageBtn3.setVisibility(View.VISIBLE);}
                  else  if(imageName.equals(appId + "_4")){
                        image4.setImageBitmap(bitmap1);
                        removeImageBtn4.setVisibility(View.VISIBLE);}
                  else  if(imageName.equals(appId + "_5")){
                        image5.setImageBitmap(bitmap1);
                        removeImageBtn5.setVisibility(View.VISIBLE);}
                  else  if(imageName.equals(appId + "_6")){
                        image6.setImageBitmap(bitmap1);
                        removeImageBtn6.setVisibility(View.VISIBLE);}
                }

            }//END OF FOR LOOP

        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

//    void updateNotes(){
//        ArrayAdapter<String> notesAdapter =
//                new ArrayAdapter<String>(OpenApplicationDetails.this, android.R.layout.simple_list_item_1, dbObject.getNotes(appId));
//if(dbObject.getNotes(appId).size()>0)noteBlock.setVisibility(View.VISIBLE);
//        notesListLV.setAdapter(notesAdapter);
//    }


    @Override
    protected void onRestart() {
        super.onRestart();


        //estimatedTemplatesListAdapter.notifyItemChanged(estimatedTemplatesListAdapter.getPos());

    }
}