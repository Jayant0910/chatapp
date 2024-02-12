package com.anass.ninflix.Activities.ShowList;

import static android.content.Context.MODE_PRIVATE;
import static com.anass.ninflix.Activities.MyList.lista.favoritlmovie.LinkDb;
import static com.anass.ninflix.Config.config.SETTINGS_NAME;
import static com.anass.ninflix.Config.config.getpathforsavefile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anass.ninflix.Activities.Auth.LoginTojcartoon;
import com.anass.ninflix.Activities.EpesodTAFASIL;
import com.anass.ninflix.Activities.Jdownload.DownloadManage;
import com.anass.ninflix.Config.getComicsnextback;
import com.anass.ninflix.DB.JseriesDB;
import com.anass.ninflix.JPLAYER.JSPLAYER;
import com.anass.ninflix.Models.EpesodModel;
import com.anass.ninflix.R;
import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inside4ndroid.jresolver.Jresolver;
import com.inside4ndroid.jresolver.Model.Jmodel;
import com.startapp.sdk.adsbase.StartAppAd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class episodes extends Fragment {


    private static final String KEY_CODE = "title";
    private static final String coverR = "cover";
    private static final String posteR = "poste";
    private static final String postkeyR = "postkey";
    private static final String ageR = "age";
    private static final String studioR = "studio";
    private static final String dateR = "date";
    private static final String descR = "desc";
    private static final String tasnifR = "tasnif";
    private static final String whereR = "where";
    private static final String castr = "cast";
    private static final String mortabit_idR = "mortabit_id";
    private static final String placeR = "place";
    private static final String chaptersR = "chapters";
    private static final String VIews = "views";


    public static final String SECURE_URI = "secure_uri";
    public static final String USER_AGENT = "User-Agent";
    public static final String VIDEOTYPE = "video/*";
    public static final String TITLE = "title";
    public static final String POSTER = "poster";
    public static final String EXTRA_HEADERS = "android.media.intent.extra.HTTP_HEADERS";
    public static final String HEADERS = "headers";
    public static final String REFER = "Referer";

    @SuppressLint("SuspiciousIndentation")
    public static Fragment newInstance(@Nullable String code, String cover, String poste, String postkey,
                                       String age, String studio, String date, String desc, String cast, String tasnif,
                                       String where, String mortabit, String link, String place, String views) {
        Bundle args = new Bundle(13);
        if (code!= null)
            args.putString(KEY_CODE, code);
        args.putString(coverR, cover);
        args.putString(posteR, poste);
        args.putString(postkeyR, postkey);
        args.putString(ageR, age);
        args.putString(studioR, studio);
        args.putString(dateR, date);
        args.putString(descR, desc);
        args.putString(castr, cast);
        args.putString(tasnifR, tasnif);
        args.putString(whereR, where);
        args.putString(mortabit_idR, mortabit);
        args.putString(placeR, place);
        args.putString(chaptersR, link);
        args.putString(VIews, views);

        episodes fragment = new episodes();
        fragment.setArguments(args);
        return fragment;
    }


    public episodes() {
        // Required empty public constructor
    }

    Boolean isinepesode = false;
    public String typeofclicking = "watch";

    RecyclerView rectmovies;
    ProgressBar progreed;
    private SharedPreferences sp;
    StartAppAd startAppAd;
    List<EpesodModel> epesodModels;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    Jresolver xGetter ;


    String posterid,title,id,poste,place,age,studio,date,desc,cast,tasnif,where,mortabit_id,chapters;
    String epetitle;
    String postkeyEpe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_episodes, container, false);
        sp = getActivity().getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        rectmovies = view.findViewById(R.id.rectmovies);
        progreed = view.findViewById(R.id.progreed);

        StartAppAd.init(getActivity(),"107796986","210473406");
        startAppAd = new StartAppAd(getActivity());
        startAppAd.loadAd(StartAppAd.AdMode.VIDEO);
        xGetter = new Jresolver(getActivity());
        InitiaStrings();

        GetEpesode();

        xGetter.onFinish(new Jresolver.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Jmodel> vidURL, boolean multiple_quality) {
                LoginTojcartoon.PleaseWait.dismiss();

                Toast.makeText(getActivity(), ""+epetitle, Toast.LENGTH_SHORT).show();
             /*   if (vidURL !=  null){
                    if (multiple_quality){ //This video you can choose qualities

                        LoginTojcartoon.PleaseWait.dismiss();
                        multipleQualityDialog2(vidURL,c,epetitle);


                    }else {//If single
                        int player = sp.getInt("playerselect",0);
                        String url = vidURL.get(0).getUrl();
                        if (typeofclicking.equals("watch")){
                            LoginTojcartoon.PleaseWait.dismiss();
                            if (player == 0){
                                watchVideo2(url,c,epetitle);
                            }else if (player == 1){
                                openWithMXPlayer2(url);
                            }else {
                                openOtherPlayers(url);
                            }

                        }else {
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                String fulltitle = title +" - "+ c.getEpetitle()+".mp4";
                                LoginTojcartoon.PleaseWait.dismiss();
                                switch (sp.getInt("downcartoonselect",0)){

                                    case 0:

                                        SingleDownloadepe(url,title,c.getEpetitle(),getActivity());

                                        break;
                                    case 1:

                                        downloadFromAdm2(getActivity(),url,fulltitle);

                                        break;
                                }

                            }
                            else {
                                LoginTojcartoon.PleaseWait.dismiss();
                                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            }
                        }
                    }
                }else {
                    LoginTojcartoon.PleaseWait.dismiss();
                    Toast.makeText(getActivity(), "The Link Is Empty Or Removed", Toast.LENGTH_SHORT).show();
                }*/

            }

            @Override
            public void onError() {

            }
        });


        return view;
    }

    private void InitiaStrings() {

        title = getArguments().getString(KEY_CODE);
        poste = getArguments().getString(posteR);
        chapters = getArguments().getString(chaptersR);
        posterid = getArguments().getString(postkeyR);
        place = getArguments().getString(placeR);
        age = getArguments().getString(ageR);
        studio = getArguments().getString(studioR);
        date = getArguments().getString(dateR);
        desc = getArguments().getString(descR);
        cast = getArguments().getString(castr);
        tasnif = getArguments().getString(tasnifR);
        where = getArguments().getString(whereR);
        mortabit_id = getArguments().getString(mortabit_idR);

    }

    private void GetEpesode() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rectmovies.setLayoutManager(linearLayoutManager);
        rectmovies.setNestedScrollingEnabled(false);

        LinkDb.child(chapters).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                epesodModels = new ArrayList<>();
                progreed.setVisibility(View.GONE);
                for (DataSnapshot sp : snapshot.getChildren()) {
                    EpesodModel el = sp.getValue(EpesodModel.class);
                    epesodModels.add(el);
                }

                getComicsnextback.Epesodes = epesodModels;
                rectmovies.setAdapter(new epesoedsAdapter(epesodModels,getActivity(),title));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public class epesoedsAdapter extends RecyclerView.Adapter<epesoedsAdapter.myviewholder> {

        List<EpesodModel> chaptermodelList;
        Context context;
        String title;
        JseriesDB comicsdb;
        private SharedPreferences sp;

        public epesoedsAdapter(List<EpesodModel> chaptermodelList, Context context, String title) {
            this.chaptermodelList = chaptermodelList;
            this.context = context;
            this.title = title;
            comicsdb = new JseriesDB(context);
            sp = context.getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        }

        @NonNull
        @Override
        public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item, parent, false);
            return new  myviewholder(view);
        }
        public List<String> getSelectedTitles(){
            List<String> selected = new ArrayList<>();
            for (EpesodModel link: chaptermodelList) {
                if (link.IsSelected){
                    selected.add(link.getEpetitle());
                }
            }

            return  selected;
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(@NonNull myviewholder holder, int position) {
            EpesodModel c = chaptermodelList.get(position);


            int gg = position;
            String NameAndEpesoed = title+":"+c.getEpetitle();
            int currentPage = sp.getInt("currentPage"+NameAndEpesoed,1);
            int allduration = sp.getInt("allduration"+NameAndEpesoed,0);
            epetitle = c.getEpetitle();




            holder.episode.setText(epetitle);
            holder.titleof.setText(title);
            if (allduration < 6000){
                holder.showWhems.setMax(0);

            }else {
                holder.showWhems.setMax(allduration);
            }

            holder.showWhems.setProgress(currentPage);

            Glide.with(holder.itemView.getContext()).load(poste).into(holder.thumbnail);


            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeofclicking = "watch";

                    Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), EpesodTAFASIL.class);
                    sendDataToDetailsActivity.putExtra("epetitle",c.getEpetitle());
                    sendDataToDetailsActivity.putExtra("fhd",c.getServer1());
                    sendDataToDetailsActivity.putExtra("hd",c.getServer2());
                    sendDataToDetailsActivity.putExtra("sd",c.getServer3());
                    sendDataToDetailsActivity.putExtra("key",c.getPostkey());
                    sendDataToDetailsActivity.putExtra("key",c.getPostkey());
                    sendDataToDetailsActivity.putExtra("name",title);
                    sendDataToDetailsActivity.putExtra("position",String.valueOf(gg));
                    sendDataToDetailsActivity.putExtra("title",title);
                    sendDataToDetailsActivity.putExtra("posterid",posterid);
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
                    sendDataToDetailsActivity.putExtra("mortabit",mortabit_id);
                    sendDataToDetailsActivity.putExtra("link",chapters);
                    holder.itemView.getContext().startActivity(sendDataToDetailsActivity);


                }
            });


          /*  holder.download_epe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeofclicking = "download";


                    if (c.getServer2().isEmpty() ){

                        Toast.makeText(context, "The Link Is Empty Or Removed", Toast.LENGTH_SHORT).show();
                    }else {
                        letGo2(c.getServer2());
                    }






                }
            });*/



        }





        @Override
        public int getItemCount() {
            return chaptermodelList.size();
        }

        public class myviewholder extends RecyclerView.ViewHolder {

            TextView episode,titleof;
            ImageView thumbnail,download_epe;
            LinearProgressIndicator showWhems;

            public myviewholder(@NonNull View itemView) {
                super(itemView);

                episode = itemView.findViewById(R.id.episode);
                titleof = itemView.findViewById(R.id.anime);
                thumbnail = itemView.findViewById(R.id.thumbnail);
                download_epe = itemView.findViewById(R.id.download_epe);
                showWhems = itemView.findViewById(R.id.showWhems);


            }


        }
}

    @SuppressLint("MissingInflatedId")
    private void multipleQualityDialog2(ArrayList<Jmodel> model, EpesodModel epetitle, String s) {

        int player = sp.getInt("playerselect",0);


        Dialog quality_selector = new Dialog(getActivity());
        quality_selector.setContentView(R.layout.quality_selector_instagram);
        quality_selector.setCancelable(true);


        LinearLayout qualities = quality_selector.findViewById(R.id.quality_list);

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        for (int i = 0; i <  model.size(); i++) {

            final int fi = i;

            View quality_bar = inflater.inflate(R.layout.quality_selector_epesode_item, null);

            TextView q = quality_bar.findViewById(R.id.quality);
            q.setText(model.get(i).getQuality());


            quality_bar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (typeofclicking.equals("watch")){

                        if (player == 0){
                            watchVideo2(model.get(fi).getUrl(),epetitle, s);
                        }else if (player == 1){
                            openWithMXPlayer2(model.get(fi).getUrl());
                        }else {
                            openOtherPlayers(model.get(fi).getUrl());
                        }
                        quality_selector.dismiss();
                    }else {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            String fulltitle = title +" - "+ epetitle.getEpetitle()+".mp4";

                            switch (sp.getInt("downcartoonselect",0)){

                                case 0:

                                    SingleDownloadepe(model.get(fi).getUrl(),title,epetitle.getEpetitle(),getActivity());

                                    break;
                                case 1:

                                    downloadFromAdm2(getActivity(),model.get(fi).getUrl(),fulltitle);

                                    break;
                            }
                            quality_selector.dismiss();
                        }
                        else {

                            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                    }



                }
            });


            qualities.addView(quality_bar);
        }


        quality_selector.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        quality_selector.show();
    }

    private void watchVideo2(String url, EpesodModel c, String epetitle) {

        Intent sendDataToDetailsActivity = new Intent(getActivity(), JSPLAYER.class);
        sendDataToDetailsActivity.putExtra("eName",epetitle);
        sendDataToDetailsActivity.putExtra("message","");
        sendDataToDetailsActivity.putExtra("url",url);
        sendDataToDetailsActivity.putExtra("fhd",c.getServer1());
        sendDataToDetailsActivity.putExtra("hd",c.getServer2());
        sendDataToDetailsActivity.putExtra("sd",c.getServer3());
        sendDataToDetailsActivity.putExtra("key",c.getPostkey());
        sendDataToDetailsActivity.putExtra("name",title);
        sendDataToDetailsActivity.putExtra("position",String.valueOf(0));
        sendDataToDetailsActivity.putExtra("title",title);
        sendDataToDetailsActivity.putExtra("posterid",posterid);
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
        sendDataToDetailsActivity.putExtra("mortabit_id",mortabit_id);
        sendDataToDetailsActivity.putExtra("chapters",chapters);
        startActivity(sendDataToDetailsActivity);
    }
    public static void downloadFromAdm2(Context context, String url, String s) {

        Intent shareVideo = new Intent(Intent.ACTION_VIEW);
        shareVideo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareVideo.setDataAndType(Uri.parse(url), VIDEOTYPE);
        shareVideo.setPackage("com.dv.adm");
        ComponentName name = new ComponentName("com.dv.adm","com.dv.get.AEditor");
        shareVideo.setComponent(name);
        shareVideo.putExtra("com.android.extra.filename",s);

        Bundle headers = new Bundle();
        shareVideo.putExtra(SECURE_URI, true);
        try {
            context.startActivity(shareVideo);
        } catch (ActivityNotFoundException ex) {
            // Open Play Store if it fails to launch the app because the package doesn't exist.
            // Alternatively you could use PackageManager.getLaunchIntentForPackage() and check for null.
            // You could try catch this and launch the Play Store website if it fails but this shouldn’t
            // fail unless the Play Store is missing.

            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dv.adm")));
            } catch (ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dv.adm")));
            }

        }
    }

    public boolean appInstalledOrNot(String str) {
        try {
            getActivity().getPackageManager().getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void openWithMXPlayer2(String url) {
        boolean appInstalledOrNot = appInstalledOrNot("com.mxtech.videoplayer.ad");
        boolean appInstalledOrNot2 = appInstalledOrNot("com.mxtech.videoplayer.pro");
        String str2;
        if (appInstalledOrNot || appInstalledOrNot2) {
            String str3;
            if (appInstalledOrNot2) {
                str2 = "com.mxtech.videoplayer.pro";
                str3 = "com.mxtech.videoplayer.ActivityScreen";
            } else {
                str2 = "com.mxtech.videoplayer.ad";
                str3 = "com.mxtech.videoplayer.ad.ActivityScreen";
            }
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "application/x-mpegURL");
                intent.setPackage(str2);
                intent.setClassName(str2, str3);
                startActivity(intent);
                return;
            } catch (Exception e) {
                e.fillInStackTrace();

                return;
            }
        }
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.mxtech.videoplayer.ad")));
        } catch (ActivityNotFoundException e2) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mxtech.videoplayer.ad")));
        }
    }
    private void openOtherPlayers(String finalUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(finalUrl), "video/*");
        startActivity(intent);
    }
    private void SingleDownloadepe(String epelink, String title, String epetitle, Context context) {

        String pathsaved = getpathforsavefile(getActivity());

        File directory = new File(pathsaved +"/.JSERIES");
        File comics = new File(directory.getPath()+"/SERIES/");
        File ComicsFolder = new File(comics.getPath()+"/"+title+"/");
        File ComicsIssueFolder = new File(ComicsFolder.getPath()+"/"+epetitle+".mp4");

        // File chapter = new File(ComicsIssueFolder.getPath()+"/"+ position+1 + ".rar");


        DownloadManage.getInstance(context)
                .addDownloadRequest(epelink,ComicsIssueFolder,title,epetitle);
    }
}