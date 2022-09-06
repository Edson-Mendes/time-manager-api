package br.com.emendes.timemanagerapi.integration;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.dto.response.detail.ExceptionDetails;
import br.com.emendes.timemanagerapi.dto.response.detail.ValidationExceptionDetails;
import br.com.emendes.timemanagerapi.model.Activity;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for /activities")
class ActivityControllerIT {

  @Autowired
  private TestRestTemplate testRestTemplate;
  @Autowired
  private ActivityRepository activityRepository;

  private final String ACTIVITIES_URI = "/activities";

  @Test
  @DisplayName("get for /activities must returns Page<ActivitiesResponseBody> when find successfully>")
  void getForActivities_MustReturnsListActivityResponseBody_WhenFindSuccessfully() {
    Activity activityToBeSaved1 = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Finances API", "A simple project");
    Activity activityToBeSaved2 = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Transaction Analyzer", "A simple project");

    activityRepository.save(activityToBeSaved1);
    activityRepository.save(activityToBeSaved2);


    ResponseEntity<PageableResponse<ActivityResponseBody>> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
    HttpStatus actualStatus = responseEntity.getStatusCode();
    Page<ActivityResponseBody> actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(actualBody).isNotNull().hasSize(2);
    Assertions.assertThat(actualBody.getContent().get(0).getName()).isEqualTo("Finances API");
    Assertions.assertThat(actualBody.getContent().get(1).getName()).isEqualTo("Transaction Analyzer");
    Assertions.assertThat(actualBody.getContent().get(0).getDescription()).isEqualTo("A simple project");
    Assertions.assertThat(actualBody.getContent().get(1).getDescription()).isEqualTo("A simple project");
  }

  @Test
  @DisplayName("get for /activities must returns ExceptionDetails when hasn't activities>")
  void getForActivities_MustReturnsExceptionDetails_WhenHasntActivities() {
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Não possui atividades");
  }

  @Test
  @DisplayName("post for /activities must returns ActivityResponseBody when save successfully")
  void postForActivities_MustReturnsActivityResponseBody_WhenSaveSuccessfully() {
    ActivityRequestBody body = new ActivityRequestBody("Finances API", "A simple project");
    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(body);
    ResponseEntity<ActivityResponseBody> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();
    ActivityResponseBody actualBody = responseEntity.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.CREATED);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getId()).isEqualTo(1L);
    Assertions.assertThat(actualBody.getName()).isEqualTo("Finances API");
    Assertions.assertThat(actualBody.getDescription()).isEqualTo("A simple project");
    Assertions.assertThat(actualBody.isEnabled()).isTrue();
  }

  @Test
  @DisplayName("post for /activities must returns ValidationExceptionDetails when save fails")
  void postForActivities_MustReturnsValidationExceptionDetails_WhenSaveFails() {
    ActivityRequestBody body = new ActivityRequestBody("", null);
    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(body);
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
    Assertions.assertThat(actualBody.getFields()).contains("name");
    Assertions.assertThat(actualBody.getFields()).contains("description");
    Assertions.assertThat(actualBody.getMessages()).contains("name must not be null or blank");
    Assertions.assertThat(actualBody.getMessages()).contains("description must not be null or blank");
  }

  @Test
  @DisplayName("put for /activities/{id} must returns status 204 when update successfully")
  void putForActivitiesId_MustReturnsStatus204_WhenUpdateSuccessfully() {
    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;
    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Finances API", "A simple project");
    activityRepository.save(activityToBeSaved);

    String activityName = "Finances Rest API";
    String activityDescription = "A simple project";
    ActivityRequestBody activityToBeUpdated = new ActivityRequestBody(activityName, activityDescription);

    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(activityToBeUpdated);

    ResponseEntity<Void> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    Assertions.assertThat(response.getBody()).isNull();
  }

  @Test
  @DisplayName("put for /activities/{id} must returns ExceptionDetails when activity don't exists")
  void putForActivitiesId_MustReturnsExceptionDetails_WhenActivityDontExists() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;

    String activityName = "Finances Rest API";
    String activityDescription = "A simple project";
    ActivityRequestBody activityToBeUpdated = new ActivityRequestBody(activityName, activityDescription);

    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(activityToBeUpdated);

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();
    ExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: " + id);
  }

  @Test
  @DisplayName("put for /activities/{id} must returns ValidationExceptionDetails when request body is invalid")
  void putForActivitiesId_MustReturnsValidationExceptionDetails_WhenRequestBodyIsInvalid() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;
    ActivityRequestBody activityToBeUpdated = new ActivityRequestBody("", null);

    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(activityToBeUpdated);

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
    Assertions.assertThat(actualBody.getFields())
        .contains("name")
        .contains("description");
    Assertions.assertThat(actualBody.getMessages())
        .contains("name must not be null or blank")
        .contains("description must not be null or blank");
  }

  @Test
  @DisplayName("delete for /activities/{id} must returns status 204 and delete activity when delete successfully")
  void deleteForActivitiesId_MustReturnsStatus204AndDeleteActivity_WhenDeleteSuccessfully() {
    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;
    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Finances API", "A simple project");
    activityRepository.save(activityToBeSaved);

    ResponseEntity<Void> response = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, null,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    Assertions.assertThat(response.getBody()).isNull();
  }

  @Test
  @DisplayName("delete for /activities/{id} must returns ExceptionDetails when activity don't exists")
  void deleteForActivitiesId_MustReturnsExceptionDetails_WhenActivityDontExists() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, null,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();
    ExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: " + id);
  }

}
