package com.bernerus.homeapp.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Andreas Bernérus (A189892) on 2017-03-17.
 */
@Controller
public class ConfigController {
  @RequestMapping(value = "/")
  public String index() {
    return "index.html";
  }
}
