package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequestBody;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class IntervalRequestBodyCreator {

  public static IntervalRequestBody validIntervalRequest(){
    return new IntervalRequestBody(
        LocalDateTime.of(2022, 8, 16, 15, 7, 0),
        LocalTime.of(0,30,0));
  }

}
