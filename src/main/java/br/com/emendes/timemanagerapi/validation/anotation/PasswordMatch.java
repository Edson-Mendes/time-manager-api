package br.com.emendes.timemanagerapi.validation.anotation;

import br.com.emendes.timemanagerapi.validation.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordMatch {
  String message() default "passwords do not match";

   Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {};
}
