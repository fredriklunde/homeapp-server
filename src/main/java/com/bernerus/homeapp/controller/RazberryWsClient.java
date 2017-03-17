package com.bernerus.homeapp.controller;

import com.bernerus.homeapp.config.UserSettings;
import com.bernerus.homeapp.controller.http.RazberryRgbHttpClient;
import com.bernerus.homeapp.controller.http.SmartMirrorHttpClient;
import com.bernerus.homeapp.controller.tasks.HallLightsTask;
import com.bernerus.homeapp.model.NoptificationHandlerHashMap;
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
import java.util.Arrays;

import static com.bernerus.homeapp.config.Config.BEDBOX_DIMMER_0;
import static com.bernerus.homeapp.config.Config.BEDBOX_DIMMER_3;
import static com.bernerus.homeapp.config.Config.BEDBOX_DIMMER_B;
import static com.bernerus.homeapp.config.Config.BEDBOX_DIMMER_1;
import static com.bernerus.homeapp.config.Config.BEDBOX_DIMMER_2;
import static com.bernerus.homeapp.config.Config.BEDBOX_DIMMER_W;
import static com.bernerus.homeapp.config.Config.BEDBOX_RGB_LIGHTS;
import static com.bernerus.homeapp.config.Config.BIG_BEDBOX_SENSOR;
import static com.bernerus.homeapp.config.Config.HALL_MOVEMENT_SENSOR;
import static com.bernerus.homeapp.config.Config.HALL_RGB_LIGHTS;

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
  private NoptificationHandlerHashMap handlers = new NoptificationHandlerHashMap();
  private DefaultNotificationHandler defaultNotificationHandler = new DefaultNotificationHandler(this::logNoHandler);
  private RazberryRgbHttpClient bedboxRgbHttpClient;
  private RazberryRgbHttpClient hallRgbHttpClient;
  private UserSettings userSettings;

  @Autowired
  public RazberryWsClient(UserSettings userSettings, HallLightsTask hallLightsTask) {
    this.userSettings = userSettings;

    this.mirrorHttpClient = new SmartMirrorHttpClient(userSettings.getMirrorHttpClientConfig());
    this.bedboxRgbHttpClient = new RazberryRgbHttpClient(userSettings.getRazberryHttpClientConfig(), BEDBOX_RGB_LIGHTS);
    this.hallRgbHttpClient = new RazberryRgbHttpClient(userSettings.getRazberryHttpClientConfig(), HALL_RGB_LIGHTS);

    this.hallLightsTask = hallLightsTask;

    handlers.put(BIG_BEDBOX_SENSOR, new BinaryNotificationHandler(this::bigBedBoxOpen, this::bigBedBoxClose));
    handlers.put(HALL_MOVEMENT_SENSOR, new BinaryNotificationHandler(this::hallMovement, this::hallNoMovement));
    handlers.put(Arrays.asList(BEDBOX_DIMMER_0, BEDBOX_DIMMER_1, BEDBOX_DIMMER_2, BEDBOX_DIMMER_3, BEDBOX_DIMMER_B, BEDBOX_DIMMER_W),
      new DefaultNotificationHandler(this::doNothing));
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

  private void doNothing(RazberryNotificationData defaultNotificationHandler) {
    //do nothing
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
      final String deviceId = data.getSource();
      handlers.getOrDefault(deviceId, defaultNotificationHandler).handleMessage(data);

    } catch (IOException e) {
      LOG.info("Could not parse ws message");
      LOG.info(message);
    }
  }

  private String cleanupMessage(String message) {
    String clean = message.replaceAll("\\\\\"", "\"");
    clean = clean.replaceAll("\"\\{", "{");
    clean = clean.replaceAll("}\"", "}");
    return clean;
  }
}
