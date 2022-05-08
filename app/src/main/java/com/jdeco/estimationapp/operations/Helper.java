package com.jdeco.estimationapp.operations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.Image;
import com.jdeco.estimationapp.ui.MainActivity;
import com.jdeco.estimationapp.ui.forms.OpenApplicationWaiver;

import java.io.ByteArrayOutputStream;

public class Helper {
    Context _context;
    Database dbObject;

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
                switch (which) {
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

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String base64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return base64;
    }

    public Bitmap fromBase64(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;
    }

    public void setImageFromDatabase(String imageName, ImageView image, TextView imageText, ImageView removeImageBtn) {
        dbObject = new Database(_context);
        Image imageFromDatabase = dbObject.getImage(imageName);
        image.setImageBitmap(getResizedBitmap(fromBase64(imageFromDatabase.getFile()), 125));
        imageText.setText(imageFromDatabase.getAttachmentType().toString());
        removeImageBtn.setVisibility(View.VISIBLE);
    }

    public void setImageFromDatabaseForDoneApplications(String imageName, ImageView image, TextView imageText) {
        dbObject = new Database(_context);
        Image imageFromDatabase = dbObject.getImage(imageName);
        image.setImageBitmap(fromBase64(imageFromDatabase.getFile()));
        imageText.setText(imageFromDatabase.getAttachmentType().toString());
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


}
