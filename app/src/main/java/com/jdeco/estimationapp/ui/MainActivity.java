package com.jdeco.estimationapp.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.operations.GetData;
import com.jdeco.estimationapp.ui.forms.ApplicationsList;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.ui.forms.DoneList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Session session;
    ProgressDialog progressDialog;
    GetData getData;

    String goTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras;


        extras = getIntent().getExtras();
        if (extras != null) {
            goTo = extras.getString("goTo");
        }

        //initiate session
        session = new Session(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_orders_list);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (goTo == "done") {
            DoneList fragment = new DoneList();
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
        } else {
            ApplicationsList fragment = new ApplicationsList();
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();

        }



        /*
         * When you want that Keybord not shown untill user clicks on one of the EditText Field.
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


    }


    @Override
    public void onBackPressed() {
        goBack();
    }

    public void goBack() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("");
        alertDialog.setMessage(getResources().getString(R.string.logout_to_exit));

        alertDialog.setPositiveButton(getResources().getString(R.string.ok_lbl),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //close the activity
                        dialog.cancel();
                    }
                });


        alertDialog.show();
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        if (id == R.id.logout) {
            //logout
            session.logoutUser(this);
            finish();
            // session.checkLogin();
            if (!session.isLoggedIn())
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.logout_success), Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_falied), Toast.LENGTH_LONG).show();
            }

            return true;
        } else if (id == R.id.updateData) {
            MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
            myAsyncTasks.execute();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_orders_list) {
            fragment = new ApplicationsList();
//            Intent goToOrderList = new Intent(this, MainActivity.class);
//            startActivity(goToOrderList);
        } else if (id == R.id.nav_done_list) {
            fragment = new DoneList();
            // Intent goToDoneList = new Intent(this, DoneList.class);
            //  startActivity(goToDoneList);
        }
        //else if (id == R.id.nav_settings) {
////            Intent goToDoneList = new Intent(this, DoneList.class);
////            startActivity(goToDoneList);
//        }
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    public class MyAsyncTasks extends AsyncTask<String, String, String> {
        GetData getData = new GetData();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            getData.loading(MainActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";
            try {

                getData.insertDataToDatabase(MainActivity.this);

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {


            // dismiss the progress dialog after receiving data from API
            //    progressDialog.dismiss();
            try {
                // JSON Parsing of data


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }


}