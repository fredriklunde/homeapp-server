package com.bernerus.homeapp.controller.device;

import com.bernerus.homeapp.config.HttpClientConfig;
import com.bernerus.homeapp.config.UserSettings;
import com.bernerus.homeapp.config.zconfig.ZwaveDeviceConfig;
import com.bernerus.homeapp.config.zconfig.ZwaveDeviceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Optional;


/**
 * Created by andreas on 2017-04-22.
 */
@Controller
public class DeviceControllerFactory {
  private HttpClientConfig httpClientConfig;

  @Autowired
  public DeviceControllerFactory(UserSettings userSettings) {
    this.httpClientConfig = userSettings.getRazberryHttpClientConfig();
  }

  public ZwaveDeviceController createZwaveDevice(ZwaveDeviceConfig deviceConfig) {
    switch (Optional.ofNullable(deviceConfig.getType()).orElse(ZwaveDeviceType.UNKNOWN)) {
      case WALLMOTE:
        return new WallMoteController(deviceConfig);
      case RGBW_DIMMER:
        return new RGBWDimmerController(deviceConfig, httpClientConfig);
      case MULTI_INPUT_RGBW_DIMMER:
        return new MultiInputRGBWDimmerController(deviceConfig, httpClientConfig);
      case BINARY_SENSOR:
        return new BinarySensorController(deviceConfig);
      case DIMMER:
        return new DimmerController(deviceConfig, httpClientConfig);
      case UNKNOWN:
      default:
        return new UnknownDeviceController(deviceConfig.getId());
    }
  }

  public UnknownDeviceController createUnknownDeviceController(String id) {
    return new UnknownDeviceController(id);
  }
}
