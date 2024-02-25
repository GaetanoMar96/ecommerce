# eCommerce Backend for Online Shop

This project is a backend application for an eCommerce platform built using Java and Spring Reactive.

## Features

- **Product Management**: CRUD operations for managing products.
- **User Authentication**: Secure user authentication using JWT.
- **Reactive Programming**: Utilizes Spring WebFlux for reactive programming.

## Technologies Used

- Java
- Spring Boot
- Spring WebFlux
- Spring Data MongoDB (or any other database)
- JSON Web Tokens (JWT) for authentication

## Setup

### Prerequisites

- Java JDK 8 or higher
- Docker

### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/GaetanoMar96/ecommerce
    ```

2. Navigate to the project directory:

    ```bash
    cd ecommerce-backend
    ```

3. Build the project:

    ```bash
    ./mvnw clean package
    ```

### Configuration

1. Configure MongoDB connection settings in `application.properties` file.

2. Update JWT secret key and expiration time in `application.properties`.

### Running the Application

1. Start MongoDB using Docker image:

2. Build the Docker image:

    ```bash
    docker build -t e-commerce.
    ```

3. Run the Docker container:

    ```bash
    docker run -p 8080:8080 e-commerce
    ```
