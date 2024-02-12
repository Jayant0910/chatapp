package com.anass.ninflix.Fragments;

import static com.anass.ninflix.Activities.MyList.lista.favoritlmovie.allseriesandmovies;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.anass.ninflix.Adapters.SeriesAdapter;
import com.anass.ninflix.Adapters.SliderAdapter;
import com.anass.ninflix.Models.SerieModel;
import com.anass.ninflix.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class series extends Fragment {


    SliderView sliderView;
    RecyclerView rectmovies;
    ProgressBar progreed,pogsslid;
    List<SerieModel> Cartoonssearch,slider;
    SliderAdapter sliderAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_series, container, false);

        rectmovies = view.findViewById(R.id.rectmovies);
        progreed = view.findViewById(R.id.progreed);
        pogsslid = view.findViewById(R.id.pogsslid);
        sliderView = view.findViewById(R.id.slider);

        sliderAdapter = new SliderAdapter(getContext(),slider);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        renewItem(sliderView);

        GetSlider();
        GetData();




        return view;
    }

    private void GetSlider() {


        allseriesandmovies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pogsslid.setVisibility(View.GONE);
                slider = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    SerieModel comicsm = data.getValue(SerieModel.class);
                    if (!comicsm.getPlace().contains("movie")) {
                        slider.add(comicsm);
                    }
                }

                Collections.shuffle(slider);
                sliderAdapter = new SliderAdapter(getContext(),slider);
                sliderView.setSliderAdapter(sliderAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void GetData() {

        rectmovies.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rectmovies.setNestedScrollingEnabled(false);

        allseriesandmovies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progreed.setVisibility(View.GONE);
                Cartoonssearch = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    SerieModel comicsm = data.getValue(SerieModel.class);

                    if (!comicsm.getPlace().contains("movie")){
                        Cartoonssearch.add(comicsm);
                    }



                }

                Collections.reverse(Cartoonssearch);


                rectmovies.setAdapter(new SeriesAdapter(getActivity(),Cartoonssearch,0));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void renewItem(SliderView sliderView) {

        slider = new ArrayList<>();
        SerieModel dataItems = new SerieModel();
        slider.add(dataItems);

        sliderAdapter.renewItem(slider);
        sliderAdapter.deleteItems(0);

    }


}