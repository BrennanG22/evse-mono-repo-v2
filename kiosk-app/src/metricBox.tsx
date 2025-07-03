type NumericMetricBoxProps = {
  metricName: string;
  metricValue: string;
  metricUnit: string;
}

export default function NumericMetricBox({ metricName, metricValue, metricUnit }: NumericMetricBoxProps) {
  return (
    <div className="flex flex-col">
      <div className="flex-1">
        {metricName}
      </div>
      <div className="flex-1 flex flex-row justify-center space-x-1">
        <div>
          {metricValue}
        </div>
        <div>
          {metricUnit}
        </div>
      </div>
    </div>
  )
}