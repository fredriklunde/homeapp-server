package com.bernerus.homeapp.controller.http;

import com.bernerus.homeapp.config.HttpClientConfig;
import com.bernerus.homeapp.model.RGBColor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;

/**
 * Created by andreas on 21/02/17.
 */
public class RazberryDimmerHttpClient extends RazberryHttpClient {
  private static final Logger LOG = LoggerFactory.getLogger(RazberryDimmerHttpClient.class);
  private static final String DIMMER_SUFFIX = "-0-38";

  public RazberryDimmerHttpClient(HttpClientConfig clientConfig, String deviceId) {
    super(clientConfig, deviceId);
  }

  public void setWhite(int value) {
    String white = String.valueOf(value);
    String url = "/ZAutomation/api/v1/devices/" + deviceId + DIMMER_SUFFIX + "/command/exact?level=" + white;
    LOG.info(url);
    HttpEntity<String> response = get(url, String.class);
    logResponse(response);
  }

  private void logResponse(HttpEntity<String> response) {
    try {
      LOG.info(response.getBody());
    } catch (Exception e) {
      LOG.error("Kass", e);
    }
  }
}
