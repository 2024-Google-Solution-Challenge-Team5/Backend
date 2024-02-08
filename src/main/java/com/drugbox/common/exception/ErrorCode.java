package com.drugbox.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // User 예외
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    EXIST_USER_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    SECURITY_CONTEXT_NOT_FOUND(HttpStatus.NOT_FOUND, "Security Context 에 인증 정보가 없습니다."),
    EXIST_USER(HttpStatus.CONFLICT, "이미 가입되어 있는 유저입니다."),
    LOGOUTED_USER(HttpStatus.CONFLICT, "로그아웃 된 사용자입니다."),


    // Token 예외
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "Refresh Token을 찾을 수 없는 사용자입니다. 다시 로그인하세요."),
    UNAUTHORIZED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 유효하지 않습니다."),
    ID_TOKEN_INVALID(HttpStatus.UNPROCESSABLE_ENTITY, "ID Token 값이 잘못되었습니다. OAUTH 로그인을 다시 시도해보세요."),
    QUIT_ERROR(HttpStatus.BAD_REQUEST, "OAUTH 탈퇴 과정 중 에러가 발생했습니다."),

    // Drugbox 예외
    NOT_FOUND_DRUGBOX(HttpStatus.NOT_FOUND, "해당 drugbox를 찾을 수 없습니다."),
    USER_ALREADY_DRUGBOX_MEMBER(HttpStatus.CONFLICT, "해당 유저가 이미 해당 구급상자 그룹에 속해있습니다."),

    // Drug 예외
    NOT_FOUND_DRUG(HttpStatus.NOT_FOUND, "해당 약 정보를 찾을 수 없습니다."),
    NOT_FOUND_DRUGINFO(HttpStatus.NOT_FOUND, "해당 약 상세정보를 찾을 수 없습니다"),
    DRUG_NOT_IN_DISPOSAL_LIST(HttpStatus.CONFLICT, "해당 약은 폐의약품 리스트에 없습니다."),

    // 이미지 예외
    IMAGE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지 파일은 10MB이하여야 합니다."),
    IMAGE_TYPE_INVALID(HttpStatus.BAD_REQUEST, "이미지 파일 타입이 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
