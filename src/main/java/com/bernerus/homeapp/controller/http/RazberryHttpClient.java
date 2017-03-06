package com.bernerus.homeapp.controller.http;

import com.bernerus.homeapp.config.HttpClientConfig;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * Created by andreas on 21/02/17.
 */
public class RazberryHttpClient extends HttpClient {

  private static final Logger LOG = LoggerFactory.getLogger(RazberryHttpClient.class);

  protected final String deviceId; // = "11-0-51-rgb";

  private HttpHeaders razberryHeaders;


  public RazberryHttpClient(HttpClientConfig clientConfig, String deviceId) {
    super(clientConfig);
    this.deviceId = deviceId;

    String plainCreds = httpClientConfig.getUsername() + ":" + httpClientConfig.getPassword();
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);

    razberryHeaders = new HttpHeaders();
    razberryHeaders.add("Authorization", "Basic " + base64Creds);
    razberryHeaders.add("Accept", "application/json");
    razberryHeaders.add("User-Agent", "java");
  }

  @Override
  protected HttpHeaders getHeaders() {
    return razberryHeaders;
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
