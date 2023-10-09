package br.com.emendes.timemanagerapi.unit.mapper;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponse;
import br.com.emendes.timemanagerapi.mapper.impl.IntervalMapperImpl;
import br.com.emendes.timemanagerapi.model.entity.Interval;
import br.com.emendes.timemanagerapi.util.creator.ActivityCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Unit tests for IntervalMapperImpl")
class IntervalMapperImplTest {

  private IntervalMapperImpl intervalMapper;

  @BeforeEach
  void setUp() {
    intervalMapper = new IntervalMapperImpl();
  }

  @Test
  @DisplayName("toInterval must return Interval when map successfully")
  void toInterval_MustReturnInterval_WhenMapSuccessfully() {
    IntervalRequest intervalRequest = IntervalRequest.builder()
        .startedAt("2023-10-09T10:00:00")
        .elapsedTime("00:30:00")
        .build();

    Interval actualInterval = intervalMapper.toInterval(intervalRequest);

    assertThat(actualInterval).isNotNull();
    assertThat(actualInterval.getStartedAt()).isNotNull().isEqualTo("2023-10-09T10:00:00");
    assertThat(actualInterval.getElapsedTime()).isNotNull().isEqualTo("00:30:00");
    assertThat(actualInterval.getId()).isNull();
  }

  @Test
  @DisplayName("toInterval must thrown IllegalArgumentException when intervalRequest is null")
  void toInterval_MustThrownIllegalArgumentException_WhenIntervalRequestIsNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> intervalMapper.toInterval(null))
        .withMessage("intervalRequest must not be null");
  }

  @Test
  @DisplayName("toIntervalResponse must return IntervalResponse when map successfully")
  void toIntervalResponse_MustReturnIntervalResponse_WhenMapSuccessfully() {
    Interval interval = Interval.builder()
        .id(100_000_000L)
        .startedAt(LocalDateTime.parse("2023-10-09T10:00:00"))
        .elapsedTime(LocalTime.parse("00:30:00"))
        .activity(ActivityCreator.withIdAndName(1L, "Lorem Ipsum"))
        .build();

    IntervalResponse actualIntervalResponse = intervalMapper.toIntervalResponse(interval);

    assertThat(actualIntervalResponse).isNotNull();
    assertThat(actualIntervalResponse.getId()).isEqualTo(100_000_000L);
    assertThat(actualIntervalResponse.getStartedAt()).isNotNull().isEqualTo("2023-10-09T10:00:00");
    assertThat(actualIntervalResponse.getElapsedTime()).isNotNull().isEqualTo("00:30:00");
  }

  @Test
  @DisplayName("toIntervalResponse must thrown IllegalArgumentException when interval is null")
  void toIntervalResponse_MustThrownIllegalArgumentException_WhenIntervalIsNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> intervalMapper.toIntervalResponse(null))
        .withMessage("interval must not be null");
  }

}