package br.com.emendes.timemanagerapi.exception;

public class ActivityNotFoundException extends RuntimeException {
  public ActivityNotFoundException(String message) {
    super(message);
  }
}
