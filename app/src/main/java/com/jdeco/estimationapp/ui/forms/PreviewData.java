package com.jdeco.estimationapp.ui.forms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.adapters.PreviewDataAdapter;
import com.jdeco.estimationapp.objects.Item;
import com.jdeco.estimationapp.operations.Database;
import com.jdeco.estimationapp.operations.Session;
import com.jdeco.estimationapp.ui.MainActivity;

import java.util.ArrayList;

public class PreviewData extends AppCompatActivity {

    ArrayList<Item>  estimatedItems = null ;
    PreviewDataAdapter estimatedItemsListAdapter;
    Database dbObject;
    Session session;
    RecyclerView itemsListRV;
    LinearLayoutManager llm ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_data);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("معاينة الطلب");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Add back arrow in action bar
        }

          estimatedItems = new ArrayList<Item>();
        dbObject = new Database(this);
        session = new Session(this);

        itemsListRV = (RecyclerView) findViewById(R.id.itemsListRV);

         llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);


        if (!dbObject.tableIsEmpty(Database.ESTIMATED_ITEMS_TABLE)) {
            estimatedItems = dbObject.getEstimatedItems(null, session.getValue("APP_ID"));
            //add this item to recycler view and refresh list
            AddItemsToList(estimatedItems);
            Log.d("previewDate", "estimatedItems size : " + estimatedItems.size());
        }




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void AddItemsToList(ArrayList<Item> list) {
        estimatedItemsListAdapter = new PreviewDataAdapter(this, list);
        itemsListRV.setLayoutManager(llm);
       // itemsListRV.setAdapter( estimatedItemsListAdapter );
        itemsListRV.setAdapter(estimatedItemsListAdapter);
        estimatedItemsListAdapter.setItems(list);
        estimatedItemsListAdapter.notifyDataSetChanged();
    }




}