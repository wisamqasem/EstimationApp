package com.jdeco.estimationapp.operations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.widget.Toast;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.ui.MainActivity;

import java.io.File;

public class GeneralFunctions {

    //package name
    public static String PACKAGE_NAME = "com.jdeco.estimationapp";

    public static void populateMsg(Context context,String text,boolean isLong)
    {
        Toast.makeText(context,text,isLong ? Toast.LENGTH_LONG :Toast.LENGTH_SHORT).show();
    }

    public static void messageBox(Context context,String title ,String text )
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("لا يوجد أتصال");
        alertDialog.setMessage("أرجاء فحص الأتصال بلأنترنت , مع العلم أنا جميع البيانات ستبقى محفوفظة . ");
        alertDialog.setPositiveButton(("OK"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public static String getMediaPath(String targetedPath) {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)  + File.separator + targetedPath;
    }

}
