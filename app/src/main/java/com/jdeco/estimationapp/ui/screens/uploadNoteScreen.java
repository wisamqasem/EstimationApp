package com.jdeco.estimationapp.ui.screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.operations.GeneralFunctions;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.ui.forms.OpenApplicationDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class uploadNoteScreen extends AppCompatActivity {


    EditText applNote,applNo;
    Button submitBtn ;

    Session session;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_note_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("أعتماد ملاحظة");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Add back arrow in action bar
        }

        applNote = (EditText)findViewById(R.id.applNote) ;
        applNo = (EditText)findViewById(R.id.applNo) ;

        submitBtn = (Button)findViewById(R.id.submitBtn) ;

        session = new Session(this);

        progress = new ProgressDialog(uploadNoteScreen.this);
        progress.setTitle(getResources().getString(R.string.please_wait));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "{" +
                        "\"application\":{" +
                        "\"applRowId\":\"" + applNo.getText().toString() + "\" ," +
                        "\"notes\":\"" + applNote.getText().toString() + "\"," +
                        "\"noteLookupID\":" + 498 + " ," +
                        "\"username\":\"" + session.getValue("username") + "\"" +
                        "}" +
                        "}";

                submitNote(data);
            }
        });










    }

    private void submitNote(String bodyData) {
        //get login url
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;
        progress.show();

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();//display the response submit success
                Log.d("sumbitNote", "Response: " + response);
                try {

                    JSONObject submitData = new JSONObject(response);
                    Log.d("submitMaterialsToServer", "Response: " + (submitData.getString("request_response").equals("Success")));
                    if (submitData.getString("request_response").equals("Success.")) {
                        GeneralFunctions.messageBox(uploadNoteScreen.this, getString(R.string.success_lbl), getResources().getString(R.string.submit_success));
                        progress.dismiss();
                        // Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();//display the response submit success
                    } else {
                        GeneralFunctions.messageBox(uploadNoteScreen.this, getString(R.string.failed), getResources().getString(R.string.submit_failed));
                        progress.dismiss();
                        //Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_failed), Toast.LENGTH_LONG).show();//display the response submit failed
                    }
                } catch (JSONException e) {
                    progress.dismiss();
                    GeneralFunctions.messageBox(uploadNoteScreen.this, getString(R.string.failed), e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getItemsFromServer", "Error Login Request :" + error.toString());
                progress.dismiss();
                GeneralFunctions.messageBox(uploadNoteScreen.this, getResources().getString(R.string.submit_failed), error.toString());
                // Toast.makeText(getApplicationContext(), "Submit note failed !", Toast.LENGTH_LONG).show();
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                //parameters
                // params.put("username", "jd");
                params.put("apiKey", CONSTANTS.API_KEY);
                params.put("action", CONSTANTS.ACTION_SUBMIT_NOTE);
                params.put("data", bodyData);
                params.put("appId",applNo.getText().toString());

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}