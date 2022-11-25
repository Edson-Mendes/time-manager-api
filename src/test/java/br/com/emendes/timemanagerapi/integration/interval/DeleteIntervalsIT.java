package br.com.emendes.timemanagerapi.integration.interval;

import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
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

import static br.com.emendes.timemanagerapi.util.constant.SQLPath.INSERT_INTERVALS_PATH;
import static br.com.emendes.timemanagerapi.util.constant.SQLPath.INSERT_USER_PATH;

//TODO : extrair métodos repetidos
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for DELETE /activities/{aId}/intervals{iId}")
class DeleteIntervalsIT {

  @Autowired
  private TestRestTemplate testRestTemplate;

  private final String INTERVALS_URI = "/activities/%s/intervals/%s";

  @Sql(value = {INSERT_INTERVALS_PATH})
  @Test
  @DisplayName("delete for /activities/{aId}/intervals/{iId} must returns status 204 when deleted successfully")
  void deleteForIntervalsId_MustReturnsStatus204_WhenDeletedSuccessfully() {
    final String uri = String.format(INTERVALS_URI, 1, 1);

    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());
    ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
  }

  @Sql(value = {INSERT_USER_PATH})
  @Test
  @DisplayName("delete for /activities/{aId}/intervals/{iId} must returns 400 and ExceptionDetails when Activity doesn't exist")
  void deleteForIntervalsId_MustReturns400AndExceptionDetails_WhenActivityDoesntExist() {
    final String uri = String.format(INTERVALS_URI, 1, 1);

    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: 1");
  }

  @Sql(value = {INSERT_USER_PATH})
  @Test
  @DisplayName("delete for /activities/{aId}/intervals/{iId} must returns 400 and ExceptionDetails when iId is invalid")
  void deleteForIntervalsId_MustReturns400AndExceptionDetails_WhenIIDIsInvalid() {
    final String uri = String.format(INTERVALS_URI, 1, "1oo");

    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
  }

  @Sql(value = {INSERT_INTERVALS_PATH})
  @Test
  @DisplayName("delete for /activities/{aId}/intervals/{iId} must returns 400 and ExceptionDetails when Interval doesn't exist")
  void deleteForIntervalsId_MustReturns400AndExceptionDetails_WhenIntervalDoesntExist() {
    final String uri = String.format(INTERVALS_URI, 1, 10000);

    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Interval not found for id: 10000");
  }

  @Test
  @DisplayName("delete for /activities/{aId}/intervals/{iId} must returns status 401 when authorization header is invalid")
  void deleteForIntervalsId_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    final String uri = String.format(INTERVALS_URI, 1, 1);

    ResponseEntity<ExceptionDetails> responseEntity =
        testRestTemplate.exchange(uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {
        });

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
