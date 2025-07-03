import { buildStyles, CircularProgressbarWithChildren } from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';
import NumericMetricBox from '../metricBox';
import { useEffect, useState } from 'react';
import type { SocketEVSEData } from '../App';

type StatsAppletProps = {
  onCloseHook: () => void;
  onStartHook: () => void;
  data?: SocketEVSEData;
}

export default function StatsApplet({ onCloseHook, data, onStartHook }: StatsAppletProps) {
  const [chargeButtonActive, setChargeButtonActive] = useState<boolean>(true);
  const [chargeButtonLabel, setChargeButtonLabel] = useState<string>("Start Charging");

  useEffect(() => {
    //Change this
    setChargeButtonActive(true);
    if (chargeButtonActive) {
      setChargeButtonLabel("Start Charging");
    }
    else {
      setChargeButtonLabel("Charging not ready...")
    }
  }, [chargeButtonActive]);


  return (
    <div className='h-full w-full p-5 flex flex-col items-center justify-start'>
      {/* <div className='top-0'>
        <img className="w-full" src={logo} />
      </div> */}
      <div className='w-11/12 items-center justify-center mt-30'>
        <CircularProgressbarWithChildren
          value={data ? data.SOC : 0}
          styles={buildStyles({
            pathColor: "#f00",
            trailColor: "#eee",
            strokeLinecap: "butt",
            pathTransitionDuration: 2
          })}
        >
          <div className='flex flex-col border-b w-60 items-center justify-center'>
            <h1>SOC</h1>
            <h1>{data ? data.SOC : "--"}%</h1>
          </div>
          <NumericMetricBox metricName='Energy' metricValue={data ? data.Energy.toString() : "--"} metricUnit='KWh' />
          <NumericMetricBox metricName='Time Elapsed' metricValue={data ? data.Time.toString() : "--:--:--"} metricUnit='' />
          <div className='flex space-x-3'>

          </div>
        </CircularProgressbarWithChildren>
      </div>
      <div className='flex w-full items-center justify-center space-x-3 m-10'>
        <div className='flex-1/3 dataCap h-full'>
          <h2>Connection</h2>
        </div>
        <div className='flex-1/3 dataCap h-full'>
          <div className='flex flex-row space-x-5 items-center justify-center'>
            <NumericMetricBox metricName='Voltage' metricValue={data ? data.Voltage.toString() : "--"} metricUnit='V' />
            <NumericMetricBox metricName='Current' metricValue={data ? data.Current.toString() : "--"} metricUnit='A' />
          </div>
          <NumericMetricBox metricName='Power' metricValue={data ? data.Power.toString() : "--"} metricUnit='W' />
        </div>
        <div className='flex-1/3 dataCap h-full'>
          <NumericMetricBox metricName='Time to Bulk' metricValue='00:40:30' metricUnit='' />
          <NumericMetricBox metricName='Estimated TOD' metricValue='12:30' metricUnit='' />
        </div>
      </div>
      <div className='flex h-24 w-full space-y-2 space-x-2 mt-auto'>
        <button className={`flex-1 ${chargeButtonActive ? "active-green-button" : "bg-gray-500"} 
        h-full font-bold text-9xl`} disabled={!chargeButtonActive} onClick={onStartHook}>
          {chargeButtonLabel}
        </button>
        <button className='flex-1 active-red-button h-full' onClick={() => { onCloseHook() }}>Stop Charging and End Session</button>
      </div>
    </div>
  )
}