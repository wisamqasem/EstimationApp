package com.jdeco.estimationapp.operations;

import java.util.HashMap;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.jdeco.estimationapp.SplashActivity;
import com.jdeco.estimationapp.objects.User;
import com.jdeco.estimationapp.ui.LoginUI;
import com.jdeco.estimationapp.ui.MainActivity;

public class Session {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "EstimationAppPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";



    // Constructor
    public Session(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(User user){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // storing user details
        editor.putInt("id", user.getId());
        editor.putString("username", user.getUsername());
        editor.putString("fullName", user.getFullName());
        editor.putString("token", user.getToken());
        editor.putString("emp_id",user.getEmployeeNo());
        editor.putString("saftey_switch",user.getSafetySwitch());
        editor.putString("password",user.getPassword());


        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        } else {
            Intent intent = new Intent(_context, LoginUI.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);
        }

    }



    /**
     * Get stored session data
     * */
    public User getUserDetails(){
        User user = new User();
        // user name
        user.setId( pref.getInt("id", -1));
        user.setUsername( pref.getString("username", null));
        user.setToken( pref.getString("token", null));
        user.setFullName( pref.getString("fullName", null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(Context context){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        editor.putBoolean(IS_LOGIN, false);

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, LoginUI.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);


    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setImageName(String name)
    {
        editor.putString("imageName", name);
        // commit changes
        editor.commit();
    }

    public String getImageName()
    {
        return pref.getString("imageName", null);
    }

    //set value and get value
    public void setValue(String key,String value)
    {
        editor.putString(key, value);

        // commit changes
        editor.commit();
    }

    //get value
    public String getValue(String key)
    {
        return pref.getString(key, null);
    }

    public boolean checkValue(String key)
    {
        if(pref.getString(key, null)!=null)return true;
        else return  false;


    }


}
