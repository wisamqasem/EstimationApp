package com.jdeco.estimationapp.adapters;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.Item;
import com.jdeco.estimationapp.objects.OnItemClickListener;
import com.jdeco.estimationapp.objects.Template;

import java.util.ArrayList;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ExampleViewHolder> {

    private ArrayList<Item> itemsArrayList;
    private OnItemClickListener onItemClickListener;
    int templateAmountNumber=1;
    String appStatus;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView itemNameCard;
        public EditText itemAmountCard;
        private CheckBox checkBox;
        protected TextView templatAmount;

        protected ImageButton moreBtn;
        protected ImageButton lessBtn;


        public ExampleViewHolder(View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.itemCB);
            itemNameCard =  itemView.findViewById(R.id.itemNameCard);
            itemAmountCard =  itemView.findViewById(R.id.itemAmountCard);
            templatAmount =  itemView.findViewById(R.id.templateAmountCard);

            moreBtn =  itemView.findViewById(R.id.templateItem_more);
            lessBtn = itemView.findViewById(R.id.templateItem_less);


        }
    }

    public ItemListAdapter(ArrayList<Item> itemsArrayList,String appStatus) {
        this.itemsArrayList = itemsArrayList;
        this.appStatus = appStatus;
    }

//public void updateItemsAmount(){itemAmountCard;  notifyDataSetChanged();}

    public ArrayList<Item> getItemList() {
         return  itemsArrayList;
    }



    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,
                parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Item item = itemsArrayList.get(position);

Log.d("onBindViewHolder",":"+appStatus);
        //the application is in done list
        if(appStatus.equals("D")){
            holder.checkBox.setEnabled(false);
            holder.moreBtn.setEnabled(false);
            holder.lessBtn.setEnabled(false);
            holder.itemAmountCard.setEnabled(false);

        }






        holder.checkBox.setChecked(item.getChecked());
        if(item.getAllowDelete().equals("N")) {holder.checkBox.setChecked(true);holder.checkBox.setEnabled(false); }
        if(item.getAllowEdit().equals("N")) {holder.moreBtn.setEnabled(false);holder.lessBtn.setEnabled(false); }

        holder.itemNameCard.setText(item.getItemName());
        holder.itemAmountCard.setText(String.valueOf(item.getItemAmount()));


        holder.templatAmount.setText("X"+templateAmountNumber);



        holder.checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ( ((CheckBox)v).isChecked() ) {
                    // perform logic
                    itemsArrayList.get(position).setChecked(true);
                }
                else {
                    itemsArrayList.get(position).setChecked(false);
                }
            }
        });

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemAmountCard.setText(String.valueOf(item.incressAmount()));
                // notifyItemChanged(i);
                notifyItemChanged(position,item);
            }
        });

        holder.lessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemAmountCard.setText(String.valueOf(item.decressAmount()));
                // notifyItemChanged(i);
                notifyItemChanged(position,item);
            }
        });



        holder.itemAmountCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(""))  item.setItemAmount(Integer.valueOf(s.toString())); ;

            }
        });




    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    public void filterList(ArrayList<Item> filteredList) {
        itemsArrayList = filteredList;
        notifyDataSetChanged();
    }


    public void setTemplateAmountNumber(int n){
        templateAmountNumber = n;
        notifyDataSetChanged();
    }


    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



}