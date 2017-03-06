package com.bernerus.homeapp.controller;

import com.bernerus.homeapp.model.RazberryNotificationData;
import com.bernerus.homeapp.model.RazberryWsNotification;

import java.util.function.Consumer;

/**
 * Created by andreas on 21/02/17.
 */
public class DefaultNotificationHandler implements NotificationHandler {
  private Consumer<RazberryNotificationData> defaultHandler;

  public DefaultNotificationHandler(Consumer<RazberryNotificationData> defaultHandler) {
    this.defaultHandler = defaultHandler;
  }

  @Override
  public void handleMessage(RazberryNotificationData notification) {
    defaultHandler.accept(notification);
  }
}
