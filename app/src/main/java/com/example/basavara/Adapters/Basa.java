package com.example.basavara.Adapters;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Basa {

    private String location,vara,address,details,contact,ad_id,user_id;
    private @ServerTimestamp Date timestamp;

    public Basa() {
    }

    public Basa(String location, String vara, String address, String details, String contact, String ad_id, String user_id, Date timestamp) {
        this.location = location;
        this.vara = vara;
        this.address = address;
        this.details = details;
        this.contact = contact;
        this.ad_id = ad_id;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVara() {
        return vara;
    }

    public void setVara(String vara) {
        this.vara = vara;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
