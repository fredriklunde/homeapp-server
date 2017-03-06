package com.bernerus.homeapp.controller.http;

import com.bernerus.homeapp.config.HttpClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**
 * Created by andreas on 21/02/17.
 */
public abstract class HttpClient {
  private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

  protected HttpClientConfig httpClientConfig;
  protected String httpPrefix;
  protected RestTemplate restTemplate;

  public HttpClient(HttpClientConfig clientConfig) {
    this.httpClientConfig = clientConfig;
    this.restTemplate = new RestTemplate();
    httpPrefix = "http://" + httpClientConfig.getHost() + ":" + httpClientConfig.getPort();
  }

  protected abstract HttpHeaders getHeaders();

  protected <T> HttpEntity<T> createRequest() {
    return new HttpEntity<>(getHeaders());
  }

  protected <T> HttpEntity<T> get(String url, Class<T> clazz) {
    try {
      return restTemplate.exchange(httpPrefix + url, HttpMethod.GET, createRequest(), clazz);
    } catch (Exception e) {
      LOG.error("Kass", e);
      return null;
    }
  }

}
