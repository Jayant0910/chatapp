package com.anass.jseriesadmin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anass.jseriesadmin.Models.UpdateModel;
import com.anass.jseriesadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddUpdate extends AppCompatActivity {

    RecyclerView update_rv;
    ImageView goback;
    RelativeLayout addupdterl;
    List<UpdateModel> updateModelList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

        update_rv = findViewById(R.id.update_rv);
        goback = findViewById(R.id.goback);
        addupdterl = findViewById(R.id.addupdterl);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addupdterl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogAddupdate();
            }
        });

        GetAllUpdates();

    }

    private void ShowDialogAddupdate() {
        Dialog quality_selector = new Dialog(AddUpdate.this);
        quality_selector.setCancelable(true);
        quality_selector.setContentView(R.layout.quality_selector_instagram);



        EditText linkpass = quality_selector.findViewById(R.id.linkofserver2);
        EditText versionup = quality_selector.findViewById(R.id.linkofserver3);
        RelativeLayout addup = quality_selector.findViewById(R.id.addepisodenn);


        addup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linkpass.getText().toString().isEmpty() || versionup.getText().toString().isEmpty()){
                    Toast.makeText(AddUpdate.this, "Add version and link", Toast.LENGTH_SHORT).show();
                }else {
                    AddUpdatetoserver(linkpass.getText().toString(),versionup.getText().toString());
                }
            }
        });



        quality_selector.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        quality_selector.show();
    }

    private void AddUpdatetoserver(String link, String version) {


        HashMap<String,String> hh = new HashMap<>();
        hh.put("link",link);
        hh.put("versionnumber",version);

        FirebaseDatabase.getInstance().getReference("updates").push().setValue(hh);

    }

    private void GetAllUpdates() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddUpdate.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        update_rv.setLayoutManager(linearLayoutManager);

        FirebaseDatabase.getInstance().getReference("updates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updateModelList = new ArrayList<>();

                for (DataSnapshot ds:snapshot.getChildren()) {
                    UpdateModel ud = ds.getValue(UpdateModel.class);
                    updateModelList.add(ud);
                }

                update_rv.setAdapter(new updateAdapter(updateModelList));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public class updateAdapter extends RecyclerView.Adapter<updateAdapter.MyViewHolder>{

        List<UpdateModel> updateModels;

        public updateAdapter(List<UpdateModel> updateModels) {
            this.updateModels = updateModels;
        }

        @NonNull
        @Override
        public updateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder( LayoutInflater.from(parent.getContext()).inflate(R.layout.update_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull updateAdapter.MyViewHolder holder, int position) {
            UpdateModel ud = updateModels.get(position);
            holder.numberofupdate.setText(ud.getVersionnumber()+"");
            holder.linkofupdate.setText(ud.getLink());

        }

        @Override
        public int getItemCount() {
            return updateModels.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView numberofupdate,linkofupdate;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                linkofupdate = itemView.findViewById(R.id.linkofupdate);
                numberofupdate = itemView.findViewById(R.id.numberofupdate);
            }
        }
    }
}