
package com.jdeco.estimationapp.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.NoteInfo;
import com.jdeco.estimationapp.objects.ServiceInfo;

import java.util.ArrayList;

/**
 * Created by wqasem on 17/07/2022.
 */

public class notesAdapter extends RecyclerView.Adapter<notesAdapter.CustomViewHolder> {
    private ArrayList<NoteInfo> list;
    private Context mContext;
    private boolean onBind;


    public notesAdapter(Context context, ArrayList<NoteInfo> list) {
        this.list = list;
        this.mContext = context;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {


        NoteInfo noteInfo = list.get(i);

        try {

            customViewHolder.noteTxt.setText(noteInfo.getComments());
            customViewHolder.note_created_by.setText(noteInfo.getCreated_by());
            customViewHolder.noteDate.setText(noteInfo.getCreation_date().substring(0,10));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        protected TextView noteTxt, note_created_by,noteDate;


        public CustomViewHolder(View view) {
            super(view);

            this.noteTxt = (TextView) view.findViewById(R.id.noteTxt);
            this.note_created_by = (TextView) view.findViewById(R.id.note_created_by);
            this.noteDate = (TextView) view.findViewById(R.id.noteDate);

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
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }


}


