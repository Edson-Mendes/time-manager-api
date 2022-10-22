package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.dto.request.SignupRequest;
import br.com.emendes.timemanagerapi.dto.response.UserResponse;
import br.com.emendes.timemanagerapi.model.entity.Role;
import br.com.emendes.timemanagerapi.model.entity.User;
import br.com.emendes.timemanagerapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper mapper;

  public UserDetails findByUsername(String username) {
//    TODO: Essa busca faz dois selects, um para buscar o usuário pelo email e outro para buscar as roles do usuário
//    pensar em como realizar com apenas um select.
    return userRepository.findByEmail(username).orElseThrow(() -> {
      throw new UsernameNotFoundException("User not found");
    });
  }

  public UserDetails findUserDetailsById(long id) {
    return userRepository.findById(id).orElseThrow(() -> {
      throw new UsernameNotFoundException("User not found");
    });
  }

//  TODO: Criar um handler p/ duplicate key value violates unique constraint "tb_user_email_unique"
  public UserResponse save(SignupRequest signupRequest){
    User user = mapper.map(signupRequest, User.class);
    user.addRole(getRole());
    user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
    user.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    user = userRepository.save(user);
    return mapper.map(user, UserResponse.class);
  }

//  TODO: Exportar para um RoleService e buscar ROLE_USER no DB, pois não há garantia de que o id seja 1.
  private Role getRole(){
    return new Role(1, "ROLE_USER");
  }

}
