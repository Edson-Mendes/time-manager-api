package br.com.emendes.timemanagerapi.unit.controller;

import br.com.emendes.timemanagerapi.controller.IntervalController;
import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponse;
import br.com.emendes.timemanagerapi.service.ActivityService;
import br.com.emendes.timemanagerapi.service.IntervalService;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
import br.com.emendes.timemanagerapi.util.creator.IntervalRequestBodyCreator;
import br.com.emendes.timemanagerapi.util.creator.IntervalResponseBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for IntervalController")
class IntervalControllerTest {

  @InjectMocks
  private IntervalController intervalController;
  @Mock
  private IntervalService intervalServiceMock;
  @Mock
  private ActivityService activityServiceMock;

  private final UriComponentsBuilder URI_BUILDER = UriComponentsBuilder.fromHttpUrl("http://localhost:8080");
  private final long EXISTENT_ACTIVITY_ID = 1L;
  private final long EXISTENT_INTERVAL_ID = 1000000L;
  private final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10, Sort.Direction.DESC, "startedAt");

  @BeforeEach
  void setUp() {
    IntervalResponse intervalResp = IntervalResponseBodyCreator
        .withIdAndStartedAtAndElapsedTime(EXISTENT_ACTIVITY_ID, "2022-08-16T15:07:00", "00:30:00");
    Page<IntervalResponse> intervalRespBodyPage = new PageImpl<>(List.of(intervalResp), DEFAULT_PAGEABLE, 1);

    BDDMockito.when(intervalServiceMock.create(EXISTENT_ACTIVITY_ID, IntervalRequestBodyCreator.validIntervalRequest()))
        .thenReturn(IntervalResponseBodyCreator.intervalRBForTests());
    BDDMockito.when(intervalServiceMock.find(EXISTENT_ACTIVITY_ID, DEFAULT_PAGEABLE))
        .thenReturn(intervalRespBodyPage);
    BDDMockito.doNothing().when(intervalServiceMock).delete(EXISTENT_ACTIVITY_ID, EXISTENT_INTERVAL_ID);

    BDDMockito.when(activityServiceMock.findById(EXISTENT_ACTIVITY_ID))
        .thenReturn(ActivityCreator.withIdAndName(EXISTENT_ACTIVITY_ID, "Lorem Ipsum Activity"));
  }

  @Nested
  @DisplayName("tests for create method")
  class CreateMethod {

    @Test
    @DisplayName("create must returns status 201 when created successfully")
    void create_MustReturnsStatus201_WhenCreatedSuccessful() {
      IntervalRequest requestBody = new IntervalRequest(
          "2022-08-16T15:07:00", "00:30:00");

      ResponseEntity<IntervalResponse> response = intervalController
          .create(EXISTENT_ACTIVITY_ID, requestBody, URI_BUILDER);
      HttpStatus actualStatusCode = response.getStatusCode();

      Assertions.assertThat(actualStatusCode).isEqualByComparingTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("create must returns ResponseEntity<IntervalResponseBody> when created successfully")
    void create_MustReturnsResponseEntityIntervalResponseBody_WhenCreatedSuccessful() {
      IntervalRequest requestBody = new IntervalRequest(
          "2022-08-16T15:07:00", "00:30:00");

      ResponseEntity<IntervalResponse> response = intervalController
          .create(EXISTENT_ACTIVITY_ID, requestBody, URI_BUILDER);
      IntervalResponse actualResponseBody = response.getBody();

      Assertions.assertThat(actualResponseBody).isNotNull();
      Assertions.assertThat(actualResponseBody.getId()).isPositive();
      Assertions.assertThat(actualResponseBody.getStartedAt()).isEqualTo("2022-08-16T15:07:00");
      Assertions.assertThat(actualResponseBody.getElapsedTime()).isEqualTo("00:30:00");
    }

    @Test
    @DisplayName("create must returns correct location when created successfully")
    void create_MustReturnsCorrectLocation_WhenCreatedSuccessful() {
      IntervalRequest requestBody = new IntervalRequest(
          "2022-08-16T15:07:00", "00:30:00");

      ResponseEntity<IntervalResponse> response = intervalController
          .create(EXISTENT_ACTIVITY_ID, requestBody, URI_BUILDER);

      Assertions.assertThat(response.getHeaders().getLocation()).isNotNull();
      Assertions.assertThat(response.getHeaders().getLocation().getPath())
          .isNotNull().contains("/activities/1/intervals/100");
    }

  }

  @Nested
  @DisplayName("tests for find method")
  class FindMethod {

    @Test
    @DisplayName("find must returns status 200 when found successfully")
    void find_MustReturnsStatus200_WhenFoundSuccessfully() {
      ResponseEntity<Page<IntervalResponse>> response = intervalController
          .find(EXISTENT_ACTIVITY_ID, DEFAULT_PAGEABLE);
      HttpStatus actualStatusCode = response.getStatusCode();

      Assertions.assertThat(actualStatusCode).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("find must returns ResponseEntity<Page<IntervalResponseBody>> when found successfuly")
    void find_MustReturnsResponseEntityPageIntervalResponseBody_WhenFoundSuccessfully() {
      ResponseEntity<Page<IntervalResponse>> response = intervalController
          .find(EXISTENT_ACTIVITY_ID, DEFAULT_PAGEABLE);
      Page<IntervalResponse> actualBody = response.getBody();

      IntervalResponse expectedIntervalRespBody = IntervalResponseBodyCreator
          .withIdAndStartedAtAndElapsedTime(EXISTENT_ACTIVITY_ID, "2022-08-16T15:07:00", "00:30:00");

      Assertions.assertThat(actualBody)
          .isNotEmpty()
          .hasSize(1)
          .contains(expectedIntervalRespBody);
    }

    @Test
    @DisplayName("find must returns empty ResponseEntity<Page<IntervalResponseBody>> when Activity doesn't have intervals")
    void find_MustReturnsEmptyPageIntervalResponseBody_WhenActivityDoesntHavaIntervals() {
      BDDMockito.when(intervalServiceMock.find(999L, DEFAULT_PAGEABLE))
          .thenReturn(Page.empty(DEFAULT_PAGEABLE));

      ResponseEntity<Page<IntervalResponse>> response = intervalController
          .find(999L, DEFAULT_PAGEABLE);
      Page<IntervalResponse> actualBody = response.getBody();

      Assertions.assertThat(actualBody).isEmpty();
    }

  }

  @Nested
  @DisplayName("tests for delete method")
  class DeleteMethod {

    @Test
    @DisplayName("delete must returns status 204 when deleted successfully")
    void delete_MustReturnsStatus204_WhenDeletedSuccessfully() {
      ResponseEntity<Void> response = intervalController
          .delete(EXISTENT_ACTIVITY_ID, EXISTENT_INTERVAL_ID);
      HttpStatus actualStatusCode = response.getStatusCode();

      Assertions.assertThat(actualStatusCode).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    }

  }

}