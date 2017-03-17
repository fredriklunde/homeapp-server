package com.bernerus.homeapp.controller.tasks;

/**
 * Created by andreas on 2017-03-05.
 */
public interface Phase {
  int ONE = 1;
  int TWO = 2;
  int THREE = 3;
  int getPhaseNumber();

  boolean isDone();
}
