package com.bernerus.homeapp.model.state;

import com.bernerus.homeapp.model.RGBColor;
import org.springframework.stereotype.Component;

/**
 * Created by andreas on 2017-04-19.
 */
@Component
public class HallLights {
  private RGBColor currentColor;

  public RGBColor getCurrentColor() {
    return currentColor;
  }

  public void setCurrentColor(RGBColor currentColor) {
    this.currentColor = currentColor;
  }
}
