package com.anass.jseriesadmin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anass.jseriesadmin.Models.SerieModel;
import com.anass.jseriesadmin.Models.UserModel;
import com.anass.jseriesadmin.R;

import java.util.List;

public class Allusers extends AppCompatActivity {


    ImageView goback;
    RecyclerView update_rv;
    List<UserModel> serieModelList;
    ProgressBar progg;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allusers);

        goback = findViewById(R.id.goback);
        update_rv = findViewById(R.id.update_rv);
        progg = findViewById(R.id.progg);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}