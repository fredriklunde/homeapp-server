package com.bernerus.homeapp.controller.device;

import com.bernerus.homeapp.config.zconfig.ZwaveConfig;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by andreas on 2017-06-08.
 */
public class ConfigHelper {

  public static ZwaveConfig readConfig() {
      Gson gson = new Gson();
      try {
        InputStream in = ConfigHelper.class.getResourceAsStream("/devices.json");
        JsonReader jsonReader = new JsonReader(new InputStreamReader(in));
        ZwaveConfig zwaveConfig = gson.fromJson(jsonReader, ZwaveConfig.class);
        return zwaveConfig;
      } catch (Exception e) {
        throw new RuntimeException("Could not read devices.json!", e);
      }
  }
}
