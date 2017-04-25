package com.bernerus.homeapp.controller.device;

import com.bernerus.homeapp.config.HttpClientConfig;
import com.bernerus.homeapp.config.zconfig.ZwaveDeviceConfig;
import com.bernerus.homeapp.controller.http.RazberryDimmerHttpClient;
import com.bernerus.homeapp.model.EventData;
import com.bernerus.homeapp.model.RazberryNotificationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by andreas on 2017-04-22.
 */
public class DimmerController extends AbstractZwaveDeviceController {
  private static final Logger LOG = LoggerFactory.getLogger(DimmerController.class);

  private static final String FUNCTION_TOGGLE_LAST_VALUE = "toggleLastValue";
  private static final int DEFAULT_MAX = 99;
  private static final int ZERO = 0;

  private RazberryDimmerHttpClient dimmerHttpClient;
  private int lastOnValue = 99;


  public DimmerController(ZwaveDeviceConfig deviceConfig, HttpClientConfig clientConfig) {
    super(deviceConfig);
    this.dimmerHttpClient = new RazberryDimmerHttpClient(clientConfig, deviceConfig.getId());
  }

  @Override
  public void callFunction(String functionName, EventData eventData) {
    LOG.info("Got call for method: {} with value: {}", functionName, eventData);
    if (FUNCTION_TOGGLE_LAST_VALUE.equalsIgnoreCase(functionName)) {
      toggleLastValue(eventData.getValueAsBoolean());
    }
  }

  @Override
  public void handleNotification(RazberryNotificationData notification) {
    //Save status
    LOG.info("Dimmer notification received: {}", notification);
  }

  private void toggleLastValue(Boolean on) {
    int white;
    if (on) {
      white = lastOnValue;
    } else {
      white = ZERO;
    }
    dimmerHttpClient.setWhite(white);
  }

}
