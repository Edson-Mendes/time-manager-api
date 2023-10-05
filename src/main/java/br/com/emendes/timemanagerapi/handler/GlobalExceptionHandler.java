package br.com.emendes.timemanagerapi.handler;

import br.com.emendes.timemanagerapi.dto.response.detail.ExceptionDetails;
import br.com.emendes.timemanagerapi.dto.response.detail.ValidationExceptionDetails;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
import br.com.emendes.timemanagerapi.exception.IntervalCreationException;
import br.com.emendes.timemanagerapi.exception.IntervalNotFoundException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe responsável por lidar com as exceptions que ocorrerem.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    ExceptionDetails responseBody = generateExceptionDetails(ex.getMessage());

    return ResponseEntity.badRequest().body(responseBody);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    ExceptionDetails responseBody = generateExceptionDetails(ex.getMessage());

    return ResponseEntity.badRequest().body(responseBody);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    List<FieldError> fieldErrors = ex.getFieldErrors();

    String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining("; "));
    String messages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining("; "));

    ValidationExceptionDetails responseBody = ValidationExceptionDetails.builder()
        .title("Bad Request")
        .status(status.value())
        .timestamp(LocalDateTime.now())
        .details("Invalid field(s)")
        .fields(fields)
        .messages(messages)
        .build();

    return ResponseEntity.badRequest().body(responseBody);
  }

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

  @ExceptionHandler(ActivityNotFoundException.class)
  public ResponseEntity<ExceptionDetails> handleActivityNotFound(ActivityNotFoundException ex) {
    ExceptionDetails responseBody = generateExceptionDetails(ex.getMessage());

    return ResponseEntity.badRequest().body(responseBody);
  }

  @ExceptionHandler(IntervalNotFoundException.class)
  public ResponseEntity<ExceptionDetails> handleIntervalNotFound(IntervalNotFoundException ex) {
    ExceptionDetails responseBody = generateExceptionDetails(ex.getMessage());

    return ResponseEntity.badRequest().body(responseBody);
  }

  @ExceptionHandler(IntervalCreationException.class)
  public ResponseEntity<ExceptionDetails> handleIntervalCreation(IntervalCreationException ex) {
    ExceptionDetails responseBody = generateExceptionDetails(ex.getMessage());

    return ResponseEntity.badRequest().body(responseBody);
  }

  private ExceptionDetails generateExceptionDetails(String ex) {
    return ExceptionDetails.builder()
        .title("Bad Request")
        .status(HttpStatus.BAD_REQUEST.value())
        .timestamp(LocalDateTime.now())
        .details(ex)
        .build();
  }

}
