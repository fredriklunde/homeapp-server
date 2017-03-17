package com.bernerus.homeapp.controller.tasks;

import com.bernerus.homeapp.controller.tasks.morninglights.MorningLightsTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by andreas on 31/12/16.
 */
@EnableScheduling
@Controller
public class Scheduler {
  private static final Logger LOG = LoggerFactory.getLogger(Scheduler.class);
  private MorningLightsTask morningLightsTask;

  @Autowired
  public Scheduler(MorningLightsTask morningLightsTask) {
    this.morningLightsTask = morningLightsTask;
  }


  @PostConstruct
  public void init() {
    LOG.info("Started scheduler");
  }

  //0 15 10 15 * ?
  @Scheduled(cron = "${lights.morning.cron.weekday}")
  public void scheduleTaskUsingCronExpression() {
    long now = System.currentTimeMillis() / 1000;
    LOG.info("Running morning script - " + now);
    morningLightsTask.execute();
  }

  @Scheduled(cron = "${lights.morning.cron.weekend}")
  public void scheduleTaskUsingCronExpressionWeekend() {
    long now = System.currentTimeMillis() / 1000;
    LOG.info("Running morning script - " + now);
    morningLightsTask.execute();
  }
}
