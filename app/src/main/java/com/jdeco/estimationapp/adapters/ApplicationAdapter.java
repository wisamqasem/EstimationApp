
package com.jdeco.estimationapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.ApplicationDetails;
import com.jdeco.estimationapp.objects.OnItemClickListener;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.GeneralFunctions;
import com.jdeco.estimationapp.ui.forms.OpenApplicationDetails;

import java.util.ArrayList;

/**
 * Created by mmuneer on 04/09/2017.
 */

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.CustomViewHolder> {
    private ArrayList<ApplicationDetails> list;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private boolean onBind;
    private String ticketType;
    Database db ;
    String appId;



    public ApplicationAdapter(Context context, ArrayList<ApplicationDetails> list, String ticketType) {
        this.list = list;
        this.mContext = context;
        this.ticketType = ticketType;
    }


    public ApplicationAdapter(Context context, ArrayList<ApplicationDetails> list) {
        this.list = list;
        this.mContext = context;
        this.db = new Database(mContext);
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.applications_list_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        protected ImageView imageView;
        protected TextView appID;
        protected TextView fullNmae;
        protected TextView address;
        protected TextView appType;
        protected TextView phoneTB;
        protected TextView DateNoTV,doneDateTV,doneDateLabelTV;
        protected TextView status;
        protected TextView serviceNoLabel;
        protected TextView branch,serviceNo;
        protected CheckBox sync;
        protected CheckBox note;




        public CustomViewHolder(View view) {
            super(view);
            this.sync = (CheckBox)view.findViewById(R.id.syncCB) ;
//            this.note = (CheckBox)view.findViewById(R.id.noteCB) ;
            this.appID = (TextView) view.findViewById(R.id.appID);
            this.fullNmae = (TextView) view.findViewById(R.id.customerNameTB);
            this.address = (TextView) view.findViewById(R.id.address);
            this.appType = (TextView) view.findViewById(R.id.appType);
            this.phoneTB = (TextView) view.findViewById(R.id.itemPhone);
            this.status = (TextView) view.findViewById(R.id.status);
            this.branch = (TextView) view.findViewById(R.id.branch);
            this.serviceNo = (TextView) view.findViewById(R.id.serviceNo);
            this.DateNoTV = (TextView) view.findViewById(R.id.DateNoTV);
            this.doneDateTV = (TextView) view.findViewById(R.id.doneDateTV);
            this.doneDateLabelTV = (TextView) view.findViewById(R.id.doneDateLabelTV);
            this.serviceNoLabel = (TextView) view.findViewById(R.id.serviceNoLabel);
            view.setOnCreateContextMenuListener(this);
        }

        @Override//ON LONG CLICK
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            int pos = getPosition();
            menu.setHeaderTitle("أختار من لقائمة");
//            menu.add(0, v.getId(), 0, "فتح").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//
//
//
//                    return false;
//                }
//            });
            menu.add(0, v.getId(), 0, "حذف").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(list.get(pos).getTicketStatus().equals("D")){
                        db.deleteApplication(list.get(pos).getAppID());
                        list.remove(pos);
                        notifyDataSetChanged();
                    }else{
                        GeneralFunctions.messageBox(mContext,"لا يمكن حذف طلب غير مقدر","هذا طلب غير مقدر لا يمكن حذفه");
                    }




                    return false;
                }
            });


        }




    }



    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {

        Database db = new Database(mContext);
        ApplicationDetails ticket = list.get(i);

        try {

//            if(db.isItemNull(Database.APPLICATIONS_TABLE,"note"))
//            {
//                customViewHolder.note.setChecked(false);
//                customViewHolder.note.setText("لا يوجد ملاحظة");
//            }else {
//                customViewHolder.note.setChecked(true);
//                customViewHolder.note.setText("يوجد ملاحظة");
//            }


            if(ticket.getSync().equals("0")){
                customViewHolder.sync.setChecked(false);
                customViewHolder.sync.setText("غير مرحل");
            }
            else if (ticket.getSync().equals("2")){
                customViewHolder.sync.setChecked(true);
                customViewHolder.sync.setText("جديد");
            }
            else{
                customViewHolder.sync.setChecked(true);
                customViewHolder.sync.setText("تم ترحيل");
            }
            Log.d("fuck", ":" +ticket.getCustomerName() + " ??? " + ticket.getSync());
            //Setting text view title
            if (ticket.getAppID() != null)
                customViewHolder.appID.setText(ticket.getAppID());
            if (ticket.getCustomerName() != null)
                customViewHolder.fullNmae.setText(ticket.getCustomerName());
            if (!ticket.getCustomerAddress().equalsIgnoreCase("null"))
                customViewHolder.address.setText(ticket.getCustomerAddress());
            else customViewHolder.address.setText(" ");
            if (ticket.getAppType() != null)
                customViewHolder.appType.setText(ticket.getAppType());
            if (ticket.getStatus() != null)
                customViewHolder.status.setText(ticket.getStatus());
//        if (ticket.getBranch() != null)
//            customViewHolder.branch.setText(ticket.getBranch());
            if (!ticket.getPhone().equalsIgnoreCase("null"))
                customViewHolder.phoneTB.setText(ticket.getPhone());
            else customViewHolder.phoneTB.setText(" ");
            if (!ticket.getService_no().equalsIgnoreCase("null"))
                customViewHolder.serviceNo.setText(ticket.getService_no());
            else {customViewHolder.serviceNo.setVisibility(View.GONE);customViewHolder.serviceNoLabel.setVisibility(View.GONE);}
            customViewHolder.DateNoTV.setText(ticket.getAppDate().substring(0, 10));
            if(ticket.getTicketStatus().equals("D")){
                customViewHolder.doneDateTV.setVisibility(View.VISIBLE);
                customViewHolder.doneDateLabelTV.setVisibility(View.VISIBLE);
                customViewHolder.doneDateTV.setText(ticket.getDone_date());

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }



    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }


}


