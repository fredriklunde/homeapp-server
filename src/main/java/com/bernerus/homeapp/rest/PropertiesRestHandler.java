package com.bernerus.homeapp.rest;

import com.bernerus.homeapp.controller.PropertiesController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by andreas on 25/12/15.
 */
//@Controller
public class PropertiesRestHandler {
  private static final Logger LOG = LoggerFactory.getLogger(PropertiesRestHandler.class);

  @Autowired
  PropertiesController propertiesController;



  @RequestMapping("/property/{key}/{value}")
  public @ResponseBody String setProperty(@PathVariable String key, @PathVariable String value) {
    propertiesController.setProperty(key, value);
    return "Property set";
  }

  @RequestMapping("/property/{key}")
  public @ResponseBody String getProperty(@PathVariable String key) {
    return propertiesController.getProperty(key);
  }

}
