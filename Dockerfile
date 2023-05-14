# build front-end
FROM node:16-slim AS frontend

RUN npm install cross-env umi -g

WORKDIR /app

COPY ./websites/package.json /app

COPY ./websites/yarn.lock /app

RUN yarn install

COPY websites /app

RUN yarn build:prod

FROM nginx
LABEL maintainer="admin@jerry.lewis"

WORKDIR /app

COPY --from=frontend /app/dist /app/public

COPY ./docker-compose/server.conf /etc/nginx/conf.d
COPY ./docker-compose/nginx.conf /etc/nginx

EXPOSE 80
