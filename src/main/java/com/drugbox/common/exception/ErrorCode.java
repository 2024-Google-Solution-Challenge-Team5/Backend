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

    // Drugbox 예외
    NOT_FOUND_DRUGBOX(HttpStatus.NOT_FOUND, "해당 drugbox를 찾을 수 없습니다."),

    // Drug 예외
    NOT_FOUND_DRUG(HttpStatus.NOT_FOUND, "해당 약 정보를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
