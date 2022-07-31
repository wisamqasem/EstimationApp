package com.jdeco.estimationapp.operations;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.adapters.ServicesAdapter;
import com.jdeco.estimationapp.adapters.notesAdapter;
import com.jdeco.estimationapp.objects.NoteInfo;
import com.jdeco.estimationapp.objects.ServiceInfo;

import java.util.ArrayList;

public class notesDialogFragment extends DialogFragment {
    private RecyclerView mRecyclerView;
    private notesAdapter adapter;
    private Button closeDialog;

    Session session;
    Context context;
    ArrayList<NoteInfo> notes ;

    public notesDialogFragment(ArrayList<NoteInfo> list){
        this.notes = list;
    }


    public static notesDialogFragment newInstance(ArrayList<NoteInfo> list) {
        return new notesDialogFragment(list);
    }




    // this method create view for your Dialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.notes_info, container, false);
        context = v.getContext();
        session = new Session(context);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.notesRV);
        closeDialog = (Button)v.findViewById(R.id.closeDialog);
        TextView noOfServicesTxt = (TextView) v.findViewById(R.id.noOfServicesTxt);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                lp.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.847);
                return true;
            }
        });
        //setadapter
        notesAdapter adapter = new notesAdapter(context, notes);
        mRecyclerView.setAdapter(adapter);


        //get your recycler view and populate it.

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesDialogFragment.super.dismiss();
            }
        });




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
