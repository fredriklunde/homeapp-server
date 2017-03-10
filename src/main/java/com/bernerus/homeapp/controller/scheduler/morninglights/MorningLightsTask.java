package com.bernerus.homeapp.controller.scheduler.morninglights;

import com.bernerus.homeapp.config.UserSettings;
import com.bernerus.homeapp.controller.http.RazberryRgbHttpClient;
import com.bernerus.homeapp.controller.scheduler.ScheduledTask;
import com.bernerus.homeapp.controller.scheduler.Scheduler;
import com.bernerus.homeapp.model.RGBColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.bernerus.homeapp.config.Config.BEDBOX_RGB_LIGHTS;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Created by andreas on 2017-02-25.
 */
@Component
public class MorningLightsTask implements ScheduledTask {
  private static final Logger LOG = LoggerFactory.getLogger(Scheduler.class);

  private RazberryRgbHttpClient razberryRgbHttpClient;
  private MorningLightsPhase currentMorningLightsPhase = MorningLightsPhase.one();

  private ScheduledExecutorService executor;

  @Autowired
  public MorningLightsTask(UserSettings userSettings) {
    razberryRgbHttpClient = new RazberryRgbHttpClient(userSettings.getRazberryHttpClientConfig(), BEDBOX_RGB_LIGHTS);
  }

  @Override
  public void start() {
    if(nonNull(executor)) {
      executor.shutdown();
    }
    executor = Executors.newScheduledThreadPool(1);
    currentMorningLightsPhase = MorningLightsPhase.one();
    LOG.info("Morning script starting phase 1");
    executor.scheduleAtFixedRate(this::run, 0L, Math.round(currentMorningLightsPhase.getSleepTimeMs()), TimeUnit.MILLISECONDS);
  }

  public void run() {
    LOG.info("Running");
    razberryRgbHttpClient.setBedBoxColor(currentMorningLightsPhase.nextColor());
    if (currentMorningLightsPhase.isDone()) {
      if (currentMorningLightsPhase.getPhaseNumber() == 1) {
        executor.shutdown();
        executor = Executors.newScheduledThreadPool(1);
        currentMorningLightsPhase = MorningLightsPhase.two();
        int phase2Delay = 45 * 60 * 1000; // 45 mins delay
        LOG.info("Morning script starting phase 2 in {} seconds", phase2Delay / 1000);
        executor.scheduleAtFixedRate(this::run, phase2Delay, Math.round(currentMorningLightsPhase.getSleepTimeMs()), TimeUnit.MILLISECONDS);
      } else if (currentMorningLightsPhase.getPhaseNumber() > 1) {
        executor.shutdown();
        LOG.info("Morning script done!");
      }
    }
  }




}
