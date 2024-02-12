package com.drugbox.controller;

import com.drugbox.dto.response.BinLocationResponse;
import com.drugbox.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("maps")
public class MapController {
    private final MapService mapService;

    @PostMapping("/seoul/save")
    public ResponseEntity<Void> saveSeoulDrugBinLocations() {
        mapService.saveSeoulDrugBinLocations();
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/division/save")
    public ResponseEntity<Void> saveDrugBinLocations() {
        mapService.saveDrugBinLocations();
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/seoul")
    public ResponseEntity<List<BinLocationResponse>> getSeoulDrugBinLocations(){
        List<BinLocationResponse> response = mapService.getSeoulDrugBinLocations();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/division")
    public ResponseEntity<List<BinLocationResponse>> getDivisionDrugBinLocations(
            @RequestParam(value="addrLvl1") String addrLvl1,
            @RequestParam(value="addrLvl2", required = false) String addrLvl2){
        List<BinLocationResponse> response = mapService.getDivisionDrugBinLocations(addrLvl1, addrLvl2);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
