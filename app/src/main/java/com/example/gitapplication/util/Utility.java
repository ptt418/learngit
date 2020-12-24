package com.example.gitapplication.util;

import android.text.TextUtils;

import com.example.gitapplication.db.City;
import com.example.gitapplication.db.County;
import com.example.gitapplication.db.Province;
import com.example.gitapplication.gson.Weather;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class Utility {
    //解析和处理服务器返回的省级数据
    public static boolean handleProvinceResponse(String response) {
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces = new JSONArray(response);
                for(int i =0;i< allProvinces.length();i++) {
                    JSONObject provinceObkect = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObkect.getString("name"));
                    province.setProvinceCode(provinceObkect.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    //市
    public static boolean handleCityResponse(String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for(int i=0;i<allCities.length();i++){
                    JSONObject cityObect = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObect.getString("name"));
                    city.setCityCode(cityObect.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //省级
    public static  boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allCounties = new JSONArray(response);
                for(int i=0; i < allCounties.length();i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather handleWeatherResponse(String response){

        try {
            JSONObject jsonObject = new JSONObject(JSONTokener(response));
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String JSONTokener(String in) {
        // consume an optional byte order mark (BOM) if it exists
        if (in != null && in.startsWith("\ufeff")) {
            in = in.substring(1);
        }
        return in;
    }

}
