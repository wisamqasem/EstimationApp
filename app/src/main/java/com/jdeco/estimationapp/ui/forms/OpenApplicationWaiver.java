package com.jdeco.estimationapp.ui.forms;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.ActionLookUp;
import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.AttchmentType;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.objects.Image;
import com.jdeco.estimationapp.objects.Warehouse;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.Helper;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OpenApplicationWaiver extends AppCompatActivity {


    TextView appID, customerNameTB, branch, appType, phoneTB, addressTB, old_customer_nameTV, customer_nameTV, appl_date, status, service_status, sub_branch, service_no, service_class, meter_no, meter_type, install_date;
    TextView last_read, last_read_date, notes, safety_switch, meter_no_form, service_no_from;

    Button submitBtn;
    private static final int REQUEST_CAMERA_CODE = 12;
    ProgressDialog progress;
    Session session;
    Database dbObject;
    Helper helper;
    //To show that this image belong to (new service) application
    private final String CHANGE_NAME = "_Change_Name";
    Spinner situationsSP, imageLookUpsSP;
    View promptsView;
    ArrayList<AttchmentType> imageLookupsArrayList = null;
    EditText employeeNotes, currentRead;
    // Add image
    ImageView image1, image2, image3, image4, image5, image6;
    TextView imageText1, imageText2, imageText3, imageText4, imageText5, imageText6;
    ImageView removeImageBtn1, removeImageBtn2, removeImageBtn3, removeImageBtn4, removeImageBtn5, removeImageBtn6;
    //    String base64;
    int imagesFlag = 0, image1Flag = 0, image2Flag = 0, image3Flag = 0, image4Flag = 0, image5Flag = 0, image6Flag = 0;
    ScrollView scrollView;

    ApplicationDetails applicationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_application_waiver);

        // Remove keyboard focus when start activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (ContextCompat.checkSelfPermission(OpenApplicationWaiver.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OpenApplicationWaiver.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.application_details_lbl));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Add back arrow in action bar
        }


        notes = findViewById(R.id.notes);
        last_read_date = findViewById(R.id.last_read_date);
        last_read = findViewById(R.id.last_read);
        install_date = findViewById(R.id.install_date);
        meter_type = findViewById(R.id.meter_type);
        meter_no = findViewById(R.id.meter_no);
        service_class = findViewById(R.id.service_class);
        service_no = findViewById(R.id.service_no);
        sub_branch = findViewById(R.id.sub_branch);
        service_status = findViewById(R.id.service_status);
        status = findViewById(R.id.status);
        appl_date = findViewById(R.id.appl_date);
        customer_nameTV = findViewById(R.id.customer_nameTV);
        old_customer_nameTV = findViewById(R.id.old_customer_nameTV);
        addressTB = findViewById(R.id.addressTB);
        phoneTB = findViewById(R.id.phoneTB);

        appType = findViewById(R.id.appType);
        branch = findViewById(R.id.branch);
        customerNameTB = findViewById(R.id.customerNameTB);
        appID = findViewById(R.id.appID);


        employeeNotes = (EditText) findViewById(R.id.EmployeeNotes);
        currentRead = (EditText) findViewById(R.id.currentRead);

        safety_switch = findViewById(R.id.safety_switch);

        service_no_from = findViewById(R.id.service_no_form);
        meter_no_form = findViewById(R.id.meter_no_form);

        situationsSP = (Spinner) findViewById(R.id.situations);
        submitBtn = (Button) findViewById(R.id.submitBtn);

        dbObject = new Database(this);
        session = new Session(this);
        helper = new Helper(this);
        applicationDetails = new ApplicationDetails();

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

        // if image table is not empty
        if (!dbObject.tableIsEmpty(Database.IMAGES_TABLE)) {
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_1" + CHANGE_NAME)) {

                helper.setImageFromDatabase(session.getValue("APP_ID") + "_1" + CHANGE_NAME, image1, imageText1, removeImageBtn1);
                image1Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_2" + CHANGE_NAME)) {

                helper.setImageFromDatabase(session.getValue("APP_ID") + "_2" + CHANGE_NAME, image2, imageText2, removeImageBtn2);
                image2Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_3" + CHANGE_NAME)) {

                helper.setImageFromDatabase(session.getValue("APP_ID") + "_3" + CHANGE_NAME, image3, imageText3, removeImageBtn3);
                image3Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_4" + CHANGE_NAME)) {

                helper.setImageFromDatabase(session.getValue("APP_ID") + "_4" + CHANGE_NAME, image4, imageText4, removeImageBtn4);
                image4Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_5" + CHANGE_NAME)) {

                helper.setImageFromDatabase(session.getValue("APP_ID") + "_5" + CHANGE_NAME, image5, imageText5, removeImageBtn5);
                image5Flag = 1;
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_6" + CHANGE_NAME)) {

                helper.setImageFromDatabase(session.getValue("APP_ID") + "_6" + CHANGE_NAME, image6, imageText6, removeImageBtn6);
                image6Flag = 1;
            }

        }

        // Add Image
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesFlag = 1;
                if (image1Flag == 1) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationWaiver.this);
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

                } else {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
                }

            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesFlag = 2;
                if (image2Flag == 1) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationWaiver.this);
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

                } else {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
                }
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesFlag = 3;
                if (image3Flag == 1) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationWaiver.this);
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

                } else {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
                }
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesFlag = 4;
                if (image4Flag == 1) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationWaiver.this);
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

                } else {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
                }
            }
        });

        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesFlag = 5;
                if (image5Flag == 1) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationWaiver.this);
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

                } else {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
                }
            }
        });

        image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesFlag = 6;
                if (image6Flag == 1) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationWaiver.this);
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

                } else {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA_CODE);
                }
            }
        });

        removeImageBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationWaiver.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn1.setVisibility(View.GONE);
                                image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image1Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_1" + CHANGE_NAME);
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationWaiver.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn2.setVisibility(View.GONE);
                                image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image2Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_2" + CHANGE_NAME);
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationWaiver.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn3.setVisibility(View.GONE);
                                image3.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image3Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_3" + CHANGE_NAME);
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationWaiver.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn4.setVisibility(View.GONE);
                                image4.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image4Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_4" + CHANGE_NAME);
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationWaiver.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn5.setVisibility(View.GONE);
                                image5.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image5Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_5" + CHANGE_NAME);
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpenApplicationWaiver.this);
                alertDialog.setTitle("");
                alertDialog.setMessage(R.string.delete_image_confirm);
                alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeImageBtn6.setVisibility(View.GONE);
                                image6.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                image6Flag = 0;
                                dbObject.deleteImage(session.getValue("APP_ID") + "_6" + CHANGE_NAME);
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






        //get application details
        applicationDetails = dbObject.getApplications(session.getValue("APP_ID"), "N", session.getValue("username")).get(0);








//        situationsSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                ApplicationDetails proced = ((ApplicationDetails) situationsSP.getSelectedItem());
//                dbObject.updateApplicationData(session.getValue("APP_ID"),"actionCode", proced.getActionCode());
//                dbObject.updateApplicationData(session.getValue("APP_ID"),"actionName", proced.getActionName());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });



        currentRead.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dbObject.updateApplicationData(session.getValue("APP_ID"),"currentRead", currentRead.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        employeeNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dbObject.updateApplicationData(session.getValue("APP_ID"),"employeeNotes", employeeNotes.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        assignData(applicationDetails);


        //submit data to the server
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



              
                if (helper.isInternetConnection()) {
                    progress = new ProgressDialog(OpenApplicationWaiver.this);
                    progress.setTitle(getResources().getString(R.string.please_wait));
                    progress.setCancelable(true);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();


                    CharSequence date = DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date());
                    //edit.................................................................................
                    // check if the edit text null
                    if (currentRead.getText().toString().isEmpty() || currentRead.getText().toString().equalsIgnoreCase(" ")) {
                        progress.dismiss();
                        currentRead.requestFocus();
                        currentRead.setError("الرجاء تعبيئة الحقل");
                        Toast.makeText(OpenApplicationWaiver.this, "الرجاء تعبئة القراءة الحالية !", Toast.LENGTH_SHORT).show();
                    } else if (employeeNotes.getText().toString().isEmpty() || employeeNotes.getText().toString().equalsIgnoreCase(" ")) {
                        progress.dismiss();
                        employeeNotes.requestFocus();
                        employeeNotes.setError("الرجاء تعبيئة الحقل");
                        Toast.makeText(OpenApplicationWaiver.this, "الرجاء تعبئة الملاحظات !", Toast.LENGTH_SHORT).show();
                    } else {
                        String bodyData = "{\n" +
                                "\"application\": {\n" +
                                "\"applRowId\": " + applicationDetails.getRowId() + ",\n" +//applicationDetails.getAppID()
                                "\"actionCode\": " + 2/*((ActionLookUp) situationsSP.getSelectedItem()).getActionCode()*/ + ",\n" +//applicationDetails.getPrjRowId()
                                "\"employeeNo\": \"" + session.getValue("emp_id") + "\",\n" +
                                "\"applId\": " + applicationDetails.getAppID() + ",\n" +//applicationDetails.getAppID()
                                "\"safetySwitch\": " + session.getValue("saftey_switch") + ",\n" +
                                "\"lastRead\": " + currentRead.getText().toString() + ",\n" +
                                "\"notes\": '" + employeeNotes.getText().toString() + "',\n" +
                                "\"username\": \"" + applicationDetails.getUsername() + "\",\n" +
                                "\"lastReadDate\": \"" + date + "\",\n" +
                                "}}\n";

                        Log.d("bodyData : ", bodyData);
                        try {
                            submitMaterialsToServer(bodyData);

                            for (int i = 1; i < 7; i++) {
                                if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_" + i + CHANGE_NAME)) {
                                    Image imageFromDatabase = dbObject.getImage(session.getValue("APP_ID") + "_" + i + CHANGE_NAME);
                                    try {
                                        submitImage(imageFromDatabase);
//                                    Log.d("bodyData :  i -> " + i, imageFromDatabase.getFileName());


                                    } catch (Exception e) {
                                        String error = e.toString();
                                        e.printStackTrace();
                                    }
                                }
                            }
                        /*Intent i = new Intent(OpenApplicationWaiver.this, MainActivity.class);
                        startActivity(i);*/

                        } catch (Exception e) {
                            String error = e.toString();
                            Toast.makeText(OpenApplicationWaiver.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    Toast.makeText(OpenApplicationWaiver.this, getResources().getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
                }
                }


        });
        ArrayList<ActionLookUp> options = new ArrayList<ActionLookUp>();
        options.add(new ActionLookUp("61", getString(R.string.no_problem)));
        options.add(new ActionLookUp("24", getString(R.string.closed)));
        options.add(new ActionLookUp("233", getString(R.string.follow_up_inspection)));
/* ArrayList<String> options = new ArrayList<String>();

        options.add(getString(R.string.no_problem));
        options.add(getString(R.string.closed));
        options.add(getString(R.string.follow_up_inspection));*/


        ArrayAdapter<ActionLookUp> adapter =
                new ArrayAdapter<ActionLookUp>(getApplicationContext(), android.R.layout.simple_spinner_item, options);
        //add adapter to spinner
        situationsSP.setAdapter(adapter);


//        int pos = 0;
//        for (int ii = 0; ii < options.size(); ii++) {
//            if (options.get(ii).getActionName().equals(applicationDetails.getActionName())) {
//                pos = ii;
//                break;
//            }
//        }
//
//        situationsSP.setSelection(pos);


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
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();//display the response submit success
                Log.d("sumbitImage", "Response: " + response);
                try {
                    JSONObject submitData = new JSONObject(response);

                    if (submitData.getString("message").equals("Created " + image.getFileName())) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();//display the response submit success
                    } else {
                        Toast.makeText(getApplicationContext(), submitData.getString("message"), Toast.LENGTH_LONG).show();//display the response submit failed
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getItemsFromServer", "Error Login Request :" + error.toString());
                Toast.makeText(getApplicationContext(), "فشل بأضافة صورة", Toast.LENGTH_LONG).show();
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
                params.put("appRowId", session.getValue("APP_ID"));
                params.put("filename", image.getFileName() + ".jpeg");//filename
                params.put("content_type", "image/jpeg");
                params.put("appId",session.getValue("APP_ID"));

                /////////////// parse to int
                params.put("attachmentType", (image.getAttachmentType().getCode())); // attachement type code
                params.put("username", session.getValue("username"));

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA_CODE) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String base64 = "";
            switch (imagesFlag) {
                case 1:
                    base64 = helper.toBase64(bitmap);
                    showImageLookUps("1", base64, imageText1);
                    removeImageBtn1.setVisibility(View.VISIBLE);
                    image1.setImageBitmap(bitmap);
                    image1Flag = 1;
                    break;
                case 2:
                    base64 = helper.toBase64(bitmap);
                    showImageLookUps("2", base64, imageText2);
                    removeImageBtn2.setVisibility(View.VISIBLE);
                    image2.setImageBitmap(bitmap);
                    image2Flag = 1;
                    break;
                case 3:
                    base64 = helper.toBase64(bitmap);
                    showImageLookUps("3", base64, imageText3);
                    image3.setImageBitmap(bitmap);
                    removeImageBtn3.setVisibility(View.VISIBLE);
                    image3Flag = 1;
                    break;
                case 4:
                    base64 = helper.toBase64(bitmap);
                    showImageLookUps("4", base64, imageText4);
                    image4.setImageBitmap(bitmap);
                    removeImageBtn4.setVisibility(View.VISIBLE);
                    image4Flag = 1;
                    break;
                case 5:
                    base64 = helper.toBase64(bitmap);
                    showImageLookUps("5", base64, imageText5);
                    image5.setImageBitmap(bitmap);
                    removeImageBtn5.setVisibility(View.VISIBLE);
                    image5Flag = 1;
                    break;
                case 6:
                    base64 = helper.toBase64(bitmap);
                    showImageLookUps("6", base64, imageText6);
                    image6.setImageBitmap(bitmap);
                    removeImageBtn6.setVisibility(View.VISIBLE);
                    image6Flag = 1;
                    break;

            }

//            image1.setImageBitmap(bitmap);
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//            byte[] imageBytes = byteArrayOutputStream.toByteArray();
//            String base64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//            base64 = imageString;
        }
    }

    void showImageLookUps(String imageId, String base64, TextView imageLookupText) {
        AlertDialog alert = null;
        promptsView = getLayoutInflater().inflate(R.layout.image_lookups, null);

        imageLookUpsSP = (Spinner) promptsView.findViewById(R.id.imageLookUpsSP);
        if (dbObject.tableIsEmpty(Database.ATTACHMENT_TYPE_TABLE)) {
            warning(getResources().getString(R.string.no_data_found));
        } else {
            imageLookupsArrayList = dbObject.getAttchmentType();
//            appendNoteLookUpsListToSpinner(noteLookUpSP, noteLookUpsArrayList, null);
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
                        image.setAppRowId(applicationDetails.getRowId());
                        image.setAttachmentType(imageLookUp);
                        image.setFile(base64);
                        image.setFileName(applicationDetails.getRowId() + "_" + imageId + CHANGE_NAME);
                        image.setUsername(session.getValue("username"));

                        dbObject.addImage(image);

                        imageLookupText.setText(imageLookUp.toString());


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
                        applicationDetails.setSync("1");
                        applicationDetails.setCurrentRead(currentRead.getText().toString());
                        applicationDetails.setEmployeeNotes(employeeNotes.getText().toString());
                        applicationDetails.setActionCode(((ActionLookUp) situationsSP.getSelectedItem()).getActionCode());
                        applicationDetails.setActionName(((ActionLookUp) situationsSP.getSelectedItem()).getActionName());
                        dbObject.updateApplicationStatus(applicationDetails.getAppID(), applicationDetails.getTicketStatus(), applicationDetails.getSync());
                        dbObject.submitChangeName(applicationDetails.getAppID(), applicationDetails.getCurrentRead(), applicationDetails.getEmployeeNotes(), applicationDetails.getActionCode(), applicationDetails.getActionName());
                        //dbObject.deleteِApplication(session.getValue("APP_ID"));
                        Intent i = new Intent(OpenApplicationWaiver.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_failed), Toast.LENGTH_LONG).show();//display the response submit failed
                        progress.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progress.setCancelable(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("getItemsFromServer", "Error Login Request :" + error.toString());
                progress.dismiss();
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                //parameters
                // params.put("username", "jd");
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_processChangeName);
                params.put("data", bodyData);
                params.put("appId", applicationDetails.getAppID());

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        helper.goBack(MainActivity.class);
       /* DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        helper.goBack(MainActivity.class);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(OpenApplicationWaiver.this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();*/
    }

    /*    public void goBack() {
            Class c = MainActivity.class;

            Intent back = new Intent(getApplicationContext(), c);
            back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(back);
        }
        */
    void assignData(ApplicationDetails task) {


        currentRead.setText(applicationDetails.getCurrentRead());


        if (task.getPhone() != null && !task.getPhone().equalsIgnoreCase("null")) {
            phoneTB.setText(task.getPhone());
        } else {
            phoneTB.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getCustomerAddress() != null && !task.getCustomerAddress().equalsIgnoreCase("null")) {
            addressTB.setText(task.getCustomerAddress());
        } else {
            addressTB.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getOld_customer_name() != null && !task.getOld_customer_name().equalsIgnoreCase("null")) {
            old_customer_nameTV.setText(task.getOld_customer_name());
        } else {
            old_customer_nameTV.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getCustomerName() != null && !task.getCustomerName().equalsIgnoreCase("null")) {
            customer_nameTV.setText(task.getCustomerName());
        } else {
            customer_nameTV.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getAppDate() != null && !task.getAppDate().equalsIgnoreCase("null")) {
            appl_date.setText(task.getAppDate().substring(0, 10));
        } else {
            appl_date.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getStatus() != null && !task.getStatus().equalsIgnoreCase("null")) {
            status.setText(task.getStatus());
        } else {
            status.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getService_status() != null && !task.getService_status().equalsIgnoreCase("null")) {
            service_status.setText(task.getService_status());
        } else {
            service_status.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getService_no() != null && !task.getService_no().equalsIgnoreCase("null")) {
            service_no.setText(task.getService_no());
        } else {
            service_no.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getService_class() != null && !task.getService_class().equalsIgnoreCase("null")) {
            service_class.setText(task.getService_class());
        } else {
            service_class.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (String.valueOf(task.getMeter_no()) != null && !String.valueOf(task.getMeter_no()).equalsIgnoreCase("null")) {
            meter_no.setText(String.valueOf(task.getMeter_no()));
        } else {
            meter_no.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getMeter_type() != null && !task.getMeter_type().equalsIgnoreCase("null")) {
            meter_type.setText(task.getMeter_type());
        } else {
            meter_type.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getInstall_date() != null && !task.getInstall_date().equalsIgnoreCase("null")) {
            install_date.setText(task.getInstall_date().substring(0, 10));
        } else {
            install_date.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getLast_read() != null && !task.getLast_read().equalsIgnoreCase("null")) {
            last_read.setText(task.getLast_read());
        } else {
            last_read.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getLast_read_date() != null && !task.getLast_read_date().equalsIgnoreCase("null")) {
            last_read_date.setText(task.getLast_read_date().substring(0, 10));
        } else {
            last_read_date.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getAppID() != null && !task.getAppID().equalsIgnoreCase("null")) {
            appID.setText(task.getAppID());
        } else {
            appID.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getCustomerName() != null && !task.getCustomerName().equalsIgnoreCase("null")) {
            customerNameTB.setText(task.getCustomerName());
        } else {
            customerNameTB.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getBranch() != null && !task.getBranch().equalsIgnoreCase("null")) {
            branch.setText(task.getBranch());
        } else {
            branch.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getsBranch() != null && !task.getsBranch().equalsIgnoreCase("null")) {
            sub_branch.setText(task.getsBranch());
        } else {
            sub_branch.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getAppType() != null && !task.getAppType().equalsIgnoreCase("null")) {
            appType.setText(task.getAppType());
        } else {
            appType.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getNotes() != null && !task.getNotes().equalsIgnoreCase("null")) {
            notes.setText(task.getNotes());
        } else {
            notes.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (session.getValue("saftey_switch") != null && !session.getValue("saftey_switch").equalsIgnoreCase("null")) {
            safety_switch.setText(session.getValue("saftey_switch"));
        } else {
            safety_switch.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getService_no() != null && !task.getService_no().equalsIgnoreCase("null")) {
            service_no_from.setText(task.getService_no());
        } else {
            service_no_from.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getEmployeeNotes() != null && !task.getEmployeeNotes().equalsIgnoreCase("null")) {
            employeeNotes.setText(task.getEmployeeNotes());
        }

        if (String.valueOf(task.getMeter_no()) != null && !String.valueOf(task.getMeter_no()).equalsIgnoreCase("null")) {
            meter_no_form.setText(String.valueOf(task.getMeter_no()));
        } else {
            meter_no_form.setText(this.getResources().getString(R.string.no_data_found_lbl));
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
        }

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

}