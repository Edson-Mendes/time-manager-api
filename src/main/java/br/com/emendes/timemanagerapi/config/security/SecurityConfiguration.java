package br.com.emendes.timemanagerapi.config.security;

import br.com.emendes.timemanagerapi.model.entity.Role;
import br.com.emendes.timemanagerapi.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@Profile({"dev"})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final UserDetailsService authenticationService;

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    Set<Role> roles = new HashSet<>();
//    roles.add(new Role(1, "ROLE_USER"));
//    User user = User.builder()
//        .id(100L)
//        .email("user@email.com")
//        .password("$2a$10$yT3KTVbQsHJTmPQSMEzhkeWQh4mgJlPbBG.dpY9Cp3mlLyQ246F6a")
//        .name("User")
//        .roles(roles)
//        .build();
//
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    auth.inMemoryAuthentication()
//        .withUser(user)
//        .passwordEncoder(passwordEncoder);

    auth.userDetailsService(authenticationService).passwordEncoder(passwordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers(HttpMethod.GET, "/activities", "/activities/**").permitAll()
        .antMatchers(HttpMethod.POST, "/signin").permitAll()
        .antMatchers(HttpMethod.POST, "/activities", "/activities/*/intervals").permitAll()
        .antMatchers(HttpMethod.PUT, "/activities/*", "/activities/*/intervals/*").permitAll()
        .antMatchers(HttpMethod.DELETE, "/activities/*", "/activities/*/intervals/*").permitAll()
        .anyRequest().authenticated()
        .and().csrf().disable();
  }
}
