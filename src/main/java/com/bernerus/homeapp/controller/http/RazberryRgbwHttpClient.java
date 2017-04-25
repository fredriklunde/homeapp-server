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
public class RazberryRgbwHttpClient extends RazberryHttpClient {
  private static final Logger LOG = LoggerFactory.getLogger(RazberryRgbwHttpClient.class);
  private static final String DIMMER_SUFFIX = "-0-38";
  private static final String WHITE_SUFFIX = "-0-51-0";
  private static final String RGB_SUFFIX = "-0-51-rgb";

  public RazberryRgbwHttpClient(HttpClientConfig clientConfig, String deviceId) {
    super(clientConfig, deviceId);
  }

  public void setColor(RGBColor color) {
    String red = String.valueOf(color.getRed());
    String green = String.valueOf(color.getGreen());
    String blue = String.valueOf(color.getBlue());
    String url = "/ZAutomation/api/v1/devices/" + deviceId + RGB_SUFFIX + "/command/exact?red=" + red + "&green=" + green + "&blue=" + blue;

    LOG.info(url);

    HttpEntity<String> response = get(url, String.class);

    logResponse(response);
  }

  public void setWhite(int white) {
    String url = "/ZAutomation/api/v1/devices/" + deviceId + WHITE_SUFFIX + "/command/exact?level=" + String.valueOf(white);
    LOG.info(url);
    HttpEntity<String> response = get(url, String.class);
    logResponse(response);
  }

  public void setDimmer(int dimmer) {
    String url = "/ZAutomation/api/v1/devices/" + deviceId + DIMMER_SUFFIX + "/command/exact?level=" + String.valueOf(dimmer);
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


  public RGBColor getCurrentColor() {
    String url = "/ZAutomation/api/v1/devices/" + deviceId;
    LOG.info(url);
    HttpEntity<String> jsonResponse = get(url, String.class);
    LOG.info(jsonResponse.getBody());
    JsonElement jelement = new JsonParser().parse(jsonResponse.getBody());
    JsonObject jobject = jelement.getAsJsonObject();
    JsonObject jsonColor = jobject.getAsJsonObject("data").getAsJsonObject("metrics").getAsJsonObject("color");
    LOG.info(String.valueOf(jsonColor));
    RGBColor rgbColor = RGBColor.of(jsonColor.get("r").getAsDouble(), jsonColor.get("g").getAsDouble(), jsonColor.get("b").getAsDouble());
    LOG.info(rgbColor.toString());
    return rgbColor;
  }
}
