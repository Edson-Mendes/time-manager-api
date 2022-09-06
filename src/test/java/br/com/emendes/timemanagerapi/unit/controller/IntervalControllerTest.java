package br.com.emendes.timemanagerapi.unit.controller;

import br.com.emendes.timemanagerapi.controller.IntervalController;
import br.com.emendes.timemanagerapi.dto.request.IntervalRequestBody;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponseBody;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
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
  private final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10, Sort.Direction.DESC, "startedAt");

  @BeforeEach
  void setUp(){
    IntervalResponseBody intervalResp = IntervalResponseBodyCreator
        .withIdAndStartedAtAndElapsedTime(EXISTENT_ACTIVITY_ID, "2022-08-16T15:07:00", "00:30:00");
    Page<IntervalResponseBody> intervalRespBodyPage = new PageImpl<>(List.of(intervalResp), DEFAULT_PAGEABLE, 1);

    BDDMockito.when(intervalServiceMock.create(EXISTENT_ACTIVITY_ID, IntervalRequestBodyCreator.validIntervalRequest()))
        .thenReturn(IntervalResponseBodyCreator.intervalRBForTests());
    BDDMockito.when(intervalServiceMock.find(EXISTENT_ACTIVITY_ID, DEFAULT_PAGEABLE))
            .thenReturn(intervalRespBodyPage);

    BDDMockito.when(activityServiceMock.findById(EXISTENT_ACTIVITY_ID))
        .thenReturn(ActivityCreator.withIdAndName(EXISTENT_ACTIVITY_ID, "Lorem Ipsum Activity"));
  }

  @Nested
  @DisplayName("tests for create method")
  class CreateMethod {

    @Test
    @DisplayName("create must returns status 201 when created successfully")
    void create_MustReturnsStatus201_WhenCreatedSuccessful() {
      IntervalRequestBody requestBody = new IntervalRequestBody(
          LocalDateTime.of(2022, 8, 16, 15, 7, 0),
          LocalTime.of(0,30,0));

      ResponseEntity<IntervalResponseBody> response = intervalController
          .create(EXISTENT_ACTIVITY_ID, requestBody, URI_BUILDER);
      HttpStatus actualStatusCode = response.getStatusCode();

      Assertions.assertThat(actualStatusCode).isEqualByComparingTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("create must returns ResponseEntity<IntervalResponseBody> when created successfully")
    void create_MustReturnsResponseEntityIntervalResponseBody_WhenCreatedSuccessful() {
      IntervalRequestBody requestBody = new IntervalRequestBody(
          LocalDateTime.of(2022, 8, 16, 15, 7, 0),
          LocalTime.of(0,30,0));

      ResponseEntity<IntervalResponseBody> response = intervalController
          .create(EXISTENT_ACTIVITY_ID, requestBody, URI_BUILDER);
      IntervalResponseBody actualResponseBody = response.getBody();

      Assertions.assertThat(actualResponseBody).isNotNull();
      Assertions.assertThat(actualResponseBody.getId()).isPositive();
      Assertions.assertThat(actualResponseBody.getStartedAt()).isEqualTo("2022-08-16T15:07:00");
      Assertions.assertThat(actualResponseBody.getElapsedTime()).isEqualTo("00:30:00");
    }

  }

  @Nested
  @DisplayName("tests for find method")
  class FindMethod {

    @Test
    @DisplayName("find must returns status 200 when found successfully")
    void find_MustReturnsStatus200_WhenFoundIntervalsSuccessfully(){
      ResponseEntity<Page<IntervalResponseBody>> response = intervalController
          .find(EXISTENT_ACTIVITY_ID, DEFAULT_PAGEABLE);
      HttpStatus actualStatusCode = response.getStatusCode();

      Assertions.assertThat(actualStatusCode).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("find must returns ResponseEntity<Page<IntervalResponseBody>> when found successfuly")
    void find_MustReturnsResponseEntityPageIntervalResponseBody_WhenFoundIntervalsSuccessfully(){
      ResponseEntity<Page<IntervalResponseBody>> response = intervalController
          .find(EXISTENT_ACTIVITY_ID, DEFAULT_PAGEABLE);
      Page<IntervalResponseBody> actualBody = response.getBody();

      IntervalResponseBody expectedIntervalRespBody = IntervalResponseBodyCreator
          .withIdAndStartedAtAndElapsedTime(EXISTENT_ACTIVITY_ID, "2022-08-16T15:07:00", "00:30:00");

      Assertions.assertThat(actualBody)
          .isNotEmpty()
          .hasSize(1)
          .contains(expectedIntervalRespBody);
    }

    @Test
    @DisplayName("find must returns empty ResponseEntity<Page<IntervalResponseBody>> when Activity doesn't have intervals")
    void find_MustReturnsEmptyPageIntervalResponseBody_WhenActivityDoesntHavaIntervals(){
      BDDMockito.when(intervalServiceMock.find(999L, DEFAULT_PAGEABLE))
          .thenReturn(Page.empty(DEFAULT_PAGEABLE));

      ResponseEntity<Page<IntervalResponseBody>> response = intervalController
          .find(999L, DEFAULT_PAGEABLE);
      Page<IntervalResponseBody> actualBody = response.getBody();

      Assertions.assertThat(actualBody).isEmpty();
    }

  }

}