package br.com.emendes.timemanagerapi.unit.controller;

import br.com.emendes.timemanagerapi.controller.SigninController;
import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.service.SigninService;
import br.com.emendes.timemanagerapi.util.creator.LoginRequestCreator;
import br.com.emendes.timemanagerapi.util.creator.TokenResponseCreator;
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

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for SigninController")
class SigninControllerTest {

  @InjectMocks
  private SigninController signinController;

  @Mock
  private SigninService signinServiceMock;

  @BeforeEach
  void setUp() {
    BDDMockito.when(signinServiceMock.signin(LoginRequestCreator.validLoginRequest()))
        .thenReturn(TokenResponseCreator.validTokenResponse());
  }

  @Test
  @DisplayName("signin must returns status 200 when signin successfully")
  void signin_MustReturnsStatus200_WhenSigninSuccessfully() {
    LoginRequest loginRequest = new LoginRequest("user@email.com", "123456");

    ResponseEntity<TokenResponse> response = signinController.signin(loginRequest);
    HttpStatus actualStatusCode = response.getStatusCode();

    Assertions.assertThat(actualStatusCode).isEqualByComparingTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("signin must returns TokenResponse when signin successfully")
  void signin_MustReturnsTokenResponse_WhenSigninSuccessfully() {
    LoginRequest loginRequest = new LoginRequest("user@email.com", "123456");

    ResponseEntity<TokenResponse> response = signinController.signin(loginRequest);
    TokenResponse actualBody = response.getBody();

    Assertions.assertThat(actualBody)
        .isNotNull()
        .isEqualTo(new TokenResponse("Bearer", "quehiqwueoiqwe017ye0h180e827he1072he871h2e08h12e81"));
  }

}
