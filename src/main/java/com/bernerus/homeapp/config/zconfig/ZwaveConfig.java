package com.bernerus.homeapp.config.zconfig;

import java.util.Map;

/**
 * Created by andreas on 2017-04-22.
 */
public class ZwaveConfig {
  private Map<String, ZwaveDeviceConfig> devices;

  public Map<String, ZwaveDeviceConfig> getDevices() {
    return devices;
  }

  public void setDevices(Map<String, ZwaveDeviceConfig> devices) {
    this.devices = devices;
  }
}
