package br.com.emendes.timemanagerapi.unit.service;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequestBody;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponseBody;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
import br.com.emendes.timemanagerapi.repository.IntervalRepository;
import br.com.emendes.timemanagerapi.service.ActivityService;
import br.com.emendes.timemanagerapi.service.IntervalService;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
import br.com.emendes.timemanagerapi.util.creator.IntervalCreator;
import br.com.emendes.timemanagerapi.util.creator.IntervalRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for IntervalService")
class IntervalServiceTest {

  @InjectMocks
  private IntervalService intervalService;
  @Mock
  private ActivityService activityServiceMock;
  @Mock
  private IntervalRepository intervalRepositoryMock;

  private final long ACTIVITY_ID = 1L;
  private final long NONEXISTENT_ACTIVITY_ID = 9999L;

  @BeforeEach
  void setUp(){
    BDDMockito.when(activityServiceMock.findById(ACTIVITY_ID))
        .thenReturn(ActivityCreator.activityWithIdAndName(ACTIVITY_ID, "Lorem Ipsum Activity"));
    BDDMockito.when(intervalRepositoryMock.save(IntervalCreator.intervalToBeSavedTest()))
        .thenReturn(IntervalCreator.intervalSavedTest());
    BDDMockito.willThrow(new ActivityNotFoundException("Activity not found for id: "+NONEXISTENT_ACTIVITY_ID))
        .given(activityServiceMock).findById(NONEXISTENT_ACTIVITY_ID);
  }

  @Test
  @DisplayName("create must returns IntervalResponseBody when create successfully")
  void create_MustReturnsIntervalResponseBody_WhenCreateSuccessfully(){
    IntervalResponseBody intervalRespBody = intervalService.create(
        ACTIVITY_ID, IntervalRequestBodyCreator.validIntervalRequest());

    Assertions.assertThat(intervalRespBody).isNotNull();
    Assertions.assertThat(intervalRespBody.getId()).isEqualTo(100L);
    Assertions.assertThat(intervalRespBody.getStartedAt()).isEqualTo("2022-08-16T15:07:00");
    Assertions.assertThat(intervalRespBody.getElapsedTime()).isEqualTo("00:30:00");
  }

  @Test
  @DisplayName("create must throws ActivityNotFoundException when activityId doesn't exist")
  void create_MustThrowsActivityNotFoundException_WhenActivityIdDoesntExist() {
    IntervalRequestBody validIntervalRequestBody = IntervalRequestBodyCreator.validIntervalRequest();
    Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
        .isThrownBy(() -> intervalService.create(
            NONEXISTENT_ACTIVITY_ID, validIntervalRequestBody))
        .withMessage("Activity not found for id: "+NONEXISTENT_ACTIVITY_ID);
  }

}