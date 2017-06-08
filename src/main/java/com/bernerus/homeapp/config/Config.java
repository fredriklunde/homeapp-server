package com.bernerus.homeapp.config;

import com.bernerus.homeapp.config.zconfig.ZwaveConfig;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by andreas on 2017-02-27.
 */
@Configuration
@EnableAutoConfiguration
public class Config {
  private static final Logger LOG = LoggerFactory.getLogger(Config.class);

  public static final String BIG_BEDBOX_SENSOR = "ZWayVDev_zway_5-0-48-1";
  public static final String HALL_MOVEMENT_SENSOR = "ZWayVDev_zway_12-0-113-7-8-A";
  public static final String HALL_RGB_LIGHTS = "ZWayVDev_zway_13-0-51-rgb";

  public static final String BEDBOX_RGB_LIGHTS = "ZWayVDev_zway_11";

  @Autowired
  Environment env;

  @Bean
  UserSettings userSettings(){
    String host = env.getProperty("razberry.host");
    String port = env.getProperty("razberry.port");
    String username = env.getProperty("razberry.user");
    String password = env.getProperty("razberry.password");
    HttpClientConfig razberryHttpClientConfig = new HttpClientConfig(host, port, username, password);

    String mirrorHost = env.getProperty("mirror.server.host");
    String mirrorPort = env.getProperty("mirror.server.port");
    HttpClientConfig mirrorHttpClientConfig = new HttpClientConfig(mirrorHost, mirrorPort);
    return new UserSettings(mirrorHttpClientConfig, razberryHttpClientConfig);
  }

  @Bean
  ZwaveConfig zwaveConfig() {
    Gson gson = new Gson();
    try {
      InputStream in = getClass().getResourceAsStream("/devices.json");
      JsonReader jsonReader = new JsonReader(new InputStreamReader(in));
      ZwaveConfig zwaveConfig = gson.fromJson(jsonReader, ZwaveConfig.class);
      return zwaveConfig;
    } catch (Exception e) {
      throw new RuntimeException("Could not read devices.json!", e);
    }
  }


}
