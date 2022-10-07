package br.com.emendes.timemanagerapi.util.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
@Lazy
@Profile("test")
public class TestRestTemplateConfig {

  @Bean(name = "withPatch")
  public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
    RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
        .requestFactory(HttpComponentsClientHttpRequestFactory.class)
        .rootUri("http://localhost:"+port);
    return new TestRestTemplate(restTemplateBuilder);
  }

}
