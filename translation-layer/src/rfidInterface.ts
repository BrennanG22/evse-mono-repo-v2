import { SendCommandData } from "./main";
import ServerInterface from "./ServerInterface";

import { WebSocketServer, WebSocket } from "ws";


export default class RFIDInterface {

  readCommand(data: SendCommandData, ws: WebSocket, setStatusCallback: (val: boolean) => void) {
    if (data.data == "readRFID") {
      this.isCardPresent().then((value) => {
        setStatusCallback(value);
      }).catch(()=>{setStatusCallback(false)});
    }
  }

  async isCardPresent(): Promise<boolean> {
    const apiUrl = "http://host.docker.internal:5000/read_card";
    let cardDetected = false;

    // Timeout helper using AbortController
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 7000); // 7 seconds read timeout

    try {
      const response = await fetch(apiUrl, {
        method: "GET",
        signal: controller.signal,
      });
      clearTimeout(timeoutId);

      if (response.ok) {
        const json = await response.json();
        const status = json.status || "";

        if (status.toLowerCase() === "success") {
          cardDetected = true;
          console.log("Card detected! UID:", json.uid);
        } else {
          console.log("API responded but no card detected:", status);
        }
      } else {
        console.log("API returned non-OK status:", response.status);
      }
    } catch (error) {
      if (error.name === "AbortError") {
        console.log("Request timed out");
        return Promise.reject();
      } else {
        console.log("Exception while calling RFID API:", error.message);
        return Promise.reject();
      }
    }

    return cardDetected;
  }
}