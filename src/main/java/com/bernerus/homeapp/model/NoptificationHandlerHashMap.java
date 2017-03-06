package com.bernerus.homeapp.model;

import com.bernerus.homeapp.controller.NotificationHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andreas on 2017-03-05.
 */
public class NoptificationHandlerHashMap extends HashMap<String, NotificationHandler> {
  public void put(List<String> ids, NotificationHandler notificationHandler) {
    ids.forEach(id -> {
      super.put(id, notificationHandler);
    });
  }
}
