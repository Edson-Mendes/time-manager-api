package br.com.emendes.timemanagerapi.validation.anotation;

import br.com.emendes.timemanagerapi.validation.validator.TimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * O elemento anotado deve ser uma {@code String} que pode ser convertido para {@code LocalTime}.<br>
 * O elemento deve estar no formato ISO_LOCAL_TIME (HH:mm:ss). <br>
 * Elementos {@code null} ou {@code blank} são considerados válidos.<br><br>
 * {@code Exemplo:} 10:35:40 é válido.<br>
 * @author Edson Mendes
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeValidator.class)
public @interface TimeValidation {

  String message() default "invalid elapsed time";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
