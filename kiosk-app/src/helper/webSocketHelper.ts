
const MAX_RETRY: number = 20;

export default class webSocketHelper {
  private socketURL: string;
  private onMessageCallback: ((data: any) => void) | undefined;
  private tryingConnectionLock: boolean = false;

  socket: WebSocket | undefined;

  constructor(url: string, callBack?: (data: any) => void) {
    if (callBack !== undefined) {
      this.onMessageCallback = callBack;
    }
    this.socketURL = url;
    // this.startSocket().catch(() => { });
  };

  async startSocket(onStartCallback?: () => void): Promise<void> {
    if (this.socket?.OPEN) { return Promise.resolve() }
    this.tryingConnectionLock = true;
    return new Promise((resolve, reject) => {
      const attemptConnection = (retryCount: number) => {
        if (retryCount >= MAX_RETRY) {
          this.tryingConnectionLock = false;
          return reject("Websocket failed to connect");
        }

        this.socket = new WebSocket(this.socketURL);

        this.socket.onopen = () => {
          this.tryingConnectionLock = false;
          if (onStartCallback) {
            onStartCallback();
          }
          resolve();
        };

        this.socket.onmessage = (event) => {
          const message: string = event.data.toString();
          const jsonData = JSON.parse(message);

          if (this.onMessageCallback !== undefined && jsonData) {
            this.onMessageCallback(jsonData);
          }
        };


        this.socket.onerror = () => {
          setTimeout(() => attemptConnection(retryCount + 1), 1000);
        };

        this.socket.onclose = () => {
        };
      };

      attemptConnection(0);
    });
  }

  async ensureSocketOpen(): Promise<void> {
    if (this.socket?.readyState !== WebSocket.OPEN && !this.tryingConnectionLock) {
      await this.startSocket()
        .then(() => { return Promise.resolve() })
        .catch(() => { return Promise.reject() })
    }
    else if (this.tryingConnectionLock) {
      return Promise.reject("Server is attempting to connect to WebSocket, please wait");
    }
    return Promise.resolve();
  }

  async sendData(data: string) {
    await this.ensureSocketOpen()
    this.socket?.send(JSON.stringify({
      data: data
    }));
  }

  sendCommand(command: string, data: string) {
    console.log(this.socket);
    this.socket?.send(JSON.stringify({
      command: command,
      data: data
    }));
  }

  closeSocket() {
    this.socket?.close();
  }
}