package br.com.emendes.timemanagerapi.unit.dto.request;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@DisplayName("Unit tests for ActivityRequestBody")
class ActivityRequestBodyTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
  private final String DESCRIPTION = "A simple project for my portfolio";
  private final String NAME = "Finances API";

  @Test
  @DisplayName("name must not returns violations when name is valid")
  void name_MustNotReturnsViolations_WhenNameIsValid(){
    String nameActivity = "Finances API";
    ActivityRequestBody activityRequestBody =
        new ActivityRequestBody(nameActivity, DESCRIPTION);

    Set<ConstraintViolation<ActivityRequestBody>> violations = validator.validate(activityRequestBody);

    Assertions.assertThat(violations).isEmpty();
  }

  @Test
  @DisplayName("name must returns violations when name is null")
  void name_MustReturnsViolations_WhenNameIsNull(){
    String nameActivity = null;
    ActivityRequestBody activityRequestBody =
        new ActivityRequestBody(nameActivity, DESCRIPTION);

    Set<ConstraintViolation<ActivityRequestBody>> violations = validator.validate(activityRequestBody);

    Assertions.assertThat(violations)
        .isNotEmpty()
        .hasSize(1);
    Assertions.assertThat(violations.stream().findFirst().get().getMessage())
        .isEqualTo("name must not be null or blank");
  }

  @Test
  @DisplayName("name must returns violations when name is empty")
  void name_MustReturnsViolations_WhenNameIsEmpty(){
    String nameActivity = "";
    ActivityRequestBody activityRequestBody =
        new ActivityRequestBody(nameActivity, DESCRIPTION);

    Set<ConstraintViolation<ActivityRequestBody>> violations = validator.validate(activityRequestBody);

    Assertions.assertThat(violations)
        .isNotEmpty()
        .hasSize(1);
    Assertions.assertThat(violations.stream().findFirst().get().getMessage())
        .isEqualTo("name must not be null or blank");
  }
  @Test
  @DisplayName("description must not returns violations when name is valid")
  void description_MustNotReturnsViolations_WhenDescriptionIsValid(){
    String description = "A simple project for my portfolio";
    ActivityRequestBody activityRequestBody =
        new ActivityRequestBody(NAME, description);

    Set<ConstraintViolation<ActivityRequestBody>> violations = validator.validate(activityRequestBody);

    Assertions.assertThat(violations).isEmpty();
  }

  @Test
  @DisplayName("description must returns violations when description is null")
  void description_MustReturnsViolations_WhenDescriptionIsNull(){
    String description = null;
    ActivityRequestBody activityRequestBody =
        new ActivityRequestBody(NAME, description);

    Set<ConstraintViolation<ActivityRequestBody>> violations = validator.validate(activityRequestBody);

    Assertions.assertThat(violations)
        .isNotEmpty()
        .hasSize(1);
    Assertions.assertThat(violations.stream().findFirst().get().getMessage())
        .isEqualTo("description must not be null or blank");
  }

  @Test
  @DisplayName("description must returns violations when description is empty")
  void description_MustReturnsViolations_WhenDescriptionIsEmpty(){
    String description = "";
    ActivityRequestBody activityRequestBody =
        new ActivityRequestBody(NAME, description);

    Set<ConstraintViolation<ActivityRequestBody>> violations = validator.validate(activityRequestBody);

    Assertions.assertThat(violations)
        .isNotEmpty()
        .hasSize(1);
    Assertions.assertThat(violations.stream().findFirst().get().getMessage())
        .isEqualTo("description must not be null or blank");
  }

}