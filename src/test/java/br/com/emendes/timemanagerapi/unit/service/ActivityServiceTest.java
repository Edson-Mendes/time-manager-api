package br.com.emendes.timemanagerapi.unit.service;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
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
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for ActivityService")
class ActivityServiceTest {

  @InjectMocks
  private ActivityService activityService;
  @Mock
  private ActivityRepository activityRepositoryMock;

  private final ActivityRequestBody ACTIVITY_REQUEST_BODY =
      new ActivityRequestBody("Finances API", "A simple project for my portfolio");
  private final Pageable PAGEABLE = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
  private final long NONEXISTING_ACTIVITY_ID = 9999L;

  @BeforeEach
  public void setUp() {
    Activity activity1 = ActivityCreator.activityWithIdAndName(1L, "Finances API");
    Activity activity2 = ActivityCreator.activityWithIdAndName(2L, "Transaction Analyzer");
    Page<Activity> activitiesPage = new PageImpl(List.of(activity1, activity2), PAGEABLE, 2);

    BDDMockito.when(activityRepositoryMock.findAll(PAGEABLE)).thenReturn(activitiesPage);
    BDDMockito.when(activityRepositoryMock.save(ArgumentMatchers.any(Activity.class))).thenReturn(activity1);
    BDDMockito.when(activityRepositoryMock.findById(NONEXISTING_ACTIVITY_ID)).thenReturn(Optional.empty());
  }

  @Test
  @DisplayName("find must returns Page<ActivityResponseBody> when DB has Activities")
  void find_MustReturnsPageActivityResponseBody_WhenDBHasActivities() {
    Page<ActivityResponseBody> activitiesResponse = activityService.find(PAGEABLE);
    List<ActivityResponseBody> listActivityRB = activitiesResponse.getContent();

    Assertions.assertThat(activitiesResponse)
        .isNotEmpty()
        .hasSize(2);
    Assertions.assertThat(listActivityRB.get(0).getId()).isEqualTo(1L);
    Assertions.assertThat(listActivityRB.get(1).getId()).isEqualTo(2L);
    Assertions.assertThat(listActivityRB.get(0).getName()).isEqualTo("Finances API");
    Assertions.assertThat(listActivityRB.get(1).getName()).isEqualTo("Transaction Analyzer");
  }

  @Test
  @DisplayName("find must throws ActivitiesNotFoundException when DB hasn't activities")
  void find_MustThrowsActivitiesNotFoundException_WhenDBHasntActivities() {
    BDDMockito.when(activityRepositoryMock.findAll(PAGEABLE)).thenReturn(Page.empty());

    Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
        .isThrownBy(() -> activityService.find(PAGEABLE))
        .withMessage("Não possui atividades");
  }

  @Test
  @DisplayName("findById must returns Activity when found successful")
  void findById_MustReturnsActivity_WhenFoundSuccessful(){
    long activityId = 1L;
    Activity activityToBeFound = ActivityCreator.activityWithIdAndName(activityId, "Finances API");
    BDDMockito.when(activityRepositoryMock.findById(activityId)).thenReturn(Optional.of(activityToBeFound));

    Activity activityFound = activityService.findById(activityId);

    Assertions.assertThat(activityFound).isNotNull().isEqualTo(activityToBeFound);
  }

  @Test
  @DisplayName("findById must throws ActivityNotFoundException when activityId doesn't exist")
  void findById_MustThrowsActivityNotFoundException_WhenActivityIdDoesntExist(){
    Activity activityToBeFound = ActivityCreator.activityWithIdAndName(NONEXISTING_ACTIVITY_ID, "Finances API");
    BDDMockito.when(activityRepositoryMock.findById(NONEXISTING_ACTIVITY_ID)).thenReturn(Optional.empty());

    Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
        .isThrownBy(() -> activityService.findById(NONEXISTING_ACTIVITY_ID))
        .withMessage("Activity not found for id: " + NONEXISTING_ACTIVITY_ID);
  }

  @Test
  @DisplayName("create must returns ActivityResponseBody when created successful")
  void create_MustReturnsActivityResponseBody_WhenCreatedSuccessful() {
    ActivityResponseBody activityResponseBody = activityService.create(ACTIVITY_REQUEST_BODY);

    Assertions.assertThat(activityResponseBody.getId())
        .isNotNull()
        .isEqualTo(1L);
    Assertions.assertThat(activityResponseBody.getName())
        .isNotNull()
        .isEqualTo("Finances API");
  }

  @Test
  @DisplayName("update must throws ActivityNotFoundException when id don't exists")
  void update_MustThrowsActivityNotFoundException_WhenIdDontExists() {
    String newName = "Finances REST API";
    String newDescription = "A simple Restful API for my portfolio";
    ActivityRequestBody activityToBeUpdated = new ActivityRequestBody(newName, newDescription);

    Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
        .isThrownBy(() -> activityService.update(NONEXISTING_ACTIVITY_ID, activityToBeUpdated))
        .withMessage("Activity not found for id: " + NONEXISTING_ACTIVITY_ID);
  }

  @Test
  @DisplayName("delete must throws ActivityNotFoundException when id don't exists")
  void deleteById_MustThrowsActivityNotFoundException_WhenIdDontExists() {
    Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
        .isThrownBy(() -> activityService.deleteById(NONEXISTING_ACTIVITY_ID))
        .withMessage("Activity not found for id: " + NONEXISTING_ACTIVITY_ID);
  }

}