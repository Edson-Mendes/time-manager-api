package br.com.emendes.timemanagerapi.unit.service;

import br.com.emendes.timemanagerapi.dto.request.SignupRequest;
import br.com.emendes.timemanagerapi.dto.response.UserResponse;
import br.com.emendes.timemanagerapi.model.entity.User;
import br.com.emendes.timemanagerapi.repository.UserRepository;
import br.com.emendes.timemanagerapi.service.impl.UserServiceImpl;
import br.com.emendes.timemanagerapi.util.creator.UserCreator;
import br.com.emendes.timemanagerapi.util.creator.UserResponseCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for UserService")
class UserServiceTest {

  @InjectMocks
  private UserServiceImpl userService;
  @Mock
  private UserRepository userRepositoryMock;
  @Mock
  private ModelMapper mapperMock;
  @Mock
  private PasswordEncoder encoderMock;

  @BeforeEach
  void setUp() {
    User user = UserCreator.withAllParameters();

    BDDMockito.when(userRepositoryMock.save(UserCreator.withoutId())).thenReturn(user);
    BDDMockito.when(encoderMock.encode("123456")).thenReturn("123456");
    BDDMockito.when(mapperMock
            .map(new SignupRequest("sql/user", "user@email.com", "123456", "123456"), User.class))
        .thenReturn(UserCreator.withNameAndEmail("sql/user", "user@email.com"));
    BDDMockito.when(mapperMock.map(user, UserResponse.class))
        .thenReturn(UserResponseCreator.withNameAndEmail("sql/user", "user@email.com"));
    BDDMockito.when(userRepositoryMock.findByEmail("user@email.com")).thenReturn(Optional.of(user));
    BDDMockito.willThrow(new UsernameNotFoundException("User not found"))
        .given(userRepositoryMock).findByEmail("nonexistent@email.com");
    BDDMockito.when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(user));
    BDDMockito.willThrow(new UsernameNotFoundException("User not found"))
        .given(userRepositoryMock).findById(10000L);
  }

  @Test
  @DisplayName("save must returns UserResponse when save successfully")
  void save_MustReturnsUserResponse_WhenSaveSuccessfully() {
    SignupRequest signupRequest = new SignupRequest("sql/user", "user@email.com", "123456", "123456");

    UserResponse actualUserResponse = userService.save(signupRequest);

    Assertions.assertThat(actualUserResponse).isNotNull();
    Assertions.assertThat(actualUserResponse.getName()).isEqualTo("sql/user");
    Assertions.assertThat(actualUserResponse.getEmail()).isEqualTo("user@email.com");
  }

  @Test
  @DisplayName("findByUsername must returns UserDetails when found successfully")
  void findByUsername_MustReturnsUserDetails_WhenFoundSuccessfully() {
    UserDetails actualUserDetails = userService.findByUsername("user@email.com");

    Assertions.assertThat(actualUserDetails).isNotNull();
    Assertions.assertThat(actualUserDetails.getUsername()).isEqualTo("user@email.com");
    Assertions.assertThat(actualUserDetails.getAuthorities()).hasSize(1);
  }

  @Test
  @DisplayName("findByUsername must throws UsernameNotFoundException when not found email")
  void findByUsername_MustThrowsUsernameNotFoundException_WhenNotFoundEmail() {
    Assertions.assertThatExceptionOfType(UsernameNotFoundException.class)
        .isThrownBy(() -> userService.findByUsername("nonexistent@email.com"))
        .withMessage("User not found");
  }

  @Test
  @DisplayName("findUserDetailsById must returns UserDetails when found successfully")
  void findUserDetailsById_MustReturnsUserDetails_WhenFoundSuccessfully() {
    UserDetails actualUserDetails = userService.findUserDetailsById(1L);

    Assertions.assertThat(actualUserDetails).isNotNull();
    Assertions.assertThat(actualUserDetails.getUsername()).isEqualTo("user@email.com");
    Assertions.assertThat(actualUserDetails.getAuthorities()).hasSize(1);
  }

  @Test
  @DisplayName("findUserDetailsById must throws UsernameNotFoundException when not found email")
  void findUserDetailsById_MustThrowsUsernameNotFoundException_WhenNotFoundById() {
    Assertions.assertThatExceptionOfType(UsernameNotFoundException.class)
        .isThrownBy(() -> userService.findUserDetailsById(10000L))
        .withMessage("User not found");
  }

}
