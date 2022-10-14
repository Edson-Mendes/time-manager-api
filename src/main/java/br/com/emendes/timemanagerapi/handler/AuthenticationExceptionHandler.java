package br.com.emendes.timemanagerapi.handler;

import br.com.emendes.timemanagerapi.dto.response.detail.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class AuthenticationExceptionHandler {

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ExceptionDetails> handle(AuthenticationException ex) {
    ExceptionDetails exceptionDetails = ExceptionDetails.builder()
        .title("Bad Request")
        .status(HttpStatus.BAD_REQUEST.value())
        .timestamp(LocalDateTime.now())
        .details(ex.getMessage())
        .build();

    return ResponseEntity.badRequest().body(exceptionDetails);
  }

}
