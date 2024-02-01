package com.drugbox.service;

import com.drugbox.domain.DrugInfo;
import com.drugbox.repository.DrugInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DrugApiService {
    @Value("${application.spring.api.key}")
    private String key;
    @Value("${application.spring.api.url}")
    private String url;

    private final DrugInfoRepository drugInfoRepository;

    public void getDrugInfo(String drugName) throws IOException, ParseException {
        URL url = new URL(createUrlForDrugDetail(drugName));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String result = sb.toString();
        parseDrugInfo(result);
    }

    public String createUrlForDrugDetail(String name) throws IOException{
        StringBuilder urlBuilder = new StringBuilder(url); // api url
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + key); // api key
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); // 검색 결과 개수
        urlBuilder.append("&" + URLEncoder.encode("itemName","UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")); // 찾는 의약품 이름
        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); // xml,json 중 json
        return urlBuilder.toString();
    }

    public void parseDrugInfo(String json) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        JSONObject object = (JSONObject) jsonObject.get("body");
        JSONArray array =(JSONArray) object.get("items");
        JSONObject getInfo = (JSONObject) array.get(0);
        addDrugInfo(getInfo);
    }

    public void addDrugInfo(JSONObject getInfo){
        DrugInfo drugInfo = DrugInfo.builder()
                .name((String)getInfo.get(""))
                .effect((String) getInfo.get("efcyQesitm"))
                .updateDate((LocalDate) getInfo.get(""))
                .build();
        drugInfoRepository.save(drugInfo);
    }

}