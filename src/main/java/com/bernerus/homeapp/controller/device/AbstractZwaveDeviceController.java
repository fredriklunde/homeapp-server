package com.bernerus.homeapp.controller.device;

import com.bernerus.homeapp.config.zconfig.ZwaveDeviceConfig;
import com.bernerus.homeapp.config.zconfig.ZwaveEventConfig;
import com.bernerus.homeapp.model.RazberryNotificationData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andreas on 2017-04-22.
 */
public abstract class AbstractZwaveDeviceController implements ZwaveDeviceController {
  protected final List<ZwaveEventConfig> eventConfigs;
  protected Map<String, ZwaveDeviceController> eventTargets;
  protected Map<String, ZwaveDeviceController> functionCallers = new HashMap<>();
  private String name;
  private String id;

  public AbstractZwaveDeviceController(ZwaveDeviceConfig deviceConfig) {
    this.id = deviceConfig.getId();
    this.name = deviceConfig.getName();
    this.eventConfigs = deviceConfig.getEvents();
  }

  protected AbstractZwaveDeviceController(String id, String name) {
    this.id = id;
    this.name = name;
    this.eventConfigs = new ArrayList<>();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setTargets(Map<String, ZwaveDeviceController> eventTargets) {
    this.eventTargets = eventTargets;
  }

  @Override
  public void addCaller(ZwaveDeviceController caller) {
    this.functionCallers.put(caller.getId(), caller);
  }

  public void postConstruct() {
    //Called when all deviceControllers have been created and targets and callers have been set.
    //Default do nothing
  }

  protected String getSubId(RazberryNotificationData notification) {
    return notification.getSource().split(this.getId() + "-")[1];
  }
}
