import logging
from flask import Flask, jsonify
from mfrc522 import SimpleMFRC522
import RPi.GPIO as GPIO
import threading
import traceback
import atexit

app = Flask(__name__)
reader = SimpleMFRC522()  # Initialize reader immediately
reader_lock = threading.Lock()

# --- Logging Setup ---
logging.basicConfig(
    level=logging.INFO,
    format='[%(asctime)s] [%(levelname)s] %(message)s',
)

logger = logging.getLogger(__name__)

def cleanup_resources():
    """Clean up GPIO resources when the program exits"""
    logger.info("Performing cleanup on program exit")
    try:
        with reader_lock:
            GPIO.cleanup()
            logger.info("GPIO resources cleaned up successfully")
    except Exception as e:
        logger.error(f"Error during cleanup: {str(e)}")
        logger.debug(traceback.format_exc())

# Register cleanup function to run at exit
atexit.register(cleanup_resources)

@app.route('/read_card', methods=['GET'])
def read_card():
    logger.info("Received request: /read_card")

    try:
        with reader_lock:
            logger.info("Waiting for a card... (blocking)")
            id, text = reader.read()
            logger.info(f"Card detected! UID: {id}, Data: {text.strip()}")
            return jsonify({
                "status": "success",
                "uid": id,
                "data": text.strip()
            }), 200

    except Exception as e:
        logger.error("Error reading card: %s", str(e))
        logger.debug(traceback.format_exc())
        return jsonify({"status": "error", "message": str(e)}), 500

if __name__ == '__main__':
    logger.info("Starting RFID REST API server with auto-initialized reader...")
    try:
        app.run(host='0.0.0.0', port=5000)
    except KeyboardInterrupt:
        logger.info("Server shutting down...")
    except Exception as e:
        logger.error(f"Unexpected error: {str(e)}")
        logger.debug(traceback.format_exc())
    finally:
        cleanup_resources()
