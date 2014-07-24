package com.buerlab.returntrunk.models;

/**
 * Created by teddywu on 14-7-23.
 */
public class Settings {
    public boolean push = false;
    public boolean gps = false;

    public Settings(){

    }

    public Settings(boolean push, boolean gps){
        this.push = push;
        this.gps = gps;
    }
}
