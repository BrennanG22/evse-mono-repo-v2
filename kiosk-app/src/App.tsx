import { useEffect, useRef, useState } from 'react'
import './App.css'
import FullscreenDropdown from './fullScreenDropdown.tsx'

import AuthApplet from './applets/authApplet.tsx';
import StatsApplet from './applets/statsApplet.tsx';
import webSocketHelper from './helper/webSocketHelper.ts';

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

const URL: string = "ws://localhost:8080";


//UserStateMachineValues
//0 -> IDLE - No User
//1 -> User Auth State
//2 -> Charge Screen
//3 -> Thank you screen

function App() {
  const [userState, setUserState] = useState<number>(0);
  const [requestedUserState, setRequestedUserState] = useState<number>(0);

  const [socketEVSEData, setSocketEVSEData] = useState<SocketEVSEData>();

  const [idleVisible, setIdleVisible] = useState<boolean>(true);

  const socketRef = useRef<webSocketHelper>(new webSocketHelper(URL, setStreamedDataCallback));
  // const [idleAnimating, setIdleAnimating] = useState<boolean>(false);

  useEffect(() => {
    socketRef.current.startSocket(onSocketOpen);
  }, []);

  function onSocketOpen() {
    socketRef.current.sendCommand("register", "random");
  }

  useEffect(() => {
    if (requestedUserState === -1) {
      return;
    }
    //Check for state transition
    if (requestedUserState == 0) {
      idleOpen();
    }
    setUserState(requestedUserState);
    setRequestedUserState(-1);
  }, [requestedUserState]);

  function setStreamedDataCallback(data: any) {
    setSocketEVSEData({
      CanStartCharge: data.canStartCharge,
      Charging: data.charging,
      Current: data.current,
      Energy: data.energy,
      EVSEConnection: data.evseConnection,
      Power: data.power,
      RFIDScanned: data.rfidScanned,
      SOC: data.soc,
      Time: data.time,
      TOD: data.tod,
      Voltage: data.voltage,
    });
  }


  function idleClose() {
    setIdleVisible(false);
    setUserState(1);
  }

  function idleOpen() {
    setIdleVisible(true);
    setUserState(0);
  }
  return (
    <div className='flex full-screen-div items-center justify-center'>
      <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet" />

      <FullscreenDropdown visible={idleVisible} onClose={idleClose} setAnimating={(state: boolean) => { state }} />

      {userState == 1 && <AuthApplet data={socketEVSEData} setStateHook={(state: number) => { setRequestedUserState(state) }} />}
      {userState == 2 && <StatsApplet data={socketEVSEData} onCloseHook={idleOpen} onStartHook={() => { socketRef.current.sendCommand("evseCommand", "startCharge") }} />}
    </div>
  )
}

export default App
