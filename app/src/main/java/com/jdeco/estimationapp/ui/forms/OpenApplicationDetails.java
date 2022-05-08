package com.jdeco.estimationapp.ui.forms;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.adapters.EstimatedItemsListAdapter;
import com.jdeco.estimationapp.adapters.EstimatedTemplatesListAdapter;
import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.AttchmentType;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.objects.EstimationItem;
import com.jdeco.estimationapp.objects.Image;
import com.jdeco.estimationapp.objects.Item;
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
import com.jdeco.estimationapp.ui.LoginUI;
import com.jdeco.estimationapp.ui.MainActivity;
import com.jdeco.estimationapp.ui.SuccessScreen;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OpenApplicationDetails extends AppCompatActivity {
    TextView appID, appDate, customerName, customerAddress, branch, sbranch, appType, phoneTB, address,
            phase1Quntitiy, phase3Quntitiy, noteTV, noOfServices, noOfPhase, propertyType;
    Spinner masterItemsDropList, subItemsDropList, itemsDropList, itemsDropList2, priceListSpinner1, wareHouseSpinner1, projectTypeSpinner1, noteLookUpSP, imageLookUpsSP, priceListSpinner2, wareHouseSpinner2;
    Spinner itemsDropListDialog;
    Button addItemToListBtn, addTemplateBtn;
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
    Database dbObject;
    Session session;
    Helper helper;
    EditText phase1, phase3, noteET;
    ImageButton showServicesBtn;

    RequestQueue mRequestQueue;


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
    ProgressDialog progress;
    String phase1txt = "0";
    String phase3txt = "0";


    String note = "";
    String appId = "";

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


    private static final int REQUEST_CAMERA_CODE = 12;


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
//        phase1 = (EditText) findViewById(R.id.Phase_1);
//        phase3 = (EditText) findViewById(R.id.Phase_3);

        phase1Quntitiy = (TextView) findViewById(R.id.phase1Quntitiy);
        phase3Quntitiy = (TextView) findViewById(R.id.phase3Quntitiy);

        showServicesBtn = (ImageButton) findViewById(R.id.showServicesBtn);

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
        context = getApplicationContext();
        helper = new Helper(this);
        applicationDetails = new ApplicationDetails();

        estimatedItemsListAdapter = new EstimatedItemsListAdapter(this, estimatedItems);
        estimatedTemplatesListAdapter = new EstimatedTemplatesListAdapter(this);


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

                helper.setImageFromDatabase(session.getValue("APP_ID") + "_1" + NEW_SERVICE, image1, imageText1, removeImageBtn1);
                submitImageBtn1.setVisibility(View.VISIBLE);
                image1Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_2" + NEW_SERVICE)) {

                helper.setImageFromDatabase(session.getValue("APP_ID") + "_2" + NEW_SERVICE, image2, imageText2, removeImageBtn2);
                submitImageBtn2.setVisibility(View.VISIBLE);
                image2Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_3" + NEW_SERVICE)) {

                helper.setImageFromDatabase(session.getValue("APP_ID") + "_3" + NEW_SERVICE, image3, imageText3, removeImageBtn3);
                submitImageBtn3.setVisibility(View.VISIBLE);
                image3Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_4" + NEW_SERVICE)) {

                helper.setImageFromDatabase(session.getValue("APP_ID") + "_4" + NEW_SERVICE, image4, imageText4, removeImageBtn4);
                submitImageBtn4.setVisibility(View.VISIBLE);
                image4Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_5" + NEW_SERVICE)) {

                helper.setImageFromDatabase(session.getValue("APP_ID") + "_5" + NEW_SERVICE, image5, imageText5, removeImageBtn5);
                submitImageBtn5.setVisibility(View.VISIBLE);
                image5Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_6" + NEW_SERVICE)) {

                helper.setImageFromDatabase(session.getValue("APP_ID") + "_6" + NEW_SERVICE, image6, imageText6, removeImageBtn6);
                submitImageBtn6.setVisibility(View.VISIBLE);
                image6Flag = 1;
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

        //initilize buttons
        submitBtn = (Button) findViewById(R.id.submitBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);


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


        // Add Image
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesFlag = 1;
                if (image1Flag == 0) {

                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA_CODE);

                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                    alertDialog.setTitle("");
                    alertDialog.setMessage(R.string.edit_image_confirm);
                    alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
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

            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesFlag = 2;
                if (image2Flag == 0) {

                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA_CODE);

                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                    alertDialog.setTitle("");
                    alertDialog.setMessage(R.string.edit_image_confirm);
                    alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
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
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesFlag = 3;
                if (image3Flag == 0) {

                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA_CODE);

                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                    alertDialog.setTitle("");
                    alertDialog.setMessage(R.string.edit_image_confirm);
                    alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
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
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesFlag = 4;
                if (image4Flag == 0) {

                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA_CODE);

                } else {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                    alertDialog.setTitle("");
                    alertDialog.setMessage(R.string.edit_image_confirm);
                    alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
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
            }
        });

        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesFlag = 5;
                if (image5Flag == 0) {

                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA_CODE);

                } else {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                    alertDialog.setTitle("");
                    alertDialog.setMessage(R.string.edit_image_confirm);
                    alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
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
            }
        });

        image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesFlag = 6;
                if (image6Flag == 0) {

                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA_CODE);

                } else {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                    alertDialog.setTitle("");
                    alertDialog.setMessage(R.string.edit_image_confirm);
                    alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
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
            }
        });


        wareHouseSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Warehouse warehouse = ((Warehouse) wareHouseSpinner1.getSelectedItem());
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
                dbObject.updateApplicationData(appId, "projectTypeId", projectType.getProjectTypeId());
                dbObject.updateApplicationData(appId, "projectTypeName", projectType.getProjectTypeName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        removeImageBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn1.setVisibility(View.GONE);
                                submitImageBtn1.setVisibility(View.GONE);
                                submitImageBtn1.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
                                image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image1Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_1" + NEW_SERVICE);
                                imageText1.setText("");
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
        });
        removeImageBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn2.setVisibility(View.GONE);
                                submitImageBtn2.setVisibility(View.GONE);
                                submitImageBtn2.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
                                image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image2Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_2" + NEW_SERVICE);
                                imageText2.setText("");
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
        });
        removeImageBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn3.setVisibility(View.GONE);
                                submitImageBtn3.setVisibility(View.GONE);
                                submitImageBtn3.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
                                image3.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image3Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_3" + NEW_SERVICE);
                                imageText3.setText("");
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
        });
        removeImageBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn4.setVisibility(View.GONE);
                                submitImageBtn4.setVisibility(View.GONE);
                                submitImageBtn4.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
                                image4.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image4Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_4" + NEW_SERVICE);
                                imageText4.setText("");
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
        });
        removeImageBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn5.setVisibility(View.GONE);
                                submitImageBtn5.setVisibility(View.GONE);
                                submitImageBtn5.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
                                image5.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image5Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_5" + NEW_SERVICE);
                                imageText5.setText("");
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
        });

        removeImageBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn6.setVisibility(View.GONE);
                                submitImageBtn6.setVisibility(View.GONE);
                                submitImageBtn6.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
                                image6.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image6Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_6" + NEW_SERVICE);
                                imageText6.setText("");

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
        });

        submitImageBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Image imageFromDatabase = dbObject.getImage(session.getValue("APP_ID") + "_1" + NEW_SERVICE);
                Log.d("imageFromDatabase", "onClick: " + imageFromDatabase.getFileName());
                submitImageBtn1.setBackground(ContextCompat.getDrawable(context, R.drawable.upload_background));
                if (image1Flag == 1) {
                    try {
                        image1Flag = 3;
                        submitImage(imageFromDatabase);
                    } catch (Exception e) {
                        String error = e.toString();
                        e.printStackTrace();
                    }
                } else if (image1Flag == 3) {
                    GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.upload_image), getString(R.string.upload_image_lbl));

                }

                /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn6.setVisibility(View.GONE);
                                image6.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image6Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_6" + NEW_SERVICE);
                                imageText6.setText("");

                            }
                        });
                alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();*/
            }
        });
        submitImageBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Image imageFromDatabase = dbObject.getImage(session.getValue("APP_ID") + "_2" + NEW_SERVICE);
                Log.d("imageFromDatabase", "onClick: " + imageFromDatabase.getFileName());
                submitImageBtn2.setBackground(ContextCompat.getDrawable(context, R.drawable.upload_background));
                if (image2Flag == 1) {
                    try {
                        submitImage(imageFromDatabase);
                        image2Flag = 3;
                    } catch (Exception e) {
                        String error = e.toString();
                        e.printStackTrace();
                    }
                } else if (image2Flag == 3) {
                    GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.upload_image), getString(R.string.upload_image_lbl));

                }
                /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn6.setVisibility(View.GONE);
                                image6.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image6Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_6" + NEW_SERVICE);
                                imageText6.setText("");

                            }
                        });
                alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();*/
            }
        });
        submitImageBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Image imageFromDatabase = dbObject.getImage(session.getValue("APP_ID") + "_3" + NEW_SERVICE);
                Log.d("imageFromDatabase", "onClick: " + imageFromDatabase.getFileName());
                submitImageBtn3.setBackground(ContextCompat.getDrawable(context, R.drawable.upload_background));
                if (image3Flag == 1) {
                    try {
                        submitImage(imageFromDatabase);
                        image3Flag = 3;
                    } catch (Exception e) {
                        String error = e.toString();
                        e.printStackTrace();
                    }
                } else if (image3Flag == 3) {
                    GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.upload_image), getString(R.string.upload_image_lbl));

                }
                /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn6.setVisibility(View.GONE);
                                image6.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image6Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_6" + NEW_SERVICE);
                                imageText6.setText("");

                            }
                        });
                alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();*/
            }
        });
        submitImageBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Image imageFromDatabase = dbObject.getImage(session.getValue("APP_ID") + "_4" + NEW_SERVICE);
                Log.d("imageFromDatabase", "onClick: " + imageFromDatabase.getFileName());
                submitImageBtn4.setBackground(ContextCompat.getDrawable(context, R.drawable.upload_background));
                if (image4Flag == 1) {
                    try {
                        submitImage(imageFromDatabase);
                        image4Flag = 3;
                    } catch (Exception e) {
                        String error = e.toString();
                        e.printStackTrace();
                    }
                } else if (image4Flag == 3) {
                    GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.upload_image), getString(R.string.upload_image_lbl));

                }
                /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn6.setVisibility(View.GONE);
                                image6.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image6Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_6" + NEW_SERVICE);
                                imageText6.setText("");

                            }
                        });
                alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();*/
            }
        });
        submitImageBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Image imageFromDatabase = dbObject.getImage(session.getValue("APP_ID") + "_5" + NEW_SERVICE);
                Log.d("imageFromDatabase", "onClick: " + imageFromDatabase.getFileName());
                submitImageBtn5.setBackground(ContextCompat.getDrawable(context, R.drawable.upload_background));
                if (image5Flag == 1) {
                    try {
                        submitImage(imageFromDatabase);
                        image5Flag = 3;
                    } catch (Exception e) {
                        String error = e.toString();
                        e.printStackTrace();
                    }
                } else if (image5Flag == 3) {
                    GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.upload_image), getString(R.string.upload_image_lbl));

                }
                /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn6.setVisibility(View.GONE);
                                image6.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image6Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_6" + NEW_SERVICE);
                                imageText6.setText("");

                            }
                        });
                alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();*/
            }
        });
        submitImageBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Image imageFromDatabase = dbObject.getImage(session.getValue("APP_ID") + "_6" + NEW_SERVICE);
                Log.d("imageFromDatabase", "onClick: " + imageFromDatabase.getFileName());
                submitImageBtn6.setBackground(ContextCompat.getDrawable(context, R.drawable.upload_background));
                if (image6Flag == 1) {
                    try {
                        submitImage(imageFromDatabase);
                        image6Flag = 3;
                    } catch (Exception e) {
                        String error = e.toString();
                        e.printStackTrace();
                    }
                } else if (image6Flag == 3) {
                    GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.upload_image), getString(R.string.upload_image_lbl));

                }
                /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationDetails.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn6.setVisibility(View.GONE);
                                image6.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image6Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_6" + NEW_SERVICE);
                                imageText6.setText("");

                            }
                        });
                alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();*/
            }
        });


        //submit data to the server
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                                                "\"templateId\":" + item.getTemplateId() + ",\n" +
                                                "\"warehouseId\": " + (item.getTemplateId().equals("0") ? item.getWarehouse().getWarehouseId() : applicationDetails.getWarehouse().getWarehouseId()) + ",\n" +//item.getWarehouse().getWarehouseId()
                                                "\"priceListId\": " + (item.getTemplateId().equals("0") ? item.getPricList().getPriceListId() : applicationDetails.getPriceList().getPriceListId()) + "\n" + //item.getPricList().getPriceListId()
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
                                            "\"warehouseId\": " + applicationDetails.getWarehouse().getWarehouseId() + ",\n" +
                                            "\"priceListId\": " + applicationDetails.getPriceList().getPriceListId() + ",\n" +
                                            "\"projectTypeId\": " + applicationDetails.getProjectType().getProjectTypeId() + ",\n" +
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
                                    Log.d("bodyData", "bodyData : " + bodyData);
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

        if (requestCode == REQUEST_CAMERA_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                String base64 = "";
                switch (imagesFlag) {
                    case 1:
                        base64 = helper.toBase64(bitmap);
                        showImageLookUps("1", base64, imageText1);
                        removeImageBtn1.setVisibility(View.VISIBLE);
                        submitImageBtn1.setVisibility(View.VISIBLE);
                        submitImageBtn1.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
                        image1.setImageBitmap(bitmap);
                        image1Flag = 1;
                        break;
                    case 2:
                        base64 = helper.toBase64(bitmap);
                        showImageLookUps("2", base64, imageText2);
                        removeImageBtn2.setVisibility(View.VISIBLE);
                        submitImageBtn2.setVisibility(View.VISIBLE);
                        submitImageBtn2.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
                        image2.setImageBitmap(bitmap);
                        image2Flag = 1;
                        break;
                    case 3:
                        base64 = helper.toBase64(bitmap);
                        showImageLookUps("3", base64, imageText3);
                        image3.setImageBitmap(bitmap);
                        removeImageBtn3.setVisibility(View.VISIBLE);
                        submitImageBtn3.setVisibility(View.VISIBLE);
                        submitImageBtn3.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
                        image3Flag = 1;
                        break;
                    case 4:
                        base64 = helper.toBase64(bitmap);
                        showImageLookUps("4", base64, imageText4);
                        image4.setImageBitmap(bitmap);
                        removeImageBtn4.setVisibility(View.VISIBLE);
                        submitImageBtn4.setVisibility(View.VISIBLE);
                        submitImageBtn4.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
                        image4Flag = 1;
                        break;
                    case 5:
                        base64 = helper.toBase64(bitmap);
                        showImageLookUps("5", base64, imageText5);
                        image5.setImageBitmap(bitmap);
                        removeImageBtn5.setVisibility(View.VISIBLE);
                        submitImageBtn5.setVisibility(View.VISIBLE);
                        submitImageBtn5.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
                        image5Flag = 1;
                        break;
                    case 6:
                        base64 = helper.toBase64(bitmap);
                        showImageLookUps("6", base64, imageText6);
                        image6.setImageBitmap(bitmap);
                        removeImageBtn6.setVisibility(View.VISIBLE);
                        submitImageBtn6.setVisibility(View.VISIBLE);
                        submitImageBtn6.setBackground(ContextCompat.getDrawable(context, R.drawable.image_border));
                        image6Flag = 1;
                        break;

                }
                if (resultCode == Activity.RESULT_CANCELED) {

                }
            }

//            image1.setImageBitmap(bitmap);
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//            byte[] imageBytes = byteArrayOutputStream.toByteArray();
//            String base64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//            base64 = imageString;
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
            phase1Quntitiy.setText(app.getPhase1Meter());
            phase3Quntitiy.setText(app.getPhase3Meter());


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


    void showImageLookUps(String imageId, String base64, TextView imageLookupText) {
        AlertDialog alert = null;
        promptsView = getLayoutInflater().inflate(R.layout.image_lookups, null);

        imageLookUpsSP = (Spinner) promptsView.findViewById(R.id.imageLookUpsSP);
        if (dbObject.tableIsEmpty(Database.ATTACHMENT_TYPE_TABLE)) {
            warning(getResources().getString(R.string.no_data_found));
        } else {
            imageLookupsArrayList = dbObject.getAttchmentType();
            // appendNoteLookUpsListToSpinner(noteLookUpSP, noteLookUpsArrayList, null);
            appendImagesLookupsListToSpinner(imageLookUpsSP, imageLookupsArrayList, null);
        }
        //create new dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.choose_item_lbl));
        builder.setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.submit_form_lbl), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        AttchmentType imageLookUp = ((AttchmentType) imageLookUpsSP.getSelectedItem());
                        Image image = new Image();

                        //need work
                        image.setAppRowId(appId);
                        image.setAttachmentType(imageLookUp);
                        image.setFile(base64);
                        image.setFileName(appId + "_" + imageId + NEW_SERVICE);
                        image.setUsername(session.getValue("username"));

                        dbObject.addImage(image);

                        imageLookupText.setText(imageLookUp.toString());


                        dialog.dismiss();
                    }
                });
                /*.setNegativeButton(getResources().getString(R.string.cancel_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });*/
        //set view to alert dialog
        builder.setView(promptsView);
        alert = builder.create();
        alert.show();
    }


    void addNote() {

        AlertDialog alert = null;
        promptsView = getLayoutInflater().inflate(R.layout.add_note, null);
//get the value of edit text
        noteET = (EditText) promptsView.findViewById(R.id.note);
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
                .setPositiveButton(getResources().getString(R.string.submit_form_lbl), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        note = noteET.getText().toString();


                        dbObject.submitNote(appId, note);
//                    {
//                        "application": {
//                        "applRowId":"220060" ,
//                                "notes":"Test from server......",
//                                "noteLookupID":498 ,
//                                "username":"JZAYDAN"
//                    }
//                    }

                        String noteLookUp = ((NoteLookUp) noteLookUpSP.getSelectedItem()).getCode();

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
    }


    private void submitNote(String bodyData) {
        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;


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
                    if (!submitData.getString("request_response").equals("Success")) {
                        GeneralFunctions.messageBox(OpenApplicationDetails.this, getString(R.string.success_lbl), getResources().getString(R.string.submit_success));
                        // Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();//display the response submit success
                    } else {
                        GeneralFunctions.messageBox(OpenApplicationDetails.this, getString(R.string.failed), getResources().getString(R.string.submit_failed));
                        //Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_failed), Toast.LENGTH_LONG).show();//display the response submit failed
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getItemsFromServer", "Error Login Request :" + error.toString());
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


    private void submitImage(Image image/*, String base64 , String lookupCode , String filename*/) {
        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;


        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();//display the response submit success
                Log.d("sumbitImage", "Response: " + response);
                try {
                    JSONObject submitData = new JSONObject(response);

                    Log.d("getfilename", "onResponse: " + image.getFileName());
                    if (submitData.getString("message").equals("Created " + image.getFileName() + ".jpeg")) {
                        //display the response submit success
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {
                        GeneralFunctions.messageBox(OpenApplicationDetails.this, getResources().getString(R.string.submit_failed), submitData.getString("message"));
                        // Toast.makeText(getApplicationContext(), submitData.getString("message"), Toast.LENGTH_LONG).show();//display the response submit failed
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                params.put("file", image.getFile()); // base64
                params.put("appRowId", appId);
                params.put("filename", image.getFileName() + ".jpeg");//filename
                params.put("content_type", "image/jpeg");
                params.put("appId", appId);

                /////////////// parse to int
                params.put("attachmentType", (image.getAttachmentType().getCode())); // attachement type code
                params.put("username", session.getValue("username"));

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
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
                .setPositiveButton(getResources().getString(R.string.submit_form_lbl), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        phase1txt = phase1.getText().toString();
                        phase3txt = phase3.getText().toString();
                        if (phase1txt.equals("") || phase1txt.equals(null) || phase1txt.equals("0")) {
                            phase1txt = "0";
                            phase1Quntitiy.setText("0");
                        } else phase1Quntitiy.setText(phase1txt);
                        if (phase3txt.equals("") || phase3txt.equals(null) || phase3txt.equals("0")) {
                            phase3txt = "0";
                            phase3Quntitiy.setText("0");
                        } else phase3Quntitiy.setText(phase3txt);
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
                        CharSequence doneDate = DateFormat.format("yyyy-MM-dd", new Date());

//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();//display the response submit success
                        applicationDetails.setTicketStatus("D");
                        applicationDetails.setSync("1");
                        dbObject.updateApplicationData(applicationDetails.getAppID(), "doneDate", applicationDetails.getDone_date());
                        dbObject.updateApplicationStatus(applicationDetails.getAppID(), applicationDetails.getTicketStatus(), "1");
                        Intent i = new Intent(OpenApplicationDetails.this, SuccessScreen.class);
//                        Intent i = new Intent(OpenApplicationDetails.this, MainActivity.class);
                        startActivity(i);
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


    @Override
    protected void onRestart() {
        super.onRestart();


        //estimatedTemplatesListAdapter.notifyItemChanged(estimatedTemplatesListAdapter.getPos());

    }
}