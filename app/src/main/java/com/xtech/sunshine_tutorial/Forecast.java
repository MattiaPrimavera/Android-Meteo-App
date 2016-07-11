package com.xtech.sunshine_tutorial;

import org.json.JSONException;
import org.json.JSONObject;

public class Forecast {
    private String dayNumber, dayString, main, temp, iconName, city;

    public Forecast(String dayNumber, String dayString, String main, String temp, String iconName, String city){
        this.dayNumber = dayNumber;
        this.dayString = dayString;
        this.main = main;
        this.temp = temp;
        this.iconName = iconName;
        this.city = city;
    }

    public String getCity(){ return this.city; }
    public String getDayString(){ return this.dayString; }
    public String getDayNumber(){ return this.dayNumber; }
    public String getMain(){ return this.main; }
    public String getTemp(){
        //No rounding made, I just cuttend the float value at the 4th position
        switch(this.temp.length()) {
            case 2:
                return this.temp + "°";
            case 3:
                return this.temp.substring(0, 2) + "°";
            case 4:
                return this.temp.substring(0, 4) + "°";
            default:
                return this.temp.substring(0,2) + "°";
        }
    }

    public String getIconName(){ return this.iconName; }

    public void setCity(String city){ this.city = city; }
    public void setDayNumber(String dayNumber){ this.dayNumber = dayNumber; }
    public void setDayString(String dayString){ this.dayString = dayString; }
    public void setTemp(String temp){ this.temp = temp; }
    public void setMain(String main){ this.main = main; }
    public void setIconName(String iconName){ this.iconName = iconName; }

    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dayNumber", dayNumber);
            jsonObject.put("dayString", dayString);
            jsonObject.put("main", main);
            jsonObject.put("city", city);
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
        String dayNumber = reader.getString("dayNumber");
        String dayString = reader.getString("dayString");
        String main = reader.getString("main");
        String city = reader.getString("city");
        String iconName = reader.getString("iconName");
        return new Forecast(dayNumber, dayString, main, temp, iconName, city);
    }
}
