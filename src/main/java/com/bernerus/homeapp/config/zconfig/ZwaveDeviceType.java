package com.bernerus.homeapp.config.zconfig;

/**
 * Created by andreas on 2017-04-22.
 */
public enum ZwaveDeviceType {
  WALLMOTE("WallMote"), MULTI_INPUT_RGBW_DIMMER("MultiInputRGBWDimmer"), RGBW_DIMMER("RGBWDimmer"), DIMMER("Dimmer"), BINARY_SENSOR("BinarySensor"),
  UNKNOWN("Unknown");

  private final String name;

  ZwaveDeviceType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
