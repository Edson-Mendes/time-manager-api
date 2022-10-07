package br.com.emendes.timemanagerapi.validation.validator;

import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.validation.anotation.StatusValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StatusValidator implements ConstraintValidator<StatusValidation, String> {

  private Status[] notAllowedStatus;

  @Override
  public void initialize(StatusValidation constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    notAllowedStatus = constraintAnnotation.notAllowed();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()) return true;

    return isConvertible(value) && isAllowed(value);
  }

  private boolean isConvertible(String value) {
    try {
      Status.valueOf(value.toUpperCase());
      return true;
    } catch (IllegalArgumentException ex){
      return false;
    }
  }

  private boolean isAllowed(String value){
    Status status = Status.valueOf(value.toUpperCase());

    for (Status notAllowed : notAllowedStatus){
      if (status == notAllowed) return false;
    }
    return true;
  }

}
