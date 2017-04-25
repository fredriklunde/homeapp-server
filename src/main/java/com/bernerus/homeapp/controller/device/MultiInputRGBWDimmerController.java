package com.bernerus.homeapp.controller.device;

import com.bernerus.homeapp.config.HttpClientConfig;
import com.bernerus.homeapp.config.zconfig.ZwaveDeviceConfig;
import com.bernerus.homeapp.model.EventData;
import com.bernerus.homeapp.model.RGBColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andreas on 2017-04-23.
 */
public class MultiInputRGBWDimmerController extends RGBWDimmerController {
  private static final Logger LOG = LoggerFactory.getLogger(MultiInputRGBWDimmerController.class);


  private Map<String, Boolean> sensorsOpen = new HashMap<>();

  public MultiInputRGBWDimmerController(ZwaveDeviceConfig deviceConfig, HttpClientConfig clientConfig) {
    super(deviceConfig, clientConfig);
  }

  @Override
  public void postConstruct() {
    functionCallers.keySet().forEach(callerId -> {
      sensorsOpen.put(callerId, false);
    });
  }

  @Override
  public void callFunction(String functionName, EventData eventData) {
    LOG.info("Got call for method: {} with value: {}", functionName, eventData);
    if (FUNCTION_TOGGLE_RGB_WHITE.equalsIgnoreCase(functionName)) {
      toggleRgbWhite(eventData);
    } if (FUNCTION_TOGGLE_WHITE.equalsIgnoreCase(functionName)) {
      toggleWhite(eventData);
    } if (FUNCTION_TOGGLE_DIMMER.equalsIgnoreCase(functionName)) {
      toggleDimmer(eventData);
    } else {
      super.callFunction(functionName, eventData);
    }
  }

  private void toggleRgbWhite(EventData eventData) {
    sensorsOpen.put(eventData.getSender().getId(), eventData.getValueAsBoolean());
    if (isAnySensorOpen()) {
      rgbwHttpClient.setColor(RGBColor.white());
    } else {
      rgbColor = RGBColor.black();
    }
    rgbwHttpClient.setColor(rgbColor);
  }

  private void toggleWhite(EventData eventData) {
    sensorsOpen.put(eventData.getSender().getId(), eventData.getValueAsBoolean());
    if (isAnySensorOpen()) {
      white = DEFAULT_MAX;
    } else {
      white = ZERO;
    }
    rgbwHttpClient.setWhite(white);
  }

  private void toggleDimmer(EventData eventData) {
    sensorsOpen.put(eventData.getSender().getId(), eventData.getValueAsBoolean());
    if (isAnySensorOpen()) {
      dimmer = DEFAULT_MAX;
    } else {
      dimmer = ZERO;
    }
    rgbwHttpClient.setDimmer(dimmer);
  }

  private boolean isAnySensorOpen() {
    return sensorsOpen.values().stream().anyMatch(Boolean.TRUE::equals);
  }
}
