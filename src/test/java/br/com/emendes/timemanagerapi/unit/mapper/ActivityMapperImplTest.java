package br.com.emendes.timemanagerapi.unit.mapper;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.mapper.impl.ActivityMapperImpl;
import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static br.com.emendes.timemanagerapi.util.creator.IntervalCreator.intervals;
import static br.com.emendes.timemanagerapi.util.creator.UserCreator.withAllParameters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Unit tests for ActivityMapperImpl")
class ActivityMapperImplTest {

  private ActivityMapperImpl activityMapper;

  @BeforeEach
  void setUp() {
    activityMapper = new ActivityMapperImpl();
  }

  @Test
  @DisplayName("toActivity must return Activity when map successfully")
  void toActivity_MustReturnActivity_WhenMapSuccessfully() {
    ActivityRequest activityRequest = ActivityRequest.builder()
        .name("Activity xpto")
        .description("Description xpto")
        .build();

    Activity actualActivity = activityMapper.toActivity(activityRequest);

    assertThat(actualActivity).isNotNull();
    assertThat(actualActivity.getName()).isNotNull().isEqualTo("Activity xpto");
    assertThat(actualActivity.getDescription()).isNotNull().isEqualTo("Description xpto");
    assertThat(actualActivity.getId()).isNull();
    assertThat(actualActivity.getCreatedAt()).isNull();
    assertThat(actualActivity.getStatus()).isNull();
  }

  @Test
  @DisplayName("toActivity must thrown IllegalArgumentException when activityRequest is null")
  void toActivity_MustThrownIllegalArgumentException_WhenActivityRequestIsNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> activityMapper.toActivity(null))
        .withMessage("activityRequest must not be null");
  }

  @Test
  @DisplayName("toActivityResponse must return ActivityResponse when map successfully")
  void toActivityResponse_MustReturnActivityResponse_WhenMapSuccessfully() {
    Activity activity = Activity.builder()
        .id(100_000L)
        .name("Activity xpto")
        .description("Description xpto")
        .status(Status.ACTIVE)
        .createdAt(LocalDateTime.parse("2023-10-06T13:40:00"))
        .intervals(intervals())
        .user(withAllParameters())
        .build();

    ActivityResponse actualActivityResponse = activityMapper.toActivityResponse(activity);

    assertThat(actualActivityResponse).isNotNull();
    assertThat(actualActivityResponse.getId()).isEqualTo(100_000L);
    assertThat(actualActivityResponse.getName()).isNotNull().isEqualTo("Activity xpto");
    assertThat(actualActivityResponse.getDescription()).isNotNull().isEqualTo("Description xpto");
    assertThat(actualActivityResponse.getCreatedAt()).isNotNull().isEqualTo("2023-10-06T13:40:00");
    assertThat(actualActivityResponse.getStatus()).isNotNull().isEqualTo(Status.ACTIVE);
  }

  @Test
  @DisplayName("toActivityResponse must thrown IllegalArgumentException when activity is null")
  void toActivityResponse_MustThrownIllegalArgumentException_WhenActivityIsNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> activityMapper.toActivityResponse(null))
        .withMessage("activity must not be null");
  }

  @Test
  @DisplayName("merge must modify Activity name and description when merge successfully")
  void merge_MustModifyActivityNameAndDescription_WhenMergeSuccessfully() {
    ActivityRequest activityRequest = ActivityRequest.builder()
        .name("Activity xpto updated")
        .description("Description xpto updated")
        .build();

    Activity activity = Activity.builder()
        .id(100_000L)
        .name("Activity xpto")
        .description("Description xpto")
        .status(Status.ACTIVE)
        .createdAt(LocalDateTime.parse("2023-10-06T13:40:00"))
        .intervals(intervals())
        .user(withAllParameters())
        .build();

    activityMapper.merge(activityRequest, activity);

    assertThat(activity).isNotNull();
    assertThat(activity.getName()).isNotNull().isEqualTo("Activity xpto updated");
    assertThat(activity.getDescription()).isNotNull().isEqualTo("Description xpto updated");
  }

  @Test
  @DisplayName("merge must thrown IllegalArgumentException when activity is null")
  void merge_MustThrownIllegalArgumentException_WhenActivityIsNull() {
    ActivityRequest activityRequest = ActivityRequest.builder()
        .name("Activity xpto updated")
        .description("Description xpto updated")
        .build();

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> activityMapper.merge(activityRequest, null))
        .withMessage("activity must not be null");
  }

  @Test
  @DisplayName("merge must thrown IllegalArgumentException when activityRequest is null")
  void merge_MustThrownIllegalArgumentException_WhenActivityRequestIsNull() {
    Activity activity = Activity.builder()
        .id(100_000L)
        .name("Activity xpto")
        .description("Description xpto")
        .status(Status.ACTIVE)
        .createdAt(LocalDateTime.parse("2023-10-06T13:40:00"))
        .intervals(intervals())
        .user(withAllParameters())
        .build();

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> activityMapper.merge(null, activity))
        .withMessage("activityRequest must not be null");
  }

}