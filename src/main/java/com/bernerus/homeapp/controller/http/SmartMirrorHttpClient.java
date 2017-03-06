package com.bernerus.homeapp.controller.http;

import com.bernerus.homeapp.config.HttpClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

/**
 * Created by andreas on 21/02/17.
 */
public class SmartMirrorHttpClient extends HttpClient {
  private static final Logger LOG = LoggerFactory.getLogger(SmartMirrorHttpClient.class);
  private final HttpHeaders mirrorHeaders;

  public SmartMirrorHttpClient(HttpClientConfig clientConfig) {
    super(clientConfig);

    mirrorHeaders = new HttpHeaders();
    mirrorHeaders.add("Accept", "text/plain");
    mirrorHeaders.add("User-Agent", "java");
  }

  @Override
  protected HttpHeaders getHeaders() {
    return null;
  }

  public void reportMovement() {

    String url = "/reportmovement";

    HttpEntity<String> response = get(url, String.class);

    try {
      LOG.info(response.getBody());
    } catch (Exception e) {
      LOG.error("Kass", e);
    }
  }


}
