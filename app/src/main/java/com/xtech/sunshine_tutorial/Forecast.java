package com.xtech.sunshine_tutorial;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Forecast {
    private String dayNumber, dayString, main, iconName, city;
    private HashMap<String, String> temp;

    public Forecast(String dayNumber, String dayString, String main, HashMap<String, String> temp, String iconName, String city){
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
    public String getTempMax(){
        String max = this.temp.get("max");
        //No rounding made, I just cuttend the float value at the 4th position
        return max.substring(0,2) + "°";
    }

    public String getTempDay(){ return this.temp.get("day"); }
    public String getTempMin(){ return this.temp.get("min"); }
    public String getTempNight(){ return this.temp.get("night"); }
    public String getTempEve(){ return this.temp.get("eve"); }
    public String getTempMorn(){ return this.temp.get("morn"); }
    public String getIconName(){ return this.iconName; }

    public void setCity(String city){ this.city = city; }
    public void setDayNumber(String dayNumber){ this.dayNumber = dayNumber; }
    public void setDayString(String dayString){ this.dayString = dayString; }
    public void setMain(String main){ this.main = main; }
    public void setIconName(String iconName){ this.iconName = iconName; }
    public void setTempMax(String max){ this.temp.put("max", max); }
    public void setTempDay(String day){ this.temp.put("day", day); }
    public void setTempEve(String eve){ this.temp.put("eve", eve); }
    public void setTempMorn(String morn){ this.temp.put("morn", morn); }
    public void setTempNight(String night){ this.temp.put("night", night); }
    public void setTempMin(String min){ this.temp.put("min", min); }

    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dayNumber", dayNumber);
            jsonObject.put("dayString", dayString);
            jsonObject.put("main", main);
            jsonObject.put("city", city);
            jsonObject.put("max", getTempMax());
            jsonObject.put("min", getTempMin());
            jsonObject.put("eve", getTempEve());
            jsonObject.put("morn", getTempMorn());
            jsonObject.put("night", getTempNight());
            jsonObject.put("day", getTempDay());

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
        HashMap<String, String> temp = new HashMap<String, String>();
        String max = reader.getString("max");
        String min = reader.getString("min");
        String eve = reader.getString("eve");
        String morn = reader.getString("morn");
        String night = reader.getString("night");
        String day = reader.getString("day");
        temp.put("max", max);
        temp.put("min", min);
        temp.put("eve", eve);
        temp.put("morn", morn);
        temp.put("night", night);
        temp.put("day", day);

        String dayNumber = reader.getString("dayNumber");
        String dayString = reader.getString("dayString");
        String main = reader.getString("main");
        String city = reader.getString("city");
        String iconName = reader.getString("iconName");
        return new Forecast(dayNumber, dayString, main, temp, iconName, city);
    }
}
