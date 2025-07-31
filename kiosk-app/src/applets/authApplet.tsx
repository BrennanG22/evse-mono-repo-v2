import type { SocketEVSEData } from '../App';
import RFIDBox from './dataBoxes/rfidBox'
import VehiclePlugInBox from './dataBoxes/vehiclePlugInBox';
import CheckMark from "@assets/check_circle.svg";

type AuthAppletProps = {
  setStateHook: (state: number) => void;
  data?: SocketEVSEData;
}

export default function AuthApplet({ setStateHook, data }: AuthAppletProps) {
  const canProceed = data?.RFIDScanned && data.EVSEConnection;
  return (
    <div>
      <div className='flex'>
        <div className='relative'>
          <RFIDBox />
          <div className={`absolute inset-0 backdrop-blur-xs bg-white/30 m-2 transition-all duration-700 ease-in
            ${data?.RFIDScanned ? 'opacity-100' : 'opacity-0 pointer-events-none'}`}>
            <img
              src={CheckMark}
              alt='overlay'
              className='absolute top-0 left-1/2 transform -translate-x-1/2 h-50 object-cover'
            />
          </div>
        </div>
        <div className='relative'>
          <VehiclePlugInBox />
          <div className={`absolute inset-0 backdrop-blur-xs bg-white/30 m-2 transition-all duration-700 ease-in
            ${data?.EVSEConnection ? 'opacity-100' : 'opacity-0 pointer-events-none'}`}>
            <img
              src={CheckMark}
              alt='overlay'
              className='absolute top-0 left-1/2 transform -translate-x-1/2 h-50 object-cover'
            />
          </div>
        </div>
      </div>
      <div className='space-x-2 mt-10'>
        <button className={`${canProceed?'active-green-button':'disabled-grey-button'} w-40`} disabled={canProceed} onClick={() => { setStateHook(2) }}>Next</button>
        <button className='active-red-button w-40' onClick={() => { setStateHook(0) }}>Cancel</button>
      </div>
    </div>
  )
}