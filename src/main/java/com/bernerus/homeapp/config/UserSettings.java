package com.bernerus.homeapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by andreas on 2017-02-27.
 */
public class UserSettings {
  private static final Logger LOG = LoggerFactory.getLogger(UserSettings.class);

  private final HttpClientConfig mirrorHttpClientConfig;
  private final HttpClientConfig razberryHttpClientConfig;
  private String morningLightsCron;

  public UserSettings(HttpClientConfig mirrorHttpClientConfig, HttpClientConfig razberryHttpClientConfig) {
    this.mirrorHttpClientConfig = mirrorHttpClientConfig;
    this.razberryHttpClientConfig = razberryHttpClientConfig;
  }

  public HttpClientConfig getMirrorHttpClientConfig() {
    return mirrorHttpClientConfig;
  }

  public HttpClientConfig getRazberryHttpClientConfig() {
    return razberryHttpClientConfig;
  }

  public String getMorningLightsCron() {
    return morningLightsCron;
  }
}
