package com.bernerus.homeapp.controller.tasks;

import com.bernerus.homeapp.config.UserSettings;
import com.bernerus.homeapp.controller.http.RazberryRgbHttpClient;
import com.bernerus.homeapp.model.RGBColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.bernerus.homeapp.config.Config.HALL_RGB_LIGHTS;

/**
 * Created by andreas on 2017-02-25.
 */
@Component
public class HallLightsTask extends ScheduledTask {
  private static final Logger LOG = LoggerFactory.getLogger(Scheduler.class);

  protected ScheduledExecutorService offExecutor;
  protected ScheduledExecutorService timeoutExecutor;


  private RazberryRgbHttpClient razberryRgbHttpClient;
  private boolean hallLightsOn = false;

  @Autowired
  public HallLightsTask(UserSettings userSettings) {
    razberryRgbHttpClient = new RazberryRgbHttpClient(userSettings.getRazberryHttpClientConfig(), HALL_RGB_LIGHTS);
  }

  @Override
  protected void start() {
    //Do nothing
  }

  @Override
  public void execute() {
    //Do nothing
  }

  public void reportMovement() {
    if (!hallLightsOn) {
      lightsOn();
      LOG.info("Hall lights is now on. Scheduling off timeout in 5 minutes");
    } else {
      LOG.info("Hall lights already on! Rescheduling off timeout in 5 minutes...");
    }
    offExecutor = resetExecutor(offExecutor);
    timeoutExecutor = resetExecutor(timeoutExecutor);
    timeoutExecutor.scheduleWithFixedDelay(this::lightsOff, 5 * 60L, 10, TimeUnit.SECONDS);
  }

  public void reportNoMovement() {
    offExecutor = resetExecutor(offExecutor);

    if (hallLightsOn) {
      LOG.info("Scheduling hall lights off in 60 seconds");
      offExecutor = resetExecutor(offExecutor);
      offExecutor.scheduleWithFixedDelay(this::lightsOff, 60L, 10, TimeUnit.SECONDS);
    } else {
      LOG.info("Hall lights already off! Ignoring...");
    }

  }


  public void lightsOn() {
    LocalTime currentTime = LocalTime.now();
    if (currentTime.isAfter(LocalTime.of(9, 0)) && currentTime.isBefore(LocalTime.of(20, 0))) {
      //Full lights
      razberryRgbHttpClient.setColor(RGBColor.white());
    } else if (currentTime.isBefore(LocalTime.of(6, 0))) {
      // Night time
      razberryRgbHttpClient.setColor(RGBColor.of(80, 60, 10));
    } else {
      // Morning/Evening
      razberryRgbHttpClient.setColor(RGBColor.of(255, 140, 60));
    }
    hallLightsOn = true;
  }

  public void lightsOff() {
    LOG.info("Turning hall lights off");
    offExecutor = resetExecutor(offExecutor);
    timeoutExecutor = resetExecutor(timeoutExecutor);

    razberryRgbHttpClient.setColor(RGBColor.black());
    hallLightsOn = false;
  }


}
