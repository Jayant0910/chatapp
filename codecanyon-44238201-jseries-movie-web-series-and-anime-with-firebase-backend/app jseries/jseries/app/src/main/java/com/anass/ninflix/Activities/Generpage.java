package com.anass.ninflix.Activities;

import static com.anass.ninflix.Activities.MyList.lista.favoritlmovie.allseriesandmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anass.ninflix.Adapters.SeriesAdapter;
import com.anass.ninflix.Models.SerieModel;
import com.anass.ninflix.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Generpage extends AppCompatActivity {


    TextView studiotitle;
    RecyclerView result_list;
    String _gener;
    ProgressBar progreed;
    List<SerieModel> allseriesmodel;
    SeriesAdapter seriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generpage);

        studiotitle = findViewById(R.id.studiotitle);
        result_list = findViewById(R.id.result_list);
        progreed = findViewById(R.id.progreed);

        _gener = getIntent().getStringExtra("gener");
        studiotitle.setText(_gener);

        GetAllGeners(_gener);

    }


    private void GetAllGeners(String gener) {

        result_list.setLayoutManager(new GridLayoutManager(Generpage.this,3));
        result_list.setNestedScrollingEnabled(false);

        allseriesandmovies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progreed.setVisibility(View.GONE);
                allseriesmodel = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    SerieModel comicsm = data.getValue(SerieModel.class);

                    if (comicsm.getGener().toLowerCase().contains(gener.toLowerCase())){
                        allseriesmodel.add(comicsm);
                    }



                }


                result_list.setAdapter(new SeriesAdapter(Generpage.this,allseriesmodel,0));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}