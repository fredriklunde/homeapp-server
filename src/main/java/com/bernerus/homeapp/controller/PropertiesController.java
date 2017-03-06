package com.bernerus.homeapp.controller;

import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by andreas on 2017-03-05.
 */
@Component
public class PropertiesController {
  private static final String APP_PROPERTIES = "application.properties";
  private static final String CUSTOM_PROPERTIES = "custom.application.properties";
  private final Properties customProperties;
  private final Properties applicationProperties;

  public PropertiesController() {
    try {
      ClassLoader classLoader = getClass().getClassLoader();

      final InputStream customPropertiesFileStream = classLoader.getResourceAsStream(CUSTOM_PROPERTIES);
      customProperties = new Properties();
      customProperties.load(customPropertiesFileStream);

      final InputStream applicationPropertiesFileStream = classLoader.getResourceAsStream(APP_PROPERTIES);
      applicationProperties = new Properties();
      applicationProperties.load(applicationPropertiesFileStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setProperty(String key, String value) {
    try {
      FileOutputStream out = new FileOutputStream("custom.properties.file");
      customProperties.setProperty(key, value);
      customProperties.store(out, null);
      out.close();

    } catch (IOException e) {

    }
  }

  public String getProperty(String key) {
    return customProperties.getProperty(key);
  }
}
