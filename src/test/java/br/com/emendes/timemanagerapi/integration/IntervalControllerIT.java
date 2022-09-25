package br.com.emendes.timemanagerapi.integration;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequestBody;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponseBody;
import br.com.emendes.timemanagerapi.dto.response.detail.ExceptionDetails;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.entity.Interval;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
import br.com.emendes.timemanagerapi.repository.IntervalRepository;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
import br.com.emendes.timemanagerapi.util.creator.IntervalCreator;
import br.com.emendes.timemanagerapi.util.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.LocalTime;
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

  @Test
  @DisplayName("get for /activities/{activityId}/intervals must returns status 200 when found successfully")
  void getForIntervals_MustReturnsStatus200_WhenFoundSuccessfully(){
    final String URI = "/activities/1/intervals";

    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Lorem Ipsum Activity", "A simple project for my portfolio");
    Activity activitySaved = activityRepository.save(activityToBeSaved);
    Interval interval = IntervalCreator.withActivityStartedAtAndElapsedTime(
        activitySaved, "2022-09-25T10:32:57", "00:30:00"
    );
    intervalRepository.save(interval);

    ResponseEntity<PageableResponse<IntervalResponseBody>> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("get for /activities/{activityId}/intervals must returns Page<IntervalResponseBody> when found successfully")
  void getForIntervals_MustReturnsPageIntervalResponseBody_WhenFoundSuccessfully() {
    final String URI = "/activities/1/intervals";
    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Lorem Ipsum Activity", "A simple project for my portfolio");
    Activity activitySaved = activityRepository.save(activityToBeSaved);
    Interval interval = IntervalCreator.withActivityStartedAtAndElapsedTime(
        activitySaved, "2022-09-25T10:32:57", "00:30:00"
    );
    intervalRepository.save(interval);

    ResponseEntity<PageableResponse<IntervalResponseBody>> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
    Page<IntervalResponseBody> actualBody = responseEntity.getBody();

    Assertions.assertThat(actualBody).isNotNull().hasSize(1);
    Assertions.assertThat(actualBody.getContent().get(0).getStartedAt()).isEqualTo("2022-09-25T10:32:57");
    Assertions.assertThat(actualBody.getContent().get(0).getElapsedTime()).isEqualTo("00:30:00");
  }

  @Test
  @DisplayName("get for /activities/{activityId}/intervals must returns status 400 when Activity doesn't exist")
  void getForIntervals_MustReturnsStatus400_WhenActivityDoesntExist(){
    final String URI = "/activities/100/intervals";

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("get for /activities/{activityId}/intervals must returns status 400 when Activity doesn't exist")
  void getForIntervals_MustReturnsExceptionDetails_WhenActivityDoesntExist(){
    final String URI = "/activities/100/intervals";

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: 100");
  }

  @Test
  @DisplayName("post for /activities/{activityId}/intervals must returns status 201 when saved successfully")
  void postForIntervals_MustReturnsStatus201_WhenSavedSuccessfully(){
    final String URI = "/activities/1/intervals";
    IntervalRequestBody intervalRequestBody = new IntervalRequestBody(
        LocalDateTime.parse("2022-09-25T14:26:00"), LocalTime.parse("00:30:00"));
    HttpEntity<IntervalRequestBody> requestEntity = new HttpEntity<>(intervalRequestBody);

    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Lorem Ipsum Activity", "A simple project for my portfolio");
    activityRepository.save(activityToBeSaved);

    ResponseEntity<IntervalResponseBody> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.CREATED);
  }

  @Test
  @DisplayName("post for /activities/{activityId}/intervals must returns IntervalResponseBody when saved successfully")
  void postForIntervals_MustReturnsIntervalResponseBody_WhenSavedSuccessfully(){
    final String URI = "/activities/1/intervals";
    IntervalRequestBody intervalRequestBody = new IntervalRequestBody(
        LocalDateTime.parse("2022-09-25T14:26:00"), LocalTime.parse("00:30:00"));
    HttpEntity<IntervalRequestBody> requestEntity = new HttpEntity<>(intervalRequestBody);

    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Lorem Ipsum Activity", "A simple project for my portfolio");
    activityRepository.save(activityToBeSaved);

    ResponseEntity<IntervalResponseBody> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    IntervalResponseBody actualBody = responseEntity.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getId()).isEqualTo(1L);
    Assertions.assertThat(actualBody.getStartedAt()).isEqualTo("2022-09-25T14:26:00");
    Assertions.assertThat(actualBody.getElapsedTime()).isEqualTo("00:30:00");

  }

  @Test
  @DisplayName("post for /activities/{activityId}/intervals must returns status 400 when Activity doesn't exist")
  void postForIntervals_MustReturnsStatus400_WhenActivityDoesntExist(){
    final String URI = "/activities/100/intervals";
    IntervalRequestBody intervalRequestBody = new IntervalRequestBody(
        LocalDateTime.parse("2022-09-25T14:26:00"), LocalTime.parse("00:30:00"));
    HttpEntity<IntervalRequestBody> requestEntity = new HttpEntity<>(intervalRequestBody);

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("post for /activities/{activityId}/intervals must returns ExceptionDetails when Activity doesn't exist")
  void postForIntervals_MustReturnsExceptionDetails_WhenActivityDoesntExist(){
    final String URI = "/activities/100/intervals";
    IntervalRequestBody intervalRequestBody = new IntervalRequestBody(
        LocalDateTime.parse("2022-09-25T14:26:00"), LocalTime.parse("00:30:00"));
    HttpEntity<IntervalRequestBody> requestEntity = new HttpEntity<>(intervalRequestBody);

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
  @DisplayName("delete for /activities/{activityId}/intervals/{id} must returns status 204 when deleted successfully")
  void deleteForIntervalsId_MustReturnsStatus204_WhenDeletedSuccessfully(){
    final String URI = "/activities/1/intervals/1";

    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Lorem Ipsum Activity", "A simple project for my portfolio");
    Activity activitySaved = activityRepository.save(activityToBeSaved);
    Interval interval = IntervalCreator.withActivityStartedAtAndElapsedTime(
        activitySaved, "2022-09-25T10:32:57", "00:30:00"
    );
    intervalRepository.save(interval);

    ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {
        });
    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @DisplayName("delete for /activities/{activityId}/intervals/{id} must returns null request body when deleted successfully")
  void deleteForIntervalsId_MustReturnsNullRequestBody_WhenDeletedSuccessfully(){
    final String URI = "/activities/1/intervals/1";

    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Lorem Ipsum Activity", "A simple project for my portfolio");
    Activity activitySaved = activityRepository.save(activityToBeSaved);
    Interval interval = IntervalCreator.withActivityStartedAtAndElapsedTime(
        activitySaved, "2022-09-25T10:32:57", "00:30:00"
    );
    intervalRepository.save(interval);

    ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {
        });

    Assertions.assertThat(responseEntity.getBody()).isNull();
  }

  @Test
  @DisplayName("delete for /activities/{activityId}/intervals/{id} must returns status 400 when Activity doesn't exist")
  void deleteForIntervalsId_MustReturnsStatus400_WhenActivityDoesntExist(){
    final String URI = "/activities/100/intervals/1";

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.DELETE,
        null, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("delete for /activities/{activityId}/intervals/{id} must returns ExceptionDetails when Activity doesn't exist")
  void deleteForIntervalsId_MustReturnsExceptionDetails_WhenActivityDoesntExist(){
    final String URI = "/activities/100/intervals/1";

    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        URI, HttpMethod.DELETE,
        null, new ParameterizedTypeReference<>() {
        });

    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: 100");
  }

}