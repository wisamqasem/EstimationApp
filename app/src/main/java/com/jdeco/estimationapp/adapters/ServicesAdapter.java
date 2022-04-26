
package com.jdeco.estimationapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.OnItemClickListener;
import com.jdeco.estimationapp.objects.ServiceInfo;
import com.jdeco.estimationapp.operations.Database;

import java.util.ArrayList;

/**
 * Created by wqasem on 25/04/2022.
 */

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.CustomViewHolder> {
    private ArrayList<ServiceInfo> list;
    private Context mContext;
    private boolean onBind;




    public ServicesAdapter(Context context, ArrayList<ServiceInfo> list) {
        this.list = list;
        this.mContext = context;

    }





    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.service_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        protected TextView phase_type , service_class , usage_type , meter_type ;



        public CustomViewHolder(View view) {
            super(view);

            this.phase_type = (TextView) view.findViewById(R.id.phase_type);
            this.service_class = (TextView) view.findViewById(R.id.service_class);
            this.usage_type = (TextView) view.findViewById(R.id.usage_type);
            this.meter_type = (TextView) view.findViewById(R.id.meter_type);

            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

//            menu.setHeaderTitle("Select The Action");
//            menu.add(0, v.getId(), 0, "Call");//groupId, itemId, order, title
//            menu.add(0, v.getId(), 0, "SMS");

        }


    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {


        ServiceInfo serviceInfo = list.get(i);

        try {


            //Setting text view title
            if (serviceInfo.getPhase_type() != null)
                customViewHolder.phase_type.setText(serviceInfo.getPhase_type());
            if (serviceInfo.getService_class() != null)
                customViewHolder.service_class.setText(serviceInfo.getService_class());
            if (serviceInfo.getUsage_type() != null)
                customViewHolder.usage_type.setText(serviceInfo.getUsage_type());
            if (serviceInfo.getMeter_type() != null)
                customViewHolder.meter_type.setText(serviceInfo.getMeter_type());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }


}


