package com.drugbox.service;

import com.drugbox.common.exception.CustomException;
import com.drugbox.common.exception.ErrorCode;
import com.drugbox.domain.Drug;
import com.drugbox.domain.Drugbox;
import com.drugbox.dto.request.DrugDetailRequest;
import com.drugbox.dto.request.DrugRequest;
import com.drugbox.dto.response.DrugResponse;
import com.drugbox.dto.response.DrugboxResponse;
import com.drugbox.repository.DrugRepository;
import com.drugbox.repository.DrugboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DrugService {
    private final DrugRepository drugRepository;
    private final DrugboxRepository drugboxRepository;

    // 의약품 추가하기
    public List<Long> addDrug(DrugRequest request){
        Drugbox drugbox = getDrugboxOrThrow(request.getDrugboxId());
        List<Long> ids = new ArrayList<>();
        for(int i=0;i<request.getDetail().size();i++){
            DrugDetailRequest detail = request.getDetail().get(i);
            Drug drug = Drug.builder()
                    .name(request.getName())
                    .type(request.getType())
                    .count(detail.getCount())
                    .location(detail.getLocation())
                    .expDate(detail.getExpDate())
                    .drugbox(drugbox)
                    .build();
            drugRepository.save(drug);
            ids.add(drug.getId());
        }

        return ids;
    }

    // 의약품 리스트 확인하기
    public List<DrugResponse> getDrugList(Long DrugboxId){
        getDrugboxOrThrow(DrugboxId);
        List<Long> list = drugboxRepository.findDrugIdByDrugboxId(DrugboxId);
        return list.stream()
                .map(id-> DrugToDrugResponse(getDrugOrThrow(id)))
                .collect(Collectors.toList());
    }

    private Drugbox getDrugboxOrThrow(Long drugboxId) {
        return drugboxRepository.findById(drugboxId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DRUGBOX));
    }

    private Drug getDrugOrThrow(Long drugId){
        return drugRepository.findById(drugId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DRUG));
    }

    public DrugResponse DrugToDrugResponse(Drug drug){
        return DrugResponse.builder()
                .id(drug.getId())
                .name(drug.getName())
                .location(drug.getLocation())
                .expDate(drug.getExpDate())
                .build();
    }
}
