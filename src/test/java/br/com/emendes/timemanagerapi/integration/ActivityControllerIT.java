package br.com.emendes.timemanagerapi.integration;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.exception.detail.ExceptionDetails;
import br.com.emendes.timemanagerapi.exception.detail.ValidationExceptionDetails;
import br.com.emendes.timemanagerapi.model.Activity;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integrations tests for /activities")
class ActivityControllerIT {

  @Autowired
  private TestRestTemplate testRestTemplate;
  @Autowired
  private ActivityRepository activityRepository;

  private final String ACTIVITIES_URI = "/activities";

  @Test
  @DisplayName("get for /activities must returns List<ActivitiesResponseBody> when find successfully>")
  void getForActivities_MustReturnsListActivityResponseBody_WhenFindSuccessfully() {
    Activity activityToBeSaved1 = ActivityCreator.activityWithoutIdAndWithNameAndDescription("Finances API", "A simple project");
    Activity activityToBeSaved2 = ActivityCreator.activityWithoutIdAndWithNameAndDescription("Transaction Analyzer", "A simple project");

    activityRepository.save(activityToBeSaved1);
    activityRepository.save(activityToBeSaved2);


    ResponseEntity<List<ActivityResponseBody>> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<ActivityResponseBody>>() {
        });
    HttpStatus responseStatus = responseEntity.getStatusCode();
    List<ActivityResponseBody> responseBody = responseEntity.getBody();

    Assertions.assertThat(responseStatus).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(responseBody).isNotNull().hasSize(2);
    Assertions.assertThat(responseBody.get(0).getName()).isEqualTo("Finances API");
    Assertions.assertThat(responseBody.get(1).getName()).isEqualTo("Transaction Analyzer");
    Assertions.assertThat(responseBody.get(0).getDescription()).isEqualTo("A simple project");
    Assertions.assertThat(responseBody.get(1).getDescription()).isEqualTo("A simple project");
  }

  @Test
  @DisplayName("get for /activities must returns ExceptionDetails when hasn't activities>")
  void getForActivities_MustReturnsExceptionDetails_WhenHasntActivities() {
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.GET, null,
        new ParameterizedTypeReference<ExceptionDetails>() {});

    HttpStatus responseStatus = responseEntity.getStatusCode();
    ExceptionDetails responseBody = responseEntity.getBody();

    Assertions.assertThat(responseStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(responseBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(responseBody.getDetails()).isEqualTo("Não possui atividades");
  }

  @Test
  @DisplayName("post for /activities must returns ActivityResponseBody when save successfully")
  void postForActivities_MustReturnsActivityResponseBody_WhenSaveSuccessfully() {
    ActivityRequestBody body = new ActivityRequestBody("Finances API", "A simple project");
    HttpEntity requestEntity = new HttpEntity(body);
    ResponseEntity<ActivityResponseBody> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<ActivityResponseBody>() {});

    HttpStatus responseStatus = responseEntity.getStatusCode();
    ActivityResponseBody responseBody = responseEntity.getBody();

    Assertions.assertThat(responseStatus).isEqualByComparingTo(HttpStatus.CREATED);
    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getId()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(responseBody.getName()).isEqualTo("Finances API");
    Assertions.assertThat(responseBody.getDescription()).isEqualTo("A simple project");
    Assertions.assertThat(responseBody.isEnabled()).isTrue();
  }

  @Test
  @DisplayName("post for /activities must returns ValidationExceptionDetails when save fails")
  void postForActivities_MustReturnsValidationExceptionDetails_WhenSaveFails() {
    ActivityRequestBody body = new ActivityRequestBody("", null);
    HttpEntity requestEntity = new HttpEntity(body);
    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<ValidationExceptionDetails>() {});

    HttpStatus responseStatus = responseEntity.getStatusCode();
    ValidationExceptionDetails responseBody = responseEntity.getBody();

    Assertions.assertThat(responseStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(responseBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(responseBody.getFields()).contains("name");
    Assertions.assertThat(responseBody.getFields()).contains("description");
    Assertions.assertThat(responseBody.getMessages()).contains("name must not be null or blank");
    Assertions.assertThat(responseBody.getMessages()).contains("description must not be null or blank");
  }


}
