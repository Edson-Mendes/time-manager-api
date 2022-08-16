package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.dto.response.IntervalResponseBody;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class IntervalResponseBodyCreator {

  public static IntervalResponseBody intervalRBForTests() {
    return new IntervalResponseBody(100L,
        LocalDateTime.of(2022, 8, 16, 15, 7, 0),
        LocalTime.of(0, 30, 0));
  }

}
