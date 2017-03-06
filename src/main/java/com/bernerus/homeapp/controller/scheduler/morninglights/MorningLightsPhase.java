package com.bernerus.homeapp.controller.scheduler.morninglights;

import com.bernerus.homeapp.controller.scheduler.Phase;
import com.bernerus.homeapp.model.RGBColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by andreas on 2017-02-26.
 */
public class MorningLightsPhase implements Phase {
  private static final Logger LOG = LoggerFactory.getLogger(MorningLightsPhase.class);

  private int phaseNumber = 1;
  private final RGBColor startColor;
  private final RGBColor endColor;
  private final int durationSeconds;
  private final double stepRed;
  private final double stepGreen;
  private final double stepBlue;
  private final double sleepTime = 2.0;


  private MorningLightsPhase(final int phaseNumber, final RGBColor startColor, final RGBColor endColor, final int durationSeconds) {
    this.phaseNumber = phaseNumber;
    this.startColor = startColor;
    this.endColor = endColor;
    this.durationSeconds = durationSeconds;

    double requestsPerSecond = 1 / sleepTime;

    this.stepRed = (endColor.getRed() - startColor.getRed()) / (durationSeconds * requestsPerSecond);
    this.stepGreen =  (endColor.getGreen() - startColor.getGreen()) / (durationSeconds * requestsPerSecond);
    this.stepBlue =  (endColor.getBlue() - startColor.getBlue()) / (durationSeconds * requestsPerSecond);
    LOG.info("Step red=" + stepRed + ", step green=" + stepGreen + ", stepBlue=" + stepBlue);
  }

  public static MorningLightsPhase one() {
    return new MorningLightsPhase(1, RGBColor.black(), RGBColor.of(255, 80, 0), 60 * 15);
  }

  public static MorningLightsPhase two() {
    return new MorningLightsPhase(2, RGBColor.of(255, 80, 0), RGBColor.of(255, 255, 255), 60 * 7);
  }

  @Override
  public int getPhaseNumber() {
    return phaseNumber;
  }

  public RGBColor getStartColor() {
    return startColor;
  }

  public RGBColor getEndColor() {
    return endColor;
  }

  public int getDurationSeconds() {
    return durationSeconds;
  }

  public double getStepRed() {
    return stepRed;
  }

  public double getStepGreen() {
    return stepGreen;
  }

  public double getStepBlue() {
    return stepBlue;
  }

  public double getSleepTimeMs() {
    return sleepTime * 1000;
  }

  @Override
  public boolean isDone(final RGBColor currentColor) {
    final boolean redDone = isColorDone(stepRed, currentColor.getRed(), endColor.getRed());
    final boolean greenDone = isColorDone(stepGreen, currentColor.getGreen(), endColor.getGreen());
    final boolean blueDone = isColorDone(stepBlue, currentColor.getBlue(), endColor.getBlue());
    return redDone && greenDone && blueDone;
  }

  private boolean isColorDone(final double step, final double startColor, final double endColor) {
    return step == 0 || (step > 0 && startColor >= endColor) || (step < 0 && startColor <= endColor);
  }
}
