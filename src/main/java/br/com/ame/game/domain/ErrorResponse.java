package br.com.ame.game.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private String externalErrorCode;
    private Integer httpCode;
    private Object errorCode;
    private String errorMessage;
}
