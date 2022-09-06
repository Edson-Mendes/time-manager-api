package br.com.emendes.timemanagerapi.exception;

public class IntervalNotFoundException extends RuntimeException {
  public IntervalNotFoundException(String message) {
    super(message);
  }
}
