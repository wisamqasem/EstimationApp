package com.jdeco.estimationapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.Item;
import com.jdeco.estimationapp.objects.OnItemClickListener;
import com.jdeco.estimationapp.objects.PriceList;
import com.jdeco.estimationapp.objects.Template;
import com.jdeco.estimationapp.objects.Warehouse;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.ui.forms.itemsList;
import com.jdeco.estimationapp.ui.forms.templatesList;

import java.util.ArrayList;

/**
 * Created by mmuneer on 04/09/2017.
 */

public class EstimatedTemplatesListAdapter extends RecyclerView.Adapter<EstimatedTemplatesListAdapter.CustomViewHolder> {
    private ArrayList<Template> list;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private boolean onBind;
    public String ticketType;
    Database dbObject;
    Session session;
    int pos;




    public EstimatedTemplatesListAdapter(Context context, ArrayList<Template> list, String ticketType) {
        this.list = list;
        this.mContext = context;
        this.ticketType = ticketType;

    }


    public EstimatedTemplatesListAdapter(Context context, ArrayList<Template> list) {
        this.list = list;
        this.mContext = context;
        Log.d("list",":"+list.size());
    }





    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.estimated_list_template, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        mContext = viewGroup.getContext();
        return viewHolder;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        protected ImageView imageView;
        protected TextView template_name;
        protected TextView material_amount;
        protected ImageView removeTemplateBtn;
        protected ImageView editTemplateBtn;
        protected ImageButton moreBtn;
        protected ImageButton lessBtn;
        protected TextView template_amount;


        public CustomViewHolder(View view) {
            super(view);
            this.template_name = (TextView) view.findViewById(R.id.template_name);
//            this.material_amount = (TextView) view.findViewById(R.id.material_amount);
            this.removeTemplateBtn = (ImageView) view.findViewById(R.id.removeTemplateBtn);
            this.editTemplateBtn = (ImageView) view.findViewById(R.id.editTemplateBtn);
          //  this.moreBtn = (ImageButton) view.findViewById(R.id.template_more);
          //  this.lessBtn = (ImageButton) view.findViewById(R.id.template_less);
            this.template_amount = (TextView) view.findViewById(R.id.template_amount);


            view.setOnCreateContextMenuListener(this);
        }


        //on hold
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {

//            menu.setHeaderTitle("Select The Action");
//            menu.add(0, v.getId(), 0, "Call");//groupId, itemId, order, title
//            menu.add(0, v.getId(), 0, "SMS");

        }
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {

Log.d("list","donn.");

        Template template = list.get(i);
        dbObject = new Database(mContext);
        session = new Session(mContext);
        try
        {

            if(ticketType=="D"){

                //customViewHolder.editTemplateBtn.setEnabled(false);
                customViewHolder.removeTemplateBtn.setEnabled(false);
                customViewHolder.template_amount.setEnabled(false);
            }




            customViewHolder.template_amount.setText(String.valueOf(template.getTemplateAmount()));


            //Setting text view title
            if (template.getTemplateName() != null)
                customViewHolder.template_name.setText(template.getTemplateName());
  //          if (item.getItemCode() != null)
//                customViewHolder.material_amount.setText(item.getItemCode());

            customViewHolder.removeTemplateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    list.remove(i);

                    //remove template from the database
                    dbObject.deleteEstimatedTemplate(template.getTemplateId());

                    //CONSTANTS.populateMsg(mContext,list.size()+"",1);
                    notifyDataSetChanged();

                }
            });

            customViewHolder.editTemplateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, itemsList.class);//make new activity for the items inside the tempalte
                    pos = i;
                    //pass parameters to application details activity
                    Bundle bundle = new Bundle();
                    bundle.putString("templateId", template.getTemplateId());
                    bundle.putString("templateName", template.getTemplateName());
                    bundle.putString("templateAmount", String.valueOf(template.getTemplateAmount()));
                    bundle.putString("status", ticketType);
                    bundle.putString("action", "update");
                    intent.putExtras(bundle); //Put your id to your next Intent
                    mContext.startActivity(intent);


                    //CONSTANTS.populateMsg(mContext,list.size()+"",1);
                    notifyDataSetChanged();

                }
            });



//            customViewHolder.moreBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    item.setItemAmount(Integer.parseInt(customViewHolder.item_amount.getText().toString()));
//                    item.setItemAmount(item.incressAmount());
//                    customViewHolder.item_amount.setText(String.valueOf(item.getItemAmount()));
//                   // notifyItemChanged(i);
//                    notifyItemChanged(i,item);
//                }
//            });

//            customViewHolder.lessBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    item.setItemAmount(Integer.parseInt(customViewHolder.item_amount.getText().toString()));
//                    item.setItemAmount(item.decressAmount());
//                    customViewHolder.item_amount.setText(String.valueOf(item.getItemAmount()));
//                    // notifyItemChanged(i);
//                    notifyItemChanged(i,item);
//                }
//            });


//            customViewHolder.item_amount.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    if (s.length() != 0)
//                    item.setItemAmount(Integer.parseInt(customViewHolder.item_amount.getText().toString()));
//                    Log.d("send", "afterTextChanged: " + item.getItemAmount());
////                    customViewHolder.item_amount.setText("");
//                }
//                @Override
//                public void afterTextChanged(Editable s) {
//                }
//            });



        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


//    public OnItemClickListener getOnItemClickListener() {
//        return onItemClickListener;
//    }
//
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
//
    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }


}