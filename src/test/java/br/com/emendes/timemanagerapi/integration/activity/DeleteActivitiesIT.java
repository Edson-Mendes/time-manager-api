package br.com.emendes.timemanagerapi.integration.activity;

import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.dto.response.detail.ExceptionDetails;
import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
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
@DisplayName("Integration tests for DELETE /activities")
class DeleteActivitiesIT {

  @Autowired
  @Qualifier(value = "withPatch")
  private TestRestTemplate testRestTemplate;
  @Autowired
  private ActivityRepository activityRepository;

  private final String ACTIVITIES_URI = "/activities/%s";

  @Sql(value = {INSERT_ACTIVITIES_PATH})
  @Test
  @DisplayName("delete /activities/{id} must returns status 204 when deleted successfully")
  void deleteActivitiesId_MustReturnsStatus204_WhenDeletedSuccessfully() {
    String uri = String.format(ACTIVITIES_URI, 1);

    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());
    ResponseEntity<Void> response = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    Assertions.assertThat(response.getBody()).isNull();
  }

  @Sql(value = {INSERT_ACTIVITIES_PATH})
  @Test
  @DisplayName("delete /activities/{id} must disable activity when deleted successfully")
  void deleteActivitiesId_MustDisableActivity_WhenDeletedSuccessfully() {
    String uri = String.format(ACTIVITIES_URI, 1);

    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());
//    Deletando
    testRestTemplate.exchange(
        uri, HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<>() {});
//    Buscando activity desativada.
    Activity disabledActivity = activityRepository.findById(1L).orElseThrow();
    Status actualStatus = disabledActivity.getStatus();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(Status.DELETED);
  }

  @Sql(value = {INSERT_ACTIVITIES_PATH})
  @Test
  @DisplayName("delete /activities/{id} must returns status 400 and ExceptionDetails when activity don't exist")
  void deleteActivitiesId_MustReturnsStatus400AndExceptionDetails_WhenActivityDontExists() {
    String uri = String.format(ACTIVITIES_URI, 999);

    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());
    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();
    ExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: " + 999);
  }


  @Test
  @DisplayName("delete /activities/{id} must returns status 401 when authorization header is invalid")
  void deleteActivitiesId_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    String uri = String.format(ACTIVITIES_URI, 999);

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

  @Sql(value = {INSERT_USER_PATH, INSERT_ACTIVITIES_TO_ANOTHER_USER_PATH})
  @Test
  @DisplayName("delete /activities/{id} must returns status 400 and ExceptionDetails when activity belongs to another user")
  void deleteActivitiesId_MustReturnsStatus400AndExceptionDetails_WhenActivityBelongsToAnotherUser(){
    String uri = String.format(ACTIVITIES_URI, 1);

    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());
    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();
    ExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: " + 1);
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
