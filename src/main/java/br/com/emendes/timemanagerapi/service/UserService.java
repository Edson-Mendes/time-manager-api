package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;

  public UserDetails findByUsername(String username) {
//    TODO: Essa busca faz dois selects, um para buscar o usuário pelo email e outro para buscar as roles do usuário
//    pensar em como realizar com apenas um select.
    return userRepository.findByEmail(username).orElseThrow(() -> {
      throw new UsernameNotFoundException("Usuário não encontrado!");
    });
  }

  public UserDetails findUserDetailsById(long id) {
    return userRepository.findById(id).orElseThrow(() -> {
      throw new UsernameNotFoundException("Usuário não encontrado!");
    });
  }
}
