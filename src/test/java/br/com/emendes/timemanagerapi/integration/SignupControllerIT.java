package br.com.emendes.timemanagerapi.integration;

import br.com.emendes.timemanagerapi.dto.request.SignupRequest;
import br.com.emendes.timemanagerapi.dto.response.UserResponse;
import br.com.emendes.timemanagerapi.dto.response.detail.ValidationExceptionDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("Integration tests for /signup")
class SignupControllerIT {

  @Autowired
  private TestRestTemplate testRestTemplate;

  private final String SIGNUP_URI = "/signup";
  private final String VALID_NAME = "New User";
  private final String VALID_EMAIL = "newuser@email.com";
  private final String VALID_PASSWORD = "123456";
  private final String VALID_CONFIRM = VALID_PASSWORD;

  @Test
  @DisplayName("post /signup must return status 201 when signup successfully")
  void postSignup_MustReturnsStatus201_WhenSignupSuccessfully() {
    SignupRequest signupRequest = new SignupRequest("New User", "newuser@email.com", "123456", "123456");
    HttpEntity<SignupRequest> httpEntity = new HttpEntity<>(signupRequest);

    ResponseEntity<UserResponse> response = testRestTemplate
        .exchange(SIGNUP_URI, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.CREATED);
  }

  @Test
  @DisplayName("post /signup must return UserResponse when signup successfully")
  void postSignup_MustReturnsUserResponse_WhenSignupSuccessfully() {
    SignupRequest signupRequest = new SignupRequest("New User", "newuser@email.com", "123456", "123456");
    HttpEntity<SignupRequest> httpEntity = new HttpEntity<>(signupRequest);

    ResponseEntity<UserResponse> response = testRestTemplate
        .exchange(SIGNUP_URI, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

    UserResponse actualBody = response.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getName()).isEqualTo("New User");
    Assertions.assertThat(actualBody.getEmail()).isEqualTo("newuser@email.com");
  }

  @Test
  @DisplayName("post /signup must returns status 400 when name is invalid")
  void postSignup_MustReturnsStatus400_WhenNameIsInvalid() {
    SignupRequest signupRequest = new SignupRequest("", VALID_EMAIL, VALID_PASSWORD, VALID_CONFIRM);
    HttpEntity<SignupRequest> httpEntity = new HttpEntity<>(signupRequest);

    ResponseEntity<UserResponse> response = testRestTemplate
        .exchange(SIGNUP_URI, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   "})
  @DisplayName("post /signup must returns ValidationExceptionDetails when name is invalid")
  void postSignup_MustReturnsValidationExceptionDetails_WhenNameIsInvalid(String name) {
    SignupRequest signupRequest = new SignupRequest(name, VALID_EMAIL, VALID_PASSWORD, VALID_CONFIRM);
    HttpEntity<SignupRequest> httpEntity = new HttpEntity<>(signupRequest);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate
        .exchange(SIGNUP_URI, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

    ValidationExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getFields()).contains("name");
    Assertions.assertThat(actualBody.getMessages()).contains("name must not be null or blank");
  }

  @Test
  @DisplayName("post /signup must returns status 400 when email is invalid")
  void postSignup_MustReturnsStatus400_WhenEmailIsInvalid() {
    SignupRequest signupRequest = new SignupRequest(VALID_NAME, "useremail.com", VALID_PASSWORD, VALID_CONFIRM);
    HttpEntity<SignupRequest> httpEntity = new HttpEntity<>(signupRequest);

    ResponseEntity<UserResponse> response = testRestTemplate
        .exchange(SIGNUP_URI, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   "})
  @DisplayName("post /signup must returns ValidationExceptionDetails when email is blank")
  void postSignup_MustReturnsValidationExceptionDetails_WhenEmailIsBlank(String email) {
    SignupRequest signupRequest = new SignupRequest(VALID_NAME, email, VALID_PASSWORD, VALID_CONFIRM);
    HttpEntity<SignupRequest> httpEntity = new HttpEntity<>(signupRequest);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate
        .exchange(SIGNUP_URI, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

    ValidationExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getFields()).contains("email");
    Assertions.assertThat(actualBody.getMessages()).contains("email must not be null or blank");
  }

  @ParameterizedTest
  @ValueSource(strings = {"sql/user", "useremail.com", "user@"})
  @DisplayName("post /signup must returns ValidationExceptionDetails when email is invalid")
  void postSignup_MustReturnsValidationExceptionDetails_WhenEmailIsInvalid(String email) {
    SignupRequest signupRequest = new SignupRequest(VALID_NAME, email, VALID_PASSWORD, VALID_CONFIRM);
    HttpEntity<SignupRequest> httpEntity = new HttpEntity<>(signupRequest);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate
        .exchange(SIGNUP_URI, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

    ValidationExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getFields()).contains("email");
    Assertions.assertThat(actualBody.getMessages()).contains("must be a well-formed email address");
  }

  @Test
  @DisplayName("post /signup must returns status 400 when password is invalid")
  void postSignup_MustReturnsStatus400_WhenPasswordIsInvalid() {
    SignupRequest signupRequest = new SignupRequest(VALID_NAME, VALID_EMAIL, "", VALID_CONFIRM);
    HttpEntity<SignupRequest> httpEntity = new HttpEntity<>(signupRequest);

    ResponseEntity<UserResponse> response = testRestTemplate
        .exchange(SIGNUP_URI, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   "})
  @DisplayName("post /signup must returns ValidationExceptionDetails when password is invalid")
  void postSignup_MustReturnsValidationExceptionDetails_WhenPasswordIsInvalid(String password) {
    SignupRequest signupRequest = new SignupRequest(VALID_NAME, VALID_EMAIL, password, VALID_CONFIRM);
    HttpEntity<SignupRequest> httpEntity = new HttpEntity<>(signupRequest);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate
        .exchange(SIGNUP_URI, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

    ValidationExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getFields()).contains("password");
    Assertions.assertThat(actualBody.getMessages()).contains("password must not be null or blank");
  }

  @Test
  @DisplayName("post /signup must returns status 400 when confirm is invalid")
  void postSignup_MustReturnsStatus400_WhenConfirmIsInvalid() {
    SignupRequest signupRequest = new SignupRequest("", VALID_EMAIL, VALID_PASSWORD, VALID_CONFIRM);
    HttpEntity<SignupRequest> httpEntity = new HttpEntity<>(signupRequest);

    ResponseEntity<UserResponse> response = testRestTemplate
        .exchange(SIGNUP_URI, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   "})
  @DisplayName("post /signup must returns ValidationExceptionDetails when confirm is invalid")
  void postSignup_MustReturnsValidationExceptionDetails_WhenConfirmIsInvalid(String confirm) {
    SignupRequest signupRequest = new SignupRequest(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, confirm);
    HttpEntity<SignupRequest> httpEntity = new HttpEntity<>(signupRequest);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate
        .exchange(SIGNUP_URI, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

    ValidationExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
    Assertions.assertThat(actualBody.getFields()).contains("confirm");
    Assertions.assertThat(actualBody.getMessages()).contains("confirm must not be null or blank");
  }

  @Test
  @DisplayName("post /signup must returns status 400 when password do not match")
  void postSignup_MustReturnsStatus400_WhenPasswordDoNotMatch(){
    SignupRequest signupRequest = new SignupRequest(VALID_NAME, VALID_EMAIL, "123456", "12345");
    HttpEntity<SignupRequest> httpEntity = new HttpEntity<>(signupRequest);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate
        .exchange(SIGNUP_URI, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

    HttpStatus actualStatus = response.getStatusCode();

    Assertions.assertThat(actualStatus).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("post /signup must returns ValidationExceptionDetails when password do not match")
  void postSignup_MustReturnsValidationExceptionDetails_WhenPasswordDoNotMatch(){
    SignupRequest signupRequest = new SignupRequest(VALID_NAME, VALID_EMAIL, "123456", "12345");
    HttpEntity<SignupRequest> httpEntity = new HttpEntity<>(signupRequest);

    ResponseEntity<ValidationExceptionDetails> response = testRestTemplate
        .exchange(SIGNUP_URI, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

    ValidationExceptionDetails actualBody = response.getBody();

    Assertions.assertThat(actualBody).isNotNull();
    Assertions.assertThat(actualBody.getTitle()).isEqualTo("Bad Request");
    Assertions.assertThat(actualBody.getDetails()).isEqualTo("Invalid field(s)");
  }

}
