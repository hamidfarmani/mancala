package com.bol.mancala.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(value = {WrongPitSelectedException.class})
  public ResponseEntity<Object> handleWrongPitSelectedException(WrongPitSelectedException e) {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    ApiException apiException =
        new ApiException(e.getMessage(), badRequest, ZonedDateTime.now(ZoneId.of("Z")));
    return new ResponseEntity<>(apiException, badRequest);
  }

  @ExceptionHandler(value = {EmptyPitSelectedException.class})
  public ResponseEntity<Object> handleEmptyPitSelectedException(EmptyPitSelectedException e) {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    ApiException apiException =
        new ApiException(e.getMessage(), badRequest, ZonedDateTime.now(ZoneId.of("Z")));
    return new ResponseEntity<>(apiException, badRequest);
  }

  @ExceptionHandler(value = {GameNotFoundException.class})
  public ResponseEntity<Object> handleGameNotFoundException(GameNotFoundException e) {
    HttpStatus notFound = HttpStatus.NOT_FOUND;
    ApiException apiException =
        new ApiException(e.getMessage(), notFound, ZonedDateTime.now(ZoneId.of("Z")));
    return new ResponseEntity<>(apiException, notFound);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException e) {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    ApiException apiException =
        new ApiException(e.getMessage(), badRequest, ZonedDateTime.now(ZoneId.of("Z")));
    return new ResponseEntity<>(apiException, badRequest);
  }
}
