package br.com.emendes.timemanagerapi.unit.controller;

import br.com.emendes.timemanagerapi.controller.ActivityController;
import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.service.ActivityService;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for ActivityController")
class ActivityControllerTest {

  @InjectMocks
  private ActivityController activityController;
  @Mock
  private ActivityService activityServiceMock;
  private final UriComponentsBuilder URI_BUILDER = UriComponentsBuilder.fromHttpUrl("http://localhost:8080");
  private final ActivityRequestBody ACTIVITY_REQUEST_BODY =
      new ActivityRequestBody("Finances API", "A simple project for my portfolio");
  private final Pageable PAGEABLE = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

  @BeforeEach
  public void setUp() {
    ActivityResponseBody activityRB1 =
        new ActivityResponseBody(ActivityCreator.activityWithIdAndName(1L, "Finances API"));
    ActivityResponseBody activityRB2 =
        new ActivityResponseBody(ActivityCreator.activityWithIdAndName(2L, "Transaction Analyzer"));

    BDDMockito.when(activityServiceMock.find(PAGEABLE))
        .thenReturn(new PageImpl(List.of(activityRB1, activityRB2), PAGEABLE, 2));
    BDDMockito.when(activityServiceMock.create(ACTIVITY_REQUEST_BODY)).thenReturn(activityRB1);
    BDDMockito.doNothing().when(activityServiceMock)
        .update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(ActivityRequestBody.class));
    BDDMockito.doNothing().when(activityServiceMock)
        .deleteById(ArgumentMatchers.anyLong());
  }

  @Test
  @DisplayName("find must returns status 200 when find successfully")
  void find_MustReturnsStatus200_WhenFindSuccessful() {
    HttpStatus statusCode = activityController.find(PAGEABLE).getStatusCode();

    Assertions.assertThat(statusCode).isEqualByComparingTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("find must returns Page<ActivityResponseBody> when find successfully")
  void find_MustReturnsPageActivityResponseBody_WhenFindSuccessful() {
    Page<ActivityResponseBody> body = activityController.find(PAGEABLE).getBody();

    Assertions.assertThat(body).isNotNull().isNotEmpty();
    Assertions.assertThat(body.getContent().get(0).getId()).isEqualTo(1L);
    Assertions.assertThat(body.getContent().get(0).getName()).isEqualTo("Finances API");
    Assertions.assertThat(body.getContent().get(1).getId()).isEqualTo(2L);
    Assertions.assertThat(body.getContent().get(1).getName()).isEqualTo("Transaction Analyzer");
  }

  @Test
  @DisplayName("create must return status 201 when created successful")
  void create_MustReturnsStatus201_WhenCreatedSuccessful() {
    HttpStatus statusCode = activityController.create(ACTIVITY_REQUEST_BODY, URI_BUILDER)
        .getStatusCode();

    Assertions.assertThat(statusCode).isEqualByComparingTo(HttpStatus.CREATED);
  }

  @Test
  @DisplayName("create must returns ActivityResponseBody when created successful")
  void create_MustReturnsActivityResponseBody_WhenCreatedSuccessful() {
    ActivityResponseBody body = activityController.create(ACTIVITY_REQUEST_BODY, URI_BUILDER).getBody();

    Assertions.assertThat(body).isNotNull();
    Assertions.assertThat(body.getId()).isEqualTo(1L);
    Assertions.assertThat(body.getName()).isEqualTo("Finances API");
    Assertions.assertThat(body.getDescription()).isEqualTo("A simple project for my portfolio");
    Assertions.assertThat(body.getCreatedAt()).isNotNull();
    Assertions.assertThat(body.isEnabled()).isTrue();
  }

  @Test
  @DisplayName("create must returns path /activities/1 when created successful")
  void create_MustReturnsPathActivities1_WhenCreatedSuccessful() {
    URI uri = activityController.create(ACTIVITY_REQUEST_BODY, URI_BUILDER)
        .getHeaders()
        .getLocation();

    Assertions.assertThat(uri).isNotNull();
    Assertions.assertThat(uri.getPath()).isEqualTo("/activities/1");
  }

  @Test
  @DisplayName("update must returns status 204 when updated successful")
  void update_MustReturnsStatus204_WhenUpdatedSuccessful() {
    Long activityId = 1L;
    String newName = "Finances REST API";
    String newDescription = "A simple Restful API for my portfolio";
    ActivityRequestBody activityToBeUpdated = new ActivityRequestBody(newName, newDescription);
    HttpStatus statusCode = activityController.update(activityId, activityToBeUpdated).getStatusCode();

    Assertions.assertThat(statusCode).isEqualByComparingTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @DisplayName("delete must returns status 204 when deleted successful")
  void delete_MustReturnsStatus204_WhenDeletedSuccessful() {
    Long activityId = 1L;
    HttpStatus statusCode = activityController.delete(activityId).getStatusCode();

    Assertions.assertThat(statusCode).isEqualByComparingTo(HttpStatus.NO_CONTENT);
  }
}