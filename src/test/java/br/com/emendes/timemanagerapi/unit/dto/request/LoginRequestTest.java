package br.com.emendes.timemanagerapi.unit.dto.request;

import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@DisplayName("Unit tests for LoginRequest")
class LoginRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
  private final String VALID_EMAIL = "user@email.com";
  private final String VALID_PASSWORD = "123456";

  @Nested
  @DisplayName("tests for email validation")
  class EmailValidation {

    @ParameterizedTest
    @ValueSource(strings = {"user@gmail.com", "user@email.com.br", "user@outlook.com"})
    @DisplayName("validate email must not return violations when email is valid")
    void validateEmail_MustNotReturnViolations_WhenEmailIsValid(String email) {
      LoginRequest loginRequest =
          new LoginRequest(email, VALID_PASSWORD);

      Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

      Assertions.assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"usergmail.com", "@emailcombr", "user@"})
    @DisplayName("validate email must not return violations when email is valid")
    void validateEmail_MustReturnViolations_WhenEmailIsInvalid(String email) {
      LoginRequest loginRequest =
          new LoginRequest(email, VALID_PASSWORD);

      Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

      Assertions.assertThat(violations).isNotEmpty().hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("must be a well-formed email address");
    }

    @Test
    @DisplayName("validate email must returns violations when email is null")
    void validateEmail_MustReturnViolations_WhenEmailIsNull() {
      LoginRequest loginRequest =
          new LoginRequest(null, VALID_PASSWORD);

      Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

      Assertions.assertThat(violations)
          .isNotEmpty()
          .hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("email must not be null or blank");
    }

    @Test
    @DisplayName("validate email must returns violations when email is empty")
    void validateEmail_MustReturnsViolations_WhenEmailIsEmpty() {
      LoginRequest loginRequest =
          new LoginRequest("", VALID_PASSWORD);

      Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

      Assertions.assertThat(violations).isNotEmpty().hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("email must not be null or blank");
    }

    @Test
    @DisplayName("validate email must returns violations when email is blank")
    void validateEmail_MustReturnsViolations_WhenEmailIsBlank() {
      LoginRequest loginRequest =
          new LoginRequest("   ", VALID_PASSWORD);

      Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

      Assertions.assertThat(violations).isNotEmpty().hasSize(2);
    }
  }

  @Nested
  @DisplayName("tests for password validation")
  class PasswordValidation {

    @Test
    @DisplayName("validate password must returns violations when password is null")
    void validatePassword_MustReturnViolations_WhenPasswordIsNull() {
      LoginRequest loginRequest =
          new LoginRequest(VALID_EMAIL, null);

      Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

      Assertions.assertThat(violations)
          .isNotEmpty()
          .hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("password must not be null or blank");
    }

    @Test
    @DisplayName("validate password must returns violations when password is empty")
    void validatePassword_MustReturnsViolations_WhenPasswordIsEmpty() {
      LoginRequest loginRequest =
          new LoginRequest(VALID_EMAIL, "");

      Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

      Assertions.assertThat(violations).isNotEmpty().hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("password must not be null or blank");
    }

    @Test
    @DisplayName("validate password must returns violations when password is blank")
    void validatePassword_MustReturnsViolations_WhenPasswordIsBlank() {
      LoginRequest loginRequest =
          new LoginRequest(VALID_EMAIL, "   ");

      Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

      Assertions.assertThat(violations).isNotEmpty().hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("password must not be null or blank");
    }

  }

}
