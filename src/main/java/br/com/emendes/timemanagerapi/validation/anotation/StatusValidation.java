package br.com.emendes.timemanagerapi.validation.anotation;

import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.validation.validator.StatusValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StatusValidator.class)
public @interface StatusValidation {

  String message() default "invalid status";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Status[] notAllowed() default {};
}
