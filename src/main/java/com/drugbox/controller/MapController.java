package com.drugbox.controller;

import com.drugbox.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
}
