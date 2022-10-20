package br.com.emendes.timemanagerapi.unit.dto.request;

import br.com.emendes.timemanagerapi.dto.request.SignupRequest;
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

@DisplayName("Unit tests for SignupRequest")
public class SignupRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private final String VALID_NAME = "User";
  private final String VALID_EMAIL = "user@email.com";
  private final String VALID_PASSWORD = "123456";
  private final String VALID_CONFIRM = "123456";

  @Nested
  @DisplayName("tests for name validation")
  class NameValidation {

    @Test
    @DisplayName("validate name must returns violations when name is null")
    void validateName_MustReturnViolations_WhenNameIsNull() {
      SignupRequest signupRequest = new SignupRequest(null, VALID_EMAIL, VALID_PASSWORD, VALID_CONFIRM);

      Set<ConstraintViolation<SignupRequest>> actualViolations = validator.validate(signupRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("name must not be null or blank");
    }

    @Test
    @DisplayName("validate name must returns violations when name is empty")
    void validateName_MustReturnsViolations_WhenNameIsEmpty() {
      SignupRequest signupRequest = new SignupRequest("", VALID_EMAIL, VALID_PASSWORD, VALID_CONFIRM);

      Set<ConstraintViolation<SignupRequest>> actualViolations = validator.validate(signupRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("name must not be null or blank");
    }

    @Test
    @DisplayName("validate name must returns violations when name is blank")
    void validateName_MustReturnsViolations_WhenNameIsBlank() {
      SignupRequest signupRequest = new SignupRequest("   ", VALID_EMAIL, VALID_PASSWORD, VALID_CONFIRM);

      Set<ConstraintViolation<SignupRequest>> actualViolations = validator.validate(signupRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("name must not be null or blank");
    }

  }

  @Nested
  @DisplayName("tests for email validation")
  class EmailValidation {

    @ParameterizedTest
    @ValueSource(strings = {"user@gmail.com", "user@email.com.br", "user@outlook.com"})
    @DisplayName("validate email must not return violations when email is valid")
    void validateEmail_MustNotReturnViolations_WhenEmailIsValid(String email) {
      SignupRequest signupRequest = new SignupRequest(VALID_NAME, email, VALID_PASSWORD, VALID_CONFIRM);

      Set<ConstraintViolation<SignupRequest>> actualViolations = validator.validate(signupRequest);

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"usergmail.com", "@emailcombr", "user@"})
    @DisplayName("validate email must not return violations when email is valid")
    void validateEmail_MustReturnViolations_WhenEmailIsInvalid(String email) {
      SignupRequest signupRequest = new SignupRequest(VALID_NAME, email, VALID_PASSWORD, VALID_CONFIRM);

      Set<ConstraintViolation<SignupRequest>> actualViolations = validator.validate(signupRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("must be a well-formed email address");
    }

    @Test
    @DisplayName("validate email must returns violations when email is null")
    void validateEmail_MustReturnViolations_WhenEmailIsNull() {
      SignupRequest signupRequest = new SignupRequest(VALID_NAME, null, VALID_PASSWORD, VALID_CONFIRM);

      Set<ConstraintViolation<SignupRequest>> actualViolations = validator.validate(signupRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("email must not be null or blank");
    }

    @Test
    @DisplayName("validate email must returns violations when email is empty")
    void validateEmail_MustReturnsViolations_WhenEmailIsEmpty() {
      SignupRequest signupRequest = new SignupRequest(VALID_NAME, "", VALID_PASSWORD, VALID_CONFIRM);

      Set<ConstraintViolation<SignupRequest>> actualViolations = validator.validate(signupRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("email must not be null or blank");
    }

    @Test
    @DisplayName("validate email must returns violations when email is blank")
    void validateEmail_MustReturnsViolations_WhenEmailIsBlank() {
      SignupRequest signupRequest = new SignupRequest(VALID_NAME, "   ", VALID_PASSWORD, VALID_CONFIRM);

      Set<ConstraintViolation<SignupRequest>> actualViolations = validator.validate(signupRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(2);
    }
  }

  @Nested
  @DisplayName("tests for password validation")
  class PasswordValidation {

    @Test
    @DisplayName("validate password must returns violations when password is null")
    void validatePassword_MustReturnViolations_WhenPasswordIsNull() {
      SignupRequest signupRequest = new SignupRequest(VALID_NAME, VALID_EMAIL, null, VALID_CONFIRM);

      Set<ConstraintViolation<SignupRequest>> actualViolations = validator.validate(signupRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(2);
    }

    @Test
    @DisplayName("validate password must returns violations when password is empty")
    void validatePassword_MustReturnsViolations_WhenPasswordIsEmpty() {
      SignupRequest signupRequest = new SignupRequest(VALID_NAME, VALID_EMAIL, "", VALID_CONFIRM);

      Set<ConstraintViolation<SignupRequest>> actualViolations = validator.validate(signupRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(2);
    }

    @Test
    @DisplayName("validate password must returns violations when password is blank")
    void validatePassword_MustReturnsViolations_WhenPasswordIsBlank() {
      SignupRequest signupRequest = new SignupRequest(VALID_NAME, VALID_EMAIL, "   ", VALID_CONFIRM);

      Set<ConstraintViolation<SignupRequest>> actualViolations = validator.validate(signupRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(2);
    }

  }

  @Nested
  @DisplayName("tests for confirm validation")
  class ConfirmValidation {

    @Test
    @DisplayName("validate confirm must returns violations when confirm is null")
    void validateConfirm_MustReturnViolations_WhenConfirmIsNull() {
      SignupRequest signupRequest = new SignupRequest(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, null);

      Set<ConstraintViolation<SignupRequest>> actualViolations = validator.validate(signupRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(2);
    }

    @Test
    @DisplayName("validate confirm must returns violations when confirm is empty")
    void validateConfirm_MustReturnsViolations_WhenNameIsEmpty() {
      SignupRequest signupRequest = new SignupRequest(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, "");

      Set<ConstraintViolation<SignupRequest>> actualViolations = validator.validate(signupRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(2);
    }

    @Test
    @DisplayName("validate confirm must returns violations when confirm is blank")
    void validateConfirm_MustReturnsViolations_WhenConfirmIsBlank() {
      SignupRequest signupRequest = new SignupRequest(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, "   ");

      Set<ConstraintViolation<SignupRequest>> actualViolations = validator.validate(signupRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(2);
    }

  }

}
