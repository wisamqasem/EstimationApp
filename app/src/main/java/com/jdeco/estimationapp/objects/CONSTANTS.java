package com.jdeco.estimationapp.objects;

import android.os.Environment;

import java.io.File;

public class CONSTANTS {
    //API LINK

   public static final String API_LINK = "https://technicalteamsportal.jdeco.net/json/EstimationAPI.ashx";
   // public static final String API_LINK = "https://technicalteamsportal.jdeco.net/json/EstimationAPI.ashx?env=test";



    public static final String API_KEY = "03c54d15a55ad3e353a279b6d319aab4";
    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_APPLICATIONS = "getAppList";
    public static final String ACTION_APPLICATIONS_GET_ITEMS = "getItems";
    public static final String ACTION_APPLICATIONS_SUBMIT_ITEMS = "processItems";
    public static final String ACTION_TEMPLATES_GET_ITEMS = "getTemplates";
    public static final String ACTION_GET_ITEMS = "getTemplateItems";
    public static final String ACTION_GET_PRICE_LIST = "getPriceList";
    public static final String ACTION_GET_PROJECT_TYPE = "getProjectTypes";
    public static final String ACTION_GET_WAREHOUSE = "getWarehouses";
    public static final String ACTION_processChangeName = "processChangeName";
    public static final String ACTION_GET_NOTE_LOOK_UPS = "getNotesLookup";
    public static final String ACTION_SUBMIT_NOTE = "processNotes";
    public static final String ACTION_GET_NOTES = "getAppNotes";
    public static final String ACTION_SUBMIT_Image = "uploadImage";
    public static final String ACTION_ATTACHMENT_TYPE = "getAttchmentTypes";
    public static final String ACTION_Application_Agreements = "getApplicationAgreements";
    public static final String ACTION_SERVICE_DETAILS = "get_agree_details";


 static File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "EstimationApp");
 public static final String IMAGE_PATH = mediaStorageDir.getPath();



 public static String getImagePath(String appDir) {
  return IMAGE_PATH + "/"+ appDir + "/" ;
 }






}
