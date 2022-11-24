package br.com.emendes.timemanagerapi.integration;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequest;
import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.request.UpdateStatusRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.dto.response.detail.ExceptionDetails;
import br.com.emendes.timemanagerapi.dto.response.detail.ValidationExceptionDetails;
import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.entity.Role;
import br.com.emendes.timemanagerapi.model.entity.User;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
import br.com.emendes.timemanagerapi.repository.UserRepository;
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
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
  @Autowired
  private UserRepository userRepository;

  private HttpHeaders headers;
  private final String ACTIVITIES_URI = "/activities";

//  @BeforeEach
//  public void addHeader() {
//    HttpEntity<LoginRequest> requestBody = new HttpEntity<>(new LoginRequest("lorem@email.com", "1234"));
//
//    ResponseEntity<TokenResponse> response = testRestTemplate.exchange(
//        "/signin", HttpMethod.POST, requestBody, new ParameterizedTypeReference<>() {
//        });
//
//    headers = new HttpHeaders();
//    headers.add("Authorization", "Bearer " + response.getBody().getToken());
//  }



  @Test
  @DisplayName("put /activities/{id} must returns status 204 when updated successfully")
  void putActivitiesId_MustReturnsStatus204_WhenUpdatedSuccessfully() {
    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;
    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Finances API", "A simple project");
    activityRepository.save(activityToBeSaved);

    String activityName = "Finances Rest API";
    String activityDescription = "A simple project";
    ActivityRequest activityToBeUpdated = new ActivityRequest(activityName, activityDescription);

    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

    ResponseEntity<Void> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    Assertions.assertThat(response.getBody()).isNull();
  }

  @Test
  @DisplayName("put /activities/{id} must returns status 400 when activity doesn't exist")
  void putActivitiesId_MustReturnsStatus400_WhenActivityDoesntExist() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;

    String activityName = "Finances Rest API";
    String activityDescription = "A simple project";
    ActivityRequest activityToBeUpdated = new ActivityRequest(activityName, activityDescription);

    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("put /activities/{id} must returns ExceptionDetails when activity doesn't exist")
  void putActivitiesId_MustReturnsExceptionDetails_WhenActivityDoesntExist() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;

    String activityName = "Finances Rest API";
    String activityDescription = "A simple project";
    ActivityRequest activityToBeUpdated = new ActivityRequest(activityName, activityDescription);

    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

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
  @DisplayName("put /activities/{id} must returns status 400 when request body is invalid")
  void putActivitiesId_MustReturnsStatus400_WhenRequestBodyIsInvalid() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;
    ActivityRequest activityToBeUpdated = new ActivityRequest("", null);

    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("put /activities/{id} must returns ValidationExceptionDetails when request body is invalid")
  void putActivitiesId_MustReturnsValidationExceptionDetails_WhenRequestBodyIsInvalid() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;
    ActivityRequest activityToBeUpdated = new ActivityRequest("", null);

    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

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
  @DisplayName("put /activities/{id} must returns status 400 when activity status is deleted")
  void putActivitiesId_MustReturnsStatus400_WhenActivityStatusIsDeleted() {
    activityRepository.save(ActivityCreator.withStatus(Status.DELETED));

    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;
    ActivityRequest activityToBeUpdated = new ActivityRequest("lorem name", "ipsum description");
    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("put /activities/{id} must returns ExceptionDetails when activity status is deleted")
  void putActivitiesId_MustReturnsExceptionDetails_WhenActivityStatusIsDeleted() {
    activityRepository.save(ActivityCreator.withStatus(Status.DELETED));

    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;
    ActivityRequest activityToBeUpdated = new ActivityRequest("lorem name", "ipsum description");
    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

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
  @DisplayName("put /activities/{id} must returns status 401 when authorization header is invalid")
  void putActivitiesId_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    activityRepository.save(ActivityCreator.withStatus(Status.DELETED));

    long id = 1L;
    String uri = ACTIVITIES_URI + "/" + id;
    ActivityRequest activityToBeUpdated = new ActivityRequest("lorem name", "ipsum description");
    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(activityToBeUpdated);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  @DisplayName("put /activities/{id} must returns status 400 when activity belongs to another user")
  void putActivitiesId_MustReturnsStatus400_WhenActivityBelongsToAnotherUser(){
    User newUser = saveNewUser();
    Long id = saveActivityWithUser(newUser).getId();

    String uri = ACTIVITIES_URI + "/" + id;
    ActivityRequest activityToBeUpdated = new ActivityRequest("lorem name", "ipsum description");
    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("put /activities/{id} must returns ExceptionDetails when activity belongs to another user")
  void putActivitiesId_MustReturnsExceptionDetails_WhenActivityBelongsToAnotherUser(){
    User newUser = saveNewUser();
    Long id = saveActivityWithUser(newUser).getId();

    String uri = ACTIVITIES_URI + "/" + id;
    ActivityRequest activityToBeUpdated = new ActivityRequest("lorem name", "ipsum description");
    HttpEntity<ActivityRequest> requestEntity = new HttpEntity<>(activityToBeUpdated, headers);

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
  @DisplayName("patch /activities/{id} must returns status 204 when update status successfully")
  void patchActivitiesId_MustReturnsStatus204_WhenUpdateStatusSuccessfully() {
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
  @DisplayName("patch /activities/{id} must returns status 400 when status is invalid")
  void patchActivitiesId_MustReturnsStatus400_WhenStatusIsInvalid() {
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
  @DisplayName("patch /activities/{id} must returns ValidationExceptionDetails when status is invalid")
  void patchActivitiesId_MustReturnsValidationExceptionDetails_WhenStatusIsInvalid() {
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
  @DisplayName("patch /activities/{id} must returns status 400 when activity doesn't exist")
  void patchActivitiesId_MustReturnsStatus400_WhenActivityDoesntExist() {
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
  @DisplayName("patch /activities/{id} must returns ExceptionDetails when activity doesn't exist")
  void patchActivitiesId_MustReturnsExceptionDetails_WhenActivityDoesntExist() {
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
  @DisplayName("patch /activities/{id} must returns status 400 when activity status is deleted")
  void patchActivitiesId_MustReturnsStatus400_WhenActivityStatusIsDeleted() {
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
  @DisplayName("patch /activities/{id} must returns ExceptionDetails when activity status is deleted")
  void patchActivitiesId_MustReturnsExceptionDetails_WhenActivityStatusIsDeleted() {
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
  @DisplayName("patch /activities/{id} must returns status 401 when authorization header is invalid")
  void patchActivitiesId_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
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
  @DisplayName("patch /activities/{id} must returns status 400 when activity belongs to another user")
  void patchActivitiesId_MustReturnsStatus400_WhenActivityBelongsToAnotherUser(){
    User newUser = saveNewUser();
    Long id = saveActivityWithUser(newUser).getId();

    String uri = ACTIVITIES_URI + "/" + id;
    UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("Concluded");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(updateStatusRequest, headers);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.PATCH, requestEntity,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("patch /activities/{id} must returns ExceptionDetails when activity belongs to another user")
  void patchActivitiesId_MustReturnsExceptionDetails_WhenActivityBelongsToAnotherUser(){
    User newUser = saveNewUser();
    Long id = saveActivityWithUser(newUser).getId();

    String uri = ACTIVITIES_URI + "/" + id;
    UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("Concluded");
    HttpEntity<UpdateStatusRequest> requestEntity = new HttpEntity<>(updateStatusRequest, headers);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate.exchange(
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
  @DisplayName("delete /activities/{id} must returns status 204 when deleted successfully")
  void deleteActivitiesId_MustReturnsStatus204_WhenDeletedSuccessfully() {
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
  @DisplayName("delete /activities/{id} must disable activity when deleted successfully")
  void deleteActivitiesId_MustDisableActivity_WhenDeletedSuccessfully() {
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
  @DisplayName("delete /activities/{id} must returns status 400 when activity don't exist")
  void deleteActivitiesId_MustReturnsStatus400_WhenActivityDontExists() {
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
  @DisplayName("delete /activities/{id} must returns ExceptionDetails when activity don't exist")
  void deleteActivitiesId_MustReturnsExceptionDetails_WhenActivityDontExists() {
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
  @DisplayName("delete /activities/{id} must returns status 401 when authorization header is invalid")
  void deleteActivitiesId_MustReturnsStatus401_WhenAuthorizationHeaderIsInvalid() {
    long id = 999L;
    String uri = ACTIVITIES_URI + "/" + id;

    ResponseEntity<ExceptionDetails> response = testRestTemplate.exchange(
        uri, HttpMethod.DELETE, null,
        new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  @DisplayName("delete /activities/{id} must returns status 400 when activity belongs to another user")
  void deleteActivitiesId_MustReturnsStatus400_WhenActivityBelongsToAnotherUser(){
    User newUser = saveNewUser();
    Long id = saveActivityWithUser(newUser).getId();

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

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("delete /activities/{id} must returns ExceptionDetails when activity belongs to another user")
  void deleteActivitiesId_MustReturnsExceptionDetails_WhenActivityBelongsToAnotherUser(){
    User newUser = saveNewUser();
    Long id = saveActivityWithUser(newUser).getId();

    String uri = ACTIVITIES_URI + "/" + id;
    Activity activityToBeSaved = ActivityCreator.withoutIdAndWithNameAndDescription(
        "Finances API", "A simple project");
    activityRepository.save(activityToBeSaved);

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

  private User saveNewUser() {
    Set<Role> roles = new HashSet<>();
    roles.add(new Role(1, "ROLE_USER"));
    User newUser = User.builder()
        .name("New User")
        .email("new.user@email.com")
        .createdAt(LocalDateTime.parse("2022-10-27T10:00:00"))
        .password("123456")
        .roles(roles)
        .enabled(true)
        .build();
    return userRepository.save(newUser);
  }

  private Activity saveActivityWithUser(User newUser) {
    Activity activity = Activity.builder()
        .user(newUser)
        .status(Status.ACTIVE)
        .name("new activity")
        .description("description of new activity")
        .createdAt(LocalDateTime.parse("2022-10-27T10:00:00"))
        .build();
    return activityRepository.save(activity);
  }

  private HttpHeaders getAuthToken() {
    headers = new HttpHeaders();
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
