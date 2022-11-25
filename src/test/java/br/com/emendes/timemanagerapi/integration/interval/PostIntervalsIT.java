package br.com.emendes.timemanagerapi.integration.interval;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;
import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponse;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.dto.response.detail.ExceptionDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
@DisplayName("Integration tests for GET /activities/{id}/intervals")
class PostIntervalsIT {

  @Autowired
  private TestRestTemplate testRestTemplate;

  private final String INTERVALS_URI = "/activities/%s/intervals";

  @Sql(value = {INSERT_INTERVALS_PATH})
  @Test
  @DisplayName("post for /activities/{id}/intervals must returns status 201 and IntervalResponse when saved successfully")
  void postForIntervals_MustReturnsStatus201AndIntervalResponse_WhenSavedSuccessfully() {
    final String uri = String.format(INTERVALS_URI, 1);
    IntervalRequest requestBody = new IntervalRequest(
        "2022-10-25T10:00:00", "00:30:00");
    HttpEntity<IntervalRequest> requestEntity = new HttpEntity<>(requestBody, getAuthToken());

    ResponseEntity<IntervalResponse> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    IntervalResponse actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.CREATED);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getId()).isEqualTo(3L);
    Assertions.assertThat(actualBody.getStartedAt()).isEqualTo("2022-10-25T10:00:00");
    Assertions.assertThat(actualBody.getElapsedTime()).isEqualTo("00:30:00");
  }

  @Sql(value = {INSERT_USER_PATH})
  @Test
  @DisplayName("post for /activities/{id}/intervals must returns status 400 and ExceptionDetails when Activity doesn't exist")
  void postForIntervals_MustReturnsStatus400AndExceptionDetails_WhenActivityDoesntExist() {
    final String uri = String.format(INTERVALS_URI, 100);
    IntervalRequest requestBody = new IntervalRequest(
        "2022-10-25T14:26:00", "00:30:00");
    HttpEntity<IntervalRequest> requestEntity = new HttpEntity<>(requestBody, getAuthToken());

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: 100");
  }

  @Sql(value = {INSERT_INTERVALS_WITH_CONCLUDED_ACTIVITY_PATH})
  @Test
  @DisplayName("post for /activities/{id}/intervals must returns status 400 and ExceptionDetails when Activity status is concluded")
  void postForIntervals_MustReturnsStatus400AndExceptionDetails_WhenActivityStatusIsConcluded() {
    final String uri = String.format(INTERVALS_URI, 1);
    IntervalRequest requestBody = new IntervalRequest(
        "2022-10-25T14:26:00", "00:30:00");
    HttpEntity<IntervalRequest> requestEntity = new HttpEntity<>(requestBody, getAuthToken());

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Cannot create interval on non active activity");
  }

  @Sql(value = {INSERT_INTERVALS_WITH_DELETED_ACTIVITY_PATH})
  @Test
  @DisplayName("post for /activities/{id}/intervals must returns status 400 and ExceptionDetails when Activity status is deleted")
  void postForIntervals_MustReturnsStatus400AndExceptionDetails_WhenActivityStatusIsDeleted() {
    final String uri = String.format(INTERVALS_URI, 1);

    IntervalRequest requestBody = new IntervalRequest(
        "2022-10-25T14:26:00", "00:30:00");
    HttpEntity<IntervalRequest> requestEntity = new HttpEntity<>(requestBody, getAuthToken());

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {});
    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Cannot create interval on non active activity");
  }

  @Test
  @DisplayName("post for /activities/{id}/intervals must returns status 401 when authorization header is invalid")
  void postForIntervals_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    final String uri = String.format(INTERVALS_URI, 1);
    IntervalRequest requestBody = new IntervalRequest(
        "2022-10-25T14:26:00", "00:30:00");
    HttpEntity<IntervalRequest> requestEntity = new HttpEntity<>(requestBody);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();

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
