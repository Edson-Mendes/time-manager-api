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

public interface UserService {

  UserDetails findByUsername(String username);

  UserDetails findUserDetailsById(long id);

  UserResponse save(SignupRequest signupRequest);

}
