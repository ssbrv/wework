#!/bin/bash

cd ./frontend/
npm install
npm run build

cd ../backend
mvn spring-boot:run
