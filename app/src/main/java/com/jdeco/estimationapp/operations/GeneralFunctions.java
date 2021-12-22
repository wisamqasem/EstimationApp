package com.jdeco.estimationapp.operations;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

public class GeneralFunctions {

    //package name
    public static String PACKAGE_NAME = "com.jdeco.estimationapp";

    public static void populateMsg(Context context,String text,boolean isLong)
    {
        Toast.makeText(context,text,isLong ? Toast.LENGTH_LONG :Toast.LENGTH_SHORT).show();
    }

    public static String getMediaPath(String targetedPath) {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)  + File.separator + targetedPath;
    }

}
