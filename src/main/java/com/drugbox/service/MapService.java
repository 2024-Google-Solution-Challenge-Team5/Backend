package com.drugbox.service;

import com.drugbox.common.Util.GeocodingUtil;
import com.drugbox.domain.BinLocation;
import com.drugbox.dto.response.BinLocationResponse;
import com.drugbox.repository.BinLocationRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MapService {
    private final BinLocationRepository binLocationRepository;
    private final GeocodingUtil geocodingUtil;

    public void saveSeoulDrugBinLocations(){
        JSONParser parser = new JSONParser();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("seoul.geojson")){
            JSONObject jsonObject = (JSONObject) parser.parse(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));

            JSONArray features = (JSONArray) jsonObject.get("features");
            for(Object obj : features){
                JSONObject feature = (JSONObject) obj;
                JSONObject properties = (JSONObject) feature.get("properties");
                String lat = (String) properties.get("COORD_Y");
                String lng = (String) properties.get("COORD_X");
                String addr = (String) properties.get("ADDR_NEW"); // 도로명주소
                String detail = (String) properties.get("VALUE_01");  // ex) 복지관, 구청, 주민센터
                String[] parts = addr.split("\\s+");
                String addrLvl1 = parts[0];
                String addrLvl2 = parts[1];

                BinLocation bin = BinLocation.builder()
                        .lat(lat)
                        .lng(lng)
                        .address(addr)
                        .detail(detail)
                        .addrLvl1(addrLvl1)
                        .addrLvl2(addrLvl2)
                        .build();
                binLocationRepository.save(bin);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    public void saveDrugBinLocations(){
        saveSeoulDrugBinLocations();

        String[] line;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drugbin.CSV")){
            CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(inputStream, "UTF-8"))
                    .withSkipLines(1) // skip header
                    .build();
            while((line = csvReader.readNext()) != null) {
                String lat = "";
                String lng = "";
                if(line[4].equals("") || line[5].equals("")){
                    Map<String, String> coords = geocodingUtil.getCoordsByAddress(line[3]);
                    lat = coords.get("lat");
                    lng = coords.get("lng");
                } else{
                    lat = line[4];
                    lng = line[5];
                }
                BinLocation bin = BinLocation.builder()
                        .lat(lat)
                        .lng(lng)
                        .addrLvl1(line[0])
                        .addrLvl2(line[1])
                        .detail(line[2])
                        .address(line[3])
                        .build();
                binLocationRepository.save(bin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<BinLocationResponse> getSeoulDrugBinLocations(){
        List<BinLocation> binLocations = binLocationRepository.findAll();
        return binLocations.stream()
                .map(bin -> BinLocationToBinLocationResponse(bin))
                .collect(Collectors.toList());
    }

    public List<BinLocationResponse> getDivisionDrugBinLocations(String addrLvl1, String addrLvl2){
        List<BinLocation> binLocations = new ArrayList<>();
        if(addrLvl2 == null){
            binLocations = binLocationRepository.findAllByAddrLvl1(addrLvl1);
        } else {
            binLocations = binLocationRepository.findAllByAddrLvl1AndAddrLvl2(addrLvl1, addrLvl2);
        }
        return binLocations.stream()
                .map(bin -> BinLocationToBinLocationResponse(bin))
                .collect(Collectors.toList());
    }

    private BinLocationResponse BinLocationToBinLocationResponse(BinLocation bin){
        return BinLocationResponse.builder()
                .id(bin.getId())
                .address(bin.getAddress())
                .lat(bin.getLat())
                .lng(bin.getLng())
                .detail(bin.getDetail())
                .addrLvl1(bin.getAddrLvl1())
                .addrLvl2(bin.getAddrLvl2())
                .build();
    }
}