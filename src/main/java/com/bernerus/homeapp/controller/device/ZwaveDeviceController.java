package com.bernerus.homeapp.controller.device;

import com.bernerus.homeapp.model.EventData;
import com.bernerus.homeapp.model.RazberryNotificationData;

import java.util.Map;

/**
 * Created by andreas on 2017-04-22.
 */
public interface ZwaveDeviceController {
  void callFunction(String functionName, EventData<?> eventData);

  void handleNotification(RazberryNotificationData notification);

  String getName();

  String getId();

  void setTargets(Map<String, ZwaveDeviceController> eventTargets);

  void addCaller(ZwaveDeviceController deviceController);

  void postConstruct();
}
