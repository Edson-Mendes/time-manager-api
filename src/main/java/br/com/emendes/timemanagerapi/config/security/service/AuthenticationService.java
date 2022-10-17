package br.com.emendes.timemanagerapi.config.security.service;

import br.com.emendes.timemanagerapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService implements UserDetailsService {

  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) {
    return userService.findByUsername(username);
  }

  public UserDetails findUserDetailsById(long id){
    return userService.findUserDetailsById(id);
  }

}
