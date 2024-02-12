package com.anass.ninflix;

import static com.anass.ninflix.Activities.Auth.Signintojcartoon2.PReqCode;
import static com.anass.ninflix.Config.config.SETTINGS_NAME;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.anass.ninflix.Activities.UpdateActivity;
import com.anass.ninflix.Fragments.home;
import com.anass.ninflix.Fragments.more;
import com.anass.ninflix.Fragments.movies;
import com.anass.ninflix.Fragments.search;
import com.anass.ninflix.Fragments.series;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
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
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {



    private long pressedTime;
    private Toast backToast;
    private SharedPreferences sp;

    BottomNavigationView navi;
    FragmentManager fragmentManager;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_main);
        StartAppSDK.init(this, "201688868", false);
        StartAppSDK.enableReturnAds(false);
        StartAppAd.disableSplash();
        sp = getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        firebaseFirestore = FirebaseFirestore.getInstance();



        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                // AppLovin SDK is initialized, start loading ads
            }
        } );

        FirebaseApp.initializeApp(this);

        navi = findViewById(R.id.navi);

        InslizBottonBar(savedInstanceState);

        CheckIfTherNewUpdates();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            CheckIfUserPaid();
        }



    }


    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Toast.makeText(this,"please accept permission",Toast.LENGTH_LONG).show();

            }

            else
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }

    }

    private void CheckIfUserPaid() {
        DocumentReference dc = firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){
                    String paidstat = documentSnapshot.getString("ispaid");
                    if (paidstat.equals("1")){
                        sp.edit().putString("ispaid","1").apply();
                    }else {
                        sp.edit().putString("ispaid","0").apply();
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void CheckIfTherNewUpdates() {


        DatabaseReference dbup = FirebaseDatabase.getInstance().getReference("updates").child("update1");

        dbup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String link = snapshot.child("link").getValue(String.class);
                Long version = snapshot.child("versionnumber").getValue(Long.class);

                if (version > getVersionCode()){
                    Intent i = new Intent(MainActivity.this, UpdateActivity.class);
                    i.putExtra("link",link);
                    startActivity(i );
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void InslizBottonBar(Bundle savedInstanceState) {


        if (savedInstanceState == null) {
            navi.setSelectedItemId(R.id.home);


            fragmentManager = getSupportFragmentManager();
            home homeFragment = new home();

            fragmentManager.beginTransaction().replace(R.id.fream, homeFragment)
                    .commit();


        }

        navi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                final Fragment[] fragment = {null};
                switch (item.getItemId()) {
                    case R.id.home:


                        fragment[0] = new home();
                        break;
                    case R.id.serie:

                        fragment[0] = new series();
                        break;
                    case R.id.movie:

                        fragment[0] = new movies();
                        break;

                    case R.id.search:

                        fragment[0] = new search();
                        break;
                    case R.id.profi:
                        fragment[0] = new more();

                        break;

                }

                if (fragment[0] != null) {
                    fragmentManager = getSupportFragmentManager();

                    fragmentManager.beginTransaction().replace(R.id.fream, fragment[0])
                            .commit();
                }

                return true;
            }
        });

        checkAndRequestForPermission();
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

    private PackageInfo pInfo;
    public int getVersionCode() {
        pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {

            Log.i("MYLOG", "NameNotFoundException: "+e.getMessage());
        }
        return pInfo.versionCode;
    }

   /* @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginTojcartoon.class));
        }
    }*/



}