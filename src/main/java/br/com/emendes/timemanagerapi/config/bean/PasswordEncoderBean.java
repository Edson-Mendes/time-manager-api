package br.com.emendes.timemanagerapi.config.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderBean {

  @Bean
  public PasswordEncoder bCrypt(){
    return new BCryptPasswordEncoder();
  }

}
