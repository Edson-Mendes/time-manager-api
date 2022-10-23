package br.com.emendes.timemanagerapi.unit.service;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequest;
import br.com.emendes.timemanagerapi.dto.request.UpdateStatusRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.model.entity.User;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
import br.com.emendes.timemanagerapi.service.ActivityService;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
import br.com.emendes.timemanagerapi.util.creator.ActivityResponseBodyCreator;
import br.com.emendes.timemanagerapi.util.creator.PageableCreator;
import br.com.emendes.timemanagerapi.util.creator.UserCreator;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for ActivityService")
class ActivityServiceTest {

  @InjectMocks
  private ActivityService activityService;
  @Mock
  private ActivityRepository activityRepositoryMock;

  private final ActivityRequest VALID_ACTIVITY_REQUEST_BODY =
      new ActivityRequest("Lorem Ipsum Activity", "A simple project for my portfolio");
  private final Pageable DEFAULT_PAGEABLE = PageableCreator.activityDefaultPageable();
  private final long EXISTENT_ACTIVITY_ID = 1000L;
  private final long NONEXISTENT_ACTIVITY_ID = 9999L;
  private final User USER = UserCreator.withAllParameters();

  //  Mocks de métodos/actions de activityServiceMock
  @BeforeEach
  public void setUp() {
    List<Activity> activities = List.of(
        ActivityCreator.withIdAndName(1L, "Lorem Ipsum Activity"),
        ActivityCreator.withIdAndName(2L, "XPTO Activity"));

    Page<Activity> activitiesPage = new PageImpl<>(activities, DEFAULT_PAGEABLE, 2);

    BDDMockito.when(activityRepositoryMock.findByUserAndStatusIsNot(DEFAULT_PAGEABLE, USER, Status.DELETED)).thenReturn(activitiesPage);

    BDDMockito.when(activityRepositoryMock.save(ArgumentMatchers.any(Activity.class)))
        .thenReturn(ActivityCreator
            .withIdNameAndDescription(1L, "Lorem Ipsum Activity", "A simple project for my portfolio"));

    BDDMockito.when(activityRepositoryMock.findByIdAndUser(NONEXISTENT_ACTIVITY_ID, USER)).thenReturn(Optional.empty());

    BDDMockito.when(activityRepositoryMock.findByIdAndUser(EXISTENT_ACTIVITY_ID, USER))
        .thenReturn(Optional.of(ActivityCreator.withIdAndStatus(EXISTENT_ACTIVITY_ID, Status.DELETED)));

    mockSecurityContextHolder();
  }

  private void mockSecurityContextHolder() {
    final Authentication authentication = mock(Authentication.class);
    final SecurityContext securityContext = mock(SecurityContext.class);

    SecurityContextHolder.setContext(securityContext);
    BDDMockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    BDDMockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
        .thenReturn(USER);
  }

  @Nested
  @DisplayName("tests for find method")
  class FindMethod {

    @Test
    @DisplayName("find must returns Page<ActivityResponseBody> when DB has Activities")
    void find_MustReturnsPageActivityResponseBody_WhenDBHasActivities() {
      Page<ActivityResponse> actualActivitiesResponse = activityService.find(DEFAULT_PAGEABLE);
      List<ActivityResponse> actualListActivityResponse = actualActivitiesResponse.getContent();

      Assertions.assertThat(actualActivitiesResponse)
          .isNotEmpty()
          .hasSize(2);
      Assertions.assertThat(actualListActivityResponse)
          .contains(ActivityResponseBodyCreator.withIdAndName(1L, "Lorem Ipsum Activity"))
          .contains(ActivityResponseBodyCreator.withIdAndName(2L, "XPTO Activity"));
    }

    @Test
    @DisplayName("find must returns empty Page when User hasn't activities")
    void find_MustReturnsEmptyPage_WhenUserHasntActivities() {
      BDDMockito.when(activityRepositoryMock.findByUserAndStatusIsNot(DEFAULT_PAGEABLE, USER, Status.DELETED))
          .thenReturn(Page.empty());

      Page<ActivityResponse> actualPage = activityService.find(DEFAULT_PAGEABLE);

      Assertions.assertThat(actualPage).isEmpty();
    }

  }

  @Nested
  @DisplayName("tests for findById method")
  class FindByIdMethod {

    @Test
    @DisplayName("findById must returns Activity when found successful")
    void findById_MustReturnsActivity_WhenFoundSuccessful() {
      Activity activityToBeFound = ActivityCreator.withIdAndName(1L, "Lorem Ipsum Activity");
      BDDMockito.when(activityRepositoryMock.findByIdAndUser(1L, USER)).thenReturn(Optional.of(activityToBeFound));

      Activity actualActivityFound = activityService.findById(1L);
      Activity expectedActivityFound = ActivityCreator.withIdAndName(1L, "Lorem Ipsum Activity");

      Assertions.assertThat(actualActivityFound).isNotNull().isEqualTo(expectedActivityFound);
    }

    @Test
    @DisplayName("findById must throws ActivityNotFoundException when activityId doesn't exist")
    void findById_MustThrowsActivityNotFoundException_WhenActivityIdDoesntExist() {
      BDDMockito.when(activityRepositoryMock.findByIdAndUser(NONEXISTENT_ACTIVITY_ID, USER)).thenReturn(Optional.empty());

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
      ActivityResponse actualActivityResponse = activityService.create(VALID_ACTIVITY_REQUEST_BODY);

      Assertions.assertThat(actualActivityResponse.getId())
          .isEqualTo(1L);
      Assertions.assertThat(actualActivityResponse.getName())
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
      ActivityRequest activityToBeUpdated =
          new ActivityRequest("Finances REST API", "A simple Restful API for my portfolio");

      Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
          .isThrownBy(() -> activityService.update(NONEXISTENT_ACTIVITY_ID, activityToBeUpdated))
          .withMessage("Activity not found for id: " + NONEXISTENT_ACTIVITY_ID);
    }

    @Test
    @DisplayName("update must throws ActivityNotFoundException when activity status is deleted")
    void update_MustThrowsActivityNotFoundException_WhenActivityStatusIsDeleted() {
      long ACTIVITY_ID_DELETED = 99999L;
      BDDMockito.when(activityRepositoryMock.findById(ACTIVITY_ID_DELETED))
          .thenReturn(Optional.of(ActivityCreator.withIdAndStatus(99999L, Status.DELETED)));
      ActivityRequest activityToBeUpdated =
          new ActivityRequest("Finances REST API", "A simple Restful API for my portfolio");

      Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
          .isThrownBy(() -> activityService.update(ACTIVITY_ID_DELETED, activityToBeUpdated))
          .withMessage("Activity not found for id: " + ACTIVITY_ID_DELETED);
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

    @Test
    @DisplayName("updateStatusById must throws ActivityNotFoundException when activity status is deleted")
    void updateStatusById_MustThrowsActivityNotFoundException_WhenActivityStatusIsDeleted() {
      UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest("CONCLUDED");

      Assertions.assertThatExceptionOfType(ActivityNotFoundException.class)
          .isThrownBy(() -> activityService.updateStatusById(EXISTENT_ACTIVITY_ID, updateStatusRequest))
          .withMessage("Activity not found for id: " + EXISTENT_ACTIVITY_ID);
    }

  }

}