package com.anass.ninflix.Fragments;

import static com.anass.ninflix.Activities.MyList.lista.favoritlmovie.allseriesandmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.anass.ninflix.Activities.LiveChannel;
import com.anass.ninflix.Adapters.CategoriesAdapter;
import com.anass.ninflix.Adapters.ChaneelhometvAdapter;
import com.anass.ninflix.Adapters.CountinuAdapter;
import com.anass.ninflix.Adapters.SeriesAdapter;
import com.anass.ninflix.Adapters.SliderAdapter;
import com.anass.ninflix.Config.Function;
import com.anass.ninflix.DB.JseriesDB;
import com.anass.ninflix.JPLAYER.LivePlayer;
import com.anass.ninflix.Models.CatigoriesModel;
import com.anass.ninflix.Models.CountinuModel;
import com.anass.ninflix.Models.SerieModel;
import com.anass.ninflix.Models.TVModel;
import com.anass.ninflix.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class home extends Fragment {


    //Database Firestore
    FirebaseFirestore mFirebaseFirestore;


    //Ads
    private StartAppAd startAppAd;



    //Slider Change
    SliderView sliderView;
    SliderAdapter sliderAdapter;
    List<SerieModel> slider;
    ProgressBar pogsslid;

    //Categories Change
    RecyclerView catigo_rv;
    CategoriesAdapter categoriesAdapter;
    List<CatigoriesModel> catigoriesModels;
    ProgressBar pogstud;


    //MyList
    RecyclerView pin_rv;
    SeriesAdapter mylistadapter;
    List<SerieModel> mylistmodel;
    ProgressBar progpin;
    RelativeLayout pin_rlt;


    //Trende
    RecyclerView trend_rv;
    SeriesAdapter trendadapter;
    List<SerieModel> trendmodel;
    ProgressBar progmoswt;


    //Anime
    RecyclerView anime_rv;
    SeriesAdapter animeadapter;
    List<SerieModel> animemodel;
    ProgressBar progmoswtco;

    //Top Movie
    RecyclerView topmovie_rv;
    SeriesAdapter topmovieadapter;
    List<SerieModel> topmoviemodel;
    ProgressBar progmoswtmovi;


    //Top series
    RecyclerView seriewatched_rv;
    SeriesAdapter seriewatchedadapter;
    List<SerieModel> seriewatchedmodel;
    ProgressBar proganew;


    //Thhis Year
    RecyclerView newyera_rv;
    SeriesAdapter newadapter;
    List<SerieModel> newmodel;
    ProgressBar progayewar;

    //Countinu watch
    RecyclerView count_rv;
    CountinuAdapter countinuAdapter;
    List<CountinuModel> countinuModels;
    ProgressBar progcont;
    RelativeLayout continowach;


    //tv channel Change
    RecyclerView live_rv;
    ChaneelhometvAdapter chaneelhometvAdapter;
    List<TVModel> tvModels;
    ProgressBar liveprog;


    //wid
    ImageView livetv;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        Banner banner = view.findViewById(R.id.startAppBanner);
        sliderView = view.findViewById(R.id.slider);
        pogsslid = view.findViewById(R.id.pogsslid);
        pogstud = view.findViewById(R.id.pogstud);
        catigo_rv = view.findViewById(R.id.catigo_rv);
        progpin = view.findViewById(R.id.progpin);
        pin_rv = view.findViewById(R.id.pin_rv);
        pin_rlt = view.findViewById(R.id.pin_rlt);
        progmoswt = view.findViewById(R.id.progmoswt);
        trend_rv = view.findViewById(R.id.trend_rv);
        progmoswtco = view.findViewById(R.id.progmoswtco);
        anime_rv = view.findViewById(R.id.anime_rv);
        topmovie_rv = view.findViewById(R.id.topmovie_rv);
        progmoswtmovi = view.findViewById(R.id.progmoswtmovi);
        proganew = view.findViewById(R.id.proganew);
        seriewatched_rv = view.findViewById(R.id.seriewatched_rv);
        progayewar = view.findViewById(R.id.progayewar);
        newyera_rv = view.findViewById(R.id.newyera_rv);
        count_rv = view.findViewById(R.id.count_rv);
        progcont = view.findViewById(R.id.progcont);
        continowach = view.findViewById(R.id.continowach);
        livetv = view.findViewById(R.id.livetv);
        liveprog = view.findViewById(R.id.liveprog);
        live_rv = view.findViewById(R.id.live_rv);


        //Fixed
        banner.loadAd();


        sliderAdapter = new SliderAdapter(getContext(),slider);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        renewItem(sliderView);

        mFirebaseFirestore = FirebaseFirestore.getInstance();

        GetSlider();
        GetCategories();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            pin_rlt.setVisibility(View.VISIBLE);
            GetMyList();
        }else{
            pin_rlt.setVisibility(View.GONE);
        }
        getTrends();
        getAnimes();
        getTopMovies();
        getTopSeire();
        getThisYear();
        getCountinuWatching();
        getChannels();

        livetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LiveChannel.class));
            }
        });

        return  view;
    }


    private void getChannels() {

        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(RecyclerView.HORIZONTAL);

        live_rv.setLayoutManager(ll);

        FirebaseDatabase.getInstance().getReference("tvchannels").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                liveprog.setVisibility(View.GONE);
                tvModels = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    TVModel comicsm = data.getValue(TVModel.class);

                    tvModels.add(comicsm);

                }


                chaneelhometvAdapter = new ChaneelhometvAdapter(tvModels,getContext());
                live_rv.setAdapter(chaneelhometvAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getCountinuWatching() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        count_rv.setLayoutManager(linearLayoutManager);

        countinuModels = new ArrayList<>();

        Cursor cursor = new JseriesDB(getActivity()).GetCountinuDB();
        progcont.setVisibility(View.GONE);

        while (cursor.moveToNext()){
            CountinuModel comicsModel = new CountinuModel();
            comicsModel.setId(cursor.getString(0));
            comicsModel.setTitle(cursor.getString(1));
            comicsModel.setPostkey(cursor.getString(2));
            comicsModel.setImage(cursor.getString(3));
            comicsModel.setDate(cursor.getString(4));
            comicsModel.setTasnifh(cursor.getString(5));
            comicsModel.setStudio(cursor.getString(6));
            comicsModel.setAge(cursor.getString(7));
            comicsModel.setDesc(cursor.getString(8));
            comicsModel.setAlmotarjim(cursor.getString(9));
            comicsModel.setSeasons(cursor.getString(10));
            comicsModel.setCast(cursor.getString(11));
            comicsModel.setEpesodes(cursor.getString(12));
            comicsModel.setLink(cursor.getString(13));
            comicsModel.setCurrnt(cursor.getString(15));
            comicsModel.setDuration(cursor.getString(16));
            comicsModel.setPosit(cursor.getString(23));
            comicsModel.setEpetitle(cursor.getString(14));
            comicsModel.setServer1(cursor.getString(18));
            comicsModel.setServer2(cursor.getString(19));
            comicsModel.setServer3(cursor.getString(20));
            comicsModel.setPostkey(cursor.getString(22));

            countinuModels.add(comicsModel);
        }



        if (countinuModels.size() <= 0){
            continowach.setVisibility(View.GONE);
        }

        countinuAdapter = new CountinuAdapter(countinuModels,getActivity());
        count_rv.setAdapter(countinuAdapter);
    }

    private void getThisYear() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);


        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(RecyclerView.HORIZONTAL);

        newyera_rv.setLayoutManager(ll);

        allseriesandmovies.limitToFirst(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progayewar.setVisibility(View.GONE);
                newmodel = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    SerieModel comicsm = data.getValue(SerieModel.class);


                    if (Function.ToInterger(comicsm.getYear()) == currentYear){
                        newmodel.add(comicsm);
                    }


                }

                newadapter = new SeriesAdapter(getContext(),newmodel,0);
                newyera_rv.setAdapter(newadapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getTopSeire() {


        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(RecyclerView.HORIZONTAL);

        seriewatched_rv.setLayoutManager(ll);

        allseriesandmovies.limitToFirst(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                proganew.setVisibility(View.GONE);
                seriewatchedmodel = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    SerieModel comicsm = data.getValue(SerieModel.class);

                    if (comicsm.getPlace().contains("serie")){
                        seriewatchedmodel.add(comicsm);
                    }




                }

                seriewatchedadapter = new SeriesAdapter(getContext(),seriewatchedmodel,1);
                seriewatched_rv.setAdapter(seriewatchedadapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTopMovies() {

        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(RecyclerView.HORIZONTAL);

        topmovie_rv.setLayoutManager(ll);

        allseriesandmovies.orderByChild("views").limitToFirst(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progmoswtmovi.setVisibility(View.GONE);
                topmoviemodel = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    SerieModel comicsm = data.getValue(SerieModel.class);


                    if (comicsm.getPlace().contains("movie")){
                        topmoviemodel.add(comicsm);
                    }



                }

                topmovieadapter = new SeriesAdapter(getContext(),topmoviemodel,0);
                topmovie_rv.setAdapter(topmovieadapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getAnimes() {
        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(RecyclerView.HORIZONTAL);

        anime_rv.setLayoutManager(ll);

        allseriesandmovies.limitToFirst(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progmoswtco.setVisibility(View.GONE);
                animemodel = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    SerieModel comicsm = data.getValue(SerieModel.class);


                    if (comicsm.getPlace().contains("anime")){
                        animemodel.add(comicsm);
                    }



                }

                Collections.shuffle(animemodel);
                animeadapter = new SeriesAdapter(getContext(),animemodel,0);
                anime_rv.setAdapter(animeadapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getTrends() {
        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(RecyclerView.HORIZONTAL);

        trend_rv.setLayoutManager(ll);

        allseriesandmovies.limitToFirst(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progmoswt.setVisibility(View.GONE);
                trendmodel = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    SerieModel comicsm = data.getValue(SerieModel.class);


                    if (comicsm.getPlace().contains("trend")){
                        trendmodel.add(comicsm);
                    }



                }

                Collections.shuffle(trendmodel);
                trendadapter = new SeriesAdapter(getContext(),trendmodel,0);
                trend_rv.setAdapter(trendadapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void GetMyList() {

        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(RecyclerView.HORIZONTAL);
        pin_rv.setLayoutManager(ll);
        mylistmodel = new ArrayList<>();


        Cursor cursor = new JseriesDB(getActivity()).GetMyListDB();

        while (cursor.moveToNext()){
            SerieModel comicsModel = new SerieModel();
            comicsModel.setId(cursor.getString(0));
            comicsModel.setTitle(cursor.getString(1));
            comicsModel.setSerie_id(cursor.getString(2));
            comicsModel.setPoster(cursor.getString(3));
            comicsModel.setYear(cursor.getString(4));
            comicsModel.setGener(cursor.getString(5));
            comicsModel.setCountry(cursor.getString(6));
            comicsModel.setAge(cursor.getString(7));
            comicsModel.setStory(cursor.getString(8));
            comicsModel.setPlace(cursor.getString(9));
            comicsModel.setCast(cursor.getString(10));
            comicsModel.setOther_season_id(cursor.getString(11));
            comicsModel.setLink_id(cursor.getString(12));
            comicsModel.setViews_db(cursor.getString(13));
            comicsModel.setRating_db(cursor.getString(14));
            comicsModel.setUserid(cursor.getString(15));


            mylistmodel.add(comicsModel);
        }

        if (mylistmodel.size() > 0){
            progpin.setVisibility(View.GONE);
            mylistadapter = new SeriesAdapter(getActivity(), mylistmodel,0);
            pin_rv.setAdapter(mylistadapter);

        }else pin_rlt.setVisibility(View.GONE);


  /*      mFirebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                collection("mylist").
                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



                        for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                            SerieModel sm = ds.toObject(SerieModel.class);
                            mylistmodel.add(sm);
                        }

                        if (mylistmodel.size() > 0){

                            mylistadapter = new SeriesAdapter(getActivity(), mylistmodel);
                            pin_rv.setAdapter(mylistadapter);

                        }else pin_rlt.setVisibility(View.GONE);




                    }
                });*/
                /*.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<SerieModel> downloadInfoList = task.getResult().toObjects(SerieModel.class);
                                for (SerieModel downloadInfo : downloadInfoList) {
                                    mylistmodel.add(downloadInfo);
                                }

                                mylistadapter = new SeriesAdapter(getActivity(), mylistmodel);
                                pin_rv.setAdapter(mylistadapter);
                            }else {

                            }
                        }

                    }
                });*/


    }

    private void GetCategories() {
        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(RecyclerView.HORIZONTAL);

        catigo_rv.setLayoutManager(ll);

        FirebaseDatabase.getInstance().getReference("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pogstud.setVisibility(View.GONE);
                catigoriesModels = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    CatigoriesModel comicsm = data.getValue(CatigoriesModel.class);

                    catigoriesModels.add(comicsm);

                }

                Collections.shuffle(slider);
                categoriesAdapter = new CategoriesAdapter(getContext(),catigoriesModels);
                catigo_rv.setAdapter(categoriesAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void GetSlider() {


        allseriesandmovies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pogsslid.setVisibility(View.GONE);
                slider = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    SerieModel comicsm = data.getValue(SerieModel.class);

                    slider.add(comicsm);

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
    private void renewItem(SliderView sliderView) {

        slider = new ArrayList<>();
        SerieModel dataItems = new SerieModel();
        slider.add(dataItems);

        sliderAdapter.renewItem(slider);
        sliderAdapter.deleteItems(0);

    }


}