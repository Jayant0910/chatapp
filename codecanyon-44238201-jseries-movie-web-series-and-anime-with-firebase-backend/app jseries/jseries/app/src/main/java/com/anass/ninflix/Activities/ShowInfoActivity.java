package com.anass.ninflix.Activities;

import static com.anass.ninflix.Activities.MyList.lista.favoritlmovie.LinkDb;
import static com.anass.ninflix.Activities.MyList.lista.favoritlmovie.RateDb;
import static com.anass.ninflix.Activities.MyList.lista.favoritlmovie.allseriesandmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anass.ninflix.Activities.Auth.LoginTojcartoon;
import com.anass.ninflix.Activities.ShowList.castinfo;
import com.anass.ninflix.Activities.ShowList.episodes;
import com.anass.ninflix.Activities.ShowList.morelikeit;
import com.anass.ninflix.Activities.ShowList.seasons;
import com.anass.ninflix.DB.JseriesDB;
import com.anass.ninflix.Models.EpesodModel;
import com.anass.ninflix.Models.SerieModel;
import com.anass.ninflix.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class ShowInfoActivity extends AppCompatActivity {



    String id,title,cover,poste,link,postkey,age,studio,date,desc
            ,cast,tasnif,mortabit,where,place,isfromeactibity,views,myAppItemId,youtubeid;
    Boolean ilikeit = false;


    MyViewPagerAdapterInfo myViewPagerAdapter;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ImageView gooback,likeicon,rateicon,imgviews;
    TextView titleser,yeartxt,agetxt,countrytxt,viewstxt,reattxt,storytxt,genertxt;
    RelativeLayout watchepenoew,downloadfierstepe;
    LinearLayout likeit,rate,shaerit;
    JseriesDB jseriesDB;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);


        gooback = findViewById(R.id.gooback);
        tabLayout = findViewById(R.id.tablay);
        viewPager2 = findViewById(R.id.viewPager);
        titleser = findViewById(R.id.titleser);
        yeartxt = findViewById(R.id.yeartxt);
        agetxt = findViewById(R.id.agetxt);
        countrytxt = findViewById(R.id.countrytxt);
        viewstxt = findViewById(R.id.viewstxt);
        reattxt = findViewById(R.id.reattxt);
        watchepenoew = findViewById(R.id.watchepenoew);
        downloadfierstepe = findViewById(R.id.downloadfierstepe);
        storytxt = findViewById(R.id.storytxt);
        genertxt = findViewById(R.id.genertxt);
        likeit = findViewById(R.id.likeit);
        likeicon = findViewById(R.id.likeicon);
        rate = findViewById(R.id.rate);
        rateicon = findViewById(R.id.rateicon);
        shaerit = findViewById(R.id.shaerit);
        imgviews = findViewById(R.id.imgviews);
        jseriesDB = new JseriesDB(this);









        isfromeactibity = getIntent().getStringExtra("isintent");

        if (isfromeactibity != null){

            title = getIntent().getStringExtra("title");
            poste = getIntent().getStringExtra("poste");
            link = getIntent().getStringExtra("link");
            postkey = getIntent().getStringExtra("postkey");
            age = getIntent().getStringExtra("age");
            studio = getIntent().getStringExtra("studio");
            date = getIntent().getStringExtra("date");
            desc = getIntent().getStringExtra("desc");
            cast = getIntent().getStringExtra("cast");
            tasnif = getIntent().getStringExtra("tasnif");
            mortabit = getIntent().getStringExtra("mortabit");
            where = getIntent().getStringExtra("where");
            place = getIntent().getStringExtra("place");
            id = getIntent().getStringExtra("id");
            views = getIntent().getStringExtra("views");
            youtubeid = getIntent().getStringExtra("youtubeid");

            initiViewPager();

            Glide.with(this).load(poste).into(imgviews);

            titleser.setText(title);
            yeartxt.setText(date);
            agetxt.setText(age);
            countrytxt.setText(studio);
            GetViews(postkey);

            storytxt.setText(desc);
            genertxt.setText(tasnif);

            SerieModel serieModel = new SerieModel();
            serieModel.setSerie_id(postkey);
            serieModel.setYoutubetrailerid(youtubeid);
            serieModel.setViews(1);
            serieModel.setPoster(poste);
            serieModel.setYear(date);
            serieModel.setPlace(place);
            serieModel.setGener(tasnif);
            serieModel.setCountry(studio);
            serieModel.setAge(age);
            serieModel.setStory(desc);
            serieModel.setOther_season_id(mortabit);
            serieModel.setTitle(title);
            serieModel.setCast(cast);
            serieModel.setLink_id(link);
            CheckLikeStat(postkey);
            likeit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Likeit(serieModel);
                }
            });


            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                ChekRateStat(postkey);
            }


            rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        Rateit(postkey);
                    }else {
                        startActivity(new Intent(ShowInfoActivity.this, LoginTojcartoon.class));
                    }

                }
            });


            shaerit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharelink();
                }
            });


            GetFierstEpisode(link);

           


        }else {
            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(ShowInfoActivity.this, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                            // Get deep link from result (may be null if no link is found)
                            Uri deepLink;
                            deepLink = pendingDynamicLinkData.getLink();
                            myAppItemId = deepLink.getQueryParameter("id");

                            DatabaseReference db = allseriesandmovies.child(myAppItemId);
                            db.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snap) {



                                    title = snap.child("title").getValue(String.class);
                                    id = snap.child("id").getValue(String.class);
                                    desc = snap.child("story").getValue(String.class);
                                    poste = snap.child("poster").getValue(String.class);
                                    link = snap.child("link_id").getValue(String.class);
                                    date = snap.child("year").getValue(String.class);
                                    mortabit = snap.child("other_season_id").getValue(String.class);
                                    studio = snap.child("country").getValue(String.class);
                                    age = snap.child("age").getValue(String.class);
                                    place = snap.child("place").getValue(String.class);
                                    tasnif = snap.child("gener").getValue(String.class);
                                    postkey = snap.child("serie_id").getValue(String.class);
                                    cast = snap.child("cast").getValue(String.class);

                                    initiViewPager();
                                    Glide.with(ShowInfoActivity.this).load(poste).into(imgviews);

                                    titleser.setText(title);
                                    yeartxt.setText(date);
                                    agetxt.setText(age);
                                    countrytxt.setText(studio);
                                    GetViews(myAppItemId);

                                    storytxt.setText(desc);
                                    genertxt.setText(tasnif);

                                    SerieModel serieModel = new SerieModel();
                                    serieModel.setSerie_id(myAppItemId);
                                    serieModel.setYoutubetrailerid(youtubeid);
                                    serieModel.setViews(1);
                                    serieModel.setPoster(poste);
                                    serieModel.setYear(date);
                                    serieModel.setPlace(place);
                                    serieModel.setGener(tasnif);
                                    serieModel.setCountry(studio);
                                    serieModel.setAge(age);
                                    serieModel.setStory(desc);
                                    serieModel.setOther_season_id(mortabit);
                                    serieModel.setTitle(title);
                                    serieModel.setCast(cast);
                                    serieModel.setLink_id(link);

                                    likeit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Likeit(serieModel);
                                        }
                                    });


                                    CheckLikeStat(myAppItemId);
                                    shaerit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            sharelink();
                                        }
                                    });

                                    GetFierstEpisode(link);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });





                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

    private void Rateit(String postkey) {
        if (!ilikeit){
            RateDb.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postkey).setValue(true);

        }else {
            RateDb.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postkey).setValue(null);

        }

    }

    private void ChekRateStat(String postkey) {
        RateDb.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postkey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){
                            ilikeit = true;
                            rateicon.setColorFilter(getResources().getColor(R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
                            rateicon.setImageDrawable(getResources().getDrawable(R.drawable.thumbsup));
                        }else {
                            ilikeit = false;
                            rateicon.setColorFilter(getResources().getColor(R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                            rateicon.setImageDrawable(getResources().getDrawable(R.drawable.thumbsup_blue));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void GetFierstEpisode(String link) {
        List<EpesodModel> firestEpe = new ArrayList<>();
        LinkDb.child(link).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot db:snapshot.getChildren()) {
                        EpesodModel ep = db.getValue(EpesodModel.class);
                        firestEpe.add(ep);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        watchepenoew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firestEpe.size()!=0){
                    EpesodModel c = firestEpe.get(0);
                    Intent sendDataToDetailsActivity = new Intent(ShowInfoActivity.this, EpesodTAFASIL.class);
                    sendDataToDetailsActivity.putExtra("epetitle",c.getEpetitle());
                    sendDataToDetailsActivity.putExtra("fhd",c.getServer1());
                    sendDataToDetailsActivity.putExtra("hd",c.getServer2());
                    sendDataToDetailsActivity.putExtra("sd",c.getServer3());
                    sendDataToDetailsActivity.putExtra("key",c.getPostkey());
                    sendDataToDetailsActivity.putExtra("key",c.getPostkey());
                    sendDataToDetailsActivity.putExtra("name",title);
                    sendDataToDetailsActivity.putExtra("position",String.valueOf(0));
                    sendDataToDetailsActivity.putExtra("title",title);
                    sendDataToDetailsActivity.putExtra("posterid",postkey);
                    sendDataToDetailsActivity.putExtra("titlecartoon",title);
                    sendDataToDetailsActivity.putExtra("id",id);
                    sendDataToDetailsActivity.putExtra("poste",poste);
                    sendDataToDetailsActivity.putExtra("place",place);
                    sendDataToDetailsActivity.putExtra("age",age);
                    sendDataToDetailsActivity.putExtra("studio",studio);
                    sendDataToDetailsActivity.putExtra("date",date);
                    sendDataToDetailsActivity.putExtra("story",desc);
                    sendDataToDetailsActivity.putExtra("cast",cast);
                    sendDataToDetailsActivity.putExtra("tasnif",tasnif);
                    sendDataToDetailsActivity.putExtra("where",where);
                    sendDataToDetailsActivity.putExtra("mortabit",mortabit);
                    sendDataToDetailsActivity.putExtra("link",link);
                    startActivity(sendDataToDetailsActivity);
                }else {
                    Toast.makeText(ShowInfoActivity.this, "No Link", Toast.LENGTH_SHORT).show();
                }


            }
        });
        downloadfierstepe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firestEpe.size()!=0){
                    EpesodModel c = firestEpe.get(0);
                    Intent sendDataToDetailsActivity = new Intent(ShowInfoActivity.this, EpesodTAFASIL.class);
                    sendDataToDetailsActivity.putExtra("epetitle",c.getEpetitle());
                    sendDataToDetailsActivity.putExtra("fhd",c.getServer1());
                    sendDataToDetailsActivity.putExtra("hd",c.getServer2());
                    sendDataToDetailsActivity.putExtra("sd",c.getServer3());
                    sendDataToDetailsActivity.putExtra("key",c.getPostkey());
                    sendDataToDetailsActivity.putExtra("key",c.getPostkey());
                    sendDataToDetailsActivity.putExtra("name",title);
                    sendDataToDetailsActivity.putExtra("position",String.valueOf(0));
                    sendDataToDetailsActivity.putExtra("title",title);
                    sendDataToDetailsActivity.putExtra("posterid",postkey);
                    sendDataToDetailsActivity.putExtra("titlecartoon",title);
                    sendDataToDetailsActivity.putExtra("id",id);
                    sendDataToDetailsActivity.putExtra("poste",poste);
                    sendDataToDetailsActivity.putExtra("place",place);
                    sendDataToDetailsActivity.putExtra("age",age);
                    sendDataToDetailsActivity.putExtra("studio",studio);
                    sendDataToDetailsActivity.putExtra("date",date);
                    sendDataToDetailsActivity.putExtra("story",desc);
                    sendDataToDetailsActivity.putExtra("cast",cast);
                    sendDataToDetailsActivity.putExtra("tasnif",tasnif);
                    sendDataToDetailsActivity.putExtra("where",where);
                    sendDataToDetailsActivity.putExtra("mortabit",mortabit);
                    sendDataToDetailsActivity.putExtra("link",link);
                    startActivity(sendDataToDetailsActivity);
                }else {
                    Toast.makeText(ShowInfoActivity.this, "No Link", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void GetViews(String postkey) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Views").child(postkey);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long vii = snapshot.child("views").getValue(Long.class);

                if (vii == null){
                    viewstxt.setText("1");
                }else {
                    String formatted = format(vii);
                    viewstxt.setText(formatted);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void Likeit(SerieModel serieModel) {



        if (jseriesDB.checkIfMyListExists(postkey)){

            boolean added = jseriesDB.DeletMyListDB(postkey);
            if (added){
                Toast.makeText(ShowInfoActivity.this, "Removed from My List", Toast.LENGTH_SHORT).show();
                likeicon.setImageDrawable(getResources().getDrawable(R.drawable.pluss));
                CheckLikeStat(serieModel.getSerie_id());
            }
        }else {

            boolean added = jseriesDB.AddtoMyListdDB(serieModel);
            if (added){
                Toast.makeText(ShowInfoActivity.this, "Added To My List", Toast.LENGTH_SHORT).show();
                likeicon.setImageDrawable(getResources().getDrawable(R.drawable.check));
                CheckLikeStat(serieModel.getSerie_id());
            }

        }
    }


    private void CheckLikeStat(String postkey) {

        if (jseriesDB.checkIfMyListExists(postkey)){

            likeicon.setImageDrawable(getResources().getDrawable(R.drawable.check));

        }else {

            likeicon.setImageDrawable(getResources().getDrawable(R.drawable.pluss));

        }


    }

    private void sharelink() {

        // created long link
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://jseries.page.link/?id="+postkey))
                .setDynamicLinkDomain("jseries.page.link")

                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("jseries.page.link").build())
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("jseries.page.link").build())
                .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder()
                        .setTitle(title)
                        .setDescription(desc)
                        .setImageUrl(Uri.parse(poste))
                        .build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();


        //for creating short link
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink.getUri())

                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {

                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();



                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT,shortLink.toString());
                            intent.setType("text/plain");
                            startActivity(intent);
                        } else {
                            Toast.makeText(ShowInfoActivity.this,"error : "+task.getException(), Toast.LENGTH_SHORT).show();

                            // Error
                            // ...
                        }
                    }
                });
    }

    private void initiViewPager() {

        gooback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        myViewPagerAdapter = new MyViewPagerAdapterInfo(ShowInfoActivity.this);
        viewPager2.setAdapter(myViewPagerAdapter);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }

    public class MyViewPagerAdapterInfo extends FragmentStateAdapter {
        public MyViewPagerAdapterInfo(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){

                case 1:
                    return new seasons().newInstance(title,cover,poste,postkey,age,studio,date
                            ,desc,cast,tasnif,where,mortabit,link,place,views);


                case 2:
                    return new morelikeit().newInstance(title,cover,poste,postkey,age,studio,date
                            ,desc,cast,tasnif,where,mortabit,link,place,views);

                case 3:
                    return new castinfo().newInstance(title,cover,poste,postkey,age,studio,date
                        ,desc,cast,tasnif,where,mortabit,link,place,views);

                default:
                    return new episodes().newInstance(title,cover,poste,postkey,age,studio,date
                            ,desc,cast,tasnif,where,mortabit,link,place,views);
            }

        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

}