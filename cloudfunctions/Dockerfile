FROM node:6.11.5-alpine
MAINTAINER Bruno de Lima e Silva <dlimaun@gmail.com>
LABEL version='4.2.0'
LABEL description='Firebase CLI packaged on alpine linux'

USER node
RUN mkdir /home/node/.npm-global
ENV PATH=/home/node/.npm-global/bin:$PATH
ENV NPM_CONFIG_PREFIX=/home/node/.npm-global

RUN npm install -g firebase-tools
USER root