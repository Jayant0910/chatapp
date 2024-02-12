package com.anass.jseriesadmin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anass.jseriesadmin.Adapters.SeriesAdapter;
import com.anass.jseriesadmin.Models.SerieModel;
import com.anass.jseriesadmin.Models.UpdateModel;
import com.anass.jseriesadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Allseries extends AppCompatActivity {

    String type;
    ImageView goback;
    RecyclerView update_rv;
    List<SerieModel> serieModelList;
    TextView typetxt;
    ProgressBar progg;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allseries);

        type = getIntent().getStringExtra("type");
        goback = findViewById(R.id.goback);
        update_rv = findViewById(R.id.update_rv);
        progg = findViewById(R.id.progg);
        typetxt = findViewById(R.id.typetxt);


        if (type.equals("serie")){
            typetxt.setText("All Series");
        }else if (type.equals("anime")){
            typetxt.setText("All Animes");
        }else {
            typetxt.setText("All Movies");
        }

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        GetAllSeries();
    }

    private void GetAllSeries() {


        update_rv.setLayoutManager(new GridLayoutManager(Allseries.this,3));

        FirebaseDatabase.getInstance().getReference("allseriesandmovies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serieModelList = new ArrayList<>();
                progg.setVisibility(View.GONE);

                for (DataSnapshot dd:snapshot.getChildren()) {
                    SerieModel ss = dd.getValue(SerieModel.class);
                    if (ss.getPlace().contains(type)){
                        serieModelList.add(ss);
                    }

                }

                update_rv.setAdapter(new SeriesAdapter(Allseries.this,serieModelList));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}