package br.com.emendes.timemanagerapi.integration.activity;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequest;
import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.dto.response.detail.ExceptionDetails;
import br.com.emendes.timemanagerapi.dto.response.detail.ValidationExceptionDetails;
import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.entity.User;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
import br.com.emendes.timemanagerapi.util.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Objects;

import static br.com.emendes.timemanagerapi.util.constant.SQLPath.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for PUT /activities")
class PutActivitiesIT {

  @Autowired
  @Qualifier(value = "withPatch")
  private TestRestTemplate testRestTemplate;

  private final String ACTIVITIES_URI = "/activities/%s";

  @Sql(value = {INSERT_ACTIVITIES_PATH})
  @Test
  @DisplayName("put /activities/{id} must returns status 204 when updated successfully")
  void putActivitiesId_MustReturnsStatus204_WhenUpdatedSuccessfully() {
    String uri = String.format(ACTIVITIES_URI, 1);
    ActivityRequest requestBody =
        new ActivityRequest("Awesome activity 1 updated", "Some awesome activity 1 updated");

    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(requestBody, getAuthToken());

    ResponseEntity<Void> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    Assertions.assertThat(response.getBody()).isNull();
  }

  @Sql(value = {INSERT_USER_PATH})
  @Test
  @DisplayName("put /activities/{id} must returns status 400 and ExceptionDetails when activity doesn't exist")
  void putActivitiesId_MustReturnsStatus400AndExceptionDetails_WhenActivityDoesntExist() {
    String uri = String.format(ACTIVITIES_URI, 999L);
    ActivityRequest requestBody = new ActivityRequest("Super awesome activity", "Some awesome activity");

    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(requestBody, getAuthToken());

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();
    ExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: " + 999L);
  }

  @Sql(value = {INSERT_USER_PATH})
  @Test
  @DisplayName("put /activities/{id} must returns status 400 and ValidationExceptionDetails when request body is invalid")
  void putActivitiesId_MustReturnsStatus400AndValidationExceptionDetails_WhenRequestBodyIsInvalid() {
    String uri = String.format(ACTIVITIES_URI, 999L);
    ActivityRequest requestBody = new ActivityRequest("", null);

    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(requestBody, getAuthToken());

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();
    ValidationExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getFields()).contains("name").contains("description");
    Assertions.assertThat(actualBody.getMessages())
        .contains("name must not be null or blank").contains("description must not be null or blank");
  }

  @Sql(value = {INSERT_DISABLED_ACTIVITIES_PATH})
  @Test
  @DisplayName("put /activities/{id} must returns status 400 and ExceptionDetails when activity status is deleted")
  void putActivitiesId_MustReturnsStatus400AndExceptionDetails_WhenActivityStatusIsDeleted() {
    String uri = String.format(ACTIVITIES_URI, 3);
    ActivityRequest requestBody = new ActivityRequest("Super awesome activity", "Some awesome activity");
    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(requestBody, getAuthToken());

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();
    ValidationExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: " + 3);
  }

  @Sql(value = {INSERT_USER_PATH, INSERT_ACTIVITIES_TO_ANOTHER_USER_PATH})
  @Test
  @DisplayName("put /activities/{id} must returns status 400 and ExceptionDetails when activity belongs to another user")
  void putActivitiesId_MustReturnsStatus400AndExceptionDetails_WhenActivityBelongsToAnotherUser(){
    String uri = String.format(ACTIVITIES_URI, 1);
    ActivityRequest requestBody = new ActivityRequest("Super awesome activity", "Some awesome activity");
    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(requestBody, getAuthToken());

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();
    ExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: " + 1);
  }

  @Test
  @DisplayName("put /activities/{id} must returns status 401 when authorization header is invalid")
  void putActivitiesId_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    String uri = String.format(ACTIVITIES_URI, 1);
    ActivityRequest requestBody = new ActivityRequest("Super awesome activity", "Some awesome activity");
    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(requestBody);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

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
