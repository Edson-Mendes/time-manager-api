package br.com.emendes.timemanagerapi.exception;

public class ActivitiesNotFoundException extends RuntimeException {
  public ActivitiesNotFoundException(String message) {
    super(message);
  }
}
