package br.com.emendes.timemanagerapi.integration.activity;

import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.request.UpdateStatusRequest;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.dto.response.detail.ExceptionDetails;
import br.com.emendes.timemanagerapi.dto.response.detail.ValidationExceptionDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Objects;

import static br.com.emendes.timemanagerapi.util.constant.SQLPath.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for PATCH /activities")
class PatchActivitiesIT {

  @Autowired
  @Qualifier(value = "withPatch")
  private TestRestTemplate testRestTemplate;

  private final String ACTIVITIES_URI = "/activities/%s";

  @Sql(value = {INSERT_ACTIVITIES_PATH})
  @Test
  @DisplayName("patch /activities/{id} must returns status 204 when update status successfully")
  void patchActivitiesId_MustReturnsStatus204_WhenUpdateStatusSuccessfully() {
    String uri = String.format(ACTIVITIES_URI, 1);
    UpdateStatusRequest requestBody = new UpdateStatusRequest("Concluded");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(requestBody, getAuthToken());

    ResponseEntity<Void> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    Assertions.assertThat(response.getBody()).isNull();
  }

  @Sql(value = {INSERT_ACTIVITIES_PATH})
  @Test
  @DisplayName("patch /activities/{id} must returns status 400 and ValidationExceptionDetails when status is invalid")
  void patchActivitiesId_MustReturnsStatus400AndValidationExceptionDetails_WhenStatusIsInvalid() {
    String uri = String.format(ACTIVITIES_URI, 1);
    UpdateStatusRequest requestBody = new UpdateStatusRequest("cconclude");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(requestBody, getAuthToken());

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();
    ValidationExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getFields()).contains("status");
    Assertions.assertThat(actualBody.getMessages()).contains("invalid status");
  }

  @Sql(value = {INSERT_USER_PATH})
  @Test
  @DisplayName("patch /activities/{id} must returns status 400 and ExceptionDetails when activity doesn't exist")
  void patchActivitiesId_MustReturnsStatus400AndExceptionDetails_WhenActivityDoesntExist() {
    String uri = String.format(ACTIVITIES_URI, 999);

    UpdateStatusRequest requestBody = new UpdateStatusRequest("concluded");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(requestBody, getAuthToken());

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();
    ExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: " + 999);
  }

  @Sql(value = {INSERT_DISABLED_ACTIVITIES_PATH})
  @Test
  @DisplayName("patch /activities/{id} must returns status 400 and ExceptionDetails when activity status is deleted")
  void patchActivitiesId_MustReturnsStatus400AndExceptionDetails_WhenActivityStatusIsDeleted() {
    String uri = String.format(ACTIVITIES_URI, 4);

    UpdateStatusRequest requestBody = new UpdateStatusRequest("concluded");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(requestBody, getAuthToken());

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();
    ExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: " + 4);
  }

  @Sql(value = {INSERT_USER_PATH, INSERT_ACTIVITIES_TO_ANOTHER_USER_PATH})
  @Test
  @DisplayName("patch /activities/{id} must returns status 400 and ExceptionDetails when activity belongs to another user")
  void patchActivitiesId_MustReturnsStatus400AndExceptionDetails_WhenActivityBelongsToAnotherUser(){
    String uri = String.format(ACTIVITIES_URI, 1);

    UpdateStatusRequest requestBody = new UpdateStatusRequest("Concluded");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(requestBody, getAuthToken());

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("patch /activities/{id} must returns status 401 when authorization header is invalid")
  void patchActivitiesId_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    String uri = String.format(ACTIVITIES_URI, 1);

    UpdateStatusRequest requestBody = new UpdateStatusRequest("concluded");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(requestBody);

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

  private HttpHeaders getAuthToken() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + attemptSignIn());
    return headers;
  }

  private String attemptSignIn() {
    HttpEntity<LoginRequest> requestBody = new HttpEntity<>(new LoginRequest("lorem@email.com", "1234"));

    ResponseEntity<TokenResponse> response = testRestTemplate.exchange(
        "/signin", HttpMethod.POST, requestBody, new ParameterizedTypeReference<>() {
        });
    return Objects.requireNonNull(response.getBody()).getToken();
  }

}
