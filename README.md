# Product Management Service

This is a Spring Boot service for managing products, providing CRUD operations and Kafka integration for product creation.

## 1. Project Overview
The Product Management Service is designed to handle product data, allowing for operations such as creation, reading, updating, and deletion. It also includes integration with Kafka to enable real-time updates for product creation.

## 2. Prerequisites
Before running this project, ensure that the following software is installed on your system:
- **Java 21** 
- **Maven** or **Gradle**
- **Docker** and **Docker Compose**
- **PostgreSQL** (if not using Docker)
- **Apache Kafka** (if not using Docker)

## 3. How to Run the Project

### 3.1 Local Setup
Follow these steps to set up and run the project locally:

1. **Clone the repository and navigate to the project directory**:
   ```bash
   git clone https://github.com/your-username/product-management-service.git
   cd product-management-service
2. **Build the project**
    ```bash
   ./gradlew build
3. **Run the application**
    ```bash
   ./gradlew bootRun
   
### 3.2 Run with Docker
If you prefer running the project using Docker, follow these steps:

1. **Ensure that Docker and Docker Compose are installed and running.**

2. **Build and run the containers:**
   ```bash
    docker-compose up --build
3. **Open your web browser and navigate to http://localhost:8543/browser/**
4. **Register a new server:**
- Right-click on "Servers" and select "Register" > "Server"
- General tab: 
  - Name: `Local PostgreSQL`
- Connection tab: 
  - Host name/address: `database` 
  - Port: `5432` 
  - Username: `postgres`
  - Password: `postgres`
  - Click "Save" to register the server. You should now see the `smg` and `smg_test` databases listed under the new server.

## 4. How to Run Tests
To run all tests in the project, use the following command:
`./gradlew test`

## 5. Project Structure
Below is an overview of the main directories and files in the project:
- **src/main/java**: Contains the main source code of the application.
- **src/test/java**: Contains the tests for the application.
- **docker-compose.yml**: Configuration file for running the project with Docker, including services for PostgreSQL, Kafka, and PgAdmin.
- **init-scripts**: Directory with SQL script used to initialize the test database.
