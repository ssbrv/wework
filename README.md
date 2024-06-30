# WeWork

A project manager application, where you can join projects or create your own and collaborate with people killing tasks together.

# Java

- Version: 17

# Node

- Version: 20.11.1

# Build and Run

Run script:

- ./build-and-run.sh

Or manually:

0. make sure docker engine is running
1. cd frontend
2. npm install
3. npm run build-custom
4. cd ../backend
5. mvn spring-boot:run

# How to access databse

Change in the following script id of your docker container and run:

- ./run-docker-shell.sh
