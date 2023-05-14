# build front-end
FROM node:16-slim AS frontend

RUN npm install cross-env umi -g

WORKDIR /app

COPY ./websites/package.json /app

COPY ./websites/yarn.lock /app

RUN yarn install

COPY websites /app

RUN yarn build:prod

# service
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=frontend /app/dist /app/public

# 安装 nginx
RUN apt-get update && apt-get install -y nginx

COPY ./docker-compose/server.conf /etc/nginx/conf.d
COPY ./docker-compose/nginx.conf /etc/nginx


EXPOSE 80

CMD service nginx start
