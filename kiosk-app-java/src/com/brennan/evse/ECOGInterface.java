package com.brennan.evse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.time.Duration;
import java.util.concurrent.*;

import com.brennan.datastate.EVSEDataState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ECOGInterface implements EVSECommunication {

  public EVSEDataState Evsedata = new EVSEDataState();

  private static final String WSURI = "ws://10.20.27.100/api/outlets/ccs/statestream";
  private static final int RETRY_INTERVAL_SECONDS = 10;

  private final HttpClient client;
  private WebSocket webSocket;
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  public ECOGInterface() {
    client = HttpClient.newHttpClient();
    connectWebSocket();
  }

  private void connectWebSocket() {
    System.out.println("Attempting to connect WebSocket...");
    client.newWebSocketBuilder()
      .connectTimeout(Duration.ofSeconds(5))
      .buildAsync(URI.create(WSURI), new Listener() {

        @Override
        public void onOpen(WebSocket webSocket) {
          ECOGInterface.this.webSocket = webSocket;
          System.out.println("WebSocket connected.");

          String json = """
              {
                "command":"register",
                "data":"random"
              }
              """.replaceAll("\\s+", "");

          webSocket.sendText(json, true);
          Listener.super.onOpen(webSocket);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
          ObjectMapper mapper = new ObjectMapper();
          try {
            Tempdata temp = mapper.readValue(data.toString(), Tempdata.class);
            Evsedata.voltage.set(temp.pv);
            Evsedata.current.set(temp.pc);
            Evsedata.soc.set(temp.soc);
          } catch (Exception e) {
            e.printStackTrace();
          }
          validate();
          return Listener.super.onText(webSocket, data, last);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
          System.err.println("WebSocket error occurred:");
          error.printStackTrace();
          retryConnection();
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
          System.out.printf("WebSocket closed: %d - %s%n", statusCode, reason);
          retryConnection();
          return Listener.super.onClose(webSocket, statusCode, reason);
        }
      }).exceptionally(ex -> {
        System.err.println("Initial WebSocket connection failed:");
        ex.printStackTrace();
        retryConnection();
        return null;
      });
  }

  private void retryConnection() {
    scheduler.schedule(this::connectWebSocket, RETRY_INTERVAL_SECONDS, TimeUnit.SECONDS);
  }

  public void startCharge() {
    String bodyJSON = """
        {
          "user": "controller",
          "auth": true,
          "plug_type": "ccs"
        }
        """;

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://10.20.27.100/api/auth?outlet=ccs"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(bodyJSON))
        .build();

    try {
      client.send(request, HttpResponse.BodyHandlers.discarding());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void stopCharge() {
    String bodyJSON = """
        {
          "user": "controller",
          "auth": false,
          "plug_type": "ccs"
        }
        """;

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://10.20.27.100/api/auth?outlet=ccs"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(bodyJSON))
        .build();

    try {
      client.send(request, HttpResponse.BodyHandlers.discarding());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public boolean isAuthed() {
    throw new UnsupportedOperationException("Unimplemented method 'isAuthed'");
  }

  public void setDataState(EVSEDataState state) {
    this.Evsedata = state;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Tempdata {
    public Float pv;
    public Float pc;
    public int soc;
  }

  @Override
  public void validate() {
    Evsedata.verifyComplete.set(
      Evsedata.rfidScanned.get() && Evsedata.evConnected.get()
    );
  }
}
