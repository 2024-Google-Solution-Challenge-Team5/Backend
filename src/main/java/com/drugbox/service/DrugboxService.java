package com.drugbox.service;

import com.drugbox.repository.DrugboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DrugboxService {
    private final DrugboxRepository drugboxRepository;
}
