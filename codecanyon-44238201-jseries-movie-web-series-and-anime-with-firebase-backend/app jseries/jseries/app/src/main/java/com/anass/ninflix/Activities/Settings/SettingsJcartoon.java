package com.anass.ninflix.Activities.Settings;


import static com.anass.ninflix.Activities.MyList.Mylist.changestatucolor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;


import com.anass.ninflix.R;

import java.io.File;
import java.util.ArrayList;

public class SettingsJcartoon extends AppCompatActivity {

    public static String FACEBOOK_URL = "https://www.facebook.com/jycartoon";
    public static String FACEBOOK_PROFI = "https://www.facebook.com/elkadianass";
    public static final String SETTINGS_NAME = "SETTINGS_COMICS";
    public static final String SETTINGS_LIBRARY_DIR = "SETTINGS_LIBRARY_DIR";


    //Save the setting
    private SharedPreferences sp;

    int playerselect,downcartoonselect,downcomicselect,choseplacesaveselect;
    boolean replayklike,newfollow,newcartoon,newcomics,howfavolist,thiore,recomended,pagenumber,visiblmore,toleft;


    ImageView gobback;
    RelativeLayout myaccanass,showfacebookpage,callus;
    Spinner choseplayer,chosedownlo,chosedownlocomics,choseplacesave;


    ArrayList<String> storageDirectories = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        changestatucolor(this);
        setContentView(R.layout.settings_jcartoon);

        myaccanass = findViewById(R.id.myaccanass);
        showfacebookpage = findViewById(R.id.showfacebookpage);
        callus = findViewById(R.id.callus);
        gobback = findViewById(R.id.gobback);
        choseplacesave = findViewById(R.id.choseplacesave);

        gobback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //saved setting
        playerselect = sp.getInt("playerselect",0);
        downcartoonselect = sp.getInt("downcartoonselect",0);
        downcomicselect = sp.getInt("downcomicselect",0);
        choseplacesaveselect = sp.getInt("choseplacesaveselect",0);

        //chaked
        replayklike = sp.getBoolean("replayklike",true);
        newfollow = sp.getBoolean("newfollow",true);
        newcartoon = sp.getBoolean("newcartoon",false);
        newcomics = sp.getBoolean("newcomics",false);
        howfavolist = sp.getBoolean("howfavolist",false);
        thiore = sp.getBoolean("thiore",true);
        recomended = sp.getBoolean("recomended",true);
        pagenumber = sp.getBoolean("pagenumber",true);
        visiblmore = sp.getBoolean("visiblmore",false);
        toleft = sp.getBoolean("toleft",true);



        GetListofStoreage();




        //Spinners
        choseplayer = findViewById(R.id.choseplayer);
        chosedownlo = findViewById(R.id.chosedownlo);
        chosedownlocomics = findViewById(R.id.chosedownlocomics);


        setupSpinners();



        myaccanass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString = FACEBOOK_PROFI;
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent1.putExtra(Browser.EXTRA_APPLICATION_ID, "com.android.chrome");
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setPackage("com.android.chrome");
                startActivity(intent1);

            }
        });
        showfacebookpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String urlString = FACEBOOK_URL;
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent1.putExtra(Browser.EXTRA_APPLICATION_ID, "com.android.chrome");
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setPackage("com.android.chrome");
                startActivity(intent1);

            }
        });
        callus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String urlString = FACEBOOK_URL;
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent1.putExtra(Browser.EXTRA_APPLICATION_ID, "com.android.chrome");
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setPackage("com.android.chrome");
                startActivity(intent1);


            }
        });
    }



    public void GetListofStoreage() {

        File[] externalDirs = getExternalFilesDirs(null);

        for (File file : externalDirs) {
            storageDirectories.add(file.getPath());
        }

    }


    private void setupSpinners() {

        ArrayAdapter<CharSequence> players =  ArrayAdapter.createFromResource(SettingsJcartoon.this,R.array.players,R.layout.custom_spinner);
        players.setDropDownViewResource(R.layout.custom_spinner);
        choseplayer.setAdapter(players);
        choseplayer.setSelection(playerselect);
        choseplayer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp.edit().putInt("playerselect",position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> downloadcartoon =  ArrayAdapter.createFromResource(SettingsJcartoon.this,R.array.downloads, R.layout.custom_spinner);
        downloadcartoon.setDropDownViewResource(R.layout.custom_spinner);
        chosedownlo.setAdapter(downloadcartoon);
        chosedownlo.setSelection(downcartoonselect);
        chosedownlo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp.edit().putInt("downcartoonselect",position).apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> downloadcomics =  ArrayAdapter.createFromResource(SettingsJcartoon.this,R.array.downloads, R.layout.custom_spinner);
        downloadcomics.setDropDownViewResource(R.layout.custom_spinner);
        chosedownlocomics.setAdapter(downloadcomics);
        chosedownlocomics.setSelection(downcomicselect);
        chosedownlocomics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp.edit().putInt("downcomicselect",position).apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String>  choseplacesaveaf = new ArrayAdapter<>(SettingsJcartoon.this, R.layout.custom_spinner,storageDirectories);
        choseplacesaveaf.setDropDownViewResource(R.layout.custom_spinner);
        choseplacesave.setAdapter(choseplacesaveaf);
        choseplacesave.setSelection(choseplacesaveselect);
        choseplacesave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp.edit().putInt("choseplacesaveselect",position).apply();
                sp.edit().putString(SETTINGS_LIBRARY_DIR,parent.getItemAtPosition(position).toString()).apply();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


}