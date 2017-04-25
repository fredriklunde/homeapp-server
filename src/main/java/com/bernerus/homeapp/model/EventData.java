package com.bernerus.homeapp.model;

import com.bernerus.homeapp.controller.device.ZwaveDeviceController;

/**
 * Created by andreas on 2017-04-23.
 */
public class EventData<T> {
  final ZwaveDeviceController sender;
  final T value;

  public EventData(ZwaveDeviceController sender, T value) {
    this.sender = sender;
    this.value = value;
  }

  public ZwaveDeviceController getSender() {
    return sender;
  }

  public T getValue() {
    return value;
  }

  public Boolean getValueAsBoolean() {
    return (Boolean) value;
  }

  public String getValueAsString() {
    return (String) value;
  }

  public Integer getValueAsInteger() {
    return (Integer) value;
  }

  public Double getValueAsDouble() {
    return (Double) value;
  }

  @Override
  public String toString() {
    return "EventData{" +
      "sender=" + sender.getId() +
      ", value=" + value +
      '}';
  }
}
