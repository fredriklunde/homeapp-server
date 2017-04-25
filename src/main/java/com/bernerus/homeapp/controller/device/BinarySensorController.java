package com.bernerus.homeapp.controller.device;

import com.bernerus.homeapp.config.zconfig.ZwaveDeviceConfig;
import com.bernerus.homeapp.config.zconfig.ZwaveEventConfig;
import com.bernerus.homeapp.model.EventData;
import com.bernerus.homeapp.model.RazberryNotificationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by andreas on 2017-04-22.
 */
public class BinarySensorController extends AbstractZwaveDeviceController {
  private static final Logger LOG = LoggerFactory.getLogger(BinarySensorController.class);
  private static final String ON = "on";
  private static final String OFF = "off";

  private boolean openStatus = false;

  public BinarySensorController(ZwaveDeviceConfig deviceConfig) {
    super(deviceConfig);
  }

  @Override
  public void callFunction(String functionName, EventData eventData) {
    LOG.warn("method '{}' not implemented", functionName);
  }

  @Override
  public void handleNotification(RazberryNotificationData notification) {
    String subId = getSubId(notification);
    openStatus = notification.getMessage().getL().equals(ON);

    Optional<ZwaveEventConfig> buttonEventConfig = eventConfigs.stream().filter(eventConfig -> eventConfig.getId().equalsIgnoreCase(subId)).findFirst();
    buttonEventConfig.ifPresent(eventConfig -> {
      eventConfig.getActions().forEach(actionConfig -> {
        String targetId = actionConfig.getTarget();
        if(eventTargets.containsKey(subId)) {
          eventTargets.get(subId).callFunction(actionConfig.getFunction(), new EventData<>(this, openStatus));
        } else {
          LOG.warn("Target with id {} does not exist", targetId);
        }
      });
    });
  }

  public boolean isOpen() {
    return openStatus;
  }

}
