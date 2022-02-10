package com.nehaanand.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

public class weather_data {
    private String mTemperature, micon, mCity, mWeatherType;
    private int mcondition;
    public static weather_data fromJson(JSONObject jsonObject){
        try{
            weather_data weatherD = new weather_data();
            weatherD.mCity=jsonObject.getString("name");
            weatherD.mcondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.micon = updateWeatherIcon(weatherD.mcondition);
            double tempresult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedvalue = (int)Math.rint(tempresult);
            weatherD.mTemperature=Integer.toString(roundedvalue);
            return weatherD;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int condition){
        if(condition>= 0 && condition <= 300){
            return "thunderstorm1";
        }
        else if(condition>= 300 && condition <= 500){
            return "lightrain";
        }
        else if(condition>=500 && condition <= 600){
            return "shower_1";
        }
        else if(condition>= 600 && condition <= 700){
            return "snow_2";
        }
        else if(condition>= 701 && condition <= 771){
            return "fog";
        }
        else if(condition>= 772 && condition <= 800){
            return "overcast";
        }
        else if(condition== 800){
            return "sunny";
        }
        else if(condition>= 801 && condition <= 804){
            return "cloudy";
        }
        else if(condition>= 900 && condition <= 902){
            return "thunderstorm1";
        }
        else if(condition==903){
            return "snow_2";
        }
        else if(condition==904){
            return "sunny";
        }
        else if(condition>= 905 && condition <= 1000){
            return "thunderstorm2";
        }
        return "dunno";
    }

    public String getmTemperature(){
        return mTemperature+"°C";
    }

    public String getMicon(){
        return micon;
    }

    public String getmCity(){
        return mCity;
    }

    public String getmWeatherType(){
        return mWeatherType;
    }
}
