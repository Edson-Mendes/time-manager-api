package br.com.emendes.timemanagerapi.unit.service;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.exception.ActivitiesNotFoundException;
import br.com.emendes.timemanagerapi.model.Activity;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for ActivityService")
class ActivityServiceTest {

  @InjectMocks
  private ActivityService activityService;

  @Mock
  private ActivityRepository activityRepository;
  private final ActivityRequestBody ACTIVITY_REQUEST_BODY =
      new ActivityRequestBody("Finances API", "A simple project for my portfolio");

  @BeforeEach
  public void setUp() {
    Activity activity1 = ActivityCreator.activityWithIdAndName(1L, "Finances API");
    Activity activity2 = ActivityCreator.activityWithIdAndName(2L, "Transaction Analyzer");

    BDDMockito.when(activityRepository.findAll()).thenReturn(List.of(activity1, activity2));
    BDDMockito.when(activityRepository.save(ArgumentMatchers.any(Activity.class))).thenReturn(activity1);
  }

  @Test
  @DisplayName("findAll must returns List<ActivityResponse> when DB has Activities")
  void findAll_MustReturnsListActivityResponseBody_WhenDBHasActivities() {
    List<ActivityResponseBody> activitiesResponse = activityService.findAll();

    Assertions.assertThat(activitiesResponse)
        .isNotEmpty()
        .hasSize(2);
    Assertions.assertThat(activitiesResponse.get(0).getId()).isEqualTo(1L);
    Assertions.assertThat(activitiesResponse.get(1).getId()).isEqualTo(2L);
    Assertions.assertThat(activitiesResponse.get(0).getName()).isEqualTo("Finances API");
    Assertions.assertThat(activitiesResponse.get(1).getName()).isEqualTo("Transaction Analyzer");
  }

  @Test
  @DisplayName("findAll must throws ActivitiesNotFoundException when DB hasn't activities")
  void findAll_MustThrowsActivitiesNotFoundException_WhenDBHasntActivities(){
    BDDMockito.when(activityRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

    Assertions.assertThatExceptionOfType(ActivitiesNotFoundException.class)
        .isThrownBy(activityService::findAll)
        .withMessage("Não possui atividades");
  }

  @Test
  @DisplayName("create must returns ActivityResponseBody when created successful")
  void create_MustReturnsActivityResponseBody_WhenCreatedSuccessful(){
    ActivityResponseBody activityResponseBody = activityService.create(ACTIVITY_REQUEST_BODY);

    Assertions.assertThat(activityResponseBody.getId())
        .isNotNull()
        .isEqualTo(1L);
    Assertions.assertThat(activityResponseBody.getName())
        .isNotNull()
        .isEqualTo("Finances API");
  }
}