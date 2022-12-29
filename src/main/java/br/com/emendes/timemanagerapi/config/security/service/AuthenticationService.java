package br.com.emendes.timemanagerapi.config.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService extends UserDetailsService {

  UserDetails findUserDetailsById(long id);

}
