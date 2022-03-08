package com.jdeco.estimationapp.operations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.AttchmentType;
import com.jdeco.estimationapp.objects.Image;
import com.jdeco.estimationapp.objects.Item;
import com.jdeco.estimationapp.objects.NoteLookUp;
import com.jdeco.estimationapp.objects.PriceList;
import com.jdeco.estimationapp.objects.ProjectType;
import com.jdeco.estimationapp.objects.Template;
import com.jdeco.estimationapp.objects.User;
import com.jdeco.estimationapp.objects.Warehouse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "estimationapp_db";
    public static final String USERS_TABLE = "users";
    public static final String APPLICATIONS_TABLE = "applications";
    public static final String ITEMS_TABLE = "items";
    public static final String PRICE_LIST_TABLE = "PriceList";
    public static final String WAREHOUSE = "Warehouse";
    public static final String PROJECT_TYPE = "ProjectType";
    public static final String TEMPLATES_TABLE = "templates";
    public static final String ESTIMATED_ITEMS_TABLE = "estimatedItemsTable";
    public static final String ESTIMATED_TEMPLATES_TABLE = "estimatedTemplatesTable";
    public static final String NOTE_LOOK_UP = "noteLookUp";
    public static final String ATTACHMENT_TYPE_TABLE = "attachmentType";
    public static final String IMAGES_TABLE = "imagesTable";


    String CREATE_USERS_TABLE = "CREATE TABLE " + USERS_TABLE + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "fullName varchar(100),"
            + "username varchar(50) UNIQUE,"
            + "password varchar(50)," +
            "emp_id varchar(50)," +
            "saftey_switch varchar(50)," +
            "token varchar(50))";

    String CREATE_APPLICATIONS_TABLE = "CREATE TABLE " + APPLICATIONS_TABLE + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "appId varchar(100),"
            + "customerID varchar(50),"
            + "customerName varchar(50),"
            + "customerAddress varchar(50),"
            + "appDate varchar(50),"
            + "appType varchar(10)," +
            "branch varchar(50)," +
            "sbranch int," +
            "username varchar(50)," +
            "location varchar(50)," +
            "status varchar(30)," +
            "isSync INTEGER," +
            "phone varchar(30)," +
            "task_status varchar(5),"
            + "rowId varchar(100),"
            + "prjRowId varchar(100)," +
            "phase1Meter varchar(10)," +
            "phase3Meter varchar(10)," +
            "old_system_no varchar(20)," +
            "old_customer_name varchar(100)," +
            "old_id_number varchar(100)," +
            "id_number varchar(100)," +
            "account_no varchar(100)," +
            "appl_type_code varchar(100)," +
            "status_code varchar(100)," +
            "status_note varchar(100)," +
            "service_status varchar(100)," +
            "usage_type varchar(100)," +
            "no_of_phase varchar(10)," +
            "service_no varchar(10)," +
            "property_type varchar(100)," +
            "service_class varchar(100)," +
            "to_user_id varchar(100)," +
            "noofservices varchar(100)," +
            "meter_type varchar(100)," +
            "install_date varchar(100)," +
            "last_read varchar(100)," +
            "last_read_date varchar(100)," +
            "last_qty varchar(100)," +
            "notes varchar(100)," +
            "meter_no varchar(100)," +
            "note varchar(500)," +
            "noteLookUp varchar(100)," +
            "sync varchar(5)) ";

    String CREATE_ITEMS_TABLE = "CREATE TABLE " + ITEMS_TABLE + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "itemId varchar(50)," +
            "itemName varchar(100),"
            + "itemAmount INTEGER,"
            + "templateId varchar(50)," +
            "inventoryItemCode varchar(50)," +
            "allowDelete varchar(5)," +
            "allowEdit varchar(5))";


    String CREATE_ESTIMATED_TEMPLATES_TABLE = "CREATE TABLE " + ESTIMATED_TEMPLATES_TABLE + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "appId varchar(50)," +
            "templateName varchar(100)," +
            "templateId varchar(100),"
            + "templateAmount INTEGER)";


    String CREATE_ESTIMATED_ITEMS_TABLE = "CREATE TABLE " + ESTIMATED_ITEMS_TABLE + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "itemId varchar(50)," +
            "itemName varchar(100),"
            + "itemAmount INTEGER,"
            + "templateId varchar(50),"
            + "priceListId INTEGER,"
            + "priceListName varchar(100),"
            + "warehouseId INTEGER,"
            + "warehouseName varchar(100),"
            + "appId varchar(50)," +
            "templateAmount INTEGER)";


    String CREATE_PRICE_LIST_TABLE = "CREATE TABLE " + PRICE_LIST_TABLE + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "priceListId varchar(50)," +
            "priceListName varchar(250))";

    String CREATE_WAREHOUSE = "CREATE TABLE " + WAREHOUSE + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "orgName varchar(50)," +
            "orgId varchar(100))";

    String CREATE_NOTELOOKUP = "CREATE TABLE " + NOTE_LOOK_UP + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name varchar(100)," +
            "code varchar(10))";

    String CREATE_ATTACHMENT_TYPE = "CREATE TABLE " + ATTACHMENT_TYPE_TABLE + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "text varchar(100)," +
            "code varchar(10))";


    String CREATE_PROJECT_TYPE = "CREATE TABLE " + PROJECT_TYPE + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "typeName varchar(50)," +
            "typeId varchar(100))";

    String CREATE_TEMPLATES_TABLE = "CREATE TABLE " + TEMPLATES_TABLE + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "templateId varchar(10)," +
            "templateName varchar(100)," +
            "templateDesc varchar(20))";


    String CREATE_IMAGES_TABLE = "CREATE TABLE " + IMAGES_TABLE + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "file  varchar(500)," +
            "username varchar(100)," +
            "appRowId varchar(20)," +
            "filename varchar(20)," +
            "attachmentTypeText varchar(100)," +
            "attachmentTypeCode varchar(10))";


    public static String DB_FILEPATH = "/data/data/" + GeneralFunctions.PACKAGE_NAME + "/databases/" + DATABASE_NAME;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //excute create
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_APPLICATIONS_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);
        db.execSQL(CREATE_PRICE_LIST_TABLE);
        db.execSQL(CREATE_WAREHOUSE);
        db.execSQL(CREATE_PROJECT_TYPE);
        db.execSQL(CREATE_TEMPLATES_TABLE);
        db.execSQL(CREATE_ESTIMATED_ITEMS_TABLE);
        db.execSQL(CREATE_ESTIMATED_TEMPLATES_TABLE);
        db.execSQL(CREATE_NOTELOOKUP);
        db.execSQL(CREATE_ATTACHMENT_TYPE);
        db.execSQL(CREATE_IMAGES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + APPLICATIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ITEMS_TABLE);


        // Create tables again
        onCreate(db);
    }


    // code to add the new user
    public boolean addUser(User user) {
        boolean isInserted = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            //values.put("id", user.getId());
            values.put("username", user.getUsername());
            values.put("password", user.getPassword());
            values.put("token", user.getToken());
            values.put("fullName", user.getFullName());
            values.put("emp_id", user.getEmployeeNo());
            values.put("saftey_switch", user.getSafetySwitch());


            // Inserting Row
            isInserted = db.insert(USERS_TABLE, null, values) > 0 ? true : false;
            Log.d("addUser", "Is user inserted " + isInserted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isInserted;

    }

    // code to get the single user
    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = new User();
        Cursor cursor = db.query(USERS_TABLE, new String[]{"id",
                        "username", "fullName", "token", "emp_id", "saftey_switch"}, "username=?",
                new String[]{username}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();


            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setFullName(cursor.getString(2));
            user.setToken(cursor.getString(3));
            user.setEmployeeNo(cursor.getString(4));
            user.setSafetySwitch(cursor.getString(5));
        }
        // return user
        return user;
    }

    public User checkUserAccess(String username, String password) {
        User user = new User();
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE username =  " + username + " AND password = " + password;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mcursor = db.rawQuery(query, null);
        if (mcursor != null) {
            mcursor.moveToFirst();

            user.setId(mcursor.getInt(0));
            user.setUsername(mcursor.getString(1));
            user.setFullName(mcursor.getString(2));

        }
        return user;


    }

    // Check user in database offline
    public boolean checkUserOffline(String username, String password) {
        /*User user = new User();
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE username =  '" + username + "' AND password = '" + password + "'";
        SQLiteDatabase db = this.getWritableDatabase();
      *//*  try{

        }catch (Exception e){
            String error = e.toString();
        }*//*
        Cursor mcursor = db.rawQuery(query, null);
        Log.d("Cursor string", "checkUserOffline: " + mcursor.getInt(0));
        if (mcursor != null) {

            return true;
           *//* mcursor.moveToFirst();

            user.setId(mcursor.getInt(0));
            user.setUsername(mcursor.getString(1));
            user.setFullName(mcursor.getString(2));*//*

        }
        return false;
*/
        boolean isExist = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = null;
            String query = "SELECT * FROM " + USERS_TABLE + " WHERE username =  '" + username + "' AND password = '" + password + "'";
            cursor = db.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                isExist = true;
            } else {
                isExist = false;
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isExist;


    }

    // code to get all users in a list view
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + USERS_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setFullName(cursor.getString(1));
                user.setUsername(cursor.getString(2));
                user.setPassword(cursor.getString(3));
                user.setToken(cursor.getString(4));

                // Adding user to list

                usersList.add(user);
            } while (cursor.moveToNext());
        }

        // return users list
        return usersList;
    }

    // code to update the single user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("fullName", user.getFullName());
        values.put("token", user.getToken());

        // updating row
        return db.update(USERS_TABLE, values, "id = ?",
                new String[]{String.valueOf(user.getId())});
    }

    // Deleting single contact
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USERS_TABLE, "id = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    // Getting contacts Count
    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + USERS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    //insert new application
    public boolean insertNewApplication(ApplicationDetails app) {
        boolean isInserted = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("appId", app.getAppID());
            values.put("customerID", app.getCustomerID());
            // Log.d("tag1",":"+app.getCustomerName());
            values.put("customerName", app.getCustomerName());
            values.put("customerAddress", app.getCustomerAddress());
            values.put("appDate", app.getAppDate());
            values.put("appType", app.getAppType());
            values.put("status", app.getStatus());
            //  Log.d("tag1",":"+app.getBranch());
            values.put("branch", app.getBranch());
            values.put("sbranch", app.getsBranch());
            values.put("username", app.getUsername());
            values.put("location", app.getLocation());
            values.put("isSync", app.getIsSync());
            Log.d("fuckig task_status  : ", app.getRowId());
            values.put("phone", app.getPhone());
            values.put("task_status", app.getTicketStatus()); // N : new , P: pending D: done
            values.put("rowId", app.getRowId());
            values.put("prjRowId", app.getPrjRowId());
            values.put("phase1Meter", app.getPhase1Meter());
            values.put("phase3Meter", app.getPhase3Meter());

            values.put("notes", app.getNotes());
            values.put("last_qty", app.getLast_qty());
            values.put("last_read_date", app.getLast_read_date());
            values.put("last_read", app.getLast_read());
            values.put("install_date", app.getInstall_date());
            values.put("meter_type", app.getMeter_type());
            values.put("noofservices", app.getNoofservices());
            values.put("to_user_id", app.getTo_user_id());
            values.put("service_class", app.getService_class());
            values.put("service_no", app.getService_no());
            values.put("property_type", app.getProperty_type());
            values.put("no_of_phase", app.getNo_of_phase());
            values.put("usage_type", app.getUsage_type());
            values.put("service_status", app.getService_status());
            values.put("status_note", app.getStatus_note());
            values.put("status_code", app.getStatus_code());
            values.put("appl_type_code", app.getAppl_type_code());
            values.put("account_no", app.getAccount_no());
            values.put("id_number", app.getId_number());
            values.put("old_id_number", app.getOld_id_number());
            values.put("old_customer_name", app.getOld_customer_name());
            values.put("old_system_no", app.getOld_system_no());
            values.put("meter_no", app.getMeter_no());
            values.put("note", app.getNotes());
            values.put("noteLookUp", app.getNoteLookUp());
            values.put("sync",app.getSync());


            // Inserting Row
            isInserted = db.insert(APPLICATIONS_TABLE, null, values) > 0 ? true : false;
            Log.d("insertNewApplication", "Is application inserted " + isInserted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isInserted;
    }

    //update new application
    public boolean updateNewApplication(ApplicationDetails app,String appStatus) {
        boolean isInserted = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("appId", app.getAppID());
            values.put("customerID", app.getCustomerID());
            // Log.d("tag1",":"+app.getCustomerName());
            values.put("customerName", app.getCustomerName());
            values.put("customerAddress", app.getCustomerAddress());
            values.put("appDate", app.getAppDate());
            values.put("appType", app.getAppType());
            values.put("status", app.getStatus());
            //  Log.d("tag1",":"+app.getBranch());
            values.put("branch", app.getBranch());
            values.put("sbranch", app.getsBranch());
            values.put("username", app.getUsername());
            values.put("location", app.getLocation());
            values.put("isSync", app.getIsSync());
            Log.d("fuckig task_status  : ", app.getRowId());
            values.put("phone", app.getPhone());
            values.put("task_status", appStatus); // N : new , P: pending D: done  //app.getTicketStatus()
            values.put("rowId", app.getRowId());
            values.put("prjRowId", app.getPrjRowId());
            values.put("phase1Meter", app.getPhase1Meter());
            values.put("phase3Meter", app.getPhase3Meter());

            values.put("notes", app.getNotes());
            values.put("last_qty", app.getLast_qty());
            values.put("last_read_date", app.getLast_read_date());
            values.put("last_read", app.getLast_read());
            values.put("install_date", app.getInstall_date());
            values.put("meter_type", app.getMeter_type());
            values.put("noofservices", app.getNoofservices());
            values.put("to_user_id", app.getTo_user_id());
            values.put("service_class", app.getService_class());
            values.put("service_no", app.getService_no());
            values.put("property_type", app.getProperty_type());
            values.put("no_of_phase", app.getNo_of_phase());
            values.put("usage_type", app.getUsage_type());
            values.put("service_status", app.getService_status());
            values.put("status_note", app.getStatus_note());
            values.put("status_code", app.getStatus_code());
            values.put("appl_type_code", app.getAppl_type_code());
            values.put("account_no", app.getAccount_no());
            values.put("id_number", app.getId_number());
            values.put("old_id_number", app.getOld_id_number());
            values.put("old_customer_name", app.getOld_customer_name());
            values.put("old_system_no", app.getOld_system_no());
            values.put("meter_no", app.getMeter_no());
            values.put("note", app.getNotes());
            values.put("noteLookUp", app.getNoteLookUp());
            values.put("sync",app.getSync());


            // Inserting Row
            isInserted = db.update(APPLICATIONS_TABLE, values, "appId = " + app.getAppID(), null) > 0 ? true : false;
            Log.d("updateNewApplication", "Is application updated " + isInserted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isInserted;
    }

    public void deleteِApplication(String appId) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + APPLICATIONS_TABLE + " WHERE appId =  " + appId);
        db.close();
    }


    public void deleteAllRows(String table) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + table); //delete all rows in a table
        db.close();
    }


    public boolean insertNewTemplate(Template app) {
        boolean isInserted = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("templateId", app.getTemplateId());
            values.put("templateName", app.getTemplateName());
            values.put("templateDesc", app.getTemplateDesc());


            Log.d("insertNewTemplate", "template : " + app.getTemplateName());

            // Inserting Row
            isInserted = db.insert(TEMPLATES_TABLE, null, values) > 0 ? true : false;
            Log.d("insertNewTemplate", "Is template inserted " + isInserted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isInserted;
    }

    //update the status of appikcation after submit
    public boolean updateApplicationStatus(String appId, String status,String sync) {
        boolean isUpdated = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("task_status", status);
            values.put("sync", sync);

            // Inserting Row
            isUpdated = db.update(APPLICATIONS_TABLE, values, "appId='" + appId + "'", null) > 0 ? true : false;
            Log.d("insertNewApplication", "Is application inserted " + isUpdated);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.d("updateApplicationStatus", ":" + isUpdated);
        return isUpdated;

    }


//    //update the amoint of estimated item amount
//    public boolean updateItem(String itemId, int amount, String appId) {
//        boolean isUpdated = false;
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put("itemAmount", amount);
//
//            // Inserting Row
//            isUpdated = db.update(ESTIMATED_ITEMS_TABLE, values, "itemId='" + itemId + "' AND appId = '" + appId + "'", null) > 0 ? true : false;
//            Log.d("updateItemTable", "updateItemTable " + isUpdated);
//            //2nd argument is String containing nullColumnHack
//            db.close(); // Closing database connection
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return isUpdated;
//    }

    //update the amoint of estimated item amount
    public boolean updateItem(String itemId, String appId ,String field,String value) {
        boolean isUpdated = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(field, value);

            // Inserting Row
            isUpdated = db.update(ESTIMATED_ITEMS_TABLE, values, "itemId= '" + itemId + "' AND appId = '" + appId + "'", null) > 0 ? true : false;

            Log.d("updateItemTable", "updateItemTable " + isUpdated);

            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return isUpdated;
    }


    //get apps from tables
    public ArrayList<ApplicationDetails> showApplications() {
        ArrayList<ApplicationDetails> applicationDetailsArrayList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + APPLICATIONS_TABLE;
        Log.d("getApplications", ": " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Log.d("getApplications",": "+cursor.moveToNext());
        // looping through all rows and adding to list
        if (cursor.moveToNext()) {
            do {
                ApplicationDetails app = new ApplicationDetails();

                app.setAppID(cursor.getString(1));
                app.setCustomerID(cursor.getString(2));
                app.setCustomerName(cursor.getString(3));
                Log.d("getApplications", ": " + cursor.getString(3));
                app.setCustomerAddress(cursor.getString(4));
                app.setAppDate(cursor.getString(5));
                app.setAppType(cursor.getString(6));
                app.setBranch(cursor.getString(7));
                app.setsBranch(cursor.getString(8));
                app.setUsername(cursor.getString(9));
                app.setLocation(cursor.getString(10));
                app.setStatus(cursor.getString(11));
                app.setIsSync(cursor.getInt(12));
                app.setPhone(cursor.getString(13));
                //  Log.d("task",cursor.getString(14));
                app.setTicketStatus(cursor.getString(14));
                app.setRowId(cursor.getString(15));
                app.setPrjRowId(cursor.getString(16));
                app.setPhase1Meter(cursor.getString(17));
                app.setPhase1Meter(cursor.getString(18));


                Log.d("showApplications", "app name : " + app.getCustomerName() + " , app status :" + app.getTicketStatus());
                // Adding user to list
                applicationDetailsArrayList.add(app);

            } while (cursor.moveToNext());
        }


        // return users list
        return applicationDetailsArrayList;
    }


    //get apps from tables
    public ArrayList<ApplicationDetails> getApplications(String appId, String status, String userName) {
        ArrayList<ApplicationDetails> applicationDetailsArrayList = new ArrayList<>();

        String whereCondition = " WHERE task_status = '" + status + "' AND username = '" + userName.toUpperCase() + "'";
        //  String whereCondition="";
        String where = "";
        if (appId != null && appId != "") {
            where += "and appId='" + appId + "' AND task_status = '" + status + "' AND username = '" + userName.toUpperCase() + "'";
        }

        if (where != "") {
            whereCondition = " WHERE " + where.substring(3);
        }


        // Select All Query
        String selectQuery = "SELECT * FROM " + APPLICATIONS_TABLE + " " + whereCondition;
        Log.d("getApplications", ": " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        //Log.d("getApplications",": "+cursor.moveToNext());
        // looping through all rows and adding to list
        if (cursor.moveToNext()) {
            do {
                ApplicationDetails app = new ApplicationDetails();

                app.setAppID(cursor.getString(1));
                app.setCustomerID(cursor.getString(2));
                app.setCustomerName(cursor.getString(3));
                Log.d("getApplications", ": " + cursor.getString(3));
                app.setCustomerAddress(cursor.getString(4));
                app.setAppDate(cursor.getString(5));
                app.setAppType(cursor.getString(6));
                app.setBranch(cursor.getString(7));
                app.setsBranch(cursor.getString(8));
                app.setUsername(cursor.getString(9));
                app.setLocation(cursor.getString(10));
                app.setStatus(cursor.getString(11));
                app.setIsSync(cursor.getInt(12));
                app.setPhone(cursor.getString(13));
                //  Log.d("task",cursor.getString(14));
                app.setTicketStatus(cursor.getString(14));
                app.setRowId(cursor.getString(15));
                app.setPrjRowId(cursor.getString(16));
                app.setPhase1Meter(cursor.getString(17));
                app.setPhase3Meter(cursor.getString(18));

                app.setOld_system_no(cursor.getString(19));
                app.setOld_customer_name(cursor.getString(20));
                app.setOld_id_number(cursor.getString(21));
                app.setId_number(cursor.getString(22));
                app.setAccount_no(cursor.getString(23));
                app.setAppl_type_code(cursor.getString(24));
                app.setStatus_code(cursor.getString(25));
                app.setStatus_note(cursor.getString(26));
                app.setService_status(cursor.getString(27));
                app.setUsage_type(cursor.getString(28));
                app.setNo_of_phase(cursor.getString(29));
                app.setService_no(cursor.getString(30));
                app.setProperty_type(cursor.getString(31));
                app.setService_class(cursor.getString(32));
                app.setTo_user_id(cursor.getString(33));
                app.setNoofservices(cursor.getString(34));
                app.setMeter_type(cursor.getString(35));
                app.setInstall_date(cursor.getString(36));
                app.setLast_read(cursor.getString(37));
                app.setLast_read_date(cursor.getString(38));
                app.setLast_qty(cursor.getString(39));
                app.setNotes(cursor.getString(40));
                app.setMeter_no(cursor.getString(41));
                app.setNotes(cursor.getString(42));
                app.setNoteLookUp(cursor.getString(43));
                app.setSync(cursor.getString(44));
                // Adding user to list
                applicationDetailsArrayList.add(app);

            } while (cursor.moveToNext());
        }

        // return users list
        return applicationDetailsArrayList;
    }


    //update the status of appikcation after submit
    public boolean submitEnclousers(String appId, String phase1, String phase3) {
        boolean isUpdated = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("phase1Meter", phase1);
            values.put("phase3Meter", phase3);

            // Inserting Row
            isUpdated = db.update(APPLICATIONS_TABLE, values, "appId='" + appId + "'", null) > 0 ? true : false;
            Log.d("submitEnclousers", "Is submitEnclousers " + isUpdated);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Log.d("updateApplicationEnclousers",":"+isUpdated);
        return isUpdated;

    }

    //update the status of appillication after submit
    public boolean submitNote(String appId, String note) {
        boolean isUpdated = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("note", note);

            // Inserting Row
            isUpdated = db.update(APPLICATIONS_TABLE, values, "appId='" + appId + "'", null) > 0 ? true : false;
            Log.d("submitNote", "Is submitNote " + isUpdated);

            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return isUpdated;

    }


    //Ammar --> get priceList from table
    public ArrayList<PriceList> getPriceList() {
        ArrayList<PriceList> priceListArrayList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + PRICE_LIST_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //set priceList properties
                PriceList priceList = new PriceList();

                priceList.setPriceListId(cursor.getString(1));
                priceList.setPriceListName(cursor.getString(2));

                // Adding priceList to list
                priceListArrayList.add(priceList);

            } while (cursor.moveToNext());
        }

        // return priceList list
        return priceListArrayList;
    }


    //Ammar --> get warehouse from table
    public ArrayList<Warehouse> getWarehouse() {
        ArrayList<Warehouse> warehouseArrayList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + WAREHOUSE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //set priceList properties
                Warehouse warehouse = new Warehouse();

                warehouse.setWarehouseId(cursor.getString(2));
                warehouse.setWarehouseName(cursor.getString(1));

                // Adding priceList to list
                warehouseArrayList.add(warehouse);

            } while (cursor.moveToNext());
        }

        // return warehouse list
        return warehouseArrayList;
    }

    public ArrayList<NoteLookUp> getNoteLookUps() {
        ArrayList<NoteLookUp> noteLookUpsArrayList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + NOTE_LOOK_UP;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //set priceList properties
                NoteLookUp noteLookUp = new NoteLookUp();

                noteLookUp.setName(cursor.getString(1));
                noteLookUp.setCode(cursor.getString(2));

                // Adding priceList to list
                noteLookUpsArrayList.add(noteLookUp);

            } while (cursor.moveToNext());
        }

        // return warehouse list
        return noteLookUpsArrayList;
    }


    public ArrayList<AttchmentType> getAttchmentType() {
        ArrayList<AttchmentType> attchmentTypeArrayList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + ATTACHMENT_TYPE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //set priceList properties
                AttchmentType attchmentType = new AttchmentType();

                attchmentType.setText(cursor.getString(1));
                attchmentType.setCode(cursor.getString(2));

                // Adding priceList to list
                attchmentTypeArrayList.add(attchmentType);

            } while (cursor.moveToNext());
        }

        // return warehouse list
        return attchmentTypeArrayList;
    }


    //Ammar --> get projectType from table
    public ArrayList<ProjectType> getProjectType() {
        ArrayList<ProjectType> projectTypeArrayList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + PROJECT_TYPE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //set priceList properties
                ProjectType projectType = new ProjectType();

                projectType.setProjectTypeId(cursor.getString(2));
                projectType.setProjectTypeName(cursor.getString(1));

                // Adding priceList to list
                projectTypeArrayList.add(projectType);

            } while (cursor.moveToNext());
        }

        // return projectType list
        return projectTypeArrayList;
    }

    public ArrayList<Template> getTemplates(String appId) {
        ArrayList<Template> templatesArrayList = new ArrayList<>();

        String whereCondition = "";
        String where = "";
        if (appId != null && appId != "") {
            where += "and appId='" + appId + "' ";
        }

        if (where != "") {
            whereCondition = "where " + where.substring(3);
        }

        // Select All Query
        String selectQuery = "SELECT * FROM " + TEMPLATES_TABLE + " " + whereCondition;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToNext()) {
            do {
                Template app = new Template();

                app.setTemplateId(cursor.getString(1));
                app.setTemplateName(cursor.getString(2));
                app.setTemplateDesc(cursor.getString(3));


                // Adding user to list
                templatesArrayList.add(app);

            } while (cursor.moveToNext());
        }

        // return users list
        return templatesArrayList;
    }

    public ArrayList<Template> getEstimatedTemplates(String appId) {
        Log.d("getEstimatedTemplates", "Done.");
        ArrayList<Template> templatesArrayList = new ArrayList<>();

//        String whereCondition = "";
//        String where = "";
//        if(appId!=null && appId!="")
//        {
//            where+="and appId='"+appId+"' ";
//        }
//
//        if(where!="")
//        {
//            whereCondition = "where "+where.substring(3);
//        }

        // Select All Query
        String selectQuery = "SELECT * FROM " + ESTIMATED_TEMPLATES_TABLE + " WHERE appId =  " + appId;
        Log.d("selectQuery", " : " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToNext()) {
            do {
                Template app = new Template();

                app.setAppId(cursor.getString(1));
                app.setTemplateName(cursor.getString(2));
                app.setTemplateId(cursor.getString(3));
                app.setTemplateAmount(cursor.getInt(4));


                // Adding user to list
                templatesArrayList.add(app);

            } while (cursor.moveToNext());
        }

        // return users list
        return templatesArrayList;
    }


    public void deleteEstimatedTemplate(String templateId) {
        Log.d("getEstimatedTemplates", "Done.");
        ArrayList<Template> templatesArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ESTIMATED_TEMPLATES_TABLE + " WHERE templateId =  " + templateId);
        db.execSQL("DELETE FROM " + ESTIMATED_ITEMS_TABLE + " WHERE templateId =  " + templateId);
        db.close();
    }


    //get items from table
    public void showItems(String templateId) {
        ArrayList<Item> itemArrayList = new ArrayList<>();


        // Select All Query
        String selectQuery = "SELECT  * FROM " + ITEMS_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //set item properities
                Item app = new Item();
                app.setId(cursor.getString(1));
                app.setItemName(cursor.getString(2));
                app.setItemAmount(cursor.getInt(3));
                app.setTemplateId(cursor.getString(4));
                app.setInventoryItemCode(cursor.getString(5));
                app.setAllowDelete(cursor.getString(6));
                app.setAllowEdit(cursor.getString(7));
//                app.setCategory(cursor.getString(4));

                // Adding user to list
                itemArrayList.add(app);

            } while (cursor.moveToNext());
        }

        // return items list
        Log.d("itemArrayList", ": " + itemArrayList);
        // return itemArrayList;
    }


    //get items from table
    public ArrayList<Item> getItems(String templateId) {
        ArrayList<Item> itemArrayList = new ArrayList<>();


        // Select All Query
        String selectQuery = "SELECT  * FROM " + ITEMS_TABLE + " WHERE templateId = " + templateId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //set item properities
                Item app = new Item();
                app.setId(cursor.getString(1));
                app.setItemName(cursor.getString(2));
                app.setItemAmount(cursor.getInt(3));
                app.setTemplateId(cursor.getString(4));
                app.setInventoryItemCode(cursor.getString(5));
                app.setAllowDelete(cursor.getString(6));
                app.setAllowEdit(cursor.getString(7));
//                app.setCategory(cursor.getString(4));

                // Adding user to list
                itemArrayList.add(app);

            } while (cursor.moveToNext());
        }

        // return items list
        return itemArrayList;
    }

    //get items from table
    public ArrayList<Item> getEstimatedItems(String templateId, String appId) {
        ArrayList<Item> itemArrayList = new ArrayList<>();
        String selectQuery = "";
        // Select All Query
        if (templateId == null)
            selectQuery = "SELECT  * FROM " + ESTIMATED_ITEMS_TABLE + " WHERE appId =  " + appId;
        else
            selectQuery = "SELECT  * FROM " + ESTIMATED_ITEMS_TABLE + " WHERE templateId = " + templateId + " AND appId = " + appId;
        Log.d("getEstimatedItems", ":" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("getEstimatedItems", ": cursor : " + cursor.moveToFirst());
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //set item properities
                Item app = new Item();
                app.setId(cursor.getString(1));
                app.setItemName(cursor.getString(2));
                app.setItemAmount(cursor.getInt(3));
                app.setTemplateId(cursor.getString(4));
                app.setPricList(new PriceList(cursor.getString(5), cursor.getString(6)));
                app.setWarehouse(new Warehouse(cursor.getString(7), cursor.getString(8)));
                app.setAppId(cursor.getString(9));
                app.setTemplateAmount(cursor.getInt(10));
                // Adding user to list
                itemArrayList.add(app);
                Log.d("getEstimatedItems", ":" + app.getItemName());
            } while (cursor.moveToNext());
        }


        // return items list
        return itemArrayList;
    }


    //insert new item
    public boolean insertItem(Item item) {
        boolean isInserted = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("itemId", item.getId());
            values.put("itemName", item.getItemName());
            values.put("itemAmount", item.getItemAmount());
            values.put("templateId", item.getTemplateId());
            values.put("inventoryItemCode", item.getInventoryItemCode());
            values.put("allowDelete", item.getAllowDelete());
            values.put("allowEdit", item.getAllowEdit());


            // Inserting Row
            isInserted = db.insert(ITEMS_TABLE, null, values) > 0 ? true : false;

            Log.d("addItem", "Is item inserted " + isInserted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isInserted;
    }


    //insert new item
    public boolean addImage(Image image) {
        boolean isInserted = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("file", image.getFile());
            values.put("username", image.getUsername());
            values.put("appRowId", image.getAppRowId());
            values.put("filename", image.getFileName());
            values.put("attachmentTypeText", image.getAttachmentType().getText());
            values.put("attachmentTypeCode", image.getAttachmentType().getCode());


            // Inserting Row
            isInserted = db.insert(IMAGES_TABLE, null, values) > 0 ? true : false;

            Log.d("addItem", "Is item inserted " + isInserted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isInserted;
    }


    //delete image
    public boolean deleteImage(String imageName) {
        boolean isDeleted = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            // update Row
            isDeleted = db.delete(IMAGES_TABLE, " filename = '" + imageName + "'", null) > 0 ? true : false;
            Log.d("addesTIMATEDItem", "Is item delete " + isDeleted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isDeleted;
    }


    public ArrayList<Image> getImages(String appId) {
        ArrayList<Image> imagesArrayList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + IMAGES_TABLE + " WHERE appId = '" + appId + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //set priceList properties
                Image image = new Image();
                AttchmentType attchmentType = new AttchmentType(cursor.getString(5), cursor.getString(6));

                image.setUsername(cursor.getString(1));
                image.setFileName(cursor.getString(2));
                image.setFile(cursor.getString(3));
                image.setAppRowId(cursor.getString(4));
                image.setAttachmentType(attchmentType);


                // Adding priceList to list
                imagesArrayList.add(image);

            } while (cursor.moveToNext());
        }

        // return warehouse list
        return imagesArrayList;
    }


    public Image getImage(String imageName) {
        Image image = new Image();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + IMAGES_TABLE + " WHERE filename = '" + imageName + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {


                AttchmentType attchmentType = new AttchmentType(cursor.getString(5), cursor.getString(6));

                image.setFile(cursor.getString(1));
                image.setUsername(cursor.getString(2));
                image.setAppRowId(cursor.getString(3));
                image.setFileName(cursor.getString(4));
                image.setAttachmentType(attchmentType);


            } while (cursor.moveToNext());
        }

        // return warehouse list
        return image;
    }


    //insert new estimated item
    public boolean insertEstimatedItem(Item item, boolean templateItem, String appId) {
        boolean isInserted = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            if (templateItem)
                values.put("itemId", item.getId());
            else
                values.put("itemId", item.getInventoryItemCode());
            values.put("itemName", item.getItemName());
            values.put("itemAmount", item.getItemAmount());
            values.put("templateId", item.getTemplateId());
            values.put("priceListId", item.getPricList().getPriceListId());
            values.put("priceListName", item.getPricList().getPriceListName());
            values.put("warehouseId", item.getWarehouse().getWarehouseId());
            values.put("warehouseName", item.getWarehouse().getWarehouseName());
            values.put("appId", appId);
            values.put("templateAmount", item.getTemplateAmount());


            // Inserting Row
            isInserted = db.insert(ESTIMATED_ITEMS_TABLE, null, values) > 0 ? true : false;

            Log.d("addesTIMATEDItem", "Is item inserted " + isInserted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isInserted;
    }

    //insert new estimated item
    public boolean updateEstimatedItem(Item item, boolean templateItem, String appId) {
        boolean isUpdated = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            if (templateItem)
                values.put("itemId", item.getId());
            else
                values.put("itemId", item.getInventoryItemCode());

            values.put("itemName", item.getItemName());
            values.put("itemAmount", item.getItemAmount());
            values.put("templateId", item.getTemplateId());

            // update Row
            isUpdated = db.update(ESTIMATED_ITEMS_TABLE, values, " templateId= '" + item.getTemplateId() + "'" + " and itemId =  '" + item.getId() + "' AND appId = '" + appId + "'", null) > 0 ? true : false;//add the application id in the future


            Log.d("addesTIMATEDItem", "Is item update " + isUpdated);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isUpdated;
    }

    //insert new estimated item
    public boolean deleteEstimatedItem(Item item, String appId) {
        boolean isDeleted = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            // update Row
            isDeleted = db.delete(ESTIMATED_ITEMS_TABLE, " templateId= '" + item.getTemplateId() + "'" + " and itemId =  '" + item.getId() + "' AND appId = '" + appId + "'", null) > 0 ? true : false;//add the application id in the future
            Log.d("addesTIMATEDItem", "Is item delete " + isDeleted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isDeleted;
    }

    //insert new estimated template
    public boolean insertEstimatedTemplate(Template template, String appId) {
        Log.d("insertEstimatedTemplate", "Done.");
        boolean isInserted = false;
        try {


            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("appId", appId);
            values.put("templateName", template.getTemplateName());
            values.put("templateId", template.getTemplateId());
            values.put("templateAmount", template.getTemplateAmount());

            // Inserting Row
            isInserted = db.insert(ESTIMATED_TEMPLATES_TABLE, null, values) > 0 ? true : false;

            Log.d("addesTIMATEDItem", "Is item inserted " + isInserted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isInserted;
    }

    //update estimated template
    public boolean upadteEstimatedTemplate(Template template, String appId) {
        Log.d("upadteEstimatedTemplate", "Done.");
        boolean isUpdated = false;
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("appId", appId);
            values.put("templateName", template.getTemplateName());
            values.put("templateId", template.getTemplateId());
            values.put("templateAmount", template.getTemplateAmount());
            Log.d("upadteEstimatedTemplate", ": " + template.getTemplateAmount());

            isUpdated = db.update(ESTIMATED_TEMPLATES_TABLE, values, "templateId= '" + template.getTemplateId() + "'  AND appId = '" + appId + "'", null) > 0 ? true : false;


            Log.d("addesTIMATEDItem", "Is item updated " + isUpdated);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isUpdated;
    }

    //Ammar -->  code to add the new priceList
    public boolean insertNewPriceList(PriceList priceList) {
        boolean isInserted = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            //values.put("id", user.getId());
            values.put("priceListId", priceList.getPriceListId());
            values.put("priceListName", priceList.getPriceListName());

            // Inserting Row
            isInserted = db.insert(PRICE_LIST_TABLE, null, values) > 0 ? true : false;
            Log.d("insertNewPriceList", "Is user inserted " + isInserted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isInserted;

    }


    //Ammar -->  code to add the new warehouse
    public boolean insertNewWarehouse(Warehouse warehouse) {
        boolean isInserted = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            //values.put("id", user.getId());
            values.put("orgId", warehouse.getWarehouseId());
            values.put("orgName", warehouse.getWarehouseName());

            // Inserting Row
            isInserted = db.insert(WAREHOUSE, null, values) > 0 ? true : false;
            Log.d("insertNewWarehouse", "Is warehouse inserted " + isInserted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isInserted;

    }

    public boolean insertNewNoteLookUp(NoteLookUp noteLookUp) {
        boolean isInserted = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            //values.put("id", user.getId());
            values.put("name", noteLookUp.getName());
            values.put("code", noteLookUp.getCode());

            // Inserting Row
            isInserted = db.insert(NOTE_LOOK_UP, null, values) > 0 ? true : false;
            Log.d("insertNewNoteLookUp", "Is NoteLookUp inserted " + isInserted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isInserted;

    }


    public boolean insertNewAttchmentType(AttchmentType attchmentType) {
        boolean isInserted = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            //values.put("id", user.getId());
            values.put("text", attchmentType.getText());
            values.put("code", attchmentType.getCode());

            // Inserting Row
            isInserted = db.insert(ATTACHMENT_TYPE_TABLE, null, values) > 0 ? true : false;
            Log.d("insertAttchmentType", "Is attchmentType inserted " + isInserted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isInserted;

    }


    //Ammar -->  code to add the new projectType
    public boolean insertNewProjectType(ProjectType projectType) {
        boolean isInserted = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            //values.put("id", user.getId());
            values.put("typeId", projectType.getProjectTypeId());
            values.put("typeName", projectType.getProjectTypeName());

            // Inserting Row
            isInserted = db.insert(PROJECT_TYPE, null, values) > 0 ? true : false;
            Log.d("insertNewProjectType", "Is projectType inserted " + isInserted);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isInserted;

    }

    //check item is exist in the table
    public boolean isItemExist(String tableName, String fieldName, String fieldValue) {
        boolean isExist = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = null;
            String sql = "SELECT * FROM " + tableName + " WHERE " + fieldName + "='" + fieldValue + "'";
            cursor = db.rawQuery(sql, null);
            Log.d("isItemExist", "Item in " + tableName + " is " + (cursor.getCount() > 0 ? "Exist" : "Not exist"));

            if (cursor.getCount() > 0) {
                isExist = true;
            } else {
                isExist = false;
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isExist;
    }



    //check item is exist in the table
    public boolean checkAppDone(String appId) {
        boolean isExist = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = null;
            String sql = "SELECT * FROM " + APPLICATIONS_TABLE + " WHERE appId = '" +appId+"' AND task_status = 'D'";
            cursor = db.rawQuery(sql, null);



            if (cursor.getCount() > 0) {
                isExist = true;
            } else {
                isExist = false;
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isExist;
    }

    //check item is exist in the table
    public boolean checkAppSync(String appId) {
        boolean isExist = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = null;
            String sql = "SELECT * FROM " + APPLICATIONS_TABLE + " WHERE appId = '" +appId+"' AND sync = '1'";
            cursor = db.rawQuery(sql, null);



            if (cursor.getCount() > 0) {
                isExist = true;
            } else {
                isExist = false;
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isExist;
    }

    //check item of the template is exist in the table
    public boolean isItemAndTemplateExist(String itemId, String templateId) {
        boolean isExist = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = null;
            String sql = "SELECT * FROM " + ITEMS_TABLE + " WHERE itemId = '" + itemId + "' AND templateId = '" + templateId + "'";
            cursor = db.rawQuery(sql, null);
            Log.d("isItemExistAndTemplate", "Item in " + ITEMS_TABLE + " is " + (cursor.getCount() > 0 ? "Exist" : "Not exist"));

            if (cursor.getCount() > 0) {
                isExist = true;
            } else {
                isExist = false;
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isExist;
    }


    //check template is exist in the table
    public boolean isTemplateExist(String tableName, String fieldName, String fieldValue, String appId) {
        boolean isExist = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = null;
            String sql = "SELECT * FROM " + tableName + " WHERE " + fieldName + "='" + fieldValue + "'" + " AND appId = '" + appId + "'";
            cursor = db.rawQuery(sql, null);
            Log.d("isItemExist", "Item in " + tableName + " is " + (cursor.getCount() > 0 ? "Exist" : "Not exist"));

            if (cursor.getCount() > 0) {
                isExist = true;
            } else {
                isExist = false;
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isExist;
    }


    //check item is exist in estimated
    public boolean isEstimatedItemExist(String tableName, String fieldName, String fieldValue, String appId, String templateId) {
        boolean isExist = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = null;
            String sql = "SELECT * FROM " + tableName + " WHERE " + fieldName + "='" + fieldValue + "'" + " AND templateId = '" + templateId + "' AND appId = '" + appId + "'";
            cursor = db.rawQuery(sql, null);
            Log.d("isItemExist", "Item in " + tableName + " is " + (cursor.getCount() > 0 ? "Exist" : "Not exist"));

            if (cursor.getCount() > 0) {
                isExist = true;
            } else {
                isExist = false;
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isExist;
    }


    public ArrayList<ApplicationDetails> getApplicationsBySearch(String appId, String searchText, String searchBy, String status) {
        ArrayList<ApplicationDetails> applicationDetailsArrayList = new ArrayList<>();

//        String whereCondition = "";
//        String where = "";
//        if(appId!=null && appId!="")
//        {
//            where+="and appId='"+appId+"' ";
//        }
//
//        if(where!="")
//        {
//            whereCondition = "where "+where.substring(3);
//        }
        String selectQuery = "";
        String tag = "gopro : ";
        //  Log.d(tag,searchBy);
        if (searchBy == "byAppID") {
            // Select All Query
            selectQuery = "SELECT * FROM " + APPLICATIONS_TABLE + " WHERE  appId LIKE  '%" + searchText + "%' AND task_status = '" + status + "'";
            Log.d(tag, selectQuery);
        } else if (searchBy == "byName") {
            selectQuery = "SELECT * FROM " + APPLICATIONS_TABLE + " WHERE  customerName LIKE '%" + searchText + "%' AND task_status = '" + status + "'";
            Log.d(tag, selectQuery);
        } else {
            selectQuery = "SELECT * FROM " + APPLICATIONS_TABLE + " WHERE task_status = '" + status + "'";
            Log.d(tag, selectQuery);
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToNext()) {
            do {
                ApplicationDetails app = new ApplicationDetails();
                Log.d("fuck2", ":" + cursor.getString(13));
                app.setAppID(cursor.getString(1));
                app.setCustomerID(cursor.getString(2));
                app.setCustomerName(cursor.getString(3));
                app.setCustomerAddress(cursor.getString(4));
                app.setAppDate(cursor.getString(5));
                app.setAppType(cursor.getString(6));
                app.setBranch(cursor.getString(7));
                app.setsBranch(cursor.getString(8));
                app.setUsername(cursor.getString(9));
                app.setLocation(cursor.getString(10));
                app.setStatus(cursor.getString(11));
                app.setIsSync(cursor.getInt(12));
                app.setPhone(cursor.getString(13));
                app.setTicketStatus(cursor.getString(14));
                app.setRowId(cursor.getString(15));
                app.setPrjRowId(cursor.getString(16));
                app.setPhase1Meter(cursor.getString(17));
                app.setPhase3Meter(cursor.getString(18));
                app.setOld_system_no(cursor.getString(19));
                app.setOld_customer_name(cursor.getString(20));
                app.setOld_id_number(cursor.getString(21));
                app.setId_number(cursor.getString(22));
                app.setAccount_no(cursor.getString(23));
                app.setAppl_type_code(cursor.getString(24));
                app.setStatus_code(cursor.getString(25));
                app.setStatus_note(cursor.getString(26));
                app.setService_status(cursor.getString(27));
                app.setUsage_type(cursor.getString(28));
                app.setNo_of_phase(cursor.getString(29));
                app.setService_no(cursor.getString(30));
                app.setProperty_type(cursor.getString(31));
                app.setService_class(cursor.getString(32));
                app.setTo_user_id(cursor.getString(33));
                app.setNoofservices(cursor.getString(34));
                app.setMeter_type(cursor.getString(35));
                app.setInstall_date(cursor.getString(36));
                app.setLast_read(cursor.getString(37));
                app.setLast_read_date(cursor.getString(38));
                app.setLast_qty(cursor.getString(39));
                app.setNotes(cursor.getString(40));
                app.setMeter_no(cursor.getString(41));
                app.setNotes(cursor.getString(42));
                app.setNoteLookUp(cursor.getString(43));
                app.setSync(cursor.getString(44));
                // Adding user to list
                applicationDetailsArrayList.add(app);

            } while (cursor.moveToNext());
        }

        // return users list
        return applicationDetailsArrayList;
    }


    public Boolean tableIsEmpty(String table) {
        String count = "SELECT count(*) FROM " + table;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0) {
            Log.d("tableIsEmpty ", "" + table + " is not Empty");
            return false;
        } else {
            Log.d("tableIsEmpty ", "" + table + " is  Empty");
            return true;
        }

    }



    public Boolean tableItemsOfTemplatesIsEmpty(String templateId) {
        String count = "SELECT count(*) FROM items WHERE templateId = " + "'" + templateId + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0) {
            Log.d("tableIsEmpty ", "items is not Empty");
            return false;
        } else {
            Log.d("tableIsEmpty ", "items is  Empty");
            return true;
        }

    }


    public static void backupDatabase() {

        try {
            //Open your local db as the input stream
            File dbFile = new File(DB_FILEPATH);
            FileInputStream fis = new FileInputStream(dbFile);

            String backUpPath = GeneralFunctions.getMediaPath("dbBackup");
            Log.d("backupDatabase", backUpPath);
            //create new backup folder if not exists
            File directory = new File(backUpPath);
            //if path not exist create new directory
            if (!directory.exists()) {
                Log.d("backupDatabase", "Backup path is exist");
                directory.mkdirs();
            } else {
                Log.d("backupDatabase", "Backup path is not exist");
            }

            Log.d("backupDatabase", "Backup path is (" + backUpPath + File.separator + "backup.db" + ")");

            //Open the empty db as the output stream
            OutputStream output = new FileOutputStream(backUpPath + File.separator + "backup.db");
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            //Close the streams
            output.flush();
            output.close();
            fis.close();
        } catch (Exception ex) {
            Log.d("backupDatabase", ex.getMessage());
            ex.printStackTrace();
        }
    }


}