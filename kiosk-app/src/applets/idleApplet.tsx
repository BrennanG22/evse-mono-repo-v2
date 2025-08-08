import uofaLogo from "@assets/university-of-alberta-logo.png"
import eliteGridLogo from "@assets/ELITELabLogo.jpg"

type IdleAppletProps ={
 onCloseCallback: () => void;
}

export default function IdleApplet({onCloseCallback}: IdleAppletProps) {
  return (
    <div className="flex items-center justify-center" onClick={onCloseCallback}>
      <div className="bg-white backdrop-blur-md text-black p-10 rounded-2xl shadow-2xl max-w-xl w-11/12">
        <div className="flex flex-col h-full items-center text-center space-y-6">
          <img
            src={uofaLogo}
            alt="University of Alberta Logo"
            className="max-w-[220px] h-auto"
          />
          <img
            src={eliteGridLogo}
            alt="EliteGrid Logo"
            className="max-w-[220px] h-auto"
          />
          <h1 className="text-2xl font-bold text-gray-800 mt-10">
            Touch anywhere to begin
          </h1>
        </div>
      </div>
    </div>
  );
}