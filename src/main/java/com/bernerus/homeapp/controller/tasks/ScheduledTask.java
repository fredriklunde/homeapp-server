package com.bernerus.homeapp.controller.tasks;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.Objects.nonNull;

/**
 * Created by andreas on 2017-02-26.
 */
public abstract class ScheduledTask {
  protected ScheduledExecutorService executor;

  protected abstract void start();

  public void execute() {
    if(nonNull(executor)) {
      executor.shutdown();
    }
    executor = Executors.newScheduledThreadPool(1);
    start();
  }

  protected void resetExecutor() {
    executor.shutdown();
    executor = Executors.newScheduledThreadPool(1);
  }

  protected ScheduledExecutorService resetExecutor(ScheduledExecutorService otherExecutor) {
    if(nonNull(otherExecutor)) {
      otherExecutor.shutdown();
    }
    otherExecutor = Executors.newScheduledThreadPool(1);
    return otherExecutor;
  }
}
