package br.com.ame.game.exception;

import lombok.Getter;

@Getter
public class BusinessException extends Exception {
    private final String externalErrorCode;
    private final Object errorCode;
    private final String errorMessage;

    public BusinessException(String externalErrorCode, Object errorCode, String errorMessage) {
        super(errorMessage);
        this.externalErrorCode = externalErrorCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
