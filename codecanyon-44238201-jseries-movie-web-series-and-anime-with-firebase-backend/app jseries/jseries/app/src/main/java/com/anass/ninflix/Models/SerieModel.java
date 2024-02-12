package com.anass.ninflix.Models;

public class SerieModel {

    public String id;
    private String title;
    private String poster;
    private String posterlogo;
    private String year;
    private String place;
    private String gener;
    private String userid;
    private String serie_id;
    private String country;
    private String age;
    private String story;
    private String other_season_id;
    private String cast;
    private String link_id;
    private long views;
    private String youtubetrailerid;
    private String views_db;
    private String rating_db;

    //fixed
    public int type;

    public SerieModel() {
    }

    public String getViews_db() {
        return views_db;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setViews_db(String views_db) {
        this.views_db = views_db;
    }

    public String getRating_db() {
        return rating_db;
    }

    public void setRating_db(String rating_db) {
        this.rating_db = rating_db;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPosterlogo() {
        return posterlogo;
    }

    public void setPosterlogo(String posterlogo) {
        this.posterlogo = posterlogo;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getGener() {
        return gener;
    }

    public void setGener(String gener) {
        this.gener = gener;
    }

    public String getSerie_id() {
        return serie_id;
    }

    public void setSerie_id(String serie_id) {
        this.serie_id = serie_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getOther_season_id() {
        return other_season_id;
    }

    public void setOther_season_id(String other_season_id) {
        this.other_season_id = other_season_id;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getLink_id() {
        return link_id;
    }

    public void setLink_id(String link_id) {
        this.link_id = link_id;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public String getYoutubetrailerid() {
        return youtubetrailerid;
    }

    public void setYoutubetrailerid(String youtubetrailerid) {
        this.youtubetrailerid = youtubetrailerid;
    }
}
