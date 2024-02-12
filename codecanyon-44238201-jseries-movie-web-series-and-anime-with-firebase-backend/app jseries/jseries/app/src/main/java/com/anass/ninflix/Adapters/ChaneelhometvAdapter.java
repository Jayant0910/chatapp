package com.anass.ninflix.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anass.ninflix.Activities.ShowInfoActivity;
import com.anass.ninflix.JPLAYER.LivePlayer;
import com.anass.ninflix.Models.TVModel;
import com.anass.ninflix.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class ChaneelhometvAdapter extends RecyclerView.Adapter<ChaneelhometvAdapter.myviewholder> {


    List<TVModel> tvModelList;
    Context context;

    public ChaneelhometvAdapter(List<TVModel> tvModelList, Context context) {
        this.tvModelList = tvModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChaneelhometvAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.livetvitem,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChaneelhometvAdapter.myviewholder holder, int position) {

        TVModel tv = tvModelList.get(position);

        Glide.with(holder.itemView.getContext()).load(tv.getImgurl()).into(holder.cat_img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), LivePlayer.class);
                sendDataToDetailsActivity.putExtra("title",tv.getTitle());
                sendDataToDetailsActivity.putExtra("url",tv.getUrl());

                holder.itemView.getContext().startActivity(sendDataToDetailsActivity);

            }
        });

    }

    @Override
    public int getItemCount() {
        return tvModelList.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView cat_img;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            cat_img = itemView.findViewById(R.id.cat_img);

        }
    }
}
