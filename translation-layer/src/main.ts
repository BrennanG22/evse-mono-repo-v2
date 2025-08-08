import ECOGInterface from "./ecogInterface.js";
import RFIDInterface from "./rfidInterface.js";
import ServerInterface from "./ServerInterface.js";

import { WebSocketServer, WebSocket } from "ws";

export type SocketEVSEData = {
  CanStartCharge: boolean;
  Charging: number;
  Current: number;
  Energy: number;
  EVSEConnection: boolean;
  Power: number;
  RFIDScanned: boolean;
  SOC: number;
  Time: number;
  TOD: number;
  Voltage: number;
};

export type SendCommandData = {
  command: string;
  data: string;
}


const evseInterface = new ECOGInterface();
const rfidInterface = new RFIDInterface();

const serverInterface = new ServerInterface(sendEVSECommand, sendRFIDCommand);

function sendEVSECommand(command: SendCommandData){
  evseInterface.sendData(command);
}

function sendRFIDCommand(command: SendCommandData, ws: WebSocket, setStatusCallback:(val: boolean)=>void){
  rfidInterface.readCommand(command, ws, setStatusCallback);
}
