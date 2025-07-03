import ECOGInterface from "./ecogInterface.js";
import ServerInterface from "./ServerInterface.js";

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

const serverInterface = new ServerInterface(sendEVSECommand);

function sendEVSECommand(command: SendCommandData){
  evseInterface.sendData(command);
}
