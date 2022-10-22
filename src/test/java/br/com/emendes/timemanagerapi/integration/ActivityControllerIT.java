package br.com.emendes.timemanagerapi.integration;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.request.UpdateStatusRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.dto.response.detail.ExceptionDetails;
import br.com.emendes.timemanagerapi.dto.response.detail.ValidationExceptionDetails;
import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
import br.com.emendes.timemanagerapi.util.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

//  TODO: Não esquecer de refatorar esses testes!
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for /activities")
class ActivityControllerIT {

  @Autowired
  @Qualifier(value = "withPatch")
  private TestRestTemplate testRestTemplate;
  @Autowired
  private ActivityRepository activityRepository;

  private HttpHeaders headers;
  private final String ACTIVITIES_URI = "/activities";

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
  @DisplayName("get for /activities must returns status 200 when found successfully")
  void getForActivities_MustReturnsStatus200_WhenFoundSuccessfully() {
    Activity activityToBeSaved1 = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Finances API", "A simple project");
    Activity activityToBeSaved2 = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Transaction Analyzer", "A simple project");
    activityRepository.save(activityToBeSaved1);
    activityRepository.save(activityToBeSaved2);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    ResponseEntity<PageableResponse<ActivityResponse>> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });
    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("get for /activities must returns Page<ActivitiesResponseBody> when found successfully")
  void getForActivities_MustReturnsPageActivitiesResponseBody_WhenFoundSuccessfully() {
    Activity activityToBeSaved1 = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Finances API", "A simple project");
    Activity activityToBeSaved2 = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Transaction Analyzer", "A simple project");
    activityRepository.save(activityToBeSaved1);
    activityRepository.save(activityToBeSaved2);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<PageableResponse<ActivityResponse>> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });
    Page<ActivityResponse> actualBody = responseEntity.getBody();

    Assertions.assertThat(actualBody).isNotNull().hasSize(2);
    Assertions.assertThat(actualBody.getContent().get(0).getName()).isEqualTo("Finances API");
    Assertions.assertThat(actualBody.getContent().get(1).getName()).isEqualTo("Transaction Analyzer");
    Assertions.assertThat(actualBody.getContent().get(0).getDescription()).isEqualTo("A simple project");
    Assertions.assertThat(actualBody.getContent().get(1).getDescription()).isEqualTo("A simple project");
  }

  @Test
  @DisplayName("get for /activities must returns only enabled activities when exists activities disabled")
  void getForActivities_MustReturnsOnlyEnabledActivities_WhenExistsActivitiesDisabled() {
    Activity activityToBeSaved1 = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Finances API", "A simple project");
    Activity activityToBeSaved2 = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Transaction Analyzer", "A simple project");
    activityToBeSaved2.setStatus(Status.DELETED);
    activityRepository.save(activityToBeSaved1);
    activityRepository.save(activityToBeSaved2);
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    ResponseEntity<PageableResponse<ActivityResponse>> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });
    Page<ActivityResponse> actualBody = responseEntity.getBody();
    ActivityResponse actualActivityRespBody = actualBody.getContent().get(0);

    Assertions.assertThat(actualBody).isNotNull().hasSize(1);
    Assertions.assertThat(actualActivityRespBody.getName()).isEqualTo("Finances API");
    Assertions.assertThat(actualActivityRespBody.getDescription()).isEqualTo("A simple project");
    Assertions.assertThat(actualActivityRespBody.getStatus()).isEqualByComparingTo(Status.ACTIVE);
  }

  @Test
  @DisplayName("get for /activities must returns status 400 when doesn't have activities")
  void getForActivities_MustReturnsStatus400_WhenDoesntHaveActivities() {
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("get for /activities must returns ExceptionDetails when  doesn't have activities>")
  void getForActivities_MustReturnsExceptionDetails_WhenDoesntHaveActivities() {
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });

    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Não possui atividades");
  }

  @Test
  @DisplayName("get for /activities must returns status 401 when authorization header is invalid")
  void getForActivities_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  @DisplayName("post for /activities must returns status 201 when saved successfully")
  void postForActivities_MustReturnsStatus201_WhenSavedSuccessfully() {
    ActivityRequestBody body = new ActivityRequestBody("Finances API", "A simple project");
    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(body, headers);
    ResponseEntity<ActivityResponse> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.CREATED);
  }

  @Test
  @DisplayName("post for /activities must returns ActivityResponseBody when saved successfully")
  void postForActivities_MustReturnsActivityResponseBody_WhenSavedSuccessfully() {
    ActivityRequestBody body = new ActivityRequestBody("Finances API", "A simple project");
    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(body, headers);
    ResponseEntity<ActivityResponse> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    ActivityResponse actualBody = responseEntity.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getId()).isEqualTo(1L);
    Assertions.assertThat(actualBody.getName()).isEqualTo("Finances API");
    Assertions.assertThat(actualBody.getDescription()).isEqualTo("A simple project");
    Assertions.assertThat(actualBody.getStatus()).isEqualByComparingTo(Status.ACTIVE);
  }

  @Test
  @DisplayName("post for /activities must returns status 400 when saved fails")
  void postForActivities_MustReturnsStatus400_WhenSavedFails() {
    ActivityRequestBody body = new ActivityRequestBody("", null);
    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(body, headers);
    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("post for /activities must returns ValidationExceptionDetails when saved fails")
  void postForActivities_MustReturnsValidationExceptionDetails_WhenSavedFails() {
    ActivityRequestBody body = new ActivityRequestBody("", null);
    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(body, headers);
    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    ValidationExceptionDetails actualBody = responseEntity.getBody();

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
  @DisplayName("post for /activities must returns status 401 when authorization header is invalid")
  void postForActivities_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    ActivityRequestBody body = new ActivityRequestBody("", null);
    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(body);
    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplate.exchange(
        ACTIVITIES_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  @DisplayName("put for /activities/{id} must returns status 204 when updated successfully")
  void putForActivitiesId_MustReturnsStatus204_WhenUpdatedSuccessfully() {
    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;
    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Finances API", "A simple project");
    activityRepository.save(activityToBeSaved);

    String activityName = "Finances Rest API";
    String activityDescription = "A simple project";
    ActivityRequestBody activityToBeUpdated = new ActivityRequestBody(activityName, activityDescription);

    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

    ResponseEntity<Void> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    Assertions.assertThat(response.getBody()).isNull();
  }

  @Test
  @DisplayName("put for /activities/{id} must returns status 400 when activity doesn't exist")
  void putForActivitiesId_MustReturnsStatus400_WhenActivityDoesntExist() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;

    String activityName = "Finances Rest API";
    String activityDescription = "A simple project";
    ActivityRequestBody activityToBeUpdated = new ActivityRequestBody(activityName, activityDescription);

    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("put for /activities/{id} must returns ExceptionDetails when activity doesn't exist")
  void putForActivitiesId_MustReturnsExceptionDetails_WhenActivityDoesntExist() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;

    String activityName = "Finances Rest API";
    String activityDescription = "A simple project";
    ActivityRequestBody activityToBeUpdated = new ActivityRequestBody(activityName, activityDescription);

    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    ExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: " + id);
  }

  @Test
  @DisplayName("put for /activities/{id} must returns status 400 when request body is invalid")
  void putForActivitiesId_MustReturnsStatus400_WhenRequestBodyIsInvalid() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;
    ActivityRequestBody activityToBeUpdated = new ActivityRequestBody("", null);

    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("put for /activities/{id} must returns ValidationExceptionDetails when request body is invalid")
  void putForActivitiesId_MustReturnsValidationExceptionDetails_WhenRequestBodyIsInvalid() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;
    ActivityRequestBody activityToBeUpdated = new ActivityRequestBody("", null);

    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    ValidationExceptionDetails actualBody = response.getBody();

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
  @DisplayName("put for /activities/{id} must returns status 400 when activity status is deleted")
  void putForActivitiesId_MustReturnsStatus400_WhenActivityStatusIsDeleted() {
    activityRepository.save(ActivityCreator.withStatus(Status.DELETED));

    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;
    ActivityRequestBody activityToBeUpdated = new ActivityRequestBody("lorem name", "ipsum description");
    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("put for /activities/{id} must returns ExceptionDetails when activity status is deleted")
  void putForActivitiesId_MustReturnsValidationExceptionDetails_WhenActivityStatusIsDeleted() {
    activityRepository.save(ActivityCreator.withStatus(Status.DELETED));

    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;
    ActivityRequestBody activityToBeUpdated = new ActivityRequestBody("lorem name", "ipsum description");
    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    ValidationExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: " + id);
  }

  @Test
  @DisplayName("put for /activities/{id} must returns status 401 when authorization header is invalid")
  void putForActivitiesId_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    activityRepository.save(ActivityCreator.withStatus(Status.DELETED));

    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;
    ActivityRequestBody activityToBeUpdated = new ActivityRequestBody("lorem name", "ipsum description");
    HttpEntity<ActivityRequestBody> requestEntity = new HttpEntity<>(activityToBeUpdated);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  @DisplayName("patch for /activities/{id} must returns status 204 when update status successfully")
  void patchForActivitiesId_MustReturnsStatus204_WhenUpdateStatusSuccessfully() {
    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;
    UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("Concluded");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(updateStatusRequest, headers);

    Activity activityToBeSaved = ActivityCreator.withStatus(Status.ACTIVE);
    activityRepository.save(activityToBeSaved);

    ResponseEntity<Void> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    Assertions.assertThat(response.getBody()).isNull();
  }

  @Test
  @DisplayName("patch for /activities/{id} must returns status 400 when status is invalid")
  void patchForActivitiesId_MustReturnsStatus400_WhenStatusIsInvalid() {
    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;
    UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("cconclude");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(updateStatusRequest, headers);

    Activity activityToBeSaved = ActivityCreator.withStatus(Status.ACTIVE);
    activityRepository.save(activityToBeSaved);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("patch for /activities/{id} must returns ValidationExceptionDetails when status is invalid")
  void patchForActivitiesId_MustReturnsValidationExceptionDetails_WhenStatusIsInvalid() {
    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;
    UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("cconclude");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(updateStatusRequest, headers);

    Activity activityToBeSaved = ActivityCreator.withStatus(Status.ACTIVE);
    activityRepository.save(activityToBeSaved);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    ValidationExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getFields()).contains("status");
    Assertions.assertThat(actualBody.getMessages()).contains("invalid status");
  }

  @Test
  @DisplayName("patch for /activities/{id} must returns status 400 when activity doesn't exist")
  void patchForActivitiesId_MustReturnsStatus400_WhenActivityDoesntExist() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;

    UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("concluded");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(updateStatusRequest, headers);

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("patch for /activities/{id} must returns ExceptionDetails when activity doesn't exist")
  void patchForActivitiesId_MustReturnsExceptionDetails_WhenActivityDoesntExist() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;

    UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("concluded");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(updateStatusRequest, headers);

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    ExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: " + id);
  }

  @Test
  @DisplayName("patch for /activities/{id} must returns status 400 when activity status is deleted")
  void patchForActivitiesId_MustReturnsStatus400_WhenActivityStatusIsDeleted() {
    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;

    activityRepository.save(ActivityCreator.withStatus(Status.DELETED));

    UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("concluded");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(updateStatusRequest, headers);

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("patch for /activities/{id} must returns ExceptionDetails when activity status is deleted")
  void patchForActivitiesId_MustReturnsExceptionDetails_WhenActivityStatusIsDeleted() {
    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;

    activityRepository.save(ActivityCreator.withStatus(Status.DELETED));

    UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("concluded");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(updateStatusRequest, headers);

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    ExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: " + id);
  }

  @Test
  @DisplayName("patch for /activities/{id} must returns status 401 when authorization header is invalid")
  void patchForActivitiesId_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;

    activityRepository.save(ActivityCreator.withStatus(Status.DELETED));

    UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("concluded");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(updateStatusRequest);

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  @DisplayName("delete for /activities/{id} must returns status 204 when deleted successfully")
  void deleteForActivitiesId_MustReturnsStatus204_WhenDeletedSuccessfully() {
    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;
    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Finances API", "A simple project");
    activityRepository.save(activityToBeSaved);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<Void> response = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    Assertions.assertThat(response.getBody()).isNull();
  }

  @Test
  @DisplayName("delete for /activities/{id} must disable activity when deleted successfully")
  void deleteForActivitiesId_MustDisableActivity_WhenDeletedSuccessfully() {
    String uri = ACTIVITIES_URI + "/" + 1L;
    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Finances API", "A simple project");
    activityRepository.save(activityToBeSaved);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//    Deletando
    testRestTemplate.exchange(
        uri, HttpMethod.DELETE, requestEntity,
        new ParameterizedTypeReference<>() {
        });
//    Buscando activity desativada.
    Activity disabledActivity = activityRepository.findById(1L).orElseThrow();
    Status actualStatus = disabledActivity.getStatus();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(Status.DELETED);
  }

  @Test
  @DisplayName("delete for /activities/{id} must returns status 400 when activity don't exist")
  void deleteForActivitiesId_MustReturnsStatus400_WhenActivityDontExists() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("delete for /activities/{id} must returns ExceptionDetails when activity don't exist")
  void deleteForActivitiesId_MustReturnsExceptionDetails_WhenActivityDontExists() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    ExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Activity not found for id: " + id);
  }

  @Test
  @DisplayName("delete for /activities/{id} must returns status 401 when authorization header is invalid")
  void deleteForActivitiesId_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, null,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

}
