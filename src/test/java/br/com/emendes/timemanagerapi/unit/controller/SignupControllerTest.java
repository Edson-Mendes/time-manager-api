package br.com.emendes.timemanagerapi.unit.controller;

import br.com.emendes.timemanagerapi.controller.SignupController;
import br.com.emendes.timemanagerapi.dto.request.SignupRequest;
import br.com.emendes.timemanagerapi.dto.response.UserResponse;
import br.com.emendes.timemanagerapi.service.UserService;
import br.com.emendes.timemanagerapi.util.creator.SignupRequestCreator;
import br.com.emendes.timemanagerapi.util.creator.UserResponseCreator;
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
@DisplayName("Unit tests for SignupController")
class SignupControllerTest {

  @InjectMocks
  private SignupController signupController;
  @Mock
  private UserService userServiceMock;

  private final UriComponentsBuilder URI_BUILDER = UriComponentsBuilder.fromHttpUrl("http://localhost:8080");

  @BeforeEach
  void setUp() {
    BDDMockito.when(userServiceMock
            .save(SignupRequestCreator.withNameEmailAndPassword("sql/user", "user@email.com", "123456")))
        .thenReturn(UserResponseCreator.withNameAndEmail("sql/user", "user@email.com"));
  }

  @Test
  @DisplayName("signup must returns status 201 when signup successfully")
  void signup_MustReturnsStatus201_WhenSignupSuccessfully() {
    SignupRequest signupRequest = new SignupRequest("sql/user", "user@email.com", "123456", "123456");

    ResponseEntity<UserResponse> response = signupController.signup(signupRequest, URI_BUILDER);
    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.CREATED);
  }

  @Test
  @DisplayName("signup must returns UserResponse when signup successfully")
  void signup_MustReturnsUserResponse_WhenSignupSuccessfully() {
    SignupRequest signupRequest = new SignupRequest("sql/user", "user@email.com", "123456", "123456");

    ResponseEntity<UserResponse> response = signupController.signup(signupRequest, URI_BUILDER);
    UserResponse actualBody = response.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getName()).isEqualTo("sql/user");
    Assertions.assertThat(actualBody.getEmail()).isEqualTo("user@email.com");
  }

}
