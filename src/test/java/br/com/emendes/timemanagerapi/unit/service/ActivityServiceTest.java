package br.com.emendes.timemanagerapi.unit.service;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.dto.request.UpdateStatusRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
import br.com.emendes.timemanagerapi.service.ActivityService;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
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

  private final ActivityRequestBody VALID_ACTIVITY_REQUEST_BODY =
      new ActivityRequestBody("Lorem Ipsum Activity", "A simple project for my portfolio");
//  FIXME: DEFAULT_PAGEABLE não está de acordo com o real pageable default.
  private final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
  private final long NONEXISTENT_ACTIVITY_ID = 9999L;

  //  Mocks de métodos/actions de activityServiceMock
  @BeforeEach
  public void setUp() {
    List<Activity> activities = List.of(
        ActivityCreator.withIdAndName(1L, "Lorem Ipsum Activity"),
        ActivityCreator.withIdAndName(2L, "XPTO Activity"));
    Page<Activity> activitiesPage = new PageImpl<>(activities, DEFAULT_PAGEABLE, 2);

    BDDMockito.when(activityRepositoryMock.findByStatusIsNot(DEFAULT_PAGEABLE, Status.DELETED)).thenReturn(activitiesPage);

    BDDMockito.when(activityRepositoryMock.save(ArgumentMatchers.any(Activity.class)))
        .thenReturn(ActivityCreator
            .withIdNameAndDescription(1L, "Lorem Ipsum Activity", "A simple project for my portfolio"));

    BDDMockito.when(activityRepositoryMock.findById(NONEXISTENT_ACTIVITY_ID)).thenReturn(Optional.empty());
  }

  @Nested
  @DisplayName("tests for find method")
  class FindMethod {

    @Test
    @DisplayName("find must returns Page<ActivityResponseBody> when DB has Activities")
    void find_MustReturnsPageActivityResponseBody_WhenDBHasActivities() {
      Page<ActivityResponseBody> actualActivitiesResponse = activityService.find(DEFAULT_PAGEABLE);
      List<ActivityResponseBody> actualListActivityResponse = actualActivitiesResponse.getContent();

      Assertions.assertThat(actualActivitiesResponse)
          .isNotEmpty()
          .hasSize(2);
      Assertions.assertThat(actualListActivityResponse)
          .contains(ActivityResponseBodyCreator.withIdAndName(1L, "Lorem Ipsum Activity"))
          .contains(ActivityResponseBodyCreator.withIdAndName(2L, "XPTO Activity"));
    }

    @Test
    @DisplayName("find must throws ActivitiesNotFoundException when DB hasn't activities")
    void find_MustThrowsActivitiesNotFoundException_WhenDBHasntActivities() {
      BDDMockito.when(activityRepositoryMock.findByStatusIsNot(DEFAULT_PAGEABLE, Status.DELETED))
          .thenReturn(Page.empty());

      Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
          .isThrownBy(() -> activityService.find(DEFAULT_PAGEABLE))
          .withMessage("Não possui atividades");
    }

  }

  @Nested
  @DisplayName("tests for findById method")
  class FindByIdMethod {

    @Test
    @DisplayName("findById must returns Activity when found successful")
    void findById_MustReturnsActivity_WhenFoundSuccessful(){
      Activity activityToBeFound = ActivityCreator.withIdAndName(1L, "Lorem Ipsum Activity");
      BDDMockito.when(activityRepositoryMock.findById(1L)).thenReturn(Optional.of(activityToBeFound));

      Activity actualActivityFound = activityService.findById(1L);
      Activity expectedActivityFound = ActivityCreator.withIdAndName(1L, "Lorem Ipsum Activity");

      Assertions.assertThat(actualActivityFound).isNotNull().isEqualTo(expectedActivityFound);
    }

    @Test
    @DisplayName("findById must throws ActivityNotFoundException when activityId doesn't exist")
    void findById_MustThrowsActivityNotFoundException_WhenActivityIdDoesntExist(){
      BDDMockito.when(activityRepositoryMock.findById(NONEXISTENT_ACTIVITY_ID)).thenReturn(Optional.empty());

      Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
          .isThrownBy(() -> activityService.findById(NONEXISTENT_ACTIVITY_ID))
          .withMessage("Activity not found for id: " + NONEXISTENT_ACTIVITY_ID);
    }

  }

  @Nested
  @DisplayName("tests for create method")
  class CreateMethod {

    @Test
    @DisplayName("create must returns ActivityResponseBody when created successful")
    void create_MustReturnsActivityResponseBody_WhenCreatedSuccessful() {
      ActivityResponseBody actualActivityResponseBody = activityService.create(VALID_ACTIVITY_REQUEST_BODY);

      Assertions.assertThat(actualActivityResponseBody.getId())
          .isEqualTo(1L);
      Assertions.assertThat(actualActivityResponseBody.getName())
          .isNotNull()
          .isEqualTo("Lorem Ipsum Activity");
    }

  }

  @Nested
  @DisplayName("tests for update method")
  class UpdateMethod {

    @Test
    @DisplayName("update must throws ActivityNotFoundException when id doesn't exist")
    void update_MustThrowsActivityNotFoundException_WhenIdDoesntExist() {
      ActivityRequestBody activityToBeUpdated =
          new ActivityRequestBody("Finances REST API", "A simple Restful API for my portfolio");

      Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
          .isThrownBy(() -> activityService.update(NONEXISTENT_ACTIVITY_ID, activityToBeUpdated))
          .withMessage("Activity not found for id: " + NONEXISTENT_ACTIVITY_ID);
    }

  }

  @Nested
  @DisplayName("tests for deleteActivityById method")
  class DeleteActivityByIdMethod {

    @Test
    @DisplayName("deleteActivityById must throws ActivityNotFoundException when id doesn't exist")
    void deleteActivityById_MustThrowsActivityNotFoundException_WhenIdDoesntExist() {
      Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
          .isThrownBy(() -> activityService.deleteActivityById(NONEXISTENT_ACTIVITY_ID))
          .withMessage("Activity not found for id: " + NONEXISTENT_ACTIVITY_ID);
    }

  }

  @Nested
  @DisplayName("tests for updateStatusById method")
  class UpdateStatusByIdMethod {

    @Test
    @DisplayName("updateStatusById must throws ActivityNotFoundException when id doesn't exist")
    void updateStatusById_MustThrowsActivityNotFoundException_WhenIdDoesntExist() {
      UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("CONCLUDED");

      Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
          .isThrownBy(() -> activityService.updateStatusById(NONEXISTENT_ACTIVITY_ID, updateStatusRequest))
          .withMessage("Activity not found for id: " + NONEXISTENT_ACTIVITY_ID);
    }

  }

}