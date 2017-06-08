package com.bernerus.homeapp.controller.device;

import com.bernerus.homeapp.config.HttpClientConfig;
import com.bernerus.homeapp.config.UserSettings;
import com.bernerus.homeapp.config.zconfig.ZwaveConfig;
import com.bernerus.homeapp.controller.DeviceRegistry;
import com.bernerus.homeapp.model.RazberryNotificationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by andreas on 2017-06-08.
 */
public class MultiInputRGBWDimmerControllerTest {
  MultiInputRGBWDimmerController dimmer;
  ZwaveDeviceController sensor1;
  ZwaveDeviceController sensor2;

  @Before
  public void setUp() throws Exception {
    HttpClientConfig razberryHttpClientConfig = new HttpClientConfig("192.168.1.2", "8083", "appz", "zW4yAppz#i0s");
    HttpClientConfig mirrorHttpClientConfig = new HttpClientConfig("192.168.1.18", "18080");
    UserSettings userSettings = new UserSettings(mirrorHttpClientConfig, razberryHttpClientConfig);
    DeviceRegistry deviceRegistry = new DeviceRegistry(new DeviceControllerFactory(userSettings));

    ZwaveConfig config = ConfigHelper.readConfig();
    deviceRegistry.registerDevices(config);

    dimmer = (MultiInputRGBWDimmerController) deviceRegistry.getDeviceController("ZWayVDev_zway_17");
    sensor1 = deviceRegistry.getDeviceController("ZWayVDev_zway_18");
    sensor2 = deviceRegistry.getDeviceController("ZWayVDev_zway_20");
  }

  @Test
  public void name() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    RazberryNotificationData sensor2notification = mapper.readValue(createNotification("on", "20"), RazberryNotificationData.class);
    sensor2.handleNotification(sensor2notification);
    Assert.assertTrue(dimmer.isAnySensorOpen());


    RazberryNotificationData sensor1notification = mapper.readValue(createNotification("on", "18"), RazberryNotificationData.class);
    sensor1.handleNotification(sensor1notification);
    Assert.assertTrue(dimmer.isAnySensorOpen());


    RazberryNotificationData sensor1notification2 = mapper.readValue(createNotification("off", "18"), RazberryNotificationData.class);
    sensor1.handleNotification(sensor1notification2);
    Assert.assertTrue(dimmer.isAnySensorOpen());

    RazberryNotificationData sensor2notification2 = mapper.readValue(createNotification("off", "20"), RazberryNotificationData.class);
    sensor2.handleNotification(sensor2notification2);
    Assert.assertFalse(dimmer.isAnySensorOpen());


  }

  private String createNotification(String status, String deviceId) {
    return "{\"id\":1496946824211,\"timestamp\":\"2017-06-08T18:33:44.211Z\",\"level\":\"device-info\",\"message\":{\"dev\":\"Door/Window Sensor (#" + deviceId + ")\",\"l\":\"" + status + "\"},\"type\":\"device-OnOff\",\"source\":\"ZWayVDev_zway_" + deviceId + "-0-113-6-Door-A\",\"redeemed\":false}";
  }
}