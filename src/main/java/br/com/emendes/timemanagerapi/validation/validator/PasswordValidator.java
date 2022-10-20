package br.com.emendes.timemanagerapi.validation.validator;

import br.com.emendes.timemanagerapi.dto.request.SignupRequest;
import br.com.emendes.timemanagerapi.validation.anotation.PasswordMatch;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class PasswordValidator implements ConstraintValidator<PasswordMatch, SignupRequest> {

  @Override
  public boolean isValid(SignupRequest value, ConstraintValidatorContext context) {
    context.buildConstraintViolationWithTemplate("passwords do not match").addPropertyNode("password");
    return Objects.equals(value.getPassword(), value.getConfirm());
  }
}
