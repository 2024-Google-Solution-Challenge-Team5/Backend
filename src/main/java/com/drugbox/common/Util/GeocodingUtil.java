package com.drugbox.common.Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Component
public class GeocodingUtil {

    @Value("${application.spring.cloud.gcp.geocodingAPI}")
    private String API_KEY;

    public Map<String, String> getCoordsByAddress(String completeAddress) {

        try {
            String surl = "https://maps.googleapis.com/maps/api/geocode/json?address="+ URLEncoder.encode(completeAddress, "UTF-8")+
                    "&key="+URLEncoder.encode(API_KEY, "UTF-8");
            URL url = new URL(surl);
            InputStream is = url.openConnection().getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            JSONParser parser = new JSONParser();
            JSONObject jo = (JSONObject) parser.parse(responseStrBuilder.toString());
            JSONArray results = (JSONArray) jo.get("results");
            String status = jo.get("status").toString();
            Map<String, String> ret = new HashMap<String, String>();
            if(status.equals("OK")) {
                JSONObject jsonObject = (JSONObject) results.get(0);
                JSONObject geometry = (JSONObject) jsonObject.get("geometry");
                JSONObject location = (JSONObject) geometry.get("location");
                String lat = location.get("lat").toString();
                String lng = location.get("lng").toString();
                ret.put("lat", lat);
                ret.put("lng", lng);

                return ret;
            }
            System.out.println("Address:" + completeAddress);
            System.out.println(responseStrBuilder);
            ret.put("lat", "0");
            ret.put("lng", "0");

            return ret;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
