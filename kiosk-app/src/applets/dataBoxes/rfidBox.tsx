import contactless from '@assets/contactless.svg'
export default function RFIDBox(){
  return(
    <div className='flex flex-col border-r p-8 items-center justify-center'>
     <img className='w-30 h-30' src={contactless}/>
     <h2>Please scan your RFID card</h2>
    </div>
  )
}