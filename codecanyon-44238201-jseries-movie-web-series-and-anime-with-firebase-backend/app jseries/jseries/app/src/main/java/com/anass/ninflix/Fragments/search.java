package com.anass.ninflix.Fragments;

import static com.anass.ninflix.Activities.MyList.lista.favoritlmovie.allseriesandmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anass.ninflix.Activities.ShowInfoActivity;
import com.anass.ninflix.Models.SerieModel;
import com.anass.ninflix.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class search extends Fragment {

    LinearLayout searchresultbg;
    TextView desc;
    EditText search_field;
    ImageView mylogo;
    ProgressBar progresssrch;
    RecyclerView result_list;

    List<SerieModel> Cartoonssearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_search, container, false);

        result_list = v.findViewById(R.id.result_list);
        progresssrch = v.findViewById(R.id.progresssrch);
        search_field = v.findViewById(R.id.search_field);
        searchresultbg = v.findViewById(R.id.searchresultbg);
        mylogo = v.findViewById(R.id.mylogo);
        GetData();

        search_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Cartoonssearch != null){
                    SearchNow(s.toString());
                }

            }
        });



        return  v;
    }

    private void GetData() {

        result_list.setLayoutManager(new GridLayoutManager(getActivity(),3));

        allseriesandmovies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progresssrch.setVisibility(View.GONE);
                Cartoonssearch = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    SerieModel comicsm = data.getValue(SerieModel.class);

                    Cartoonssearch.add(comicsm);

                }

                Collections.reverse(Cartoonssearch);


                result_list.setAdapter(new SearchAdapterMo(getActivity(),Cartoonssearch));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public class SearchAdapterMo extends RecyclerView.Adapter<SearchAdapterMo.MyViewHolder> {

        Context context;
        List<SerieModel> castModels;

        public SearchAdapterMo(Context context, List<SerieModel> castModels) {
            this.context = context;
            this.castModels = castModels;
        }



        @NonNull
        @Override
        public  MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seire_item_search,parent,false);
            return new  MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


            SerieModel w = castModels.get(position);

            holder.textView.setText(w.getTitle());
            if (w.getPlace().contains("movie")){
                holder.tasnif.setText("movie");
            } else if (w.getPlace().contains("serie")){
                holder.tasnif.setText("serie");
            }else  holder.tasnif.setText("anime");

            Glide.with(holder.itemView.getContext()).load(w.getPoster()).into(holder.imageView);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), ShowInfoActivity.class);
                    sendDataToDetailsActivity.putExtra("title",w.getTitle());
                    sendDataToDetailsActivity.putExtra("id",w.getId());
                    sendDataToDetailsActivity.putExtra("cover",w.getPoster());
                    sendDataToDetailsActivity.putExtra("poste",w.getPoster());
                    sendDataToDetailsActivity.putExtra("link",w.getLink_id());
                    sendDataToDetailsActivity.putExtra("postkey",w.getSerie_id());
                    sendDataToDetailsActivity.putExtra("age",w.getAge());
                    sendDataToDetailsActivity.putExtra("studio",w.getCountry());
                    sendDataToDetailsActivity.putExtra("date",w.getYear());
                    sendDataToDetailsActivity.putExtra("rat","");
                    sendDataToDetailsActivity.putExtra("desc",w.getStory());
                    sendDataToDetailsActivity.putExtra("cast",w.getCast());
                    sendDataToDetailsActivity.putExtra("mortabit",w.getOther_season_id());
                    sendDataToDetailsActivity.putExtra("place",w.getPlace());
                    sendDataToDetailsActivity.putExtra("tasnif",w.getGener());
                    sendDataToDetailsActivity.putExtra("isintent","1");

                    holder.itemView.getContext().startActivity(sendDataToDetailsActivity);



                }
            });
        }

        @Override
        public int getItemCount() {
            return castModels.size();

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageView;
            private TextView textView,tasnif;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);


                imageView = itemView.findViewById(R.id.imageprev);
                textView = itemView.findViewById(R.id.season_name);
                tasnif = itemView.findViewById(R.id.tasnif);
            }
        }
    }


    public void SearchNow(String search) {

        List<SerieModel> serieModelList = new ArrayList<>();

        if(Cartoonssearch.size() != 0 ){
            for (SerieModel s : Cartoonssearch) {
                if (s.getTitle().toLowerCase().contains(search.toLowerCase())){
                    serieModelList.add(s);

                };
            }

            if (serieModelList.size() == 0){
                searchresultbg.setVisibility(View.VISIBLE);
                progresssrch.setVisibility(View.INVISIBLE);

            }else {
                searchresultbg.setVisibility(View.INVISIBLE);
                progresssrch.setVisibility(View.INVISIBLE);


            }

            result_list.setAdapter(new SearchAdapterMo(getActivity(),serieModelList));
        }else {
            Toast.makeText(getActivity(), "Just Wait ...", Toast.LENGTH_SHORT).show();
        }






    }
}