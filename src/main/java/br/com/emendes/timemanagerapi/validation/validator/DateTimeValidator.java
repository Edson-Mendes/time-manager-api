package br.com.emendes.timemanagerapi.validation.validator;

import br.com.emendes.timemanagerapi.validation.anotation.DateTimeValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeValidator implements ConstraintValidator<DateTimeValidation, String> {

  private DateTimeFormatter formatter;

  @Override
  public void initialize(DateTimeValidation constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    formatter = DateTimeFormatter.ofPattern(constraintAnnotation.pattern());
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()){
      return true;
    }
    return isConvertible(value);
  }

  private boolean isConvertible(String value) {
    try {
      LocalDateTime.parse(value, formatter);
      return true;
    } catch (DateTimeParseException ex){
      return false;
    }
  }

}
