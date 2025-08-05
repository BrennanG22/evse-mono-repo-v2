package com.brennan.evse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;

import com.brennan.datastate.EVSEDataState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ECOGInterface implements EVSECommunication {

  public EVSEDataState Evsedata = new EVSEDataState();

  final String WSURI = "ws://localhost:8080";

  HttpClient client;
  WebSocket webSocket;

  public ECOGInterface() {
    // TODO add a websocket helper for auto rejoin
    client = HttpClient.newHttpClient();
    webSocket = client.newWebSocketBuilder().buildAsync(URI.create(WSURI), new Listener() {
      @Override
      public void onOpen(WebSocket webSocket) {
        String json = """
            {
              "command":"register",
              "data":"random"
            }
            """.replaceAll("\\s+", ""); // Optional: only if the server is sensitive
        webSocket.sendText(json, true);
        System.out.println("Connected");
        Listener.super.onOpen(webSocket);
      }

      @Override
      public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        ObjectMapper mapper = new ObjectMapper();
        try {
          Tempdata temp = mapper.readValue(data.toString(), Tempdata.class);
          Evsedata.power.set(temp.power);
          Evsedata.soc.set(temp.soc);

        } catch (Exception e) {
          e.printStackTrace();
        }
        validate();
        return Listener.super.onText(webSocket, data, last);
      }

      @Override
      public void onError(WebSocket webSocket, Throwable error) {
        error.printStackTrace();
      }

      @Override
      public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        System.out.println("Closed: " + statusCode + " - " + reason);
        return Listener.super.onClose(webSocket, statusCode, reason);
      }
    }).join();

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
        .uri(URI.create("http://localhost:3000/api/auth?outlet=ccs"))
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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'stopCharge'");
  }

  public boolean isAuthed() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isAuthed'");
  }

  public void setDataState(EVSEDataState state) {
    this.Evsedata = state;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Tempdata {
    public Float power;
    public int soc;
  }

  @Override
  public void validate() {
    if (Evsedata.rfidScanned.get() && Evsedata.evConnected.get()) {
      Evsedata.verifyComplete.set(true);
    } else {
      Evsedata.verifyComplete.set(false);
    }
  }

}