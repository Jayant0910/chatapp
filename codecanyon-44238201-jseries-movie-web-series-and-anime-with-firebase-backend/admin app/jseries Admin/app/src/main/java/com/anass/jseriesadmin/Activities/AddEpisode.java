package com.anass.jseriesadmin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anass.jseriesadmin.MainActivity;
import com.anass.jseriesadmin.Models.EpesodModel;
import com.anass.jseriesadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEpisode extends AppCompatActivity {


    RelativeLayout addepisodenn;
    EditText titleofepisod,linkofserver1,linkofserver2,linkofserver3,idofepisod;
    ImageView goback;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_episode);

        addepisodenn = findViewById(R.id.addepisodenn);
        titleofepisod = findViewById(R.id.titleofepisod);
        linkofserver1 = findViewById(R.id.linkofserver1);
        linkofserver2 = findViewById(R.id.linkofserver2);
        linkofserver3 = findViewById(R.id.linkofserver3);
        idofepisod = findViewById(R.id.idofepisod);
        goback = findViewById(R.id.goback);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        addepisodenn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idofepisod.getText().toString().isEmpty()){
                    Toast.makeText(AddEpisode.this, "The link_id is requierd", Toast.LENGTH_SHORT).show();
                }else {

                    AddEpisodetoServer();

                }

            }
        });
    }

    private void AddEpisodetoServer() {
        String title = titleofepisod.getText().toString();
        String idofepi = idofepisod.getText().toString();
        String server1 = linkofserver1.getText().toString();
        String server2 = linkofserver2.getText().toString();
        String server3 = linkofserver3.getText().toString();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("links").child(idofepi).push();

        EpesodModel epesodModel = new EpesodModel();
        epesodModel.setEpetitle(title);
        epesodModel.setPostkey(db.getKey());
        epesodModel.setServer1(server1);
        epesodModel.setServer2(server2);
        epesodModel.setServer3(server3);

        db.setValue(epesodModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(AddEpisode.this, "Added Successfuly ... check your DB", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddEpisode.this, "Not Successfuly ... : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });;


    }
}