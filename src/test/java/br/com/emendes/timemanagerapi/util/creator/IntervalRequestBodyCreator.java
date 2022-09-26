package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class IntervalRequestBodyCreator {

  public static IntervalRequest validIntervalRequest(){
    return new IntervalRequest(
        "2022-08-16T15:07:00", "00:30:00");
  }

}
