import { WebSocketServer, WebSocket } from "ws";
import { SendCommandData } from "./main.js";
import ECOGInterface from "./ecogInterface.js";

export default class ServerInterface {
  wss: WebSocketServer = new WebSocketServer({ port: 8080 });

  rfidStatus: boolean = false;

  evseInterface = new ECOGInterface();

  setRfidStatus = (val: boolean): void => {
    this.rfidStatus = val;
  };

  constructor(
    evseCommandCallback: (command: SendCommandData) => void,
    rfidCommandCallback: (
      command: SendCommandData,
      ws: WebSocket,
      setRfidCallback: (val: boolean) => void
    ) => void
  ) {
    console.log("Websocket started on ws://localhost:8080");

    this.wss.on("connection", (ws: WebSocket) => {
      console.log("Client Connected");

      ws.on("message", (message: string) => {
        const parsedData = JSON.parse(message);

        if (parsedData.command === "serverCommand") {
          if (parsedData.data === "resetSession") {
            this.setRfidStatus(false);
          }
        }

        if (parsedData.command === "evseCommand") {
          evseCommandCallback({
            command: parsedData.command,
            data: parsedData.data,
          });
        }

        if (parsedData.command === "rfidCommand") {
          rfidCommandCallback(
            { command: parsedData.command, data: parsedData.data },
            ws,
            this.setRfidStatus
          );
        }

        if (
          parsedData.command === "register" &&
          parsedData.data === "random"
        ) {
          console.log("Registered");

          const sendInterval = setInterval(() => {
            const dummyData = {
              canStartCharge: true,
              charging: Math.random() > 0.5,
              current: +(Math.random() * 32).toFixed(2),
              energy: +(Math.random() * 100).toFixed(2),
              evseConnection: Math.random() > 0.5,
              power: +(Math.random() * 7500).toFixed(2),
              rfidScanned: this.rfidStatus,
              soc: Math.floor(Math.random() * 101),
              time: Math.floor(Math.random() * 3600),
              tod: new Date().toLocaleTimeString(),
              voltage: +(Math.random() * 240).toFixed(2),
            };

            ws.send(JSON.stringify(dummyData));
          }, 1000);
        }

        if (parsedData.command === "register" &&
          parsedData.data === "ecog"
        ) {
          const sendInterval = setInterval(() => {
            ws.send(JSON.stringify(this.evseInterface.getData()));
          }, 1000)
        }

        ws.on("close", () => {
          console.log("Client disconnected");
        });
      }
      );
    });
  }
}

