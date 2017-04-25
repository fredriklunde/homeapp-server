package com.bernerus.homeapp.config.zconfig;

/**
 * Created by andreas on 2017-04-22.
 */
public class ZwaveActionConfig {
  private String target;
  private String function;

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    this.function = function;
  }

  public static ZwaveActionConfig empty() {
    ZwaveActionConfig c = new ZwaveActionConfig();
    c.setTarget("");
    c.setFunction("");
    return c;
  }
}
