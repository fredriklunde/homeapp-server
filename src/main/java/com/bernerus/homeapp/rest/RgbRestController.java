package com.bernerus.homeapp.rest;

import com.bernerus.homeapp.config.UserSettings;
import com.bernerus.homeapp.controller.http.RazberryRgbwHttpClient;
import com.bernerus.homeapp.model.RGBColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.bernerus.homeapp.config.Config.HALL_RGB_LIGHTS;

/**
 * Created by andreas on 2017-04-19.
 */
@Controller
public class RgbRestController {
  private static final Logger LOG = LoggerFactory.getLogger(RgbRestController.class);

  private RazberryRgbwHttpClient razberryRgbwHttpClient;

  @Autowired
  public RgbRestController(UserSettings userSettings) {
    razberryRgbwHttpClient = new RazberryRgbwHttpClient(userSettings.getRazberryHttpClientConfig(), HALL_RGB_LIGHTS);
  }


  @RequestMapping("/hall/rgb/{red}/{green}/{blue}")
  @ResponseBody
  public String setHallRgb(@PathVariable Double red, @PathVariable Double green, @PathVariable Double blue) {
    razberryRgbwHttpClient.setColor(RGBColor.of(red, green, blue));
    return "Hall lights set";
  }

  @RequestMapping("/hall/rgb")
  @ResponseBody
  public RGBColor getHallRgb() {
    return razberryRgbwHttpClient.getCurrentColor();
  }

}
