package br.com.emendes.timemanagerapi.unit.controller;

import br.com.emendes.timemanagerapi.controller.ActivityController;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.service.ActivityService;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for ActivityController")
class ActivityControllerTest {

  @InjectMocks
  private ActivityController activityController;

  @Mock
  private ActivityService activityService;

  @BeforeEach
  public void setUp() {
    ActivityResponseBody activityRB1 =
        new ActivityResponseBody(ActivityCreator.activityWithIdAndName(1L, "Finances API"));
    ActivityResponseBody activityRB2 =
        new ActivityResponseBody(ActivityCreator.activityWithIdAndName(2L, "Transaction Analyzer"));

    BDDMockito.when(activityService.findAll()).thenReturn(List.of(activityRB1, activityRB2));
  }

  @Test
  @DisplayName("findAll must returns status 200 when successful")
  void findAll_MustReturnsStatus200_WhenSuccessful() {
    ResponseEntity<List<ActivityResponseBody>> response = activityController.findAll();
    List<ActivityResponseBody> body = response.getBody();

    Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    Assertions.assertThat(body)
        .isNotEmpty()
        .hasSize(2);
    Assertions.assertThat(body.get(0).getId()).isEqualTo(1L);
    Assertions.assertThat(body.get(1).getId()).isEqualTo(2L);
    Assertions.assertThat(body.get(0).getName()).isEqualTo("Finances API");
    Assertions.assertThat(body.get(1).getName()).isEqualTo("Transaction Analyzer");
  }
@Test
  @DisplayName("findAll must returns List<ActivityResponseBody> when successful")
  void findAll_MustReturnsListActivityResponseBody_WhenSuccessful() {
    List<ActivityResponseBody> body = activityController.findAll().getBody();

    Assertions.assertThat(body.get(0).getId()).isEqualTo(1L);
    Assertions.assertThat(body.get(1).getId()).isEqualTo(2L);
    Assertions.assertThat(body.get(0).getName()).isEqualTo("Finances API");
    Assertions.assertThat(body.get(1).getName()).isEqualTo("Transaction Analyzer");
  }

}