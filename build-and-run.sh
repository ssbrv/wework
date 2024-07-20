#!/bin/bash

cd ./frontend/
npm install
npm run build-custom

cd ../backend
mvn spring-boot:run
