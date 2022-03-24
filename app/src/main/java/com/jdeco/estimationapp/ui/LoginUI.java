package com.jdeco.estimationapp.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.objects.Crypt;
import com.jdeco.estimationapp.objects.User;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.GeneralFunctions;
import com.jdeco.estimationapp.operations.Helper;
import com.jdeco.estimationapp.operations.Session;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginUI extends AppCompatActivity {

    TextInputEditText username, password;
    Button loginBtn, backUpBtn;
    Session session;
    Database database;
    Helper helper;
    String TAG = "LoginUI";
    Crypt encryptionObject;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ui);


        //initilize controls
        init();

    }

    private void init() {


        //request storage permission
        ActivityCompat.requestPermissions(LoginUI.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE,},
                1001);

        //initilize controls
        username = (TextInputEditText) findViewById(R.id.username);
        password = (TextInputEditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        backUpBtn = (Button) findViewById(R.id.backUpBtn);


        // username.setText("jzaydan");//delete this .............
        //  password.setText("12345");//delete this .............
        session = new Session(this);
        database = new Database(this);
        helper = new Helper(this);


//        session.checkLogin();





        /*
         * When you want that Keybord not shown untill user clicks on one of the EditText Field.
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        //handle button login click
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //check username is empty
                if (username.getText().toString().matches("")) {
                    //show error msg to the user invalid username
                    username.setError(getResources().getString(R.string.fill_username_lbl));
                    username.requestFocus();
                } else if (password.getText().toString().matches("")) {
                    //show error msg to the user invalid password
                    password.setError(getResources().getString(R.string.fill_password_lbl));
                    password.requestFocus();
                } else if (helper.isInternetConnection()) {
                    encryptionObject = new Crypt(username.getText().toString());

                    //send login request
                    user = new User();
                    //user.setId(1);
                    //  user.setUsername(username.getText().toString());
                    //  user.setFullName(username.getText().toString());

                    //user.setToken("123");


                    //send post request to the server
                    doLogin(username.getText().toString(), password.getText().toString());


                    //Toast.makeText(getApplicationContext(),username.getText().toString(),Toast.LENGTH_LONG).show();
                } else if (database.checkUserOffline(username.getText().toString(), password.getText().toString())) {
                    User userOffline = database.getUser(username.getText().toString());

                    session.createLoginSession(userOffline);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_success), Toast.LENGTH_LONG).show();//display the response login success

                    //go to main screen
                    Intent intent = new Intent(LoginUI.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    database.getAllUsers();
                    loginBtn.setError(getResources().getString(R.string.check_internet_connection));
                    GeneralFunctions.messageBox(LoginUI.this, getResources().getString(R.string.check_internet_connection), getString(R.string.check_internet_saved_data));
                   // Toast.makeText(LoginUI.this, getResources().getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });

        //for test only

        backUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database.backupDatabase();
            }
        });


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goBack();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1001: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void updateAndroidSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    //send login request
    private void doLogin(String usernameTxt, String passwordTxt) {


        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        Log.d("doLogin", username + " " + password);


        updateAndroidSecurityProvider();
        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("doLogin", "Response: " + response);

                //create json object
                try {
                    JSONObject loginResultObject = new JSONObject(response);

                    //get login result
                    //for testing
                    if (loginResultObject.getBoolean("success")) {
                        user.setEmployeeNo(loginResultObject.getString("emp_id"));
                        user.setSafetySwitch(loginResultObject.getString("saftey_switch"));
                        user.setUsername(usernameTxt);
                        user.setFullName(usernameTxt);
                        user.setPassword(passwordTxt);
                        if (!database.isItemExist("users", "username", usernameTxt))
                            //insert user id
                            database.addUser(user);

                        //create user session
                        session.createLoginSession(user);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_success), Toast.LENGTH_LONG).show();//display the response login success

                        //go to main screen
                        Intent intent = new Intent(LoginUI.this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_falied), Toast.LENGTH_LONG).show();//display the response login failed
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             //  GeneralFunctions.messageBox(getApplicationContext(),"فشل تسجيل الدخول .",error.toString());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();//display the response login failed
                Log.i(TAG, "Error Login Request :" + error.toString());
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                Log.d("username999", usernameTxt);
                Log.d("username999p", encryptionObject.encryptAsBase64(passwordTxt.getBytes()));
                //parameters
                params.put("username", usernameTxt);
                params.put("password", encryptionObject.encryptAsBase64(passwordTxt.getBytes()));
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_LOGIN);

                Log.d("doLogin", "Parameters: " + username + " " + encryptionObject.encryptAsBase64(passwordTxt.getBytes()) + " " + CONSTANTS.ACTION_LOGIN + " " + CONSTANTS.API_KEY);
                return params;
            }
        };

        Log.d("Volley String Request", "string request: " + mStringRequest.getUrl());
        String url = mStringRequest.getUrl();


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

    public void goBack() {
        finish();
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//        alertDialog.setTitle("");
//        alertDialog.setMessage(getResources().getString(R.string.close_form_confirmation_msg));
//        alertDialog.setPositiveButton(getResources().getString(R.string.yes_lbl),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //close the activity
//                        finish();
//                    }
//                });
//        alertDialog.setNegativeButton(getResources().getString(R.string.no_lbl),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        alertDialog.show();
    }


}