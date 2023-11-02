package com.example.m_hike.model;

import android.graphics.Bitmap;

public class Observation {

    private int _id;
    private String animals;
    private String vegetation;
    private String weather;
    private String trails;
    private String date;
    private String time;
    private String comments;

    private Bitmap image;
    private int hike_id;

    public Observation(int _id,String animals,String vegetation,String weather,String trails,String date,String time,String comments, Bitmap image, int hike_id){

        this._id = _id;
        this.animals = animals;
        this.vegetation = vegetation;
        this.weather = weather;
        this.trails = trails;
        this.date = date;
        this.time = time;
        this.comments = comments;
        this.image = image;
        this.hike_id = hike_id;


    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getAnimals() {
        return animals;
    }

    public void setAnimals(String animals) {
        this.animals = animals;
    }

    public String getVegetation() {
        return vegetation;
    }

    public void setVegetation(String vegetation) {
        this.vegetation = vegetation;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTrails() {
        return trails;
    }

    public void setTrails(String trails) {
        this.trails = trails;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getHike_id() {
        return hike_id;
    }

    public void setHike_id(int hike_id) {
        this.hike_id = hike_id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
