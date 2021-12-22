package com.jdeco.estimationapp.adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.OnItemClickListener;
import com.jdeco.estimationapp.objects.Template;

import java.util.ArrayList;

public class TemplateListAdapter extends RecyclerView.Adapter<TemplateListAdapter.ExampleViewHolder> {

    private ArrayList<Template> templateArrayList;
    private OnItemClickListener onItemClickListener;
    private int selectedItemPosition = 0;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView templateNameCard;
        public TextView templateDescCard;
        public TextView itemAmountCard;
        public CardView templateCard;
        protected ImageButton moreBtn;
        protected ImageButton lessBtn;

        public ExampleViewHolder(View itemView) {
            super(itemView);



            templateCard = itemView.findViewById(R.id.templateCard);
            templateNameCard = itemView.findViewById(R.id.templateNameCard);
            templateDescCard = itemView.findViewById(R.id.templateDescCard);



        }
    }

    public TemplateListAdapter(ArrayList<Template> templateArrayList) {
        this.templateArrayList = templateArrayList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_card,
                parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Template template = templateArrayList.get(position);


        holder.templateNameCard.setText(template.getTemplateName());
        holder.templateDescCard.setText(template.getTemplateDesc());






    }

    @Override
    public int getItemCount() {
        return templateArrayList.size();
    }

    public void filterList(ArrayList<Template> filteredList) {
        templateArrayList = filteredList;
        notifyDataSetChanged();
    }


    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



}