package br.com.emendes.timemanagerapi.validation.validator;

import br.com.emendes.timemanagerapi.validation.anotation.TimeValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class TimeValidator implements ConstraintValidator<TimeValidation, String> {

  @Override
  public void initialize(TimeValidation constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()){
      return true;
    }
    return isConvertible(value);
  }

  private boolean isConvertible(String value){
    try{
      LocalTime.parse(value);
      return true;
    }catch(Exception ex){
      return false;
    }
  }

}
