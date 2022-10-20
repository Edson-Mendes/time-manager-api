package br.com.emendes.timemanagerapi.integration;

import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.dto.response.detail.ExceptionDetails;
import br.com.emendes.timemanagerapi.dto.response.detail.ValidationExceptionDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for /signin")
class SigninControllerIT {


  @Autowired
  @Qualifier(value = "withPatch")
  private TestRestTemplate testRestTemplate;
  private final String SIGNIN_URI = "/signin";

  @Test
  @DisplayName("post for /signin must returns status 200 when signin successfully")
  void postForSignin_MustReturnsStatus200_WhenSigninSuccessfully() {
    HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(new LoginRequest("user@email.com", "1234"));

    ResponseEntity<TokenResponse> response =
        testRestTemplate.exchange(SIGNIN_URI, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("post for /signin must returs TokenResponse when signin successfully")
  void postForSignin_MustReturnsTokenResponse_WhenSigninSuccessfully() {
    HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(new LoginRequest("user@email.com", "1234"));

    ResponseEntity<TokenResponse> response =
        testRestTemplate.exchange(SIGNIN_URI, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
        });

    TokenResponse actualBody = response.getBody();
    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getType()).isNotNull().isEqualTo("Bearer");
    Assertions.assertThat(actualBody.getToken()).isNotBlank();
  }

  @Test
  @DisplayName("post for /signin must returns status 400 when email and password are invalid")
  void postForSignin_MustReturnsStatus400_WhenEmailAndPasswordAreInvalid() {
    HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(new LoginRequest("useremailcom", ""));
    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplate.exchange(
        SIGNIN_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("post for /signin must returns ValidationExceptionDetails when email and password are invalid")
  void postForSignin_MustReturnsValidationExceptionDetails_WhenEmailAndPasswordAreInvalid() {
    HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(new LoginRequest("useremailcom", ""));
    ResponseEntity<ValidationExceptionDetails> responseEntity = testRestTemplate.exchange(
        SIGNIN_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    ValidationExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualBody.getFields()).contains("email");
    Assertions.assertThat(actualBody.getFields()).contains("password");
    Assertions.assertThat(actualBody.getMessages()).contains("must be a well-formed email address");
    Assertions.assertThat(actualBody.getMessages()).contains("password must not be null or blank");
  }

  @Test
  @DisplayName("post for /signin must returns status 400 when email or password are bad credentials")
  void postForSignin_MustReturnsStatus400_WhenEmailOrPasswordAreBadCredentials() {
    HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(new LoginRequest("user@email.com", "123456"));
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        SIGNIN_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    HttpStatus actualStatus = responseEntity.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("post for /signin must returns ExceptionDetails when email or password are bad credentials")
  void postForSignin_MustReturnsValidationExceptionDetails_WhenEmailOrPasswordAreBadCredentials() {
    HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(new LoginRequest("user@email.com", "123456"));
    ResponseEntity<ExceptionDetails> responseEntity = testRestTemplate.exchange(
        SIGNIN_URI, HttpMethod.POST,
        requestEntity, new ParameterizedTypeReference<>() {
        });

    ExceptionDetails actualBody = responseEntity.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Bad credentials");
    Assertions.assertThat(actualBody.getStatus()).isEqualTo(400);
  }

}
