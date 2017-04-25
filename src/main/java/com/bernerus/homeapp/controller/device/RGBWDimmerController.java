package com.bernerus.homeapp.controller.device;

import com.bernerus.homeapp.config.HttpClientConfig;
import com.bernerus.homeapp.config.zconfig.ZwaveDeviceConfig;
import com.bernerus.homeapp.controller.http.RazberryRgbwHttpClient;
import com.bernerus.homeapp.model.EventData;
import com.bernerus.homeapp.model.RGBColor;
import com.bernerus.homeapp.model.RazberryNotificationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by andreas on 2017-04-22.
 */
public class RGBWDimmerController extends AbstractZwaveDeviceController {
  private static final Logger LOG = LoggerFactory.getLogger(RGBWDimmerController.class);

  protected static final String FUNCTION_TOGGLE_WHITE = "toggleWhite";
  protected static final String FUNCTION_TOGGLE_RGB = "toggleRgb";
  protected static final String FUNCTION_TOGGLE_RGB_WHITE = "toggleRgbWhite";
  protected static final String FUNCTION_TOGGLE_DIMMER = "toggleDimmer";
  protected static final int DEFAULT_MAX = 99;
  protected static final int ZERO = 0;

  protected RazberryRgbwHttpClient rgbwHttpClient;
  protected int dimmer = 0;
  protected int white = 0;
  protected RGBColor rgbColor = RGBColor.black();
  private List<RGBColor> rgbColorCycle = RGBColor.predefined();
  private List<RGBColor> rgbWhiteColorCycle = Arrays.asList(RGBColor.white(), RGBColor.black());


  public RGBWDimmerController(ZwaveDeviceConfig deviceConfig, HttpClientConfig clientConfig) {
    super(deviceConfig);
    this.rgbwHttpClient = new RazberryRgbwHttpClient(clientConfig, deviceConfig.getId());
  }

  @Override
  public void callFunction(String functionName, EventData eventData) {
    LOG.info("Got call for method: {} with value: {}", functionName, eventData);
    if (FUNCTION_TOGGLE_WHITE.equalsIgnoreCase(functionName)) {
      toggleWhite(eventData.getValueAsBoolean());
    } else if (FUNCTION_TOGGLE_RGB.equalsIgnoreCase(functionName)) {
      toggleRgb(rgbColorCycle);
    } else if (FUNCTION_TOGGLE_RGB_WHITE.equalsIgnoreCase(functionName)) {
      toggleRgbWhite(eventData.getValueAsBoolean());
    } else if (FUNCTION_TOGGLE_DIMMER.equalsIgnoreCase(functionName)) {
      toggleDimmer(eventData.getValueAsBoolean());
    }
  }

  private void setColor(RGBColor rgbColor) {
    this.rgbColor = rgbColor;
    rgbwHttpClient.setColor(rgbColor);
  }

  @Override
  public void handleNotification(RazberryNotificationData notification) {
    // DO nothing
    //TODO: save status of colors, qubino don't send this info but fibaro does.
  }

  private void toggleRgb(List<RGBColor> cycle) {
    int currentIndex = 0;
    for (int i = 0; i < cycle.size(); i++) {
      if (cycle.get(i).equals(rgbColor)) {
        currentIndex = i;
        break;
      }
    }
    currentIndex = currentIndex == cycle.size() - 1 ? 0 : currentIndex + 1;
    RGBColor nextColor = cycle.get(currentIndex);
    setColor(nextColor);
    LOG.info("Toggle RGB to {}", nextColor);
  }

  private void toggleRgbWhite(Boolean value) {
    if (value) {
      rgbColor = RGBColor.white();
    } else {
      rgbColor = RGBColor.black();
    }
    rgbwHttpClient.setColor(rgbColor);
  }

  private void toggleWhite(Boolean value) {
    if (value) {
      white = DEFAULT_MAX;
    } else {
      white = ZERO;
    }
    rgbwHttpClient.setWhite(white);
  }

  private void toggleDimmer(Boolean value) {
    if (value) {
      dimmer = DEFAULT_MAX;
    } else {
      dimmer = ZERO;
    }
    rgbwHttpClient.setDimmer(dimmer);
  }
}
