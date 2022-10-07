package br.com.emendes.timemanagerapi.unit.dto.request;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
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

@DisplayName("Unit tests for ActivityRequestBody")
class ActivityRequestBodyTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
  private final String VALID_DESCRIPTION = "A simple project for my portfolio";
  private final String VALID_NAME = "Finances API";

  @Nested
  @DisplayName("tests for name validation")
  class NameValidation {

    @ParameterizedTest
    @ValueSource(strings = {"Finances API", "F", "Lorem Ipsum Project"})
    @DisplayName("validate name must not returns violations when name is valid")
    void validateName_MustNotReturnsViolations_WhenNameIsValid(String activityName){
      ActivityRequestBody activityRequestBody =
          new ActivityRequestBody(activityName, VALID_DESCRIPTION);

      Set<ConstraintViolation<ActivityRequestBody>> violations = validator.validate(activityRequestBody);

      Assertions.assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("validate name must returns violations when name is null")
    void validateName_MustReturnsViolations_WhenNameIsNull(){
      ActivityRequestBody activityRequestBody =
          new ActivityRequestBody(null, VALID_DESCRIPTION);

      Set<ConstraintViolation<ActivityRequestBody>> violations = validator.validate(activityRequestBody);

      Assertions.assertThat(violations)
          .isNotEmpty()
          .hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("name must not be null or blank");
    }

    @Test
    @DisplayName("validate name must returns violations when name is empty")
    void validateName_MustReturnsViolations_WhenNameIsEmpty(){
      ActivityRequestBody activityRequestBody =
          new ActivityRequestBody("", VALID_DESCRIPTION);

      Set<ConstraintViolation<ActivityRequestBody>> violations = validator.validate(activityRequestBody);

      Assertions.assertThat(violations).isNotEmpty().hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("name must not be null or blank");
    }

    @Test
    @DisplayName("validate name must returns violations when name is blank")
    void validateName_MustReturnsViolations_WhenNameIsBlank(){
      ActivityRequestBody activityRequestBody =
          new ActivityRequestBody("   ", VALID_DESCRIPTION);

      Set<ConstraintViolation<ActivityRequestBody>> violations = validator.validate(activityRequestBody);

      Assertions.assertThat(violations).isNotEmpty().hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("name must not be null or blank");
    }

  }

  @Nested
  @DisplayName("tests for description validation")
  class DescriptionValidation {

    @ParameterizedTest
    @ValueSource(strings = {"A simple project for my portfolio", "description", "d"})
    @DisplayName("validate description must not returns violations when name is valid")
    void validateDescription_MustNotReturnsViolations_WhenDescriptionIsValid(String description){
      ActivityRequestBody activityRequestBody =
          new ActivityRequestBody(VALID_NAME, description);

      Set<ConstraintViolation<ActivityRequestBody>> violations = validator.validate(activityRequestBody);

      Assertions.assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("validate description must returns violations when description is null")
    void validateDescription_MustReturnsViolations_WhenDescriptionIsNull(){
      ActivityRequestBody activityRequestBody =
          new ActivityRequestBody(VALID_NAME, null);

      Set<ConstraintViolation<ActivityRequestBody>> violations = validator.validate(activityRequestBody);

      Assertions.assertThat(violations).isNotEmpty().hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("description must not be null or blank");
    }

    @Test
    @DisplayName("validate description must returns violations when description is empty")
    void validateDescription_MustReturnsViolations_WhenDescriptionIsEmpty(){
      ActivityRequestBody activityRequestBody =
          new ActivityRequestBody(VALID_NAME, "");

      Set<ConstraintViolation<ActivityRequestBody>> violations = validator.validate(activityRequestBody);

      Assertions.assertThat(violations)
          .isNotEmpty()
          .hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("description must not be null or blank");
    }

    @Test
    @DisplayName("validate description must returns violations when description is blank")
    void validateDescription_MustReturnsViolations_WhenDescriptionIsBlank(){
      ActivityRequestBody activityRequestBody =
          new ActivityRequestBody(VALID_NAME, "   ");

      Set<ConstraintViolation<ActivityRequestBody>> violations = validator.validate(activityRequestBody);

      Assertions.assertThat(violations)
          .isNotEmpty()
          .hasSize(1);
      Assertions.assertThat(violations.stream().findFirst().get().getMessage())
          .isEqualTo("description must not be null or blank");
    }

  }

}