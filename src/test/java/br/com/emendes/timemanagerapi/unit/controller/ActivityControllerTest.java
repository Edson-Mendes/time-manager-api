package br.com.emendes.timemanagerapi.unit.controller;

import br.com.emendes.timemanagerapi.controller.ActivityController;
import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.service.ActivityService;
import br.com.emendes.timemanagerapi.util.creator.ActivityResponseBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
  private final ActivityRequestBody VALID_ACTIVITY_REQUEST_BODY =
      new ActivityRequestBody("Lorem Ipsum Activity", "A simple project for my portfolio");
  private final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

//  Mocks de métodos/actions de activityServiceMock
  @BeforeEach
  public void setUp() {
    List<ActivityResponseBody> listActivityRespBody =
        List.of(ActivityResponseBodyCreator.withIdAndName(1L, "Lorem Ipsum Activity"),
            ActivityResponseBodyCreator.withIdAndName(2L, "XPTO Activity"));

    BDDMockito.when(activityServiceMock.find(DEFAULT_PAGEABLE))
        .thenReturn(new PageImpl<>(listActivityRespBody, DEFAULT_PAGEABLE, 2));

    BDDMockito.when(activityServiceMock.create(VALID_ACTIVITY_REQUEST_BODY))
        .thenReturn(ActivityResponseBodyCreator
            .withIdNameAndDescription(1L, "Lorem Ipsum Activity", "A simple project for my portfolio"));

    BDDMockito.doNothing().when(activityServiceMock)
        .update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(ActivityRequestBody.class));

    BDDMockito.doNothing().when(activityServiceMock)
        .deleteById(ArgumentMatchers.anyLong());
  }

  @Nested
  @DisplayName("tests for find method")
  class FindMethod {

    @Test
    @DisplayName("find must returns status 200 when found successfully")
    void find_MustReturnsStatus200_WhenFoundSuccessfully() {
      HttpStatus actualStatusCode = activityController.find(DEFAULT_PAGEABLE).getStatusCode();

      Assertions.assertThat(actualStatusCode).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("find must returns Page<ActivityResponseBody> when found successfully")
    void find_MustReturnsPageActivityResponseBody_WhenFoundSuccessfully() {
      Page<ActivityResponseBody> actualBody = activityController.find(DEFAULT_PAGEABLE).getBody();

      ActivityResponseBody expectedActivityRespBody1 = ActivityResponseBodyCreator
          .withIdAndName(1L, "Lorem Ipsum Activity");
      ActivityResponseBody expectedActivityRespBody2 = ActivityResponseBodyCreator
          .withIdAndName(2L, "XPTO Activity");

      Assertions.assertThat(actualBody).isNotNull().isNotEmpty().hasSize(2);
      Assertions.assertThat(actualBody.getContent())
          .contains(expectedActivityRespBody1).contains(expectedActivityRespBody2);
    }

  }

  @Nested
  @DisplayName("tests for create method")
  class CreateMethod {

    @Test
    @DisplayName("create must return status 201 when created successfully")
    void create_MustReturnsStatus201_WhenCreatedSuccessfully() {
      HttpStatus actualStatusCode = activityController.create(VALID_ACTIVITY_REQUEST_BODY, URI_BUILDER)
          .getStatusCode();

      Assertions.assertThat(actualStatusCode).isEqualByComparingTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("create must returns ActivityResponseBody when created successfully")
    void create_MustReturnsActivityResponseBody_WhenCreatedSuccessfully() {
      ActivityResponseBody actualBody = activityController.create(VALID_ACTIVITY_REQUEST_BODY, URI_BUILDER).getBody();

      Assertions.assertThat(actualBody).isNotNull();
      Assertions.assertThat(actualBody.getId()).isEqualTo(1L);
      Assertions.assertThat(actualBody.getName()).isEqualTo("Lorem Ipsum Activity");
      Assertions.assertThat(actualBody.getDescription()).isEqualTo("A simple project for my portfolio");
      Assertions.assertThat(actualBody.getCreatedAt()).isNotNull();
      Assertions.assertThat(actualBody.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("create must returns path /activities/1 when created successfully")
    void create_MustReturnsPathActivities1_WhenCreatedSuccessfully() {
      URI actualUri = activityController.create(VALID_ACTIVITY_REQUEST_BODY, URI_BUILDER)
          .getHeaders()
          .getLocation();

      Assertions.assertThat(actualUri).isNotNull();
      Assertions.assertThat(actualUri.getPath()).isEqualTo("/activities/1");
    }

  }

  @Nested
  @DisplayName("tests for update method")
  class UpdateMethod {

    @Test
    @DisplayName("update must returns status 204 when updated successfully")
    void update_MustReturnsStatus204_WhenUpdatedSuccessfully() {
      ActivityRequestBody activityToBeUpdated = new ActivityRequestBody(
          "Finances REST API", "A simple Restful API for my portfolio");

      HttpStatus actualStatusCode = activityController.update(1L, activityToBeUpdated).getStatusCode();

      Assertions.assertThat(actualStatusCode).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    }

  }

  @Nested
  @DisplayName("tests for delete method")
  class DeleteMethod {

    @Test
    @DisplayName("delete must returns status 204 when deleted successfully")
    void delete_MustReturnsStatus204_WhenDeletedSuccessfully() {
      long activityId = 1L;

      HttpStatus actualStatusCode = activityController.delete(activityId).getStatusCode();

      Assertions.assertThat(actualStatusCode).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    }

  }

}