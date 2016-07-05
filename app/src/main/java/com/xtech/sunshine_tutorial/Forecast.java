package com.xtech.sunshine_tutorial;

public class Forecast {
    private String day, main, temp;

    public Forecast(String day, String main, String temp){
        this.day = day;
        this.main = main;
        this.temp = temp;
    }

    public String getDay(){ return this.day; }
    public String getMain(){ return this.main; }
    public String getTemp(){ return this.temp; }

    public void setDay(String day){ this.day = day; }
    public void setTemp(String temp){ this.temp = temp; }
    public void setMain(String main){ this.main = main; }

    public String toString(){
        return this.day + " - " + this.main + " - " + this.temp;
    }
}
