package com.xtech.sunshine_tutorial;

public class Forecast {
    private String day, main, temp, iconName;

    public Forecast(String day, String main, String temp, String iconName){
        this.day = day;
        this.main = main;
        this.temp = temp;
        this.iconName = iconName;
    }

    public String getDay(){ return this.day; }
    public String getMain(){ return this.main; }
    public String getTemp(){ return this.temp; }
    public String getIconName(){ return this.iconName; }

    public void setDay(String day){ this.day = day; }
    public void setTemp(String temp){ this.temp = temp; }
    public void setMain(String main){ this.main = main; }
    public void setIconName(String iconName){ this.iconName = iconName; }

    public String toString(){
        return this.day + " - " + this.main + " - " + this.temp;
    }
}
