package br.com.emendes.timemanagerapi.unit.dto.request;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;
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

@DisplayName("Unit tests for IntervalRequest")
class IntervalRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
  private final String VALID_STARTED_AT = "A simple project for my portfolio";
  private final String VALID_ELAPSED_TIME = "Finances API";

  @Nested
  @DisplayName("tests for name validation")
  class NameValidation {

    @ParameterizedTest
    @ValueSource(strings = {"2022-09-26T11:32:40", "2022-10-02T00:00:00", "2022-10-01T23:59:59"})
    @DisplayName("validate startedAt must not returns violations when startedAt is valid")
    void validateStartedAt_MustNotReturnViolations_WhenStartedAtIsValid(String intervalStartedAt){
      IntervalRequest intervalRequest =
          new IntervalRequest(intervalStartedAt, VALID_ELAPSED_TIME);

      Set<ConstraintViolation<IntervalRequest>> violations = validator.validate(intervalRequest);

      Assertions.assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("validate startedAt must returns violations when startedAt is null")
    void validateStartedAt_MustReturnsViolations_WhenStartedAtIsNull(){
      IntervalRequest intervalRequest =
          new IntervalRequest(null, VALID_STARTED_AT);

      Set<ConstraintViolation<IntervalRequest>> violations = validator.validate(intervalRequest);

      Assertions.assertThat(violations)
          .isNotEmpty()
          .hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("startedAt must not be null or blank");
    }

    @Test
    @DisplayName("validate startedAt must returns violations when startedAt is empty")
    void validateStartedAt_MustReturnsViolations_WhenStartedAtIsEmpty(){
      IntervalRequest intervalRequest =
          new IntervalRequest("", VALID_STARTED_AT);

      Set<ConstraintViolation<IntervalRequest>> violations = validator.validate(intervalRequest);

      Assertions.assertThat(violations).isNotEmpty().hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("startedAt must not be null or blank");
    }

  }

  @Nested
  @DisplayName("tests for elapsedTime validation")
  class ElapsedTimeValidation {

    @ParameterizedTest
    @ValueSource(strings = {"A simple project for my portfolio", "elapsedTime", "d"})
    @DisplayName("validate elapsedTime must not returns violations when name is valid")
    void validateElapsedTime_MustNotReturnsViolations_WhenElapsedTimeIsValid(String elapsedTime){
      IntervalRequest intervalRequest =
          new IntervalRequest(VALID_ELAPSED_TIME, elapsedTime);

      Set<ConstraintViolation<IntervalRequest>> violations = validator.validate(intervalRequest);

      Assertions.assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("validate elapsedTime must returns violations when elapsedTime is null")
    void validateElapsedTime_MustReturnsViolations_WhenElapsedTimeIsNull(){
      IntervalRequest intervalRequest =
          new IntervalRequest(VALID_ELAPSED_TIME, null);

      Set<ConstraintViolation<IntervalRequest>> violations = validator.validate(intervalRequest);

      Assertions.assertThat(violations).isNotEmpty().hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("elapsedTime must not be null or blank");
    }

    @Test
    @DisplayName("validate elapsedTime must returns violations when elapsedTime is empty")
    void validateElapsedTime_MustReturnsViolations_WhenElapsedTimeIsEmpty(){
      IntervalRequest intervalRequest =
          new IntervalRequest(VALID_ELAPSED_TIME, "");

      Set<ConstraintViolation<IntervalRequest>> violations = validator.validate(intervalRequest);

      Assertions.assertThat(violations)
          .isNotEmpty()
          .hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("elapsedTime must not be null or blank");
    }

  }
}