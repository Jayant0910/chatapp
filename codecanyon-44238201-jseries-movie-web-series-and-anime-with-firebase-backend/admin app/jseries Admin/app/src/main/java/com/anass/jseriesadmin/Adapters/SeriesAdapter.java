package com.anass.jseriesadmin.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.anass.jseriesadmin.Models.SerieModel;
import com.anass.jseriesadmin.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.List;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.MyViewHolder> {

    private Context context;
    List<SerieModel> serieModels;

    public SeriesAdapter(Context context, List<SerieModel> serieModels) {
        this.context = context;
        this.serieModels = serieModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.seire_item_search, parent, false));





    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_animation_fall_down);
        SerieModel w = serieModels.get(position);

        holder.textView.setText(w.getTitle());
        Glide.with(holder.itemView.getContext()).load(w.getPoster()).into(holder.imageView);





        holder.itemView.startAnimation(animation);
    }


    @Override
    public int getItemCount() {
        return serieModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView,back;
        private TextView textView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageprev);
            textView = itemView.findViewById(R.id.season_name);
            back = itemView.findViewById(R.id.imageVie3);


        }
    }
}
