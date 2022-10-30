package com.jdeco.estimationapp.ui.screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.AttchmentType;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.objects.Image;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.GeneralFunctions;
import com.jdeco.estimationapp.operations.Helper;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.ui.forms.OpenApplicationDetails;
import com.jdeco.estimationapp.ui.forms.OpenApplicationWaiver;
import com.viethoa.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class uploadImageScreen extends AppCompatActivity {

    Button submitBtn ;
    EditText appLNo;

    ImageView image;

    Session session;

    ProgressDialog progress;

    String base64Image="";

    String appId ;

    boolean takeAnotherImage = false;

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int REQUEST_CAMERA_CODE = 100;//12;
    private static final String IMAGE_DIRECTORY_NAME = "EstimationImages";

    private Uri fileUri; // file url to store image/video

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("أعتماد صورة");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Add back arrow in action bar
        }

        // Remove keyboard focus when start activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        submitBtn = (Button)findViewById(R.id.submitBtn) ;
        appLNo = (EditText)findViewById(R.id.applNo) ;

        image = (ImageView)findViewById(R.id.image) ;

        session = new Session(this);

        appId=appLNo.getText().toString();

        progress = new ProgressDialog(uploadImageScreen.this);
        progress.setTitle(getResources().getString(R.string.please_wait));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);



        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage(appId);
            }
        });

submitBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        if (appLNo.getText().toString().isEmpty() || appLNo.getText().toString().equalsIgnoreCase(" ")){
            appLNo.requestFocus();
        appLNo.setError(getString(R.string.fill_field));


        }
        else {
            String path  = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appLNo.getText().toString() + ".jpg";
            if(takeAnotherImage)
                captureImage(appLNo.getText().toString());
            else
                submitImage(path,appId,"98");
        }


    }
});




    }
//});










   // }

    private void submitImage(String imagePath,String imageName ,String imageLookupsCode) {
        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;
        try {


            submitBtn.setText("التقاط صورة أخرى");
            takeAnotherImage= true;

            progress.show();
            //RequestQueue initialized
            mRequestQueue = Volley.newRequestQueue(this);

            Helper helper = new Helper(this);


            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 4;//8
            File pic = new File(imagePath);
            Log.d("submitImages","imagePath : "+imagePath);
            if (pic.exists()) {
                final Bitmap bitmap = BitmapFactory.decodeFile(imagePath,
                        options);
                base64Image = helper.toBase64(bitmap);

            }else { throw new Exception("صورة غير موجودة .");}

            //String Request initialized
            mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();//display the response submit success
                    Log.d("sumbitImage", "Response: " + response);
                    try {

                        JSONObject submitData = new JSONObject(response);
                        image.setBackground(ContextCompat.getDrawable(uploadImageScreen.this, R.drawable.upload_background));

                        //if the user try to upload again the app will show "u already uploaded"
//                if(imageFlag==1)image1Flag = 3;
//                else if(imageFlag==2)image2Flag = 3;
//                else if(imageFlag==3)image3Flag = 3;
//                else if(imageFlag==4)image4Flag = 3;
//                else if(imageFlag==5)image5Flag = 3;
//                else if(imageFlag==6)image6Flag = 3;


                        if (submitData.getString("message").equals("Created " + imageName)) {
                            //display the response submit success
                              Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG);
                               toast.setGravity(Gravity.CENTER, 0, 0);
                               toast.show();
                            //   image.setIsSync(1);
                            // dbObject.updateImageTable(image);
                            progress.dismiss();
                         //   Toast.makeText(uploadImageScreen.this, "تم أعتماد الصورة بنجاح", Toast.LENGTH_LONG).show();


                        } else {
                            progress.dismiss();
                            GeneralFunctions.messageBox(uploadImageScreen.this, getResources().getString(R.string.submit_failed), submitData.getString("message"));
                            // Toast.makeText(getApplicationContext(), submitData.getString("message"), Toast.LENGTH_LONG).show();//display the response submit failed
                        }
                    }catch (JSONException ex ){
                        progress.dismiss();
                        GeneralFunctions.messageBox(uploadImageScreen.this, getResources().getString(R.string.submit_failed), ex.toString());
                        ex.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    Log.d("getItemsFromServer", "Error Login Request :" + error.toString());
                    GeneralFunctions.messageBox(uploadImageScreen.this, getResources().getString(R.string.submit_failed), error.toString()+" , "+appLNo.getText().toString());
                    // Toast.makeText(getApplicationContext(), getString(R.string.submit_image_failed), Toast.LENGTH_LONG).show();
                }

            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    //parameters
                    params.put("apiKey", CONSTANTS.API_KEY);
                    params.put("action", CONSTANTS.ACTION_SUBMIT_Image);
                    params.put("file", base64Image); // base64
                    params.put("appRowId", appLNo.getText().toString());
                    params.put("filename", imageName);//filename
                    params.put("content_type", "image/jpeg");
                    params.put("appId", appLNo.getText().toString());

                    /////////////// parse to int
                    params.put("attachmentType", imageLookupsCode); // attachement type code
                    params.put("username", session.getValue("username"));

                    return params;
                }

            };

            mRequestQueue.add(mStringRequest);

        } catch (Exception e) {
            progress.dismiss();
            GeneralFunctions.messageBox(uploadImageScreen.this, getResources().getString(R.string.submit_failed)+" صور ", e.toString());
            e.printStackTrace();
        }

    }//end of fun


    /*
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private Uri captureImage(String name) {

        submitBtn.setText("أعتماد");
        takeAnotherImage= false;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri(MEDIA_TYPE_IMAGE, name));
        } else {
            File file = new File(getOutputMediaFileUri(MEDIA_TYPE_IMAGE, name).getPath());
            Uri fileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }


        /**
         Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, name);
         Log.d("Directory ", fileUri.getPath());
         intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

         // start the image capture Intent
         startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
         **/
        return fileUri;
    }

    public Uri getOutputMediaFileUri(int type, String name) {
        return Uri.fromFile(getOutputMediaFile(type, name));
    }

    private File getOutputMediaFile(int type, String name) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/EstimationApp/" + IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + name + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }






    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        boolean isCaptureImage = true;



        //insert new image
      //  String imagePath = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + session.getImageName() + ".jpg";
       // String imageName = session.getImageName();
        if (requestCode == REQUEST_CAMERA_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                //  File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                //   Bitmap bitmap = decodeSampledBitmapFromFile(currentPhotoPath, 1500, 1920);

                //   Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                String base64 = "";
                String path  = CONSTANTS.getImagePath(IMAGE_DIRECTORY_NAME) + appId + ".jpg";


                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                File pic = new File(path);

                if (pic.exists()) {
                    final Bitmap bitmap = BitmapFactory.decodeFile(path,
                            options);
                        image.setImageBitmap(bitmap);
                }

              //  image.setImageBitmap(getImage(true, path, appId));
                image.setOnTouchListener(new ImageMatrixTouchHandler(this));//for zoom





                // Get screen width and height in pixels
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                // The absolute width of the available display size in pixels.
                int displayWidth = displayMetrics.widthPixels;
                // The absolute height of the available display size in pixels.
                int displayHeight = displayMetrics.heightPixels;

                // Initialize a new window manager layout parameters
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                // Copy the alert dialog window attributes to new layout parameter instance
                layoutParams.copyFrom(this.getWindow().getAttributes());

                // Set the alert dialog window width and height
                // Set alert dialog width equal to screen width 90%
                // int dialogWindowWidth = (int) (displayWidth * 0.9f);
                // Set alert dialog height equal to screen height 90%
                // int dialogWindowHeight = (int) (displayHeight * 0.9f);

                // Set alert dialog width equal to screen width 70%
                int dialogWindowWidth = (int) (displayWidth * 0.9f);
                // Set alert dialog height equal to screen height 70%
                int dialogWindowHeight = (int) (displayHeight * 0.95f);

                // Set the width and height for the layout parameters
                // This will bet the width and height of alert dialog
                layoutParams.width = dialogWindowWidth;
                layoutParams.height = dialogWindowHeight;

                // Apply the newly created layout parameters to the alert dialog window
                this.getWindow().setAttributes(layoutParams);


                if (resultCode == Activity.RESULT_CANCELED) {
                    GeneralFunctions.messageBox(this,"something wrong happiend","");
                }
            }






//            image1.setImageBitmap(bitmap);
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//            byte[] imageBytes = byteArrayOutputStream.toByteArray();
//            String base64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//            base64 = imageString;
        }

        try
        {
            //GeneralFunctions.populateToastMsg(getApplicationContext(),imagePath,true);
            //  if(isCaptureImage)
            //  previewCapturedImage();

        } catch (Exception ex) {
            Log.d("upload image", ex.getMessage() + "");
            ex.printStackTrace();
        }



    }




    private Bitmap getImage(boolean imageByPath, String path, String appId) {

        Bitmap bitmap = null;
        try {
            if (!imageByPath) {
                path = CONSTANTS.getImagePath("EstimationImages")+ appId  + ".jpg";

            }

            ImageView imageView = new ImageView(this);
            Log.d("getImage Path", path);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger
            // images

            options.inSampleSize = 4;
            File pic = new File(path);
            if (pic.exists()) {
                bitmap = BitmapFactory.decodeFile(path, options);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add_image);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bitmap;
    }
}