package com.fazevaib.shushit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Vaibhav on 27-07-2017.
 * Project: ShushIt
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    Context c;
    ArrayList<WorkLocation> workLocationArrayList;

    public RecyclerViewAdapter(Context c, ArrayList<WorkLocation> workLocationArrayList) {
        this.c = c;
        this.workLocationArrayList = workLocationArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater l = LayoutInflater.from(c);
        View v = l.inflate(R.layout.single_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WorkLocation workLocation = workLocationArrayList.get(position);
        holder.t1.setText(workLocation.getName());

    }

    @Override
    public int getItemCount() {
        return workLocationArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView t1;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            t1 = (TextView)itemView.findViewById(R.id.textViewSavedPlaces);
            cardView = (CardView)itemView.findViewById(R.id.cardView1);

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int i = getAdapterPosition();
                    workLocationArrayList.remove(i);
                    notifyDataSetChanged();
                    return true;
                }
            });
        }
    }
}

