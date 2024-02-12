package com.anass.jseriesadmin;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.anass.jseriesadmin.Activities.AddEpisode;
import com.anass.jseriesadmin.Activities.AddUpdate;
import com.anass.jseriesadmin.Activities.Additem;
import com.anass.jseriesadmin.Activities.Allseries;
import com.anass.jseriesadmin.Activities.Allusers;
import com.anass.jseriesadmin.Activities.usersplans;
import com.anass.jseriesadmin.Models.SerieModel;
import com.anass.jseriesadmin.Models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    //Widgets
    TextView usercounttxt,seriecounttxt,moviescounttxt,animescounttxt,planscounttxt;
    RelativeLayout additem,addepisode,addupdate,allusersrel,goseries,gomovies,
            goanimes,goplans;
    SwipeRefreshLayout swip;


    private long pressedTime;
    private Toast backToast;
    private SharedPreferences sp;

    FragmentManager fragmentManager;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;

    //list
    List<SerieModel> serieModels,moviemodel,animesmod;
    List<UserModel> userplansModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        sp = getSharedPreferences("admin", MODE_PRIVATE);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //Init
        InitialViews();

        //GET COUNT
        GetUsersCont();
        GetSeriesCont();
        GetMoviesCont();
        GetAnimeCont();
        GetUsersWithPlanesCont();


        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetUsersCont();
                GetSeriesCont();
                GetMoviesCont();
                GetAnimeCont();
                GetUsersWithPlanesCont();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swip.setRefreshing(false);

                        //Do your work
                    }
                },1500);

            }
        });





    }

    public void InitialViews() {

        usercounttxt = findViewById(R.id.usercounttxt);
        seriecounttxt = findViewById(R.id.seriecounttxt);
        moviescounttxt = findViewById(R.id.moviescounttxt);
        animescounttxt = findViewById(R.id.animescounttxt);
        swip = findViewById(R.id.swip);
        planscounttxt = findViewById(R.id.planscounttxt);
        additem = findViewById(R.id.additem);
        addepisode = findViewById(R.id.addepisode);
        addupdate = findViewById(R.id.addupdate);
        allusersrel = findViewById(R.id.allusersrel);
        goseries = findViewById(R.id.goseries);
        gomovies = findViewById(R.id.gomovies);
        goanimes = findViewById(R.id.goanimes);
        goplans = findViewById(R.id.goplans);

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSeriesToFirebase();

            }
        });
        addepisode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddEpisode.class));
            }
        });
        addupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddUpdate.class));
            }
        });
      /*  allusersrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Allusers.class));
            }
        });*/
        goseries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Allseries.class).putExtra("type","serie"));
            }
        });
        gomovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Allseries.class).putExtra("type","movie"));
            }
        });
        goanimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Allseries.class).putExtra("type","anime"));
            }
        });
       /* goplans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, usersplans.class));
            }
        });*/

    }

    private void AddSeriesToFirebase() {

        DatabaseReference db = firebaseDatabase.getReference("allseriesandmovies").push();
        SerieModel serieModel = new SerieModel();
        serieModel.setSerie_id(db.getKey());
        serieModel.setId(db.getKey());
        serieModel.setYoutubetrailerid("");
        serieModel.setViews(566);
        serieModel.setPoster("Add poster Image link");
        serieModel.setYear("2005");
        serieModel.setPlace("serie");
        serieModel.setGener("action,comedy");
        serieModel.setCountry("USA");
        serieModel.setAge("+15");
        serieModel.setStory("Add Story ");
        serieModel.setOther_season_id("squidgame");
        serieModel.setTitle("Squid Game S1");
        serieModel.setCast("squidgame");
        serieModel.setLink_id("squidgamelink");


        db.setValue(serieModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(MainActivity.this, "Added Successfuly ... check your DB", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Not Successfuly ... : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetUsersWithPlanesCont() {

        firebaseFirestore.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                userplansModels = new ArrayList<>();

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    if (!userModel.getPlanmode().equals("none")){
                        userplansModels.add(userModel);
                    }

                }

                planscounttxt.setText(userplansModels.size()+"");


            }
        });
    }

    private void GetAnimeCont() {
        firebaseDatabase.getReference("allseriesandmovies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                animesmod = new ArrayList<>();

                for (DataSnapshot dd:snapshot.getChildren()) {
                    SerieModel ss = dd.getValue(SerieModel.class);
                    if (ss.getPlace().contains("anime")){
                        animesmod.add(ss);
                    }

                }

                animescounttxt.setText(animesmod.size()+"");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetMoviesCont() {
        firebaseDatabase.getReference("allseriesandmovies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                moviemodel = new ArrayList<>();

                for (DataSnapshot dd:snapshot.getChildren()) {
                    SerieModel ss = dd.getValue(SerieModel.class);
                    if (ss.getPlace().contains("movie")){
                        moviemodel.add(ss);
                    }

                }

                moviescounttxt.setText(moviemodel.size()+"");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetUsersCont() {

        firebaseFirestore.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                usercounttxt.setText(queryDocumentSnapshots.getDocuments().size()+"");

            }
        });

        
    }

    private void GetSeriesCont() {


        firebaseDatabase.getReference("allseriesandmovies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serieModels = new ArrayList<>();

                for (DataSnapshot dd:snapshot.getChildren()) {
                    SerieModel ss = dd.getValue(SerieModel.class);
                    if (ss.getPlace().contains("serie")){
                        serieModels.add(ss);
                    }

                }

                seriecounttxt.setText(serieModels.size()+"");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            finish();
            return;
        } else {
            backToast = Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT);
            backToast.show();
        }


        pressedTime = System.currentTimeMillis();
    }


}