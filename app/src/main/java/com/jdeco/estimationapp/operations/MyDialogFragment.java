package com.jdeco.estimationapp.operations;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.adapters.ServicesAdapter;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.objects.ServiceInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyDialogFragment extends DialogFragment {
    private RecyclerView mRecyclerView;
    private ServicesAdapter adapter;



    Session session;
    Context context;
    ArrayList<ServiceInfo> services ;

    public MyDialogFragment(ArrayList<ServiceInfo> list){
        this.services = list;
    }


    public static MyDialogFragment newInstance(ArrayList<ServiceInfo> list) {
        return new MyDialogFragment(list);
    }




    // this method create view for your Dialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.services_info, container, false);
        context = v.getContext();
        session = new Session(context);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.servicesRV);
        TextView noOfServicesTxt = (TextView) v.findViewById(R.id.noOfServicesTxt);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                lp.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.847);
                return true;
            }
        });
        //setadapter
        ServicesAdapter adapter = new ServicesAdapter(context, services);
        mRecyclerView.setAdapter(adapter);

        noOfServicesTxt.setText(noOfServicesTxt.getText() + " " + adapter.getItemCount());
        //get your recycler view and populate it.
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
//        mRecyclerView.getLayoutParams().width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);

            dialog.getWindow().setLayout(width, height);
        }
    }
}
