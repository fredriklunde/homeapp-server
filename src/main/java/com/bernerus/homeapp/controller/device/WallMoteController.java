package com.bernerus.homeapp.controller.device;

import com.bernerus.homeapp.config.zconfig.ZwaveDeviceConfig;
import com.bernerus.homeapp.config.zconfig.ZwaveEventConfig;
import com.bernerus.homeapp.model.EventData;
import com.bernerus.homeapp.model.RazberryNotificationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by andreas on 2017-04-22.
 */
public class WallMoteController extends AbstractZwaveDeviceController {
  private static final Logger LOG = LoggerFactory.getLogger(WallMoteController.class);
  private Map<String, Boolean> buttonStatuses = new HashMap<>();

  public WallMoteController(ZwaveDeviceConfig deviceConfig) {
    super(deviceConfig);

    buttonStatuses.put("0-0-1-S", false);
    buttonStatuses.put("0-0-2-S", false);
    buttonStatuses.put("0-0-3-S", false);
    buttonStatuses.put("0-0-4-S", false);
  }

  @Override
  public void callFunction(String functionName, EventData eventData) {
    LOG.warn("method '{}' not implemented", functionName);
  }

  @Override
  public void handleNotification(RazberryNotificationData notification) {
    // Get sub Id
    String subId = getSubId(notification);
    Boolean newButtonStatus = !buttonStatuses.get(subId);
    buttonStatuses.put(subId, newButtonStatus);

    Optional<ZwaveEventConfig> buttonEventConfig = eventConfigs.stream().filter(eventConfig -> eventConfig.getId().equalsIgnoreCase(subId)).findFirst();
    buttonEventConfig.ifPresent(eventConfig -> {
      eventConfig.getActions().forEach(actionConfig -> {
        String targetId = actionConfig.getTarget();
        if (eventTargets.containsKey(subId)) {
          eventTargets.get(subId).callFunction(actionConfig.getFunction(), new EventData<>(this, newButtonStatus));
        } else {
          LOG.warn("Target with id {} does not exist", targetId);
        }
      });
    });
  }
}
