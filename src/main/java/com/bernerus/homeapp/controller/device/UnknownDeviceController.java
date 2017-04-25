package com.bernerus.homeapp.controller.device;

import com.bernerus.homeapp.config.zconfig.ZwaveDeviceConfig;
import com.bernerus.homeapp.model.EventData;
import com.bernerus.homeapp.model.RazberryNotificationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by andreas on 2017-04-22.
 */
public class UnknownDeviceController extends AbstractZwaveDeviceController {
  private static final Logger LOG = LoggerFactory.getLogger(UnknownDeviceController.class);

  public UnknownDeviceController(String id) {
    super(id, "Unknown Device");
  }

  @Override
  public void callFunction(String functionName, EventData eventData) {
    LOG.info("Got call for method: {} with value: {}", functionName, eventData);
  }

  @Override
  public void handleNotification(RazberryNotificationData notification) {
    LOG.info("Got notification for unknown device: {}", notification);
  }

}
