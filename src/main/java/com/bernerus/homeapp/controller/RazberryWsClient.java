package com.bernerus.homeapp.controller;

import com.bernerus.homeapp.config.UserSettings;
import com.bernerus.homeapp.config.zconfig.ZwaveConfig;
import com.bernerus.homeapp.controller.device.DeviceControllerFactory;
import com.bernerus.homeapp.controller.device.UnknownDeviceController;
import com.bernerus.homeapp.controller.device.ZwaveDeviceController;
import com.bernerus.homeapp.controller.http.RazberryRgbwHttpClient;
import com.bernerus.homeapp.controller.http.SmartMirrorHttpClient;
import com.bernerus.homeapp.controller.tasks.HallLightsTask;
import com.bernerus.homeapp.model.RGBColor;
import com.bernerus.homeapp.model.RazberryNotificationData;
import com.bernerus.homeapp.model.RazberryWsNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bernerus.homeapp.config.Config.BEDBOX_RGB_LIGHTS;
import static java.util.Objects.nonNull;

/**
 * Created by andreas on 21/02/17.
 */
@Component
@ClientEndpoint
public class RazberryWsClient extends TextWebSocketHandler {
  private static final Logger LOG = LoggerFactory.getLogger(RazberryWsClient.class);
  private final SmartMirrorHttpClient mirrorHttpClient;
  private final HallLightsTask hallLightsTask;
  private Session userSession = null;
  private RazberryRgbwHttpClient bedboxRgbHttpClient;
  private UserSettings userSettings;
  private final Pattern deviceIdPattern;
  private final DeviceControllerFactory deviceControllerFactory;

  private Map<String, ZwaveDeviceController> deviceControllers = new HashMap<>();

  @Autowired
  public RazberryWsClient(UserSettings userSettings, ZwaveConfig zwaveZwaveConfig, HallLightsTask hallLightsTask, DeviceControllerFactory deviceControllerFactory) {
    this.userSettings = userSettings;
    this.deviceControllerFactory = deviceControllerFactory;

    this.mirrorHttpClient = new SmartMirrorHttpClient(userSettings.getMirrorHttpClientConfig());
    this.bedboxRgbHttpClient = new RazberryRgbwHttpClient(userSettings.getRazberryHttpClientConfig(), BEDBOX_RGB_LIGHTS);

    this.hallLightsTask = hallLightsTask;

    registerDevices(zwaveZwaveConfig);

    //Setup pattern for deviceId
    String deviceIdRegex = "(ZWayVDev[^\\-]*)-.*";
    deviceIdPattern = Pattern.compile(deviceIdRegex);
  }

  private void registerDevices(ZwaveConfig zwaveZwaveConfig) {
    //Create deviceControllers
    zwaveZwaveConfig.getDevices().values().forEach(deviceConfig -> {
      ZwaveDeviceController deviceController = deviceControllerFactory.createZwaveDevice(deviceConfig);
      deviceControllers.put(deviceConfig.getId(), deviceController);
    });

    //Add targets
    zwaveZwaveConfig.getDevices().values().forEach(deviceConfig -> {
      ZwaveDeviceController deviceController = deviceControllers.get(deviceConfig.getId());
      Map<String, ZwaveDeviceController> eventTargets = new HashMap<>();
      deviceConfig.getEvents()
        .forEach(eventConfig -> {
          eventConfig.getActions().forEach(actionConfig -> {
            ZwaveDeviceController targetDeviceController = deviceControllers.get(actionConfig.getTarget());
            if (nonNull(targetDeviceController)) {
              eventTargets.putIfAbsent(eventConfig.getId(), targetDeviceController);
            }
          });
        });
      deviceController.setTargets(eventTargets);
      eventTargets.values().forEach(target -> {
        target.addCaller(deviceController);

      });
    });
  }

  @PostConstruct
  public void init() {
    try {
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      URI endpointURI = URI.create("ws://" + userSettings.getRazberryHttpClientConfig().getHost() + ":" + userSettings.getRazberryHttpClientConfig().getPort());
      container.connectToServer(this, endpointURI);
    } catch (Exception e) {
      LOG.error("Could not connect to websocket. Re-initialize when config is set!");
    }
  }

  private void logNoHandler(RazberryNotificationData razberryWsNotification) {
    LOG.info("No handler specified for {}", razberryWsNotification.getSource());
    LOG.info(razberryWsNotification.toString());
  }

  private void bigBedBoxOpen(RazberryNotificationData razberryWsNotification) {
    LOG.info("Turning bed lights on!");
    bedboxRgbHttpClient.setColor(RGBColor.white());
  }

  private void bigBedBoxClose(RazberryNotificationData razberryWsNotification) {
    LOG.info("BedBox closed: Turning bed lights off!");
    bedboxRgbHttpClient.setColor(RGBColor.black());
  }

  private void hallMovement(RazberryNotificationData razberryWsNotification) {
    LOG.info("Reporting movement");
    mirrorHttpClient.reportMovement();
    hallLightsTask.reportMovement();
  }

  private void hallNoMovement(RazberryNotificationData razberryWsNotification) {
    LOG.info("No movement report!");
    hallLightsTask.reportNoMovement();
  }

  @OnOpen
  public void onOpen(final Session userSession) {
    LOG.info("WS open to Razberry");
    this.userSession = userSession;
  }

  @OnClose
  public void onClose(final Session userSession, final CloseReason reason) {
    LOG.info("WS closed to Razberry");
    this.userSession = null;
  }

  @OnMessage
  public void onMessage(final String message) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      RazberryWsNotification notification = mapper.readValue(message, RazberryWsNotification.class);
      RazberryNotificationData data = mapper.readValue(notification.getData(), RazberryNotificationData.class);
      Matcher deviceIdMatcher = deviceIdPattern.matcher(data.getSource());
      if (deviceIdMatcher.matches()) {
        String deviceIdShort = deviceIdMatcher.group(1);
        if (deviceControllers.containsKey(deviceIdShort)) {
          deviceControllers.get(deviceIdShort).handleNotification(data);
        } else {
          LOG.warn("No device with id '{}' has been registered. Registering an unknown device", deviceIdShort);
          UnknownDeviceController unknownDevice = deviceControllerFactory.createUnknownDeviceController(deviceIdShort);
          deviceControllers.put(deviceIdShort, unknownDevice);
        }
      } else {
        LOG.warn("Bad regex for: {}", data.getSource());
      }

    } catch (IOException e) {
      LOG.info("Could not parse ws message");
      LOG.info(message);
    }
  }
}
