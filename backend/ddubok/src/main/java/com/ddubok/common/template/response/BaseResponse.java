package com.ddubok.common.template.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BaseResponse<T> {

    private final String code;
    private final String message;
    private final T data;

    @Builder
    private BaseResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 반환 데이터가 없는 성공 메시지 템플릿
     *
     * @param <T> 반환 데이터의 타입을 나타내는 제네릭 타입 파라미터
     * @return BaseResponse 객체만 반한
     */
    public static <T> BaseResponse<T> ofSuccess(ResponseCode responseCode) {
        return BaseResponse.<T>builder()
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(null)
            .build();
    }

    /**
     * 반환 데이터가 있는 성공 메시지 템플릿
     *
     * @param data 반환 데이터
     * @param <T>  반환 데이터의 타입을 나타내는 제네릭 타입 파라미터
     * @return BaseResponse 객체만 반한
     */
    public static <T> BaseResponse<T> ofSuccess(T data) {
        return BaseResponse.<T>builder()
            .code(ResponseCode.OK.getCode())
            .message(ResponseCode.OK.getMessage())
            .data(data)
            .build();
    }

    /**
     * 예외를 반환하는 실패 메시지 템플릿
     *
     * @param responseCode 상태코드 enum 클래스
     * @param <T>          반환 데이터의 타입을 나타내는 제네릭 타입 파라미터
     * @return 예외를 반환하는 실패 메시지 템플릿
     * @see ResponseCode
     */
    public static <T> BaseResponse<T> ofFail(ResponseCode responseCode) {
        return BaseResponse.<T>builder()
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(null)
            .build();
    }

}