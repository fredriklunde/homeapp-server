package com.bernerus.homeapp.controller.scheduler;

import com.bernerus.homeapp.model.RGBColor;

/**
 * Created by andreas on 2017-03-05.
 */
public interface Phase {
  int getPhaseNumber();

  boolean isDone(RGBColor currentColor);
}
