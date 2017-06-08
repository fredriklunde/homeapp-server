package com.bernerus.homeapp.controller;

import com.bernerus.homeapp.config.zconfig.ZwaveConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by andreas on 2017-06-08.
 */
@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
  @Autowired
  private DeviceRegistry deviceRegistry;
  @Autowired
  private ZwaveConfig zwaveConfig;

  /**
   * This event is executed as late as conceivably possible to indicate that
   * the application is ready to service requests.
   */
  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {

    deviceRegistry.registerDevices(zwaveConfig);

    return;
  }
}
