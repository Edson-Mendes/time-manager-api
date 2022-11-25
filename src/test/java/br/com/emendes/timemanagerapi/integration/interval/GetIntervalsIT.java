package br.com.emendes.timemanagerapi.integration.interval;

import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponse;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.dto.response.detail.ExceptionDetails;
import br.com.emendes.timemanagerapi.util.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static br.com.emendes.timemanagerapi.util.constant.SQLPath.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for GET /activities/{id}/intervals")
class GetIntervalsIT {

  @Autowired
  private TestRestTemplate testRestTemplate;

  private final String INTERVALS_URI = "/activities/%s/intervals";

  @Sql(value = {INSERT_INTERVALS_PATH})
  @Test
  @DisplayName("get for /activities/{id}/intervals must returns status 200 and Page<IntervalResponse> when found successfully")
  void getForIntervals_MustReturnsStatus200AndPageIntervalResponse_WhenFoundSuccessfully() {
    final String uri = String.format(INTERVALS_URI, 1);

    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());
    ResponseEntity<PageableResponse<IntervalResponse>> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    Page<IntervalResponse> actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().hasSize(2);

    List<LocalDateTime> actualListStartedAt = actualBody.get().map(IntervalResponse::getStartedAt).toList();
    List<LocalTime> actualListElapsedTime = actualBody.get().map(IntervalResponse::getElapsedTime).toList();

    Assertions.assertThat(actualListStartedAt)
        .contains(LocalDateTime.parse("2022-10-23T10:00:00"), LocalDateTime.parse("2022-10-24T10:00:00"));
    Assertions.assertThat(actualListElapsedTime)
        .contains(LocalTime.parse("00:30:00"), LocalTime.parse("00:45:00"));
  }

  @Sql(value = {INSERT_INTERVALS_WITH_CONCLUDED_ACTIVITY_PATH})
  @Test
  @DisplayName("get for /activities/{id}/intervals must returns status 200 and Page<IntervalResponse> when Activity status is concluded")
  void getForIntervals_MustReturnsStatus200AndIntervalResponse_WhenActivityStatusIsConcluded() {
    final String uri = String.format(INTERVALS_URI, 1);

    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());
    ResponseEntity<PageableResponse<IntervalResponse>> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    Page<IntervalResponse> actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().hasSize(1);
    Assertions.assertThat(actualBody.getContent().get(0).getStartedAt()).isEqualTo("2022-10-23T10:00:00");
    Assertions.assertThat(actualBody.getContent().get(0).getElapsedTime()).isEqualTo("00:30:00");
  }

  @Sql(value = {INSERT_USER_PATH})
  @Test
  @DisplayName("get for /activities/{id}/intervals must returns status 400 and ExceptionDetails when Activity doesn't exist")
  void getForIntervals_MustReturnsStatus400AndExceptionDetails_WhenActivityDoesntExist() {
    final String uri = String.format(INTERVALS_URI, 100);

    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: 100");
  }

  @Sql(value = {INSERT_INTERVALS_WITH_DELETED_ACTIVITY_PATH})
  @Test
  @DisplayName("get for /activities/{id}/intervals must returns status 400 and ExceptionDetails when activity status is deleted")
  void getForIntervals_MustReturnsStatus400AndExceptionDetails_WhenActivityStatusIsDeleted() {
    final String uri = String.format(INTERVALS_URI, 1);

    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: 1");
  }

  @Test
  @DisplayName("get for /activities/{id}/intervals must returns status 401 when authorization header is invalid")
  void getForIntervals_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    final String uri = String.format(INTERVALS_URI, 1);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
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
