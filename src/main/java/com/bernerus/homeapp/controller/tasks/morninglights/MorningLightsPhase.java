package com.bernerus.homeapp.controller.tasks.morninglights;

import com.bernerus.homeapp.controller.tasks.Phase;
import com.bernerus.homeapp.model.RGBColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by andreas on 2017-02-26.
 */
public class MorningLightsPhase implements Phase {
  private static final Logger LOG = LoggerFactory.getLogger(MorningLightsPhase.class);

  private final int phaseNumber;
  private final RGBColor endColor;
  private RGBColor currentColor;
  private final double stepRed;
  private final double stepGreen;
  private final double stepBlue;
  private final double sleepTime = 2.0;


  private MorningLightsPhase(final int phaseNumber, final RGBColor startColor, final RGBColor endColor, final int durationSeconds) {
    this.phaseNumber = phaseNumber;
    this.endColor = endColor;
    this.currentColor = startColor;

    double requestsPerSecond = 1 / sleepTime;

    this.stepRed = (endColor.getRed() - startColor.getRed()) / (durationSeconds * requestsPerSecond);
    this.stepGreen = (endColor.getGreen() - startColor.getGreen()) / (durationSeconds * requestsPerSecond);
    this.stepBlue = (endColor.getBlue() - startColor.getBlue()) / (durationSeconds * requestsPerSecond);
    LOG.info("Step red=" + stepRed + ", step green=" + stepGreen + ", stepBlue=" + stepBlue);
  }

  public static MorningLightsPhase one() {
    return new MorningLightsPhase(1, RGBColor.black(), RGBColor.of(255, 80, 0), 60 * 15);
  }

  public static MorningLightsPhase two() {
    return new MorningLightsPhase(2, RGBColor.of(255, 80, 0), RGBColor.white(), 60 * 7);
  }

  public static MorningLightsPhase three() {
    return new MorningLightsPhase(3, RGBColor.white(), RGBColor.black(), 1);
  }

  @Override
  public int getPhaseNumber() {
    return phaseNumber;
  }

  public double getSleepTimeMs() {
    return sleepTime * 1000;
  }

  public RGBColor nextColor() {
    final Double nextRed = currentColor.getRed() + stepRed;
    final Double nextGreen = currentColor.getGreen() + stepGreen;
    final Double nextBlue = currentColor.getBlue() + stepBlue;
    this.currentColor = RGBColor.of(nextRed, nextGreen, nextBlue);
    return currentColor;
  }

  @Override
  public boolean isDone() {
    final boolean redDone = isColorDone(stepRed, currentColor.getRed(), endColor.getRed());
    final boolean greenDone = isColorDone(stepGreen, currentColor.getGreen(), endColor.getGreen());
    final boolean blueDone = isColorDone(stepBlue, currentColor.getBlue(), endColor.getBlue());
    return redDone && greenDone && blueDone;
  }

  private boolean isColorDone(final double step, final double startColor, final double endColor) {
    return step == 0 || (step > 0 && startColor >= endColor) || (step < 0 && startColor <= endColor);
  }
}
