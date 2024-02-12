package com.anass.ninflix.Adapters;


import static com.anass.ninflix.Activities.MyList.lista.favoritlmovie.LinkDb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.anass.ninflix.Activities.Auth.LoginTojcartoon;
import com.anass.ninflix.Activities.EpesodTAFASIL;
import com.anass.ninflix.Activities.ShowInfoActivity;
import com.anass.ninflix.Config.getComicsnextback;
import com.anass.ninflix.DB.JseriesDB;
import com.anass.ninflix.Models.CountinuModel;
import com.anass.ninflix.Models.EpesodModel;
import com.anass.ninflix.R;
import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CountinuAdapter extends RecyclerView.Adapter<CountinuAdapter.Myview> {

    List<CountinuModel> countinuModels;
    Context context;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference rexomnd;
    List<EpesodModel> eemodles;
    JseriesDB jdb;



    public CountinuAdapter(List<CountinuModel> countinuModels, Context context) {
        this.countinuModels = countinuModels;
        this.context = context;
        jdb = new JseriesDB(context);
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seire_item_countinu, parent, false);
        return new Myview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, int position) {

        CountinuModel w = countinuModels.get(position);


        int ff = position;

        holder.textView.setText(w.getEpetitle());
        Glide.with(holder.itemView.getContext()).load(w.image).into(holder.imageView);


        holder.progrecurr.setMax( Integer.parseInt(w.duration));
        holder.progrecurr.setProgress( Integer.parseInt(w.currnt));

        holder.gotoseries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginTojcartoon.PleaseWait.show(context);

                GetEpes(w.link,holder,w);




            }
        });
        holder.gotoDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), ShowInfoActivity.class);
                sendDataToDetailsActivity.putExtra("title",w.title);
                sendDataToDetailsActivity.putExtra("id",w.getId());
                sendDataToDetailsActivity.putExtra("cover",w.cover);
                sendDataToDetailsActivity.putExtra("poste",w.image);
                sendDataToDetailsActivity.putExtra("link",w.link);
                sendDataToDetailsActivity.putExtra("postkey",w.postkey);
                sendDataToDetailsActivity.putExtra("age",w.age);
                sendDataToDetailsActivity.putExtra("studio",w.studio);
                sendDataToDetailsActivity.putExtra("date",w.date);
                sendDataToDetailsActivity.putExtra("rat",w.rat);
                sendDataToDetailsActivity.putExtra("desc",w.desc);
                sendDataToDetailsActivity.putExtra("almotarjim",w.almotarjim);
                sendDataToDetailsActivity.putExtra("cast",w.cast);
                sendDataToDetailsActivity.putExtra("rat",w.rat);
                sendDataToDetailsActivity.putExtra("mortabit",w.epesodes);
                sendDataToDetailsActivity.putExtra("where",w.almotarjim);
                sendDataToDetailsActivity.putExtra("place",w.seasons);
                sendDataToDetailsActivity.putExtra("tasnif",w.tasnifh);
                sendDataToDetailsActivity.putExtra("views",w.views);
                sendDataToDetailsActivity.putExtra("isintent","1");


                holder.itemView.getContext().startActivity(sendDataToDetailsActivity);


            }
        });


        holder.removefromdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(w.id,ff);
            }
        });



    }

    private void showConfirmationDialog(String postkey, int ff) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Inflate the layout for the dialog
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);

        // Set the title and message for the dialog
        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        titleTextView.setText("Confirmation Delet");

        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
        messageTextView.setText("Do you want to continue?");

        // Set the Cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        // Set the Continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Continue button
                // Do something here...
                countinuModels.remove(ff);
                boolean ww = jdb.DeletCountinuDB(postkey);
                if (ww){
                    Toast.makeText(context, "Delet Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Delet not Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set the custom layout for the dialog
        builder.setView(dialogView);

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void GetEpes(String link, Myview holder,CountinuModel e) {

        eemodles = new ArrayList<>();

        LinkDb.child(link).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot postsnap:snapshot.getChildren()){


                    EpesodModel epesodModel = postsnap.getValue(EpesodModel.class);


                    eemodles.add(epesodModel);

                }

                getComicsnextback.Epesodes.clear();
                getComicsnextback.Epesodes = eemodles;
                LoginTojcartoon.PleaseWait.dismiss();
                GoToEpes(holder,e);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }

    private void GoToEpes(Myview holder, CountinuModel w) {

        Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), EpesodTAFASIL.class);
        sendDataToDetailsActivity.putExtra("epetitle",w.getEpetitle());
        sendDataToDetailsActivity.putExtra("fhd",w.getServer1());
        sendDataToDetailsActivity.putExtra("hd",w.getServer2());
        sendDataToDetailsActivity.putExtra("sd",w.getServer3());
        sendDataToDetailsActivity.putExtra("key",w.getPostkey());
        sendDataToDetailsActivity.putExtra("name",w.getTitle());
        sendDataToDetailsActivity.putExtra("position",w.getPosit());
        sendDataToDetailsActivity.putExtra("title",w.getTitle());
        sendDataToDetailsActivity.putExtra("id",w.getId());
        sendDataToDetailsActivity.putExtra("cover",w.getCover());
        sendDataToDetailsActivity.putExtra("poste",w.getId());
        sendDataToDetailsActivity.putExtra("link",w.getLink());
        sendDataToDetailsActivity.putExtra("posterid",w.getPostkey());
        sendDataToDetailsActivity.putExtra("age",w.getAge());
        sendDataToDetailsActivity.putExtra("studio",w.getStudio());
        sendDataToDetailsActivity.putExtra("date",w.getDate());
        sendDataToDetailsActivity.putExtra("rat",w.getRat());
        sendDataToDetailsActivity.putExtra("desc",w.getDesc());
        sendDataToDetailsActivity.putExtra("almotarjim",w.getAlmotarjim());
        sendDataToDetailsActivity.putExtra("cast",w.getCast());
        sendDataToDetailsActivity.putExtra("mortabit",w.getEpesodes());
        sendDataToDetailsActivity.putExtra("where",w.getAlmotarjim());
        sendDataToDetailsActivity.putExtra("place",w.getSeasons());
        sendDataToDetailsActivity.putExtra("tasnif",w.getTasnifh());
        sendDataToDetailsActivity.putExtra("views",w.getViews());

        holder.itemView.getContext().startActivity(sendDataToDetailsActivity);
    }

    @Override
    public int getItemCount() {
        return countinuModels.size();
    }

    public class Myview extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        LinearProgressIndicator progrecurr;
        ConstraintLayout gotoseries;
        RelativeLayout removefromdb,gotoDt;


        public Myview(@NonNull View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.imageprev);
            textView = itemView.findViewById(R.id.season_name);
            progrecurr = itemView.findViewById(R.id.progrecurr);
            gotoseries = itemView.findViewById(R.id.gotoseries);
            removefromdb = itemView.findViewById(R.id.removefromdb);
            gotoDt = itemView.findViewById(R.id.gotoDt);
        }
    }
}
