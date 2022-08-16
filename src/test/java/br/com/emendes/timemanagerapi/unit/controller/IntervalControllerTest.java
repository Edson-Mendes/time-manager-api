package br.com.emendes.timemanagerapi.unit.controller;

import br.com.emendes.timemanagerapi.controller.IntervalController;
import br.com.emendes.timemanagerapi.dto.request.IntervalRequestBody;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponseBody;
import br.com.emendes.timemanagerapi.service.IntervalService;
import br.com.emendes.timemanagerapi.util.creator.IntervalRequestBodyCreator;
import br.com.emendes.timemanagerapi.util.creator.IntervalResponseBodyCreator;
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
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for /activities/{id}/intervals")
class IntervalControllerTest {

  @InjectMocks
  private IntervalController intervalController;
  @Mock
  private IntervalService intervalServiceMock;

  private final UriComponentsBuilder URI_BUILDER = UriComponentsBuilder.fromHttpUrl("http://localhost:8080");

  @BeforeEach
  public void setUp(){
    BDDMockito.when(intervalServiceMock.create(1L, IntervalRequestBodyCreator.validIntervalRequest()))
        .thenReturn(IntervalResponseBodyCreator.intervalRBForTests());
  }

  @Test
  @DisplayName("create must returns status 201 when created successful")
  void create_MustReturnsStatus201_WhenCreatedSuccessful() {
    IntervalRequestBody requestBody = IntervalRequestBodyCreator.validIntervalRequest();
    ResponseEntity<IntervalResponseBody> response = intervalController.create(1L, requestBody, URI_BUILDER);

    HttpStatus statusCode = response.getStatusCode();
    IntervalResponseBody responseBody = response.getBody();

    Assertions.assertThat(statusCode).isEqualByComparingTo(HttpStatus.CREATED);
    Assertions.assertThat(responseBody).isNotNull();
    Assertions.assertThat(responseBody.getId()).isEqualTo(100L);
    Assertions.assertThat(responseBody.getStartedAt()).isEqualTo("2022-08-16T15:07:00");
    Assertions.assertThat(responseBody.getElapsedTime()).isEqualTo("00:30:00");
  }
}