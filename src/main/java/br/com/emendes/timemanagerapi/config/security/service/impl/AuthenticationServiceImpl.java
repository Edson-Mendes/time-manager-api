package br.com.emendes.timemanagerapi.config.security.service.impl;

import br.com.emendes.timemanagerapi.config.security.service.AuthenticationService;
import br.com.emendes.timemanagerapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) {
    return userService.findByUsername(username);
  }

  @Override
  public UserDetails findUserDetailsById(long id) {
    return userService.findUserDetailsById(id);
  }

}
