package com.anass.ninflix.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.anass.ninflix.Activities.Chrachters;
import com.anass.ninflix.Models.Cast;
import com.anass.ninflix.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> {

    Context context;
    List<Cast> castModels;

    public CastAdapter(Context context, List<Cast> castModels) {
        this.context = context;
        this.castModels = castModels;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chrachter_item_2,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        Cast w = castModels.get(position);
        holder.textView.setText(w.getName());
        Glide.with(holder.itemView.getContext()).load(w.getPoster()).into(holder.imageView);
        Glide.with(holder.itemView.getContext()).load(w.getPoster()).into(holder.fullback);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), Chrachters.class);
                sendDataToDetailsActivity.putExtra("title",w.getName());
                sendDataToDetailsActivity.putExtra("type",w.getType());
                sendDataToDetailsActivity.putExtra("image",w.getPoster());
                sendDataToDetailsActivity.putExtra("castid",w.getCast_id());
                sendDataToDetailsActivity.putExtra("postkey",w.getChrachter_id());
                holder.itemView.getContext().startActivity(sendDataToDetailsActivity);


            }
        });

    }

    @Override
    public int getItemCount() {
        return castModels.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView,fullback;
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageprev);
            textView = itemView.findViewById(R.id.season_name);
            fullback = itemView.findViewById(R.id.fullback);
        }
    }
}
