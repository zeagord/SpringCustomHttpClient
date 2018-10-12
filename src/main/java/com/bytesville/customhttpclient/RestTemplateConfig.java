package com.bytesville.customhttpclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/***
 * RestTemplate to use the Apache HttpClient rather than JDK one. This one will set only Max Connection and Max per Route
 * For advanced Operation, refer AdvancedRestTemplate
 */
@Configuration
public class RestTemplateConfig {

  @Autowired @Qualifier("apacheRestTemplate")
  ClientHttpRequestFactory customRequestFactory;

  @Autowired @Qualifier("apacheSpringCommonsRestTemplate")
  ClientHttpRequestFactory apacheHttpRequestFactory;

  @Autowired @Qualifier("OKSpringCommonsRestTemplate")
  ClientHttpRequestFactory okHttpRequestFactory;

  @Bean
  @Qualifier("defaultRestTemplate")
  @Primary
  public RestTemplate defaultRestTemplate(){
    return new RestTemplate();
  }

  @Bean
  @Qualifier("apacheRestTemplate")
  public RestTemplate createCustomRestTemplate(){
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(customRequestFactory);
    return restTemplate;
  }

  @Bean
  @Qualifier("apacheSpringCommonsRestTemplate")
  public RestTemplate createApacheCustomRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(apacheHttpRequestFactory);
    return restTemplate;
  }

  @Bean
  @Qualifier("OKSpringCommonsRestTemplate")
  public RestTemplate createOKCustomRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(okHttpRequestFactory);
    return restTemplate;
  }
}
