package com.drugbox.service;

import com.drugbox.common.exception.CustomException;
import com.drugbox.common.exception.ErrorCode;
import com.drugbox.domain.Drug;
import com.drugbox.domain.Drugbox;
import com.drugbox.dto.request.DrugDetailRequest;
import com.drugbox.dto.request.DrugRequest;
import com.drugbox.dto.request.DrugUseRequest;
import com.drugbox.dto.response.DrugDetailResponse;
import com.drugbox.dto.response.DrugListResponse;
import com.drugbox.dto.response.DrugResponse;
import com.drugbox.repository.DrugRepository;
import com.drugbox.repository.DrugboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DrugService {
    private final DrugRepository drugRepository;
    private final DrugboxRepository drugboxRepository;
    private final DrugApiService drugApiService;

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

            List<Drug> drugs = drugbox.getDrugs();
            drugs.add(drug);
            drugbox.setDrugs(drugs);
            drugboxRepository.save(drugbox);

            ids.add(drug.getId());
        }

        return ids;
    }

    // 의약품 리스트 확인하기
    public List<DrugResponse> getDrugList(Long DrugboxId){
        Drugbox drugbox = getDrugboxOrThrow(DrugboxId);
        List<Drug> list = drugbox.getDrugs();
        return list.stream()
                .map(drug-> DrugToDrugResponse(getDrugOrThrow(drug)))
                .collect(Collectors.toList());
    }

    // 의약품 사용하기
    public void useDrug(List<DrugUseRequest> drugUseRequests){
        for(DrugUseRequest drugUseList : drugUseRequests){
            Drugbox drugbox = getDrugboxOrThrow(drugUseList.getDrugboxId());
            for(Long drugId : drugUseList.getDrugIds()){
                Drug drug = getDrugOrThrowById(drugId);
                int count = drug.getCount()-1;

                if(count==0){ // count 0되면 삭제
                    drugRepository.delete(drug);
                    List<Drug> drugs = drugbox.getDrugs();
                    for (Iterator<Drug> iterator = drugs.iterator(); iterator.hasNext();) {
                        Drug boxDrug = iterator.next();
                        if (boxDrug.getId().equals(drugId)) {
                            iterator.remove(); // 리스트에서 삭제
                            break;
                        }
                    }
                }else {
                    drug.setCount(count);
                    drugRepository.save(drug);
                    drugboxRepository.save(drugbox);
                }
            }
        }
    }

    // 의약품 폐기리스트로 옮기기
    public void disposeDrug(Long drugboxId, Long drugId){
        Drugbox drugbox = getDrugboxOrThrow(drugboxId);
        Drug drug = getDrugOrThrowById(drugId);

        drug.setStatus(1);
        drugRepository.save(drug);
        drugboxRepository.save(drugbox);
    }

    // 의약품 상세정보 확인하기
    public DrugDetailResponse getDrugDetail(Long drugboxId, String name) throws IOException, ParseException {
        List<Drug> drugs = getDrugboxOrThrow(drugboxId).getDrugs();
        List<Drug> results = drugs.stream()
                .filter(drug -> name.equals(drug.getName()))
                .collect(Collectors.toList());

        List<DrugListResponse> drugListResponses = new ArrayList<>();
        for(Drug result : results){
            DrugListResponse drugListResponse = DrugListResponse.builder()
                    .location(result.getLocation())
                    .count(result.getCount())
                    .expDate(result.getExpDate())
                    .build();
            drugListResponses.add(drugListResponse);
        }

        return DrugDetailResponse.builder()
                .name(name)
                .drugListResponseList(drugListResponses)
                .effect(drugApiService.getDrugInfo(name))
                .build();
    }

    private Drugbox getDrugboxOrThrow(Long drugboxId) {
        return drugboxRepository.findById(drugboxId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DRUGBOX));
    }

    private Drug getDrugOrThrow(Drug drug){
        Long drugId = drug.getId();
        return drugRepository.findById(drugId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DRUG));
    }

    private Drug getDrugOrThrowById(Long drugId){
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
