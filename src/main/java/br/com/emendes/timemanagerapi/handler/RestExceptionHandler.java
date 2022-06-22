package br.com.emendes.timemanagerapi.handler;

import br.com.emendes.timemanagerapi.dto.response.BadRequestResponseBody;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(ActivityNotFoundException.class)
  public ResponseEntity<BadRequestResponseBody> handle(ActivityNotFoundException ex) {

    BadRequestResponseBody responseBody = BadRequestResponseBody.builder()
        .title("Bad Request")
        .status(HttpStatus.BAD_REQUEST.value())
        .timestamp(LocalDateTime.now())
        .details(ex.getMessage())
        .build();

    return ResponseEntity.badRequest().body(responseBody);
  }

}
