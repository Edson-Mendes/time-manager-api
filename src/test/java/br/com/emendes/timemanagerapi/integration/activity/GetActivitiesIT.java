package br.com.emendes.timemanagerapi.integration.activity;

import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.dto.response.detail.ExceptionDetails;
import br.com.emendes.timemanagerapi.model.Status;
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
@DisplayName("Integration tests for GET /activities")
class GetActivitiesIT {

  @Autowired
  @Qualifier(value = "withPatch")
  private TestRestTemplate testRestTemplate;

  private final String ACTIVITIES_URI = "/activities";

  @Sql(value = {INSERT_ACTIVITIES_PATH})
  @Test
  @DisplayName("get /activities must returns status 200 and Page<ActivitiesResponse> when found successfully")
  void getActivities_MustReturnsStatus200AndPageActivitiesResponse_WhenFoundSuccessfully() {
    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());

    ResponseEntity<PageableResponse<ActivityResponse>> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();
    Page<ActivityResponse> actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().hasSize(4);

    List<String> actualListActivitiesName = actualBody.stream().map(ActivityResponse::getName).toList();

    Assertions.assertThat(actualListActivitiesName)
        .contains("Awesome activity 1", "Awesome activity 2", "Another awesome activity 1", "Another awesome activity 2");
  }

  @Sql(value = {INSERT_DISABLED_ACTIVITIES_PATH})
  @Test
  @DisplayName("get /activities must returns only enabled activities when exists activities disabled")
  void getActivities_MustReturnsOnlyEnabledActivities_WhenExistsActivitiesDisabled() {
    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());

    ResponseEntity<PageableResponse<ActivityResponse>> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });
    Page<ActivityResponse> actualBody = responseEntity.getBody();
    assert actualBody != null;
    ActivityResponse actualActivityRespBody = actualBody.getContent().get(0);

    Assertions.assertThat(actualBody).isNotNull().hasSize(1);
    Assertions.assertThat(actualActivityRespBody.getName()).isEqualTo("Awesome activity 1");
    Assertions.assertThat(actualActivityRespBody.getDescription()).isEqualTo("Some awesome activity 1");
    Assertions.assertThat(actualActivityRespBody.getStatus()).isEqualByComparingTo(Status.ACTIVE);
  }

  @Sql(value = {INSERT_USER_PATH})
  @Test
  @DisplayName("get /activities must returns status 200 and empty Page when user doesn't have activities")
  void getActivities_MustReturnsStatus200AndEmptyPage_WhenUserDoesntHaveActivities() {
    HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthToken());
    ResponseEntity<PageableResponse<ActivityResponse>> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();
    PageableResponse<ActivityResponse> actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("get /activities must returns status 401 when authorization header is invalid")
  void getActivities_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
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
