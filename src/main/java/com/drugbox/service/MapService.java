package com.drugbox.service;

import com.drugbox.domain.BinLocation;
import com.drugbox.dto.response.BinLocationResponse;
import com.drugbox.repository.BinLocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MapService {
    private final BinLocationRepository binLocationRepository;

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
