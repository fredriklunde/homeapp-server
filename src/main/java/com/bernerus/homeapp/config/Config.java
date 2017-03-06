package com.bernerus.homeapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by andreas on 2017-02-27.
 */
@Configuration
public class Config {
  private static final Logger LOG = LoggerFactory.getLogger(Config.class);

  public static final String BIG_BEDBOX_SENSOR = "ZWayVDev_zway_5-0-48-1";
  public static final String HALL_MOVEMENT_SENSOR = "ZWayVDev_zway_12-0-113-7-8-A";
  public static final String BEDBOX_RGB_LIGHTS = "ZWayVDev_zway_11-0-51-rgb";
  public static final String BEDBOX_DIMMER_0 = "ZWayVDev_zway_11-0-38";
  public static final String BEDBOX_DIMMER_1 = "ZWayVDev_zway_11-1-38";
  public static final String BEDBOX_DIMMER_2 = "ZWayVDev_zway_11-2-38";
  public static final String BEDBOX_DIMMER_3 = "ZWayVDev_zway_11-3-38";
  public static final String BEDBOX_DIMMER_B = "ZWayVDev_zway_11-4-38";
  public static final String BEDBOX_DIMMER_W = "ZWayVDev_zway_11-0-49-4";

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


}
