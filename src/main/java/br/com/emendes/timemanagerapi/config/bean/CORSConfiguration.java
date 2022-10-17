package br.com.emendes.timemanagerapi.config.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfiguration {

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/activities").allowedOrigins("http://127.0.0.1:5500")
            .allowedMethods("GET", "POST");
        registry.addMapping("/activities/*").allowedOrigins("http://127.0.0.1:5500")
            .allowedMethods("DELETE", "PUT");
      }
    };
  }

}
