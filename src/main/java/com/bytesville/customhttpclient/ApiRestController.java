package com.bytesville.customhttpclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiRestController {

  @Autowired(required = false) @Qualifier("defaultRestTemplate")
  RestTemplate restTemplate;

  @Autowired(required = false) @Qualifier("apacheRestTemplate")
  RestTemplate apacheRestTemplate;

  @Autowired(required = false) @Qualifier("apacheSpringCommonsRestTemplate")
  RestTemplate apacheSpringCommonsRestTemplate;

  @Autowired(required = false) @Qualifier("OKSpringCommonsRestTemplate")
  RestTemplate okRestTemplate;

  @GetMapping("/default")
  public String getDefault() {
    return restTemplate.getForObject("http://httpbin.org/anything", String.class);
  }

  @GetMapping("/apache")
  public String getApache() {
    return apacheRestTemplate.getForObject("http://httpbin.org/anything", String.class);
  }

  @GetMapping("/apachespring")
  public String getApacheSpring() {
    return apacheSpringCommonsRestTemplate.getForObject("http://httpbin.org/anything", String.class);
  }
  @GetMapping("/ok")
  public String getOk() {
    return okRestTemplate.getForObject("http://httpbin.org/anything", String.class);
  }

}
