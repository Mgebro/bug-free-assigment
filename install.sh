#!/bin/bash

CORE_DIR="./core"
USER_DIR="./user"
ORDERS_DIR="./order"

echo "Building and publishing the core library"
cd $CORE_DIR || { echo "Core directory not found"; exit 1; }
./gradlew publishToMavenLocal || { echo "Failed to publish core library"; exit 1; }
cd - || { echo "Failed to return to previous directory"; exit 1; }

echo "Building the user project"
cd $USER_DIR || { echo "User directory not found"; exit 1; }
./gradlew build || { echo "Failed to build user project"; exit 1; }

echo "Building Docker image for the user project"
docker build -t croco/interview-user . || { echo "Failed to build Docker image for user project"; exit 1; }
cd - || { echo "Failed to return to previous directory"; exit 1; }

echo "Building the orders project"
cd $ORDERS_DIR || { echo "Orders directory not found"; exit 1; }
./gradlew build || { echo "Failed to build orders project"; exit 1; }

echo "Building Docker image for the orders project"
docker build -t croco/interview-orders . || { echo "Failed to build Docker image for orders project"; exit 1; }
cd - || { echo "Failed to return to previous directory"; exit 1; }

echo "All tasks completed successfully."

docker compose up ./compose.yaml;