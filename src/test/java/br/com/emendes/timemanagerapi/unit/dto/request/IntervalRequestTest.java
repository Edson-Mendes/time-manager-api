package br.com.emendes.timemanagerapi.unit.dto.request;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import lombok.extern.log4j.Log4j2;
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
  private final String VALID_STARTED_AT = "2022-09-26T10:30:00";
  private final String VALID_ELAPSED_TIME = "00:30:00";

  @Nested
  @DisplayName("tests for startedAt validation")
  class StartedAtValidation {

    @ParameterizedTest
    @ValueSource(strings = {"2022-09-26T11:32:40", "2022-10-02T00:00:00", "2022-10-01T23:59:59"})
    @DisplayName("validate startedAt must not return violations when startedAt is valid")
    void validateStartedAt_MustNotReturnViolations_WhenStartedAtIsValid(String intervalStartedAt){
      IntervalRequest intervalRequest =
          new IntervalRequest(intervalStartedAt, VALID_ELAPSED_TIME);

      Set<ConstraintViolation<IntervalRequest>> actualViolations = validator.validate(intervalRequest);

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("validate startedAt must return violations when startedAt is null")
    void validateStartedAt_MustReturnsViolations_WhenStartedAtIsNull(){
      IntervalRequest intervalRequest =
          new IntervalRequest(null, VALID_ELAPSED_TIME);

      Set<ConstraintViolation<IntervalRequest>> actualViolations = validator.validate(intervalRequest);

      Assertions.assertThat(actualViolations)
          .isNotEmpty()
          .hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("startedAt must not be null or blank");
    }

    @Test
    @DisplayName("validate startedAt must return violations when startedAt is empty")
    void validateStartedAt_MustReturnsViolations_WhenStartedAtIsEmpty(){
      IntervalRequest intervalRequest =
          new IntervalRequest("", VALID_ELAPSED_TIME);

      Set<ConstraintViolation<IntervalRequest>> actualViolations = validator.validate(intervalRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("startedAt must not be null or blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"lorem", "2022-10-35T00:00:00", "2022-10-01T29:00:00", "2022-10-01 00:00:00"})
    @DisplayName("validate startedAt must return violations when startedAt is invalid")
    void validateStartedAt_MustReturnViolations_WhenStartedAtIsInvalidDateTime(String intervalStartedAt){
      IntervalRequest intervalRequest =
          new IntervalRequest(intervalStartedAt, VALID_ELAPSED_TIME);

      Set<ConstraintViolation<IntervalRequest>> actualViolations = validator.validate(intervalRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("Invalid Date Time");
    }

  }

  @Nested
  @DisplayName("tests for elapsedTime validation")
  class ElapsedTimeValidation {

    @ParameterizedTest
    @ValueSource(strings = {"00:35:00", "00:00:00", "23:59:59"})
    @DisplayName("validate elapsedTime must not return violations when name is valid")
    void validateElapsedTime_MustNotReturnsViolations_WhenElapsedTimeIsValid(String intervalElapsedTime){
      IntervalRequest intervalRequest = new IntervalRequest(VALID_STARTED_AT, intervalElapsedTime);

      Set<ConstraintViolation<IntervalRequest>> actualViolations = validator.validate(intervalRequest);

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("validate elapsedTime must return violations when elapsedTime is null")
    void validateElapsedTime_MustReturnsViolations_WhenElapsedTimeIsNull(){
      IntervalRequest intervalRequest = new IntervalRequest(VALID_STARTED_AT, null);

      Set<ConstraintViolation<IntervalRequest>> actualViolations = validator.validate(intervalRequest);

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("elapsedTime must not be null or blank");
    }

    @Test
    @DisplayName("validate elapsedTime must return violations when elapsedTime is empty")
    void validateElapsedTime_MustReturnsViolations_WhenElapsedTimeIsEmpty(){
      IntervalRequest intervalRequest = new IntervalRequest(VALID_STARTED_AT, "");

      Set<ConstraintViolation<IntervalRequest>> actualViolations = validator.validate(intervalRequest);

      Assertions.assertThat(actualViolations)
          .isNotEmpty()
          .hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("elapsedTime must not be null or blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"lorem", "0:30:00", "30:00", "00:60:00", "00:00:60", "25:00:00"})
    @DisplayName("validate elapsedTime must return violations when elapsedTime is invalid")
    void validateElapsedTime_MustReturnsViolations_WhenElapsedTimeIsInvalid(String intervalElapsedTime){
      IntervalRequest intervalRequest = new IntervalRequest(VALID_STARTED_AT, intervalElapsedTime);

      Set<ConstraintViolation<IntervalRequest>> actualViolations = validator.validate(intervalRequest);

      Assertions.assertThat(actualViolations)
          .isNotEmpty()
          .hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("invalid elapsed time");
    }

  }

}