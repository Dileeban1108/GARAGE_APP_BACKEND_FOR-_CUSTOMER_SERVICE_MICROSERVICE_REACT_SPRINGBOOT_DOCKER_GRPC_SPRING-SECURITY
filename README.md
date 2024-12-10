# Garage Application->Customer Service - Back-End

## Project Overview

This is the back-end of the **Garage Application**, built using **Spring Boot** with a **microservices architecture**. The back-end handles the core business logic of the application, including user authentication, profile management, and booking services. The services communicate with each other using **gRPC** for efficient inter-service communication.

Key features of the back-end include:
- **User Authentication**: JWT-based authentication and secure login.
- **Service Booking**: Managing service bookings with real-time status updates.
- **Microservices Communication**: Using **gRPC** for communication between microservices.
- **Database Management**: Handling data storage and retrieval for user profiles and booking information.

### Key Technologies Used
- **Spring Boot** for back-end development.
- **Spring Security (JWT)** for user authentication.
- **gRPC** for inter-service communication.
- **MySQL** or **PostgreSQL** (depending on the configuration) for data storage.
- **Docker** for containerization.

## Features

1. **User Authentication**:
   - **Sign-Up**: Handles new user registration and stores user data.
   - **Sign-In**: Secure login using JWT tokens for authentication.
   
2. **Profile Management**:
   - Users can edit and update their profile information.

3. **Service Booking**:
   - Users can book services, view available services, and track their booking statuses.
   - Booking statuses are updated in real-time (Accepted/Rejected).

4. **gRPC Communication**:
   - The service uses **gRPC** to send and receive booking details to and from other microservices in the system.

5. **Database**:
   - The back-end is connected to a database (e.g., MySQL/PostgreSQL) to persist user and booking data.

## Project Setup

### Prerequisites

Before setting up the project, ensure you have the following installed:
- [Java 17](https://adoptopenjdk.net/)
- [Gradle](https://maven.apache.org/)
- [Docker](https://www.docker.com/get-started)
- [MySQL](https://www.mysql.com/)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/Dileeban1108/GARAGE_APP_BACKEND_FOR-_CUSTOMER_SERVICE_MICROSERVICE_REACT_SPRINGBOOT_DOCKER_GRPC_SPRING-SECURITY.git
