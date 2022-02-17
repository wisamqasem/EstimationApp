package com.jdeco.estimationapp.operations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.ui.MainActivity;
import com.jdeco.estimationapp.ui.forms.OpenApplicationWaiver;

public class Helper {
    Context _context;

    public Helper(Context _context) {
        this._context = _context;
    }

    public boolean isInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }

    public void goBack(Class className) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Intent back = new Intent(_context, className);
                        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        _context.startActivity(back);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setMessage(_context.getResources().getString(R.string.confirmation)).setPositiveButton(_context.getResources().getString(R.string.yes_lbl), dialogClickListener)
                .setNegativeButton(_context.getResources().getString(R.string.no_lbl), dialogClickListener).show();
    }

}
