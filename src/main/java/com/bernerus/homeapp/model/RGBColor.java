package com.bernerus.homeapp.model;

import java.util.Arrays;
import java.util.List;

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

  public static RGBColor of(final double r, final double g, final double b) {
    return new RGBColor(r, g, b);
  }

  public static RGBColor white() {
    return new RGBColor(255, 255, 255);
  }

  public static RGBColor black() {
    return new RGBColor(0, 0, 0);
  }

  public static RGBColor red() {
    return new RGBColor(255, 0, 0);
  }

  public static RGBColor orange() {
    return new RGBColor(255, 127, 0);
  }

  public static RGBColor yellow() {
    return new RGBColor(255, 255, 0);
  }

  public static RGBColor lime() {
    return new RGBColor(127, 255, 0);
  }

  public static RGBColor green() {
    return new RGBColor(0, 255, 0);
  }

  public static RGBColor spring() {
    return new RGBColor(0, 255, 127);
  }

  public static RGBColor teal() {
    return new RGBColor(0, 255, 255);
  }

  public static RGBColor sky() {
    return new RGBColor(0, 127, 255);
  }

  public static RGBColor blue() {
    return new RGBColor(0, 0, 255);
  }

  public static RGBColor purple() {
    return new RGBColor(127, 0, 255);
  }

  public static RGBColor magenta() {
    return new RGBColor(255, 0, 255);
  }

  public static RGBColor pink() {
    return new RGBColor(255, 0, 127);
  }

  public static List<RGBColor> predefined() {
    return Arrays.asList(RGBColor.black(), RGBColor.red(), RGBColor.orange(), RGBColor.yellow(), RGBColor.lime(), RGBColor.green(), RGBColor.spring(),
      RGBColor.teal(), RGBColor.sky(), RGBColor.blue(), RGBColor.purple(), RGBColor.magenta(), RGBColor.pink(), RGBColor.white());
  }

  private double rgbLimit(double color) {
    return color > 255 ? 255 : color < 0 ? 0 : color;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RGBColor rgbColor = (RGBColor) o;

    if (Double.compare(rgbColor.red, red) != 0) return false;
    if (Double.compare(rgbColor.green, green) != 0) return false;
    return Double.compare(rgbColor.blue, blue) == 0;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(red);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(green);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(blue);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "RGBColor{" +
      "red=" + red +
      ", green=" + green +
      ", blue=" + blue +
      '}';
  }
}
