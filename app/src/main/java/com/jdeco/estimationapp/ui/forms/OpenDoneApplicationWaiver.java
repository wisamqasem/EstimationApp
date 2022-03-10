package com.jdeco.estimationapp.ui.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.AttchmentType;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.Helper;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.ui.MainActivity;

import java.util.ArrayList;

public class OpenDoneApplicationWaiver extends AppCompatActivity {
    //To show that this image belong to (new service) application
    private final String CHANGE_NAME = "_Change_Name";
    TextView appID, customerNameTB, branch, appType, phoneTB, addressTB, old_customer_nameTV, customer_nameTV, appl_date, status, service_status, sub_branch, service_no, service_class, meter_no, meter_type, install_date;
    TextView last_read, last_read_date, notes, safety_switch, meter_no_form, service_no_from;
    Button resetApp;
    Spinner situationsSP, imageLookUpsSP;
    ProgressDialog progress;
    Session session;
    Database dbObject;
    Helper helper;
    View promptsView;
    ArrayList<AttchmentType> imageLookupsArrayList = null;
    EditText employeeNotes, currentRead;
    // Add image
    ImageView image1, image2, image3, image4, image5, image6;
    TextView imageText1, imageText2, imageText3, imageText4, imageText5, imageText6;
    // Blocks
    View imagesBlocks, imagesBlock1, imagesBlock2, image1CardView, image2CardView, image3CardView, image4CardView, image5CardView, image6CardView;


    ApplicationDetails applicationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_done_application_waiver);

        // Remove keyboard focus when start activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


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
//        submitBtn = (Button) findViewById(R.id.submitBtn);

        dbObject = new Database(this);
        session = new Session(this);
        helper = new Helper(this);
        applicationDetails = new ApplicationDetails();

        resetApp = (Button) findViewById(R.id.resetBtn);


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

        image1CardView = findViewById(R.id.image1CardView);
        image2CardView = findViewById(R.id.image2CardView);
        image3CardView = findViewById(R.id.image3CardView);
        image4CardView = findViewById(R.id.image4CardView);
        image5CardView = findViewById(R.id.image5CardView);
        image6CardView = findViewById(R.id.image6CardView);

        // if image table is not empty
        if (!dbObject.tableIsEmpty(Database.IMAGES_TABLE)) {
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_1" + CHANGE_NAME)) {

                helper.setImageFromDatabaseForDoneApplications(session.getValue("APP_ID") + "_1" + CHANGE_NAME, image1, imageText1);
            } else {
                image1CardView.setVisibility(View.GONE);
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_2" + CHANGE_NAME)) {

                helper.setImageFromDatabaseForDoneApplications(session.getValue("APP_ID") + "_2" + CHANGE_NAME, image2, imageText2);
            } else {
                image2CardView.setVisibility(View.GONE);
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_3" + CHANGE_NAME)) {

                helper.setImageFromDatabaseForDoneApplications(session.getValue("APP_ID") + "_3" + CHANGE_NAME, image3, imageText3);
            } else {
                image3CardView.setVisibility(View.GONE);
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_4" + CHANGE_NAME)) {

                helper.setImageFromDatabaseForDoneApplications(session.getValue("APP_ID") + "_4" + CHANGE_NAME, image4, imageText4);
            } else {
                image4CardView.setVisibility(View.GONE);
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_5" + CHANGE_NAME)) {

                helper.setImageFromDatabaseForDoneApplications(session.getValue("APP_ID") + "_5" + CHANGE_NAME, image5, imageText5);
            } else {
                image5CardView.setVisibility(View.GONE);
            }
            if (dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_6" + CHANGE_NAME)) {

                helper.setImageFromDatabaseForDoneApplications(session.getValue("APP_ID") + "_6" + CHANGE_NAME, image6, imageText6);
            } else {
                image6CardView.setVisibility(View.GONE);
            }

           /* if (!(dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_1") && dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_2") && dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_3"))) {

                imagesBlock1.setVisibility(View.GONE);
            } else {
                imagesBlock1.setVisibility(View.VISIBLE);
            }
            if (!(dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_4") && dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_5") && dbObject.isItemExist(dbObject.IMAGES_TABLE, "filename", session.getValue("APP_ID") + "_6"))) {

                imagesBlock2.setVisibility(View.GONE);
            } else {
                imagesBlock2.setVisibility(View.VISIBLE);
            }
*/
        } else {
            imagesBlocks.setVisibility(View.GONE);
        }

        resetApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbObject.updateApplicationStatus(session.getValue("APP_ID"), "N", "1");
                Intent goToMain = new Intent(OpenDoneApplicationWaiver.this, OpenApplicationWaiver.class);
                startActivity(goToMain);
            }
        });

        //get application details
        applicationDetails = dbObject.getApplications(session.getValue("APP_ID"), "D", session.getValue("username")).get(0);


        assignData(applicationDetails);


        ArrayList<String> options = new ArrayList<String>();
        options.add(getString(R.string.no_problem));
        options.add(getString(R.string.add_note));


        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, options);
        //add adapter to spinner
        situationsSP.setAdapter(adapter);


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

    void assignData(ApplicationDetails task) {

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

        if (task.getCurrentRead() != null && !task.getCurrentRead().equalsIgnoreCase("null")) {
            currentRead.setText(task.getCurrentRead());
        } else {
            currentRead.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        if (task.getEmployeeNotes() != null && !task.getEmployeeNotes().equalsIgnoreCase("null")) {
            employeeNotes.setText(task.getEmployeeNotes());
        } else {
            service_no_from.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }

        /*if (task.getService_no() != null && !task.getService_no().equalsIgnoreCase("null")) {
            service_no_from.setText(task.getService_no());
        } else {
            service_no_from.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }*/

        if (String.valueOf(task.getMeter_no()) != null && !String.valueOf(task.getMeter_no()).equalsIgnoreCase("null")) {
            meter_no_form.setText(String.valueOf(task.getMeter_no()));
        } else {
            meter_no_form.setText(this.getResources().getString(R.string.no_data_found_lbl));
        }


    }


}