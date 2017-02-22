package com.bernerus.homeapp.controller;

import com.bernerus.smartmirror.model.zwave.RazberryWsNotification;

/**
 * Created by andreas on 21/02/17.
 */
public interface NotificationHandler {
  void handleMessage(RazberryWsNotification notification);
}
