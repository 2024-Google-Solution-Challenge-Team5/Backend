package com.drugbox.controller;

import com.drugbox.service.DrugboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("drugbox")
public class DrugboxController {
    private final DrugboxService drugboxService;
}
