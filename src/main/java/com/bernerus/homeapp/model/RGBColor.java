package com.bernerus.homeapp.model;

/**
 * Created by andreas on 21/02/17.
 */
public class RGBColor {
  private final double red;
  private final double green;
  private final double blue;

  public RGBColor(double red, double green, double blue) {
    this.red = rgbLimit(red);
    this.green = rgbLimit(green);
    this.blue = rgbLimit(blue);
  }

  private double rgbLimit(double color) {
    return color > 255 ? 255 : color < 0 ? 0 : color;
  }

  public static RGBColor white() {
    return new RGBColor(255, 255, 255);
  }

  public static RGBColor black() {
    return new RGBColor(0, 0, 0);
  }

  public static RGBColor of(final double r, final double g, final double b) {
    return new RGBColor(r, g, b);
  }

  public double getRed() {
    return red;
  }

  public double getGreen() {
    return green;
  }

  public double getBlue() {
    return blue;
  }
}
