package com.anass.ninflix.Activities;

import static com.anass.ninflix.Config.config.Livelink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anass.ninflix.Adapters.ChaneelhometvAdapter;
import com.anass.ninflix.Models.TVModel;
import com.anass.ninflix.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import io.lindstrom.m3u8.model.AlternativeRendition;
import io.lindstrom.m3u8.model.MasterPlaylist;
import io.lindstrom.m3u8.model.MediaType;
import io.lindstrom.m3u8.model.Variant;
import io.lindstrom.m3u8.parser.MasterPlaylistParser;
import io.lindstrom.m3u8.parser.PlaylistParserException;

public class LiveChannel extends AppCompatActivity {

    //tv channel Change
    RecyclerView live_rv;
    ChaneelhometvAdapter chaneelhometvAdapter;
    List<TVModel> tvModels;
    ProgressBar liveprog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_channel);


        live_rv = findViewById(R.id.rectmovies);
        liveprog = findViewById(R.id.progreed);

        GetData();




    }

    private void GetData() {



        live_rv.setLayoutManager(new GridLayoutManager(LiveChannel.this,3));

        FirebaseDatabase.getInstance().getReference("tvchannels").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                liveprog.setVisibility(View.GONE);
                tvModels = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    TVModel comicsm = data.getValue(TVModel.class);

                    tvModels.add(comicsm);

                }


                chaneelhometvAdapter = new ChaneelhometvAdapter(tvModels,LiveChannel.this);
                live_rv.setAdapter(chaneelhometvAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}