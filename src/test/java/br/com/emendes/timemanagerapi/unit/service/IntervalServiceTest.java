package br.com.emendes.timemanagerapi.unit.service;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequestBody;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponseBody;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
import br.com.emendes.timemanagerapi.exception.IntervalNotFoundException;
import br.com.emendes.timemanagerapi.model.Activity;
import br.com.emendes.timemanagerapi.model.Interval;
import br.com.emendes.timemanagerapi.repository.IntervalRepository;
import br.com.emendes.timemanagerapi.service.ActivityService;
import br.com.emendes.timemanagerapi.service.IntervalService;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for IntervalService")
class IntervalServiceTest {

  @InjectMocks
  private IntervalService intervalService;
  @Mock
  private ActivityService activityServiceMock;
  @Mock
  private IntervalRepository intervalRepositoryMock;

  private final long EXISTENT_ACTIVITY_ID = 1L;
  private final long NONEXISTENT_ACTIVITY_ID = 9999L;
  private final long EXISTENT_INTERVAL_ID = 100000L;
  private final long NONEXISTENT_INTERVAL_ID = 9999999L;
  private final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10, Sort.Direction.DESC, "startedAt");
  private final Activity DEFAULT_ACTIVITY = ActivityCreator.withIdAndName(EXISTENT_ACTIVITY_ID, "Lorem Ipsum Activity");

  @BeforeEach
  void setUp(){
    Interval intervalToBeSaved = Interval.builder()
        .activity(DEFAULT_ACTIVITY)
        .startedAt(LocalDateTime.parse("2022-08-16T15:07:00"))
        .elapsedTime(LocalTime.parse("00:30:00"))
        .build();
    Interval intervalSaved = Interval.builder()
        .id(100L)
        .startedAt(LocalDateTime.parse("2022-08-16T15:07:00"))
        .elapsedTime(LocalTime.parse("00:30:00"))
        .activity(DEFAULT_ACTIVITY)
        .build();

    Page<Interval> intervalPage = new PageImpl<>(List.of(intervalSaved), DEFAULT_PAGEABLE, 1);

    BDDMockito.when(activityServiceMock.findById(EXISTENT_ACTIVITY_ID))
        .thenReturn(DEFAULT_ACTIVITY);

    BDDMockito.when(intervalRepositoryMock.save(intervalToBeSaved))
        .thenReturn(intervalSaved);
    BDDMockito.when(intervalRepositoryMock.findByActivity(DEFAULT_ACTIVITY, DEFAULT_PAGEABLE))
        .thenReturn(intervalPage);

    BDDMockito.willThrow(new IntervalNotFoundException("Interval not found for id: "+NONEXISTENT_INTERVAL_ID))
        .given(intervalRepositoryMock).findById(NONEXISTENT_INTERVAL_ID);

    BDDMockito.willThrow(new ActivityNotFoundException("Activity not found for id: "+NONEXISTENT_ACTIVITY_ID))
        .given(activityServiceMock).findById(NONEXISTENT_ACTIVITY_ID);
  }

  @Nested
  @DisplayName("tests for create method")
  class CreateMethod {

    @Test
    @DisplayName("create must returns IntervalResponseBody when created successfully")
    void create_MustReturnsIntervalResponseBody_WhenCreatedSuccessfully(){
      IntervalResponseBody actualIntervalRespBody = intervalService.create(
          EXISTENT_ACTIVITY_ID,
          new IntervalRequestBody(
              LocalDateTime.of(2022, 8, 16, 15, 7, 0),
              LocalTime.of(0,30,0)));

      Assertions.assertThat(actualIntervalRespBody).isNotNull();
      Assertions.assertThat(actualIntervalRespBody.getId()).isEqualTo(100L);
      Assertions.assertThat(actualIntervalRespBody.getStartedAt()).isEqualTo("2022-08-16T15:07:00");
      Assertions.assertThat(actualIntervalRespBody.getElapsedTime()).isEqualTo("00:30:00");
    }

    @Test
    @DisplayName("create must throws ActivityNotFoundException when activityId doesn't exist")
    void create_MustThrowsActivityNotFoundException_WhenActivityIdDoesntExist() {
      IntervalRequestBody validIntervalRequestBody = new IntervalRequestBody(
          LocalDateTime.of(2022, 8, 16, 15, 7, 0),
          LocalTime.of(0,30,0));

      Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
          .isThrownBy(() -> intervalService.create(
              NONEXISTENT_ACTIVITY_ID, validIntervalRequestBody))
          .withMessage("Activity not found for id: "+NONEXISTENT_ACTIVITY_ID);
    }

  }

  @Nested
  @DisplayName("tests for find method")
  class FindMethod {

    @Test
    @DisplayName("find must returns Page<IntervalResponseBody> when ActivityId exists")
    void find_MustReturnsPageIntervalResponseBody_WhenActivityIdExists(){
      Page<IntervalResponseBody> intervalsPage = intervalService.find(EXISTENT_ACTIVITY_ID, DEFAULT_PAGEABLE);

      IntervalResponseBody expectedIntervalRespBody = IntervalResponseBodyCreator
          .withIdAndStartedAtAndElapsedTime(100L, "2022-08-16T15:07:00", "00:30:00");

      Assertions.assertThat(intervalsPage).hasSize(1);
      Assertions.assertThat(intervalsPage.getContent()).contains(expectedIntervalRespBody);
    }

    @Test
    @DisplayName("find must returns empty Page<IntervalResponseBody> when Activity doesn't have Intervals")
    void find_MustReturnsEmptyPageIntervalResponseBody_WhenActivityDoesntHaveIntervals(){
      BDDMockito.when(intervalRepositoryMock.findByActivity(DEFAULT_ACTIVITY, DEFAULT_PAGEABLE))
          .thenReturn(Page.empty(DEFAULT_PAGEABLE));

      Page<IntervalResponseBody> intervalsPage = intervalService.find(EXISTENT_ACTIVITY_ID, DEFAULT_PAGEABLE);

      Assertions.assertThat(intervalsPage).isEmpty();
    }

    @Test
    @DisplayName("find must throws ActivityNotFoundException when activityId doesn't exist")
    void create_MustThrowsActivityNotFoundException_WhenActivityIdDoesntExist() {
      Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
          .isThrownBy(() -> intervalService.find(
              NONEXISTENT_ACTIVITY_ID, DEFAULT_PAGEABLE))
          .withMessage("Activity not found for id: "+NONEXISTENT_ACTIVITY_ID);
    }

  }

  @Nested
  @DisplayName("tests for delete method")
  class DeleteMethod {

    @Test
    @DisplayName("delete must throws ActivityNotFoundException when activityId doesn't exist")
    void delete_MustThrowsActivityNotFoundException_WhenActivityIdDoenstExist(){
      Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
          .isThrownBy(() -> intervalService.delete(
              NONEXISTENT_ACTIVITY_ID, EXISTENT_INTERVAL_ID))
          .withMessage("Activity not found for id: "+NONEXISTENT_ACTIVITY_ID);
    }

    @Test
    @DisplayName("delete must throws IntervalNotFoundException when intervalId doesn't exist")
    void delete_MustThrowsIntervalNotFoundException_WhenIntervalIdDoenstExist(){
      Assertions.assertThatExceptionOfType(IntervalNotFoundException.class)
          .isThrownBy(() -> intervalService.delete(
              EXISTENT_ACTIVITY_ID, NONEXISTENT_INTERVAL_ID))
          .withMessage("Interval not found for id: "+NONEXISTENT_INTERVAL_ID);
    }

  }

}