package com.bernerus.homeapp.controller;

import com.bernerus.homeapp.model.RazberryNotificationData;
import com.bernerus.homeapp.model.RazberryWsNotification;

import java.util.function.Consumer;

/**
 * Created by andreas on 21/02/17.
 */
public class BinaryNotificationHandler implements NotificationHandler {
  private static final String ON = "on";
  private static final String OFF = "off";
  private Consumer<RazberryNotificationData> onOnEvent;
  private Consumer<RazberryNotificationData> onOffEvent;

  public BinaryNotificationHandler(Consumer<RazberryNotificationData> onOnEvent, Consumer<RazberryNotificationData> onOffEvent) {
    this.onOnEvent = onOnEvent;
    this.onOffEvent = onOffEvent;
  }

  @Override
  public void handleMessage(RazberryNotificationData notification) {
    if (notification.getMessage().getL().equals(ON)) {
      onOnEvent.accept(notification);
    } else {
      onOffEvent.accept(notification);
    }
  }
}
