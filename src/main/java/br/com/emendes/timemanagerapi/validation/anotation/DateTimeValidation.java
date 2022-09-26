package br.com.emendes.timemanagerapi.validation.anotation;

import br.com.emendes.timemanagerapi.validation.validator.DateTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * O elemento anotado deve ser uma {@code String} que pode ser convertido para {@code LocalDateTime} de acordo com o
 * {@code pattern} padrão/informado.<br>
 * Elementos {@code null} ou {@code blank} são considerados válidos.<br>
 * O pattern padrão é o ISO_LOCAL_DATE_TIME ou yyyy-MM-ddTHH:mm:ss<br>
 * Se o {@code pattern} informado for <strong>INVÁLIDO</strong>, então ValidationException será lançada ao validar.<br><br>
 *
 * {@code Exemplo:} 26/09/2022 10:35:40 é válido para o pattern dd/MM/yyyy HH:mm:ss.<br>
 * @author Edson Mendes
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTimeValidator.class)
public @interface DateTimeValidation {

  String message() default "Invalid Date Time";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String pattern() default "yyyy-MM-dd'T'HH:mm:ss";

}
