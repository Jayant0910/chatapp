package com.anass.ninflix.Activities.ShowList;

import static com.anass.ninflix.Activities.MyList.lista.favoritlmovie.allseriesandmovies;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class morelikeit extends Fragment {

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

        morelikeit fragment = new morelikeit();
        fragment.setArguments(args);
        return fragment;
    }

    public morelikeit() {
        // Required empty public constructor
    }

    RecyclerView otherseason_rv;
    ProgressBar progreed;
    TextView shownose;

    String posterid,title,id,poste,place,age,studio,date,desc,cast,tasnif,where,mortabit_id,chapters;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_morelikeit, container, false);
        progreed = view.findViewById(R.id.progreed);
        otherseason_rv = view.findViewById(R.id.otherseason_rv);
        shownose = view.findViewById(R.id.shownose);

        InitiaStrings();

        CheckifThereSeaons(posterid,tasnif,place);
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
    List<SerieModel> serieModelList;
    private void CheckifThereSeaons(String postkey, String tasnif, String place) {

        otherseason_rv.setLayoutManager(new GridLayoutManager(getActivity(),3));
        otherseason_rv.setNestedScrollingEnabled(false);

        allseriesandmovies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serieModelList = new ArrayList<>();
                progreed.setVisibility(View.GONE);
                for (DataSnapshot ss:snapshot.getChildren()) {
                    SerieModel sp = ss.getValue(SerieModel.class);

                    if (sp.getGener().equals(tasnif) && !sp.getSerie_id().equals(postkey) ){
                        serieModelList.add(sp);
                    }
                }

                if (serieModelList.size() > 0){
                    otherseason_rv.setAdapter(new SeriesAdapter(getActivity(),serieModelList,0));
                }else {
                    shownose.setVisibility(View.VISIBLE);
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}