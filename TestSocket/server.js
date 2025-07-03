const WebSocket = require('ws');

const wss = new WebSocket.Server({ port: 8080 });

console.log('WebSocket server is running on ws://localhost:8080');

wss.on('connection', (ws) => {
  console.log('Client connected');

  const sendInterval = setInterval(() => {
    const dummyData = {
      canStartCharge: true,
      charging: Math.random() > 0.5,
      current: +(Math.random() * 32).toFixed(2),         // 0 - 32 A
      energy: +(Math.random() * 100).toFixed(2),         // 0 - 100 kWh
      evseConnection: Math.random() > 0.5 ? true : false,
      power: +(Math.random() * 7500).toFixed(2),         // 0 - 7500 W
      rfidScanned: Math.random() > 0.5 ? true : false, 
      soc: Math.floor(Math.random() * 101),              // 0 - 100 %
      time: Math.floor(Math.random() * 3600),            // 0 - 3600 seconds
      tod: new Date().toLocaleTimeString(),              // Time of day
      voltage: +(Math.random() * 240).toFixed(2),        // 0 - 240 V
    };

    ws.send(JSON.stringify(dummyData));
  }, 1000);

  ws.on("message", (event) => {console.log(event.data)});

  ws.on('close', () => {
    console.log('Client disconnected');
    clearInterval(sendInterval);
  });
});
