package com.example.m_hike.model;

import java.io.Serializable;

public class Hike implements Serializable {

    private int hikeId;
    private String name;
    private String location;
    private String dateOfHike;
    private String parkingAvailable;
    private String lengthOfHeight;
    private String difficulty;
    private String startPoint;
    private String endPoint;
    private String description;

    public Hike(int hikeId, String name, String location, String dateOfHike, String parkingAvailable,
                String lengthOfHeight, String difficulty, String startPoint, String endPoint, String description) {

        this.hikeId = hikeId;
        this.name = name;
        this.location = location;
        this.dateOfHike = dateOfHike;
        this.parkingAvailable = parkingAvailable;
        this.lengthOfHeight = lengthOfHeight;
        this.difficulty = difficulty;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.description = description;

    }

    public int getHikeId() {
        return hikeId;
    }

    public void setHikeId(int hikeId) {
        this.hikeId = hikeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateOfHike() {
        return dateOfHike;
    }

    public void setDateOfHike(String dateOfHike) {
        this.dateOfHike = dateOfHike;
    }

    public String getParkingAvailable() {
        return parkingAvailable;
    }

    public void setParkingAvailable(String parkingAvailable) {
        this.parkingAvailable = parkingAvailable;
    }

    public String getLengthOfHeight() {
        return lengthOfHeight;
    }

    public void setLengthOfHeight(String lengthOfHeight) {
        this.lengthOfHeight = lengthOfHeight;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
