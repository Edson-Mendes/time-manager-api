package br.com.emendes.timemanagerapi.integration;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;
import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponse;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.dto.response.detail.ExceptionDetails;
import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.entity.Interval;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
import br.com.emendes.timemanagerapi.repository.IntervalRepository;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
import br.com.emendes.timemanagerapi.util.creator.IntervalCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

//  TODO: Não esquecer de refatorar esses testes!
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for /activities/{activityId}/intervals")
class IntervalControllerIT {

  @Autowired
  private TestRestTemplate testRestTemplate;
  @Autowired
  private ActivityRepository activityRepository;
  @Autowired
  private IntervalRepository intervalRepository;

  private HttpHeaders headers;

  @BeforeEach
  public void addHeader() {
    HttpEntity<LoginRequest> requestBody = new HttpEntity<>(new LoginRequest("user@email.com", "1234"));

    ResponseEntity<TokenResponse> response = testRestTemplate.exchange(
        "/signin", HttpMethod.POST, requestBody, new ParameterizedTypeReference<>() {
        });

    headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + response.getBody().getToken());
  }



  @Test
  @DisplayName("post for /activities/{activityId}/intervals must returns status 201 when saved successfully")
  void postForIntervals_MustReturnsStatus201_WhenSavedSuccessfully() {
    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Lorem Ipsum Activity", "A simple project for my portfolio");
    activityRepository.save(activityToBeSaved);

    final String URI = "/activities/1/intervals";
    IntervalRequest intervalRequest = new IntervalRequest(
        "2022-09-25T14:26:00", "00:30:00");
    HttpEntity<IntervalRequest> requestEntity = new HttpEntity<>(intervalRequest, headers);

    ResponseEntity<IntervalResponse> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });
    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.CREATED);
  }

  @Test
  @DisplayName("post for /activities/{activityId}/intervals must returns IntervalResponseBody when saved successfully")
  void postForIntervals_MustReturnsIntervalResponseBody_WhenSavedSuccessfully() {
    final String URI = "/activities/1/intervals";
    IntervalRequest intervalRequest = new IntervalRequest(
        "2022-09-25T14:26:00", "00:30:00");
    HttpEntity<IntervalRequest> requestEntity = new HttpEntity<>(intervalRequest, headers);

    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Lorem Ipsum Activity", "A simple project for my portfolio");
    activityRepository.save(activityToBeSaved);

    ResponseEntity<IntervalResponse> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    IntervalResponse actualBody = responseEntity.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getId()).isEqualTo(1L);
    Assertions.assertThat(actualBody.getStartedAt()).isEqualTo("2022-09-25T14:26:00");
    Assertions.assertThat(actualBody.getElapsedTime()).isEqualTo("00:30:00");

  }

  @Test
  @DisplayName("post for /activities/{activityId}/intervals must returns status 400 when Activity doesn't exist")
  void postForIntervals_MustReturnsStatus400_WhenActivityDoesntExist() {
    final String URI = "/activities/100/intervals";
    IntervalRequest intervalRequest = new IntervalRequest(
        "2022-09-25T14:26:00", "00:30:00");
    HttpEntity<IntervalRequest> requestEntity = new HttpEntity<>(intervalRequest, headers);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("post for /activities/{activityId}/intervals must returns ExceptionDetails when Activity doesn't exist")
  void postForIntervals_MustReturnsExceptionDetails_WhenActivityDoesntExist() {
    final String URI = "/activities/100/intervals";
    IntervalRequest intervalRequest = new IntervalRequest(
        "2022-09-25T14:26:00", "00:30:00");
    HttpEntity<IntervalRequest> requestEntity = new HttpEntity<>(intervalRequest, headers);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: 100");
  }

  @Test
  @DisplayName("post for /activities/{activityId}/intervals must returns status 400 when Activity status is concluded")
  void postForIntervals_MustReturnsStatus400_WhenActivityStatusIsConcluded() {
    Activity activityConcluded = ActivityCreator.withStatus(Status.CONCLUDED);
    activityRepository.save(activityConcluded);

    final String URI = "/activities/1/intervals";
    IntervalRequest intervalRequest = new IntervalRequest(
        "2022-09-25T14:26:00", "00:30:00");
    HttpEntity<IntervalRequest> requestEntity = new HttpEntity<>(intervalRequest, headers);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });
    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("post for /activities/{activityId}/intervals must returns ExceptionDetails when Activity status is concluded")
  void postForIntervals_MustReturnsExceptionDetails_WhenActivityStatusIsConcluded() {
    Activity activityConcluded = ActivityCreator.withStatus(Status.CONCLUDED);
    activityRepository.save(activityConcluded);

    final String URI = "/activities/1/intervals";
    IntervalRequest intervalRequest = new IntervalRequest(
        "2022-09-25T14:26:00", "00:30:00");
    HttpEntity<IntervalRequest> requestEntity = new HttpEntity<>(intervalRequest, headers);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Cannot create interval on non active activity");
  }

  @Test
  @DisplayName("post for /activities/{activityId}/intervals must returns status 400 when Activity status is deleted")
  void postForIntervals_MustReturnsStatus400_WhenActivityStatusIsDeleted() {
    Activity activityDeleted = ActivityCreator.withStatus(Status.DELETED);
    activityRepository.save(activityDeleted);

    final String URI = "/activities/1/intervals";
    IntervalRequest intervalRequest = new IntervalRequest(
        "2022-09-25T14:26:00", "00:30:00");
    HttpEntity<IntervalRequest> requestEntity = new HttpEntity<>(intervalRequest, headers);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });
    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("post for /activities/{activityId}/intervals must returns ExceptionDetails when Activity status is deleted")
  void postForIntervals_MustReturnsExceptionDetails_WhenActivityStatusIsDeleted() {
    Activity activityDeleted = ActivityCreator.withStatus(Status.DELETED);
    activityRepository.save(activityDeleted);

    final String URI = "/activities/1/intervals";
    IntervalRequest intervalRequest = new IntervalRequest(
        "2022-09-25T14:26:00", "00:30:00");
    HttpEntity<IntervalRequest> requestEntity = new HttpEntity<>(intervalRequest, headers);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Cannot create interval on non active activity");
  }

  @Test
  @DisplayName("post for /activities/{activityId}/intervals must returns status 401 when authorization header is invalid")
  void postForIntervals_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    Activity activityDeleted = ActivityCreator.withStatus(Status.DELETED);
    activityRepository.save(activityDeleted);

    final String URI = "/activities/1/intervals";
    IntervalRequest intervalRequest = new IntervalRequest(
        "2022-09-25T14:26:00", "00:30:00");
    HttpEntity<IntervalRequest> requestEntity = new HttpEntity<>(intervalRequest);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });
    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  @DisplayName("delete for /activities/{activityId}/intervals/{id} must returns status 204 when deleted successfully")
  void deleteForIntervalsId_MustReturnsStatus204_WhenDeletedSuccessfully() {
    final String URI = "/activities/1/intervals/1";

    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Lorem Ipsum Activity", "A simple project for my portfolio");
    Activity activitySaved = activityRepository.save(activityToBeSaved);
    Interval interval = IntervalCreator.withActivityStartedAtAndElapsedTime(
        activitySaved, "2022-09-25T10:32:57", "00:30:00"
    );
    intervalRepository.save(interval);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<>() {
        });
    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @DisplayName("delete for /activities/{activityId}/intervals/{id} must returns null request body when deleted successfully")
  void deleteForIntervalsId_MustReturnsNullRequestBody_WhenDeletedSuccessfully() {
    final String URI = "/activities/1/intervals/1";

    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Lorem Ipsum Activity", "A simple project for my portfolio");
    Activity activitySaved = activityRepository.save(activityToBeSaved);
    Interval interval = IntervalCreator.withActivityStartedAtAndElapsedTime(
        activitySaved, "2022-09-25T10:32:57", "00:30:00"
    );
    intervalRepository.save(interval);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<>() {
        });

    Assertions.assertThat(responseEntity.getBody()).isNull();
  }

  @Test
  @DisplayName("delete for /activities/{activityId}/intervals/{id} must returns status 400 when Activity doesn't exist")
  void deleteForIntervalsId_MustReturnsStatus400_WhenActivityDoesntExist() {
    final String URI = "/activities/100/intervals/1";

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.DELETE,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("delete for /activities/{activityId}/intervals/{id} must returns ExceptionDetails when Activity doesn't exist")
  void deleteForIntervalsId_MustReturnsExceptionDetails_WhenActivityDoesntExist() {
    final String URI = "/activities/100/intervals/1";

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.DELETE,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: 100");
  }

  @Test
  @DisplayName("delete for /activities/{activityId}/intervals/{id} must returns status 401 when authorization header is invalid")
  void deleteForIntervalsId_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    final String URI = "/activities/100/intervals/1";


    ResponseEntity<ExceptionDetails> responseEntity =
        testRestTemplate.exchange(URI, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

}