package com.jdeco.estimationapp.ui.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.operations.Database;

public class TemplatesGallry extends AppCompatActivity {

    ImageButton cabels2Btn,pelerBtn,keysBtn,connectorsBtn,stationsBtn,polesBtn,metersBtn,allTemplatesBtn,cablesBtn;
    private Database dbObject;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates_gallry);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("القوالب");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Add back arrow in action bar
        }

        context = getApplicationContext();

        cablesBtn = (ImageButton) findViewById(R.id.cablesBtn);
        cabels2Btn= (ImageButton) findViewById(R.id.cabels2Btn);
        pelerBtn= (ImageButton) findViewById(R.id.pelerBtn);
        keysBtn= (ImageButton) findViewById(R.id.keysBtn);
        connectorsBtn= (ImageButton) findViewById(R.id.connectorsBtn);
        stationsBtn= (ImageButton) findViewById(R.id.stationsBtn);
        polesBtn = (ImageButton) findViewById(R.id.polesBtn);
        metersBtn  = (ImageButton) findViewById(R.id.metersBtn);
        allTemplatesBtn =  (ImageButton) findViewById(R.id.allTemplatesBtn);

        dbObject= new Database(this);





        cablesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, templatesList.class);
                Bundle bundle = new Bundle();
                bundle.putString("keyWord", "كابل"); //Your id
                i.putExtras(bundle); //Put your id to your next Intent
                startActivity(i);
            }
        });


        cabels2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, templatesList.class);
                Bundle bundle = new Bundle();
                bundle.putString("keyWord", "مجدول"); //Your id
                i.putExtras(bundle); //Put your id to your next Intent
                startActivity(i);
              //  dbObject.getTemplates("كابل",context);
            }
        });
        pelerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, templatesList.class);
                Bundle bundle = new Bundle();
                bundle.putString("keyWord", "بلر"); //Your id
                i.putExtras(bundle); //Put your id to your next Intent
                startActivity(i);
               // dbObject.getTemplates("بلر",context);
            }
        });
        keysBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(context, templatesList.class);
                Bundle bundle = new Bundle();
                bundle.putString("keyWord", "مفتاح"); //Your id
                i.putExtras(bundle); //Put your id to your next Intent
                startActivity(i);
                //dbObject.getTemplates("مفتاح",context);
            }
        });
        connectorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, templatesList.class);
                Bundle bundle = new Bundle();
                bundle.putString("keyWord", "مربط"); //Your id
                i.putExtras(bundle); //Put your id to your next Intent
                startActivity(i);
               // dbObject.getTemplates("مربط",context);
            }
        });
        stationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, templatesList.class);
                Bundle bundle = new Bundle();
                bundle.putString("keyWord", "محطة"); //Your id
                i.putExtras(bundle); //Put your id to your next Intent
                startActivity(i);
               // dbObject.getTemplates("محطة",context);
            }
        });
        polesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, templatesList.class);
                Bundle bundle = new Bundle();
                bundle.putString("keyWord", "عمود"); //Your id
                i.putExtras(bundle); //Put your id to your next Intent
                startActivity(i);
              //  dbObject.getTemplates("عمود",context);
            }
        });

        allTemplatesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, templatesList.class);
                Bundle bundle = new Bundle();
                bundle.putString("keyWord", null); //Your id
                i.putExtras(bundle); //Put your id to your next Intent
                startActivity(i);
              //  dbObject.getTemplates("عمود",context);
            }
        });

        metersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, templatesList.class);
                Bundle bundle = new Bundle();
                bundle.putString("keyWord", "عداد"); //Your id
                i.putExtras(bundle); //Put your id to your next Intent
                startActivity(i);
                //  dbObject.getTemplates("عمود",context);
            }
        });




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}