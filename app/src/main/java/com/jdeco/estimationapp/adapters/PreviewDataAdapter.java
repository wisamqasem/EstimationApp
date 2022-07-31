
package com.jdeco.estimationapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.Item;

import java.util.ArrayList;

/**
 * Created by wqasem on 11/06/2022.
 */

public class PreviewDataAdapter extends RecyclerView.Adapter<PreviewDataAdapter.CustomViewHolder> {
    private ArrayList<Item> list;
    private Context mContext;
    private boolean onBind;


    public PreviewDataAdapter(Context context, ArrayList<Item> list) {
        this.list = list;
        this.mContext = context;
        Log.d("previewDateAdapter","???????????????????");
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.preview_data_item,viewGroup,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        protected TextView itemName, itemAmount;
        public CustomViewHolder(View view) {
            super(view);

            this.itemName = (TextView) view.findViewById(R.id.itemName);
            this.itemAmount = (TextView) view.findViewById(R.id.itemAmount);


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


        Item item = list.get(i);

    //    try {
            Log.d("previewDateAdapter", " : " + item.getItemAmount());

            //Setting text view title
            if (item.getItemName() != null ) {
                customViewHolder.itemName.setText(item.getItemName());
            } else {
                customViewHolder.itemName.setText(mContext.getResources().getString(R.string.no_data_found_lbl));
            }
            customViewHolder.itemAmount.setText(String.valueOf(item.getItemAmount()));

      //  } catch (Exception ex) {
     //       ex.printStackTrace();
     //   }

    }


    public void setItems(ArrayList<Item> items) {
        list = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }


}


