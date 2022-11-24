package br.com.emendes.timemanagerapi.integration.activity;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequest;
import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.dto.response.detail.ValidationExceptionDetails;
import br.com.emendes.timemanagerapi.model.Status;
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

import static br.com.emendes.timemanagerapi.util.constant.SQLPath.INSERT_USER_PATH;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for POST /activities")
class PostActivitiesIT {

  @Autowired
  @Qualifier(value = "withPatch")
  private TestRestTemplate testRestTemplate;

  private final String ACTIVITIES_URI = "/activities";

  @Sql(value = {INSERT_USER_PATH})
  @Test
  @DisplayName("post /activities must returns status 201 and ActivityResponse when saved successfully")
  void postActivities_MustReturnsStatus201AndActivityResponse_WhenSavedSuccessfully() {
    ActivityRequest body = new ActivityRequest("Awesome activity", "Some awesome activity");
    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(body, getAuthToken());
    ResponseEntity<ActivityResponse> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ActivityResponse actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.CREATED);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getId()).isEqualTo(1L);
    Assertions.assertThat(actualBody.getName()).isEqualTo("Awesome activity");
    Assertions.assertThat(actualBody.getDescription()).isEqualTo("Some awesome activity");
    Assertions.assertThat(actualBody.getStatus()).isEqualByComparingTo(Status.ACTIVE);
  }

  @Sql(value = {INSERT_USER_PATH})
  @Test
  @DisplayName("post /activities must returns status 400 and ValidationExceptionDetails when request body is invalid")
  void postActivities_MustReturnsStatus400AndValidationExceptionDetails_WhenRequestBodyIsInvalid() {
    ActivityRequest body = new ActivityRequest("", null);
    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(body, getAuthToken());
    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ValidationExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getFields())
        .contains("name", "description");
    Assertions.assertThat(actualBody.getMessages())
        .contains("name must not be null or blank", "description must not be null or blank");
  }


  @Test
  @DisplayName("post /activities must returns status 401 when authorization header is invalid")
  void postActivities_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    ActivityRequest body = new ActivityRequest("", null);
    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(body);
    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
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
