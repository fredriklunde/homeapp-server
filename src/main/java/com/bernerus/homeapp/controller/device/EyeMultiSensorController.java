package com.bernerus.homeapp.controller.device;

import com.bernerus.homeapp.config.zconfig.ZwaveDeviceConfig;
import com.bernerus.homeapp.model.EventData;
import com.bernerus.homeapp.model.RazberryNotificationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by andreas on 2017-04-22.
 */
public class EyeMultiSensorController extends AbstractZwaveDeviceController {
  private static final Logger LOG = LoggerFactory.getLogger(EyeMultiSensorController.class);

  public EyeMultiSensorController(ZwaveDeviceConfig deviceConfig) {
    super(deviceConfig);
  }

  @Override
  public void callFunction(String functionName, EventData eventData) {
    LOG.warn("method '{}' not implemented", functionName);
  }

  @Override
  public void handleNotification(RazberryNotificationData notification) {
    //TODO: Movement, temp
  }
}
