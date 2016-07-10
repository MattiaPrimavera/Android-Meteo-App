package com.xtech.sunshine_tutorial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("day", day);
            jsonObject.put("main", main);
            jsonObject.put("temp", temp);
            jsonObject.put("iconName", iconName);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Forecast fromJSON(String json) throws JSONException {
        JSONObject reader = new JSONObject(json);
        String temp = reader.getString("temp");
        String day = reader.getString("day");
        String main = reader.getString("main");
        String iconName = reader.getString("iconName");
        return new Forecast(day, main, temp, iconName);
    }
}
