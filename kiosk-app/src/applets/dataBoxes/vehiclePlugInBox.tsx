import electric_car from "@assets/electric_car.svg"

export default function VehiclePlugInBox() {
  return (
    <>
      <div className="flex flex-col border-l p-8 items-center justify-center">
        <img className="w-30 h-30 border-" src={electric_car} />
        <h2>Please plug in your vehicle</h2>
      </div>
    </>
  )
}