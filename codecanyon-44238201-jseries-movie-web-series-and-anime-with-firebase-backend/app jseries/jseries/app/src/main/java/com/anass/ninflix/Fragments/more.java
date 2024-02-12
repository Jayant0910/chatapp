package com.anass.ninflix.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Browser;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anass.ninflix.Activities.Auth.LoginTojcartoon;
import com.anass.ninflix.Activities.Auth.MyProfile;
import com.anass.ninflix.Activities.Jdownload.DownloadList;
import com.anass.ninflix.Activities.MyList.Mylist;
import com.anass.ninflix.Activities.Premium;
import com.anass.ninflix.Activities.Settings.SettingsJcartoon;
import com.anass.ninflix.R;
import com.google.firebase.auth.FirebaseAuth;


public class more extends Fragment {


    LinearLayout mylistgo,mywaitingd;
    RelativeLayout gositting,gototelegr,gotosite,goplans;
    TextView loginanad;
    int Count = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);


        mylistgo = view.findViewById(R.id.mylistgo);
        mywaitingd = view.findViewById(R.id.mywaitingd);
        gotosite = view.findViewById(R.id.gotosite);
        gositting = view.findViewById(R.id.gositting);
        gototelegr = view.findViewById(R.id.gototelegr);
        goplans = view.findViewById(R.id.goplans);
        loginanad = view.findViewById(R.id.loginanad);

        /*SerieModel serieModel = new SerieModel();
        serieModel.setSerie_id(allseriesandmovies.push().getKey());
        serieModel.setId(allseriesandmovies.getKey());
        serieModel.setYoutubetrailerid("");
        serieModel.setViews(566);
        serieModel.setPoster("Fdhfd");
        serieModel.setYear("2005");
        serieModel.setPlace("serie");
        serieModel.setGener("action,comedy");
        serieModel.setCountry("Netflix");
        serieModel.setAge("+15");
        serieModel.setStory("fdfo woowr kdjfkw");
        serieModel.setOther_season_id("squidgame");
        serieModel.setTitle("Squid Game S1");
        serieModel.setCast("squidgame");
        serieModel.setLink_id("squidgamelink");*/




     /*   Cast cast = new Cast();
        cast.setCast_id("squidgame");
        cast.setName("Lee Jung-Jae");
        cast.setPoster("https://asianwiki.com/images/2/21/Lee_Jung-Jae-Assassiantion-GV.jpg");
        cast.setChrachter_id(CastDb.push().getKey());


        CastDb.child("squidgame").push().setValue(cast).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Count++;
            }
        });*/

        CheckIfLogin();


        mylistgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Mylist.class));
            }
        });
        mywaitingd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DownloadList.class));
            }
        });
        gositting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingsJcartoon.class));
            }
        });
        goplans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Premium.class));
            }
        });
        gototelegr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tgintent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://t.me/jycartoon"));
                tgintent.setPackage("org.telegram.messenger");
                startActivity(tgintent);

            }
        });
        gotosite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString = "https://jcartoon.hyperwatching.com/";
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent1.putExtra(Browser.EXTRA_APPLICATION_ID, "com.android.chrome");
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setPackage("com.android.chrome");
                startActivity(intent1);
            }
        });

        return view;
    }

    private void CheckIfLogin() {

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            loginanad.setText("Login Now");
            loginanad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), LoginTojcartoon.class));
                }
            });
        }else {
            loginanad.setText("My Account");
            loginanad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), MyProfile.class).putExtra("userid",FirebaseAuth.getInstance().getCurrentUser().getUid()));
                }
            });
        }

    }
}