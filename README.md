# Project README

## Overview

This repository contains a Java 21 project structured into three distinct modules:

1. **Core**: A library that needs to be published to Maven locally.
2. **User**: A microservice responsible for user management.
3. **Order**: A microservice responsible for order management.

Project includes `compose.yaml` for setting up a local Docker environment and `install.sh` script for building and
starting the production Docker environment.

## Structure

- **core/**: Contains the core library code.
- **user/**: Contains the user microservice code.
- **order/**: Contains the order microservice code.
- **compose.yaml**: Docker Compose file for local development environment.
- **install.sh**: Script to build and start the production Docker environment.

## Prerequisites

- Java 21
- Gradle
- Docker
- Docker Compose

## Setup

### Production Environment

Ensure you are in the root directory of the repository.

Give the read,write,execute permission for install script

  ```bash
  chmod +x install.sh
   ```

Run the `install.sh` script to build and start the production Docker environment:

  ```bash
  sudo ./install.sh
   ```

### Local Environment

#### Core Library

1. Navigate to the core module directory:
   ```bash
   cd core
   ```
2. Build and publish the core library to the local Maven repository:
   ```bash
   ./gradlew publishToMavenLocal
   ```

#### User Microservice

1. Navigate to the user module directory:
   ```bash
   cd user
   ```
2. Build the user microservice:
   ```bash
   ./gradlew build
   ```

#### Order Microservice

1. Navigate to the order module directory:
   ```bash
   cd order
   ```
2. Build the order microservice:
   ```bash
   ./gradlew build
   ```

Ensure you are in the root directory of the repository.
Build and start the local Docker environment using Docker Compose:

   ```bash
   docker compose
   ```

1. Run user microservice

  ```bash
  java -jar user/build/libs/user-0.0.1-SNAPSHOT.jar
   ```

1. Run order microservice

  ```bash
  java -jar order/build/libs/order-0.0.1-SNAPSHOT.jar
   ```

## Usage

### OpenAPI Documentation

Both microservices provide OpenAPI documentation accessible at `server:port/swagger-ui/index.html`

