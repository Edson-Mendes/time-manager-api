package br.com.emendes.timemanagerapi.unit.service;

import br.com.emendes.timemanagerapi.config.security.service.TokenService;
import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.service.SigninService;
import br.com.emendes.timemanagerapi.util.creator.TokenResponseCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for SigninService")
class SigninServiceTest {

  @InjectMocks
  private SigninService signinService;
  @Mock
  private TokenService tokenServiceMock;
  @Mock
  private AuthenticationManager authManagerMock;

  @BeforeEach
  public void setUp(){
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken("user@email.com", "123456");

    BDDMockito.when(authManagerMock.authenticate(usernamePasswordAuthenticationToken))
            .thenReturn(usernamePasswordAuthenticationToken);
    BDDMockito.when(tokenServiceMock.generateToken(ArgumentMatchers.any(Authentication.class)))
        .thenReturn(TokenResponseCreator.validTokenResponse().getToken());

    BDDMockito.willThrow(new BadCredentialsException("Bad credentials"))
        .given(authManagerMock)
        .authenticate(new UsernamePasswordAuthenticationToken("user@email.com", "1234"));
  }

  @Test
  @DisplayName("signin must returns TokenResponse when signin successfully")
  void signin_MustReturnsTokenResponse_WhenSigninSuccessfully(){
    TokenResponse actualTokenResponse = signinService.signin(new LoginRequest("user@email.com", "123456"));

    Assertions.assertThat(actualTokenResponse)
        .isNotNull()
        .isEqualTo(new TokenResponse("Bearer", "quehiqwueoiqwe017ye0h180e827he1072he871h2e08h12e81"));
  }

  @Test
  @DisplayName("signin must throws AuthenticationException when signin fails")
  void signin_MustThrowsAuthenticationException_WhenSigninFails(){
    LoginRequest loginRequest = new LoginRequest("user@email.com", "1234");

    Assertions.assertThatExceptionOfType(BadCredentialsException.class)
        .isThrownBy(() -> signinService.signin(loginRequest))
        .withMessage("Bad credentials");
  }

}
