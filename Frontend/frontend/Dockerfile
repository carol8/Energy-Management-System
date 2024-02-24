FROM node:18-alpine

WORKDIR /ds2023/frontend

COPY package.json /ds2023/frontend/

RUN npm install 

COPY public/ /ds2023/frontend/public
COPY src/ /ds2023/frontend/src

RUN npm run build

EXPOSE 3000

CMD [ "npm", "start" ]