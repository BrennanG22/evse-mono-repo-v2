FROM node:20

WORKDIR /app

COPY ./dist/. /app/
COPY ./package.json /app/

RUN npm install

CMD [ "node", "main.js" ]