package br.com.emendes.timemanagerapi.unit.service;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequestBody;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponseBody;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
import br.com.emendes.timemanagerapi.model.Interval;
import br.com.emendes.timemanagerapi.repository.IntervalRepository;
import br.com.emendes.timemanagerapi.service.ActivityService;
import br.com.emendes.timemanagerapi.service.IntervalService;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;

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

  @BeforeEach
  void setUp(){
    Interval intervalToBeSaved = Interval.builder()
        .activity(ActivityCreator.withIdAndName(EXISTENT_ACTIVITY_ID, "Lorem Ipsum Activity"))
        .startedAt(LocalDateTime.parse("2022-08-16T15:07:00"))
        .elapsedTime(LocalTime.parse("00:30:00"))
        .build();

    Interval intervalSaved = Interval.builder()
        .id(100L)
        .startedAt(LocalDateTime.parse("2022-08-16T15:07:00"))
        .elapsedTime(LocalTime.parse("00:30:00"))
        .activity(ActivityCreator.withIdAndName(EXISTENT_ACTIVITY_ID, "Lorem Ipsum Activity"))
        .build();

    BDDMockito.when(activityServiceMock.findById(EXISTENT_ACTIVITY_ID))
        .thenReturn(ActivityCreator.withIdAndName(EXISTENT_ACTIVITY_ID, "Lorem Ipsum Activity"));

    BDDMockito
        .when(intervalRepositoryMock
            .save(intervalToBeSaved))
        .thenReturn(intervalSaved);

    BDDMockito.willThrow(new ActivityNotFoundException("Activity not found for id: "+NONEXISTENT_ACTIVITY_ID))
        .given(activityServiceMock).findById(NONEXISTENT_ACTIVITY_ID);
  }

  @Nested
  @DisplayName("tests for create method")
  class CreateMethod {

    @Test
    @DisplayName("create must returns IntervalResponseBody when create successfully")
    void create_MustReturnsIntervalResponseBody_WhenCreateSuccessfully(){
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

}