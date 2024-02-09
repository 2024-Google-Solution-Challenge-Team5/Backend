package com.drugbox.service;

import com.drugbox.common.exception.CustomException;
import com.drugbox.common.exception.ErrorCode;
import com.drugbox.domain.Drug;
import com.drugbox.domain.DrugInfo;
import com.drugbox.domain.Drugbox;
import com.drugbox.domain.User;
import com.drugbox.dto.request.DrugDetailRequest;
import com.drugbox.dto.request.DrugRequest;
import com.drugbox.dto.request.DrugUpdateRequest;
import com.drugbox.dto.response.DisposalResponse;
import com.drugbox.dto.response.DrugDetailResponse;
import com.drugbox.dto.response.DrugResponse;
import com.drugbox.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
    private final DrugInfoRepository drugInfoRepository;
    private final UserRepository userRepository;
    private final UserDrugboxRepository userDrugboxRepository;

    private final DrugApiService drugApiService;


    // 의약품 추가하기
    public List<Long> addDrug(DrugRequest request) throws IOException, ParseException {
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

            checkDrugInfoSave(request.getName());

            ids.add(drug.getId());
        }

        return ids;
    }

    // 의약품 리스트 확인하기
    public List<DrugResponse> getDrugs(Long DrugboxId){
        Drugbox drugbox = getDrugboxOrThrow(DrugboxId);
        List<Drug> drugs = drugbox.getDrugs();
        return drugs.stream()
                .map(drug -> getDrugOrThrow(drug))
                .filter(drug -> !drug.isInDisposalList())
                .map(drug-> DrugToDrugResponse(drug))
                .collect(Collectors.toList());
    }

    // 의약품 사용하기
    public void useDrug(List<DrugUpdateRequest> drugUpdateRequests){
        for(DrugUpdateRequest updateRequest : drugUpdateRequests){
            Drugbox drugbox = getDrugboxOrThrow(updateRequest.getDrugboxId());
            for(Long drugId : updateRequest.getDrugIds()){
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

        drug.addToDisposalList();
        drugRepository.save(drug);
        drugboxRepository.save(drugbox);
    }

    // 의약품 상세정보 확인하기
    public DrugDetailResponse getDrugDetail(Long drugboxId, String name) throws IOException, ParseException {
        List<Drug> drugs = getDrugboxOrThrow(drugboxId).getDrugs();
        List<Drug> results = drugs.stream()
                .filter(drug -> name.equals(drug.getName()))
                .collect(Collectors.toList());

        List<DrugResponse> drugResponses = new ArrayList<>();
        for(Drug result : results){
            DrugResponse drugResponse = DrugResponse.builder()
                    .id(result.getId())
                    .name(name)
                    .location(result.getLocation())
                    .count(result.getCount())
                    .expDate(result.getExpDate())
                    .build();
            drugResponses.add(drugResponse);
        }
        DrugInfo drugInfo = getDrugInfoOrThrow(name);
        return DrugDetailResponse.builder()
                .name(name)
                .drugResponseList(drugResponses)
                .effect(drugInfo.getEffect())
                .build();
    }

    // 폐의약품 리스트에서 약 삭제하기
    public void deleteDrugFromDisposalList(List<DrugUpdateRequest> drugUpdateRequests){
        for(DrugUpdateRequest updateRequest : drugUpdateRequests){
            Drugbox drugbox = getDrugboxOrThrow(updateRequest.getDrugboxId());
            for(Long drugId : updateRequest.getDrugIds()){
                Drug drug = getDrugOrThrowById(drugId);
                if(!drug.isInDisposalList()){
                    throw new CustomException(ErrorCode.DRUG_NOT_IN_DISPOSAL_LIST);
                }
                drugRepository.delete(drug);
                drugboxRepository.save(drugbox);
            }
        }

    }

    // 폐의약품 리스트 가져오기
    public List<DisposalResponse> getDisposalList(Long userId){
        getUserOrThrow(userId);
        List<Long> ids = userDrugboxRepository.findDrugboxIdByUserId(userId);
        List<DisposalResponse> disposalResponses = new ArrayList<>();
        for(Long id : ids){
            Drugbox drugbox = getDrugboxOrThrow(id);
            List<Drug> drugs = drugbox.getDrugs();
            List<DrugResponse> drugResponses = drugs.stream()
                    .map(drug -> getDrugOrThrow(drug))
                    .filter(drug -> drug.isInDisposalList())
                    .map(drug-> DrugToDrugResponse(drug))
                    .collect(Collectors.toList());

            DisposalResponse disposalResponse = DisposalResponse.builder()
                    .drugboxName(drugbox.getName())
                    .drugResponses(drugResponses)
                    .build();
            disposalResponses.add(disposalResponse);
        }
        return disposalResponses;
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

    private DrugInfo getDrugInfoOrThrow(String name){
        return drugInfoRepository.findByName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DRUGINFO));
    }

    private void checkDrugInfoSave(String name) throws IOException, ParseException {
        Optional<DrugInfo> drugInfo = drugInfoRepository.findByName(name);
        if(drugInfo.isEmpty()){
            drugApiService.getDrugInfo(name);
        }
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public DrugResponse DrugToDrugResponse(Drug drug){
        return DrugResponse.builder()
                .id(drug.getId())
                .name(drug.getName())
                .location(drug.getLocation())
                .expDate(drug.getExpDate())
                .count(drug.getCount())
                .isInDisposalList(drug.isInDisposalList())
                .build();
    }
}
