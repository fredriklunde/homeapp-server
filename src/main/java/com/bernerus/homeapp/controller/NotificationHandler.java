package com.bernerus.homeapp.controller;

import com.bernerus.homeapp.model.RazberryNotificationData;

/**
 * Created by andreas on 21/02/17.
 */
public interface NotificationHandler {
  void handleMessage(RazberryNotificationData notification);
}
