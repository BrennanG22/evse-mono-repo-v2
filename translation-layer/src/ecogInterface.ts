import webSocketHelper from "./helpers/webSocketHelper.js";
import { SendCommandData, SocketEVSEData } from "./main.js";

const ECOG_URL: string = "http://10.0.27.100";


export default class ECOGInterface {
  ecogSocket: webSocketHelper = new webSocketHelper(ECOG_URL, this.onNewMessage);

  message: any = "";

  ECOGInterface() {
    this.ecogSocket.startSocket();
  }

  private onNewMessage(data: string) {
    this.message = JSON.parse(data);
  }

  getData(): SocketEVSEData {
    const socketData: SocketEVSEData = {
      CanStartCharge: true,
      Charging: 1,
      Current: this.message.pc,
      Energy: 0,
      EVSEConnection: false,
      Power: this.message.pp,
      RFIDScanned: false,
      SOC: this.message.soc*100,
      Time: 0,
      TOD: 0,
      Voltage: this.message.pv
    }

    return socketData;
  }

  sendData(data: SendCommandData): void {
    if (data.data === "startCharge") {
      fetch(`${ECOG_URL}/api/auth?outlet=ccs`, {
        method: 'POST',
        body: JSON.stringify({
          user: "translationLayer",
          auth: true,
          plug_type: "ccs"
        })
      })
        .catch(error => console.error('Error fetching data:', error));
    }

    if (data.data === "stopCharge") {
      fetch(`${ECOG_URL}/api/auth?outlet=ccs`, {
        method: 'POST',
        body: JSON.stringify({
          user: "translationLayer",
          auth: false,
          plug_type: "ccs"
        })
      })
        .catch(error => console.error('Error fetching data:', error));
    }
  }

}