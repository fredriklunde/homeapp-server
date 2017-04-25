package com.bernerus.homeapp.config.zconfig;

import java.util.List;
import java.util.Map;

/**
 * Created by andreas on 2017-04-22.
 */
public class ZwaveDeviceConfig {
  private String name;
  private String id;
  private ZwaveDeviceType type;
  private List<ZwaveEventConfig> events;
  private Map<String, String> config;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ZwaveDeviceType getType() {
    return type;
  }

  public void setType(ZwaveDeviceType type) {
    this.type = type;
  }

  public List<ZwaveEventConfig> getEvents() {
    return events;
  }

  public void setEvents(List<ZwaveEventConfig> events) {
    this.events = events;
  }

  public Map<String, String> getConfig() {
    return config;
  }

  public void setConfig(Map<String, String> config) {
    this.config = config;
  }
}
