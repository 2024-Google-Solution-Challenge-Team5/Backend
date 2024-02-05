package com.drugbox.service;

import com.drugbox.domain.Drug;
import com.drugbox.domain.DrugInfo;
import com.drugbox.repository.DrugInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public String getDrugApi(URL url) throws IOException{
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
        return sb.toString();
    }

    public void getDrugInfo(String drugName) throws IOException, ParseException{
        String result = getDrugApi(createUrlForDrugDetail(drugName));
        addDrugInfo(parseDrugInfo(result));
    }

    public URL createUrlForDrugDetail(String name) throws IOException{
        StringBuilder urlBuilder = new StringBuilder(url); // api url
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + key); // api key
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); // 검색 결과 개수
        urlBuilder.append("&" + URLEncoder.encode("itemName","UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")); // 찾는 의약품 이름
        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); // xml,json 중 json
        return new URL(urlBuilder.toString());
    }

    public JSONArray parseDrugInfo(String json) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        JSONObject object = (JSONObject) jsonObject.get("body");
        return (JSONArray) object.get("items");
    }

    public void addDrugInfo(JSONArray array){
        JSONObject getInfo = (JSONObject) array.get(0);
        DrugInfo drugInfo = DrugInfo.builder()
                .name((String)getInfo.get("itemName"))
                .effect((String) getInfo.get("efcyQesitm"))
                .updateDate(LocalDate.parse((String)getInfo.get("updateDe")))
                .build();
        drugInfoRepository.save(drugInfo);
    }

    @Scheduled(cron="0 0 0 */14 * ?")
    public void checkDrugInfoUpdate() throws IOException, ParseException {
        List<DrugInfo> drugInfos = drugInfoRepository.findAll();
        for (Iterator<DrugInfo> iterator = drugInfos.iterator(); iterator.hasNext();) {
            DrugInfo drugInfo = iterator.next();
            String result = getDrugApi(createUrlForDrugDetail(drugInfo.getName()));
            JSONArray array = parseDrugInfo(result);
            JSONObject jsonObject = (JSONObject) array.get(0);
            LocalDate updateDate = LocalDate.parse((String)jsonObject.get("updateDe"));

            if(!updateDate.isEqual(drugInfo.getUpdateDate())){
                addDrugInfo(array);
            }
        }
    }

    // 약 이름으로 검색하기
    public List<String> getSearchDrugs(String name) throws IOException, ParseException {
        String result = getDrugApi(createUrlForSearchDrugs(name));
        JSONArray array = parseDrugInfo(result);
        List<String> drugs = new ArrayList<>();
        for (Object o : array) {
            JSONObject jsonObject = (JSONObject) o;
            drugs.add((String) jsonObject.get("itemName"));
        }
        return drugs;
    }

    public URL createUrlForSearchDrugs(String name) throws IOException{
        StringBuilder urlBuilder = new StringBuilder(url); // api url
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + key); // api key
        urlBuilder.append("&" + URLEncoder.encode("itemName","UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")); // 찾는 의약품 이름
        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); // xml,json 중 json
        return new URL(urlBuilder.toString());
    }

}
