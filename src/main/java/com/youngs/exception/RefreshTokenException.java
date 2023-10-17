package com.youngs.exception;

/**
 * Refresh Token 관련 예외를 나타내는 사용자 정의 예외 클래스
 * @author : 박상희
 **/
public class RefreshTokenException extends RuntimeException {
    /**
     * @author : 박상희
     * @param message : 예외에 대한 설명 메시지
     */
    public RefreshTokenException(String message) {
        super(message);
    }
}
