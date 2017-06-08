package com.bernerus.homeapp.controller;

import com.bernerus.homeapp.config.zconfig.ZwaveConfig;
import com.bernerus.homeapp.controller.device.DeviceControllerFactory;
import com.bernerus.homeapp.controller.device.MultiInputRGBWDimmerController;
import com.bernerus.homeapp.controller.device.UnknownDeviceController;
import com.bernerus.homeapp.controller.device.ZwaveDeviceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * Created by andreas on 2017-06-08.
 */
@Component
public class DeviceRegistry {
  private final DeviceControllerFactory deviceControllerFactory;
  private Map<String, ZwaveDeviceController> deviceControllers = new HashMap<>();

  @Autowired
  public DeviceRegistry(DeviceControllerFactory deviceControllerFactory) {
    this.deviceControllerFactory = deviceControllerFactory;
  }

  public void registerDevices(ZwaveConfig zwaveZwaveConfig) {
    //Create deviceControllers
    zwaveZwaveConfig.getDevices().values().forEach(deviceConfig -> {
      ZwaveDeviceController deviceController = deviceControllerFactory.createZwaveDevice(deviceConfig);
      deviceControllers.put(deviceConfig.getId(), deviceController);
    });

    //Add targets
    zwaveZwaveConfig.getDevices().values().forEach(deviceConfig -> {
      ZwaveDeviceController deviceController = deviceControllers.get(deviceConfig.getId());
      Map<String, ZwaveDeviceController> eventTargets = new HashMap<>();
      deviceConfig.getEvents()
        .forEach(eventConfig -> {
          eventConfig.getActions().forEach(actionConfig -> {
            ZwaveDeviceController targetDeviceController = deviceControllers.get(actionConfig.getTarget());
            if (nonNull(targetDeviceController)) {
              eventTargets.putIfAbsent(eventConfig.getId(), targetDeviceController);
            }
          });
        });
      deviceController.setTargets(eventTargets);
      eventTargets.values().forEach(target -> {
        target.addCaller(deviceController);

      });
    });

    //Finally call postConstruct when all devices has been created
    deviceControllers.values().forEach(ZwaveDeviceController::postConstruct);
  }

  public Map<String, ZwaveDeviceController> getDeviceControllers() {
    return deviceControllers;
  }

  public UnknownDeviceController createUnknownDeviceController(String deviceIdShort) {
    return deviceControllerFactory.createUnknownDeviceController(deviceIdShort);
  }

  public ZwaveDeviceController getDeviceController(String id) {
    return deviceControllers.get(id);
  }
}
