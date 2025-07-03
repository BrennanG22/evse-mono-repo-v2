import { motion, AnimatePresence } from "framer-motion";
import uofaLogo from "./assets/university-of-alberta-logo.png"
import eliteGridLogo from "./assets/ELITELabLogo.jpg"

type FullscreenDropdownProps = {
  visible: boolean;
  onClose: () => void
  setAnimating: (state: boolean) => void;
}

function FullscreenDropdown({ visible, onClose, setAnimating }: FullscreenDropdownProps) {
  return (
    <AnimatePresence>
      {visible && (
        <motion.div
          className="fixed inset-0 bg-black bg-opacity-90 z-[9999] flex items-center justify-center"
          initial={{ y: "-100%" }}
          animate={{ y: 0 }}
          exit={{ y: "-100%" }}
          transition={{ type: "spring", stiffness: 80, damping: 18 }}
          onClick={onClose}
          onAnimationStart={() => setAnimating(true)}
          onAnimationComplete={() => setAnimating(false)}
        >
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
        </motion.div>
      )}
    </AnimatePresence>
  );
}


export default FullscreenDropdown;
