package com.bernerus.homeapp.config.zconfig;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * Created by andreas on 2017-04-22.
 */
public class ZwaveEventConfig {
  private String name;
  private String id;
  private String type;
  private List<ZwaveActionConfig> actions;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<ZwaveActionConfig> getActions() {
    return isNull(actions) ? new ArrayList<>() : actions;
  }

  public void setActions(List<ZwaveActionConfig> actions) {
    this.actions = actions;
  }
}
