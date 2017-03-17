package com.bernerus.homeapp.rest;

import com.bernerus.homeapp.controller.tasks.morninglights.MorningLightsTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by andreas on 25/12/15.
 */
@Controller
public class TaskExecutorRestHandler {
  private static final Logger LOG = LoggerFactory.getLogger(TaskExecutorRestHandler.class);

  @Autowired
  MorningLightsTask morningLightsTask;

  @RequestMapping("/reportMovement/morninglights")
  public @ResponseBody String setMessage() {
    LOG.info("Executing morning lights task.");
    morningLightsTask.execute();
    return "Ok";
  }

}
