package com.bernerus.homeapp.config.zconfig;

import java.util.Map;
import java.util.Optional;

/**
 * Created by andreas on 2017-04-22.
 */
public class ZwaveConfig {
  private Map<String, ZwaveDeviceConfig> devices;

  public ZwaveDeviceConfig getDeviceById(String deviceId) {
    return devices.get(deviceId);
  }

  public Optional<ZwaveDeviceConfig> getDeviceByName(String deviceName) {
    return devices.values().stream().filter(deviceConfig -> deviceConfig.getName().equals(deviceName)).findFirst();
  }

  public Map<String, ZwaveDeviceConfig> getDevices() {
    return devices;
  }

  public void setDevices(Map<String, ZwaveDeviceConfig> devices) {
    this.devices = devices;
  }
}
