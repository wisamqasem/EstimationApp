package com.jdeco.estimationapp.operations;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.widget.Toast;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.ui.MainActivity;
import com.jdeco.estimationapp.ui.forms.ApplicationsList;

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
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setPositiveButton((context.getString(R.string.ok_lbl)),
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




    public static void startLoading(ProgressDialog progress){
        progress.setTitle("أرجاء الأنتظار");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    public static void stopLoading(ProgressDialog progress){
        progress.dismiss();
    }

}
