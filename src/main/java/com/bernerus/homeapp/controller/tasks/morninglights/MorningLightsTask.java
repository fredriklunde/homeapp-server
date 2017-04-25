package com.bernerus.homeapp.controller.tasks.morninglights;

import com.bernerus.homeapp.config.UserSettings;
import com.bernerus.homeapp.controller.http.RazberryRgbwHttpClient;
import com.bernerus.homeapp.controller.tasks.Phase;
import com.bernerus.homeapp.controller.tasks.ScheduledTask;
import com.bernerus.homeapp.controller.tasks.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.bernerus.homeapp.config.Config.BEDBOX_RGB_LIGHTS;

/**
 * Created by andreas on 2017-02-25.
 */
@Component
public class MorningLightsTask extends ScheduledTask {
  private static final Logger LOG = LoggerFactory.getLogger(Scheduler.class);

  private RazberryRgbwHttpClient razberryRgbwHttpClient;
  private MorningLightsPhase currentMorningLightsPhase = MorningLightsPhase.one();


  @Autowired
  public MorningLightsTask(UserSettings userSettings) {
    razberryRgbwHttpClient = new RazberryRgbwHttpClient(userSettings.getRazberryHttpClientConfig(), BEDBOX_RGB_LIGHTS);
  }

  @Override
  protected void start() {
    currentMorningLightsPhase = MorningLightsPhase.one();
    LOG.info("Morning script starting phase 1");
    executor.scheduleAtFixedRate(this::run, 0L, Math.round(currentMorningLightsPhase.getSleepTimeMs()), TimeUnit.MILLISECONDS);
  }

  public void run() {
    LOG.info("Running");
    razberryRgbwHttpClient.setColor(currentMorningLightsPhase.nextColor());
    if (currentMorningLightsPhase.isDone()) {
      switch (currentMorningLightsPhase.getPhaseNumber()) {
        case Phase.ONE:
          resetExecutor();
          currentMorningLightsPhase = MorningLightsPhase.two();
          int phase2Delay = 45 * 60 * 1000; // 45 mins delay
          LOG.info("Morning script starting phase 2 in {} seconds", phase2Delay / 1000);
          executor.scheduleAtFixedRate(this::run, phase2Delay, Math.round(currentMorningLightsPhase.getSleepTimeMs()), TimeUnit.MILLISECONDS);
          break;
        case Phase.TWO:
          resetExecutor();
          currentMorningLightsPhase = MorningLightsPhase.three();
          int phase3Delay = 45 * 60 * 1000; // 45 mins delay
          LOG.info("Morning script starting phase 2 in {} seconds", phase3Delay / 1000);
          executor.scheduleAtFixedRate(this::run, phase3Delay, Math.round(currentMorningLightsPhase.getSleepTimeMs()), TimeUnit.MILLISECONDS);
          break;
        case Phase.THREE:
          executor.shutdown();
          LOG.info("Morning script done!");
          break;
      }
    }
  }




}
