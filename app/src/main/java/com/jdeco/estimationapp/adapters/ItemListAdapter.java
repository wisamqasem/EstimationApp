package com.jdeco.estimationapp.adapters;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.GeneralFunctions;
import com.jdeco.estimationapp.operations.Session;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ExampleViewHolder> {

    private ArrayList<Item> itemsArrayList;
    private OnItemClickListener onItemClickListener;
    int templateAmountNumber=1;
    String appStatus;
    Database dbObject;
    Session session;
    Context context;

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
         //   templatAmount =  itemView.findViewById(R.id.templateAmountCard);

            moreBtn =  itemView.findViewById(R.id.templateItem_more);
            lessBtn = itemView.findViewById(R.id.templateItem_less);


        }
    }

    public ItemListAdapter(ArrayList<Item> itemsArrayList,String appStatus,Context context) {
        this.itemsArrayList = itemsArrayList;
        this.appStatus = appStatus;
        this.context = context;
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
        Item item = itemsArrayList.get(holder.getAbsoluteAdapterPosition());

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
        if(item.getAllowEdit().equals("N")) {holder.moreBtn.setEnabled(false);holder.lessBtn.setEnabled(false);holder.itemAmountCard.setEnabled(false); }

        holder.itemNameCard.setText(item.getItemName());
        holder.itemAmountCard.setText(String.valueOf(item.getItemAmount()));



        //holder.templatAmount.setText("X"+templateAmountNumber);



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

            }
        });
        holder.moreBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.itemAmountCard.setText(String.valueOf(item.incressAmount10()));
                return false;
            }
        });





    //    Timer timer=new Timer();


//    holder.moreBtn.setOnTouchListener(new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            try{
//
//                if (event.getAction() == MotionEvent.ACTION_DOWN)
//                {
//
//                    timer.scheduleAtFixedRate(new TimerTask(){
//                        @Override
//                        public void run(){
//                            Log.i("tag", "A Kiss every 5 seconds");
//                            holder.itemAmountCard.setText(String.valueOf(item.incressAmount()));
//                        }
//                    },0,100);
//
//
//                }
//                else if (event.getAction() == MotionEvent.ACTION_UP)
//                {
//                    timer.cancel();
//                }
//
//
//        }catch (Exception ex){ Log.d("tag", ":"+ex.toString());}
//            return false;
//        }
//    });





        holder.lessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemAmountCard.setText(String.valueOf(item.decressAmount()));
            //    notifyItemChanged(position);
          //      notifyDataSetChanged();
           //     notifyItemChanged(position,item);
            }
        });


        holder.lessBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.itemAmountCard.setText(String.valueOf(item.decressAmount10()));
                return false;
            }
        });


//        holder.itemAmountCard.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (holder.itemAmountCard.hasFocus()) {
//                    // is only executed if the EditText was directly changed by the user
//                    if (s.length() != 0)
//                        item.setItemAmount(Integer.parseInt( holder.itemAmountCard.getText().toString()));
//                    dbObject.updateEstimatedItemAmount(item.getId(), session.getValue("APP_ID"), "itemAmount", item.getItemAmount());
//                    //dbObject.showEstimatedItems();
//                   // if(!s.toString().equals(""))  item.setItemAmount(Integer.valueOf(s.toString()));
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//
//
//            }
//        });




    }

    @Override
    public int getItemCount() {
        try{
            return itemsArrayList.size();
        }catch(Exception ex){
            GeneralFunctions.messageBox(context,"حدث خطاء","أرجاء تحديث البيانات");
            return 0;

        }

    }




    public void filterList(ArrayList<Item> filteredList) {
        itemsArrayList = filteredList;
        notifyDataSetChanged();
    }


    public void setTemplateAmountNumber(int n){
        templateAmountNumber = n;
     //   notifyDataSetChanged();
    }


    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



}