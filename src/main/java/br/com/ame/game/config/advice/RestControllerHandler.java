package br.com.ame.game.config.advice;

import br.com.ame.game.domain.ErrorResponse;
import br.com.ame.game.exception.PlanetAlreadyExistsException;
import br.com.ame.game.exception.PlanetNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerHandler {

    @ExceptionHandler(PlanetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(PlanetNotFoundException planetNotFoundException) {
        log.error(planetNotFoundException.getMessage(), planetNotFoundException);
        return new ResponseEntity<>(
                new ErrorResponse(planetNotFoundException.getExternalErrorCode(), HttpStatus.NOT_FOUND.value(), planetNotFoundException.getErrorCode(), planetNotFoundException.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlanetAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(PlanetAlreadyExistsException planetAlreadyExistsException) {
        log.error(planetAlreadyExistsException.getMessage(), planetAlreadyExistsException);
        return new ResponseEntity<>(
                new ErrorResponse(planetAlreadyExistsException.getExternalErrorCode(), HttpStatus.CONFLICT.value(), planetAlreadyExistsException.getErrorCode(), planetAlreadyExistsException.getErrorMessage()), HttpStatus.CONFLICT);
    }
}
