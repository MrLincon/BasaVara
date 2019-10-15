package com.example.basavara;

public class Basa {

    public String room,varanda,toilet,extra,vara;

    public Basa() {
    }

    public Basa(String room, String varanda, String toilet, String extra, String vara) {
        this.room = room;
        this.varanda = varanda;
        this.toilet = toilet;
        this.extra = extra;
        this.vara = vara;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getVaranda() {
        return varanda;
    }

    public void setVaranda(String varanda) {
        this.varanda = varanda;
    }

    public String getToilet() {
        return toilet;
    }

    public void setToilet(String toilet) {
        this.toilet = toilet;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getVara() {
        return vara;
    }

    public void setVara(String vara) {
        this.vara = vara;
    }
}
