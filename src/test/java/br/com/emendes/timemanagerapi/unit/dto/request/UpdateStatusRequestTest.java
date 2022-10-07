package br.com.emendes.timemanagerapi.unit.dto.request;

import br.com.emendes.timemanagerapi.dto.request.UpdateStatusRequest;
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

@DisplayName("Unit tests for UpdateStatusRequest")
class UpdateStatusRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Nested
  @DisplayName("tests for status validation")
  class StatusValidation {

    @ParameterizedTest
    @ValueSource(strings = {"ACTIVE", "concluded", "Active", "CoNcLuDeD"})
    @DisplayName("validate status must not return violations when status is valid")
    void validateStatus_MustNotReturnViolations_WhenStatusIsValid(String status){
      UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest(status);

      Set<ConstraintViolation<UpdateStatusRequest>> actualViolations = validator.validate(updateStatusRequest);

      Assertions.assertThat(actualViolations).isNotNull().isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"AACTIVE", "concludedd", "xalala", "conclude"})
    @DisplayName("validate status must return violations when status is invalid")
    void validateStatus_MustReturnViolations_WhenStatusIsNotConvertible(String status){
      UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest(status);

      Set<ConstraintViolation<UpdateStatusRequest>> actualViolations = validator.validate(updateStatusRequest);

      Assertions.assertThat(actualViolations).isNotNull().isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("invalid status");
    }

    @Test
    @DisplayName("validate status must return violations when status is deleted")
    void validateStatus_MustReturnViolations_WhenStatusIsDeleted(){
      UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("deleted");

      Set<ConstraintViolation<UpdateStatusRequest>> actualViolations = validator.validate(updateStatusRequest);

      Assertions.assertThat(actualViolations).isNotNull().isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("invalid status");
    }

    @Test
    @DisplayName("validate status must return violations when status is null")
    void validataStatus_MustReturnViolations_WhenStatusIsNull(){
      UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest(null);

      Set<ConstraintViolation<UpdateStatusRequest>> actualViolations = validator.validate(updateStatusRequest);

      Assertions.assertThat(actualViolations).isNotNull().isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("status must not be null or blank");
    }

    @Test
    @DisplayName("validate status must return violations when status is blank")
    void validataStatus_MustReturnViolations_WhenStatusIsBlank(){
      UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("   ");

      Set<ConstraintViolation<UpdateStatusRequest>> actualViolations = validator.validate(updateStatusRequest);

      Assertions.assertThat(actualViolations).isNotNull().isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("status must not be null or blank");
    }

    @Test
    @DisplayName("validate status must return violations when status is empty")
    void validataStatus_MustReturnViolations_WhenStatusIsEmpty(){
      UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("");

      Set<ConstraintViolation<UpdateStatusRequest>> actualViolations = validator.validate(updateStatusRequest);

      Assertions.assertThat(actualViolations).isNotNull().isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("status must not be null or blank");
    }

  }

}