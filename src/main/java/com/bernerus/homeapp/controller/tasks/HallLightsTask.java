package com.bernerus.homeapp.controller.tasks;

import com.bernerus.homeapp.config.UserSettings;
import com.bernerus.homeapp.controller.http.RazberryRgbwHttpClient;
import com.bernerus.homeapp.model.RGBColor;
import com.bernerus.homeapp.model.state.HallLights;
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


  private RazberryRgbwHttpClient razberryRgbwHttpClient;
  private HallLights hallLights;

  @Autowired
  public HallLightsTask(UserSettings userSettings, HallLights hallLights) {
    razberryRgbwHttpClient = new RazberryRgbwHttpClient(userSettings.getRazberryHttpClientConfig(), HALL_RGB_LIGHTS);
    this.hallLights = hallLights;
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
    RGBColor currentColor = razberryRgbwHttpClient.getCurrentColor();
    if (currentColor.equals(RGBColor.black())) {
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
    RGBColor currentColor = razberryRgbwHttpClient.getCurrentColor();
    if (!currentColor.equals(RGBColor.black())) {
      LOG.info("Scheduling hall lights off in 60 seconds");
      offExecutor = resetExecutor(offExecutor);
      offExecutor.scheduleWithFixedDelay(this::lightsOff, 60L, 10, TimeUnit.SECONDS);
    } else {
      LOG.info("Hall lights already off! Ignoring...");
    }

  }


  public void lightsOn() {
    LocalTime currentTime = LocalTime.now();
    if (currentTime.isAfter(LocalTime.of(9, 0)) && currentTime.isBefore(LocalTime.of(19, 30))) {
      //Full lights
      razberryRgbwHttpClient.setColor(RGBColor.white());
    } else if (currentTime.isAfter(LocalTime.of(21, 30)) && currentTime.isBefore(LocalTime.of(7, 0))) {
      // Night time
      razberryRgbwHttpClient.setColor(RGBColor.of(80, 60, 10));
    } else {
      // Morning/Evening
      razberryRgbwHttpClient.setColor(RGBColor.of(255, 140, 60));
    }
  }

  public void lightsOff() {
    LOG.info("Turning hall lights off");
    offExecutor = resetExecutor(offExecutor);
    timeoutExecutor = resetExecutor(timeoutExecutor);

    razberryRgbwHttpClient.setColor(RGBColor.black());
  }


}
