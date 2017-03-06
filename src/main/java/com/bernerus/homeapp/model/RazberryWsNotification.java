package com.bernerus.homeapp.model;

/**
 * Created by andreas on 21/02/17.
 */
public class RazberryWsNotification {
  //{"type":"me.z-wave.notifications.add","data":"{\"id\":1487701837539,\"timestamp\":\"2017-02-21T18:30:37.539Z\",\"level\":\"device-info\",\"message\":{\"dev\":\"Hall Movement\",\"l\":\"off\"},\"type\":\"device-OnOff\",\"source\":\"ZWayVDev_zway_12-0-113-7-8-A\",\"redeemed\":false}"}

  private String type;
  private String jsonData;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getData() {
    return jsonData;
  }

  public void setData(String jsonData) {
    this.jsonData = jsonData;
  }

  @Override
  public String toString() {
    return "RazberryWsNotification{" +
      "type='" + type + '\'' +
      ", data=" + jsonData +
      '}';
  }
}
