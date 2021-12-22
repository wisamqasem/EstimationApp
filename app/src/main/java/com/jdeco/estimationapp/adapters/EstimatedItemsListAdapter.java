package com.jdeco.estimationapp.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.EstimationItem;
import com.jdeco.estimationapp.objects.Item;
import com.jdeco.estimationapp.objects.OnItemClickListener;
import com.jdeco.estimationapp.objects.PriceList;
import com.jdeco.estimationapp.objects.Warehouse;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.Session;

import java.util.ArrayList;

/**
 * Created by mmuneer on 04/09/2017.
 */

public class EstimatedItemsListAdapter extends RecyclerView.Adapter<EstimatedItemsListAdapter.CustomViewHolder> {
    private ArrayList<Item> list;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private boolean onBind;
    private String ticketType;
    Database dbObject;
    Session session;





    public EstimatedItemsListAdapter(Context context, ArrayList<Item> list, String ticketType) {
        this.list = list;
        this.mContext = context;
        this.ticketType = ticketType;
    }


    public EstimatedItemsListAdapter(Context context, ArrayList<Item> list) {
        this.list = list;
        this.mContext = context;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.estimated_list_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        protected ImageView imageView;
        protected TextView material_name;
        protected TextView material_amount;
        protected ImageView removeItemBtn;
        protected ImageButton moreBtn;
        protected ImageButton lessBtn;
        protected TextView item_amount;
        protected TextView priceList;
        protected TextView warehouse;


        public CustomViewHolder(View view) {
            super(view);
            this.material_name = (TextView) view.findViewById(R.id.material_name);
//            this.material_amount = (TextView) view.findViewById(R.id.material_amount);
            this.removeItemBtn = (ImageView) view.findViewById(R.id.removeItemBtn);
            this.moreBtn = (ImageButton) view.findViewById(R.id.item_more);
            this.lessBtn = (ImageButton) view.findViewById(R.id.item_less);
            this.item_amount = (TextView) view.findViewById(R.id.item_amount);
            this.priceList = (TextView) view.findViewById(R.id.priceListSelectedTxt);
            this.warehouse = (TextView) view.findViewById(R.id.warehouseSelectedTxt);


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

        dbObject = new Database(mContext);
        session = new Session(mContext);
        Item item = list.get(i);
        ArrayList<PriceList> priceLists = dbObject.getPriceList();
        ArrayList<Warehouse> warehouses = dbObject.getWarehouse();


        try
        {
            if(ticketType=="D"){
                customViewHolder.moreBtn.setEnabled(false);
                customViewHolder.lessBtn.setEnabled(false);
                customViewHolder.removeItemBtn.setEnabled(false);
                customViewHolder.item_amount.setEnabled(false);
            }




            customViewHolder.item_amount.setText(String.valueOf(item.getItemAmount()));
            customViewHolder.priceList.setText(item.getPricList().toString());
            customViewHolder.warehouse.setText(item.getWarehouse().toString());





            //Setting text view title
            if (item.getItemName() != null)
                customViewHolder.material_name.setText(item.getItemName());
  //          if (item.getItemCode() != null)
//                customViewHolder.material_amount.setText(item.getItemCode());

            customViewHolder.removeItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dbObject.deleteEstimatedItem(list.get(i),session.getValue("APP_ID"));

                    list.remove(i);
                    //CONSTANTS.populateMsg(mContext,list.size()+"",1);
                    notifyDataSetChanged();

                }
            });



            customViewHolder.moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setItemAmount(Integer.parseInt(customViewHolder.item_amount.getText().toString()));
                    item.setItemAmount(item.incressAmount());
                    customViewHolder.item_amount.setText(String.valueOf(item.getItemAmount()));
                   // notifyItemChanged(i);
                    notifyItemChanged(i,item);
                }
            });

            customViewHolder.lessBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setItemAmount(Integer.parseInt(customViewHolder.item_amount.getText().toString()));
                    item.setItemAmount(item.decressAmount());
                    customViewHolder.item_amount.setText(String.valueOf(item.getItemAmount()));
                    // notifyItemChanged(i);
                    notifyItemChanged(i,item);
                }
            });


            customViewHolder.item_amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() != 0)
                    item.setItemAmount(Integer.parseInt(customViewHolder.item_amount.getText().toString()));
                    dbObject.updateItem(item.getId(),item.getItemAmount(),session.getValue("APP_ID"));
                    Log.d("send", "afterTextChanged: " + item.getItemAmount());
//                    customViewHolder.item_amount.setText("");
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });



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


    public void setItems(ArrayList<Item> items){
        list=items;
        notifyDataSetChanged();

    }

    public void setItem(Item item){
        list.add(item);
        notifyDataSetChanged();

    }


}