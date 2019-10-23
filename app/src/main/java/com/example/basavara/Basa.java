package com.example.basavara;

public class Basa {

    public String location,vara,address,details,contact;

    public Basa() {
    }

    public Basa(String location, String vara, String address, String details, String contact) {
        this.location = location;
        this.vara = vara;
        this.address = address;
        this.details = details;
        this.contact = contact;
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
}
