
---

# Glofox SaaS Platform - Class and Booking Management

This project is a RESTful API for managing classes and bookings, designed as part of a SaaS platform for studios, boutiques, and gyms. It allows the creation, retrieval, and updating of classes and bookings with proper validations to ensure data integrity.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Setup](#setup)
- [API Documentation](#api-documentation)
- [Database Configuration](#database-configuration)
- [Constraints](#constraints)
- [Code_coverage](#code_coverage)
- [Future Improvements](#future-improvements)

### Architecture Diagram
![Architecture Diagram](images/architecture-diagram.png)


## Features

- Create and manage fitness classes (e.g., Yoga, Zumba).
- Book a spot in a class with date, name and class_id.
- RESTful APIs for creating, updating, and retrieving classes and bookings.
- In-memory data management with H2 database.
- Custom error handling for better debugging and user feedback.
- Validation rules to ensure data accuracy.
- Test coverage of over **94%** to ensure robustness.

## Tech Stack

- **Java 17**
- **Spring Boot**: For building RESTful APIs.
- **H2 Database**: In-memory database for testing.
- **Swagger UI**: API documentation and testing.
- **Maven**: Dependency management and build automation.

## Setup

### Prerequisites

- Java 17 installed.
- Maven installed.

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/bhskr-j-dka/glofox.git
   cd glofox
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access the Swagger UI for API documentation:
   - URL: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

5. Access the H2 database console:
   - URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: `password`


## API Documentation

### Class Endpoints

- **POST /classes**: Create a new class.
  - **Validations**:
    - **Start Date <= End Date**: The start date of a class must be before or equal to the end date.
    - **Class Date Overlap**: A new class cannot overlap with the date range of an existing class.
  - **Sample request body**:
    ```json
    {
      "name": "Pilates Advanced",
      "startDate": "2009-01-01",
      "endDate": "2011-01-01",
      "capacity": 10
    }
    ```
  - **Possible Responses**:
    - **201 Created**: If the class is created successfully.
    - **400 Bad Request**: If:
      - The start date is after the end date:
        ```json
        {
          "error": "The start date must be on or before the end date."
        }
        ```
      - There is an overlap with an existing class:
        ```json
        {
          "error": "Class overlaps with existing classes in the given date range."
        }
        ```

- **PUT /classes/{id}**: Update an existing class by ID.
  - **Validations**:
    - **Start Date <= End Date**: The start date must be before or equal to the end date when updating.
    - **Class Date Overlap**: Updates must not cause date overlaps with other classes.
  - **Sample request body**:
    ```json
    {
      "name": "Yoga Advanced",
      "startDate": "2004-01-01",
      "endDate": "2005-01-01",
      "capacity": 18
    }
    ```
  - **Possible Responses**:
    - **200 OK**: If the class is updated successfully.
    - **400 Bad Request**: If:
      - The start date is after the end date:
        ```json
        {
          "error": "The start date must be on or before the end date."
        }
        ```
      - There is an overlap with an existing class:
        ```json
        {
          "error": "Class overlaps with existing classes in the given date range."
        }
        ```
    - **404 Not Found**: If the class ID does not exist:
        ```json
        {
          "error": "Class not found with ID: 1"
        }
        ```

- **GET /classes**: Retrieve all classes.
  - **Returns**: A list of all classes, or a `204 No Content` status if no classes are available.

### Booking Endpoints

- **POST /bookings**: Create a new booking.
  - **Validations**:
    - **Booking Date Within Class Range**: The booking date must fall within the class's start and end date.
      
  - **Sample request body**:
    ```json
    {
      "name": "Alice",
      "date": "2001-01-01",
      "classId": 1
    }
    ```
  - **Possible Responses**:
    - **201 Created**: If the booking is created successfully.
    - **400 Bad Request**: If:
      - The booking date is outside the class date range:
        ```json
        {
          "error": "Booking date must be in the range of the class start date and end date."
        }
        ```
      - The class ID is invalid:
        ```json
        {
          "error": "Invalid Class ID. Please provide a valid class ID."
        }
        ```


- **GET /bookings**: Retrieve all bookings.
  - **Returns**: A list of all bookings, or a `204 No Content` status if no bookings are available.

- **PUT /bookings/{id}**: Update an existing booking by ID.
  - **Validations**:
    - **Booking Date Within Class Range**: The booking date must remain within the class's start and end date after an update.
  - **Sample request body**:
    ```json
    {
      "name": "Alice Updated",
      "date": "2001-01-02",
      "classId": 1
    }
    ```
  - **Possible Responses**:
    - **200 OK**: If the booking is updated successfully.
    - **400 Bad Request**: If:
      - The booking date is outside the class date range:
        ```json
        {
          "error": "Booking date must be in the range of the class start date and end date."
        }
        ```
      - The class ID is invalid:
        ```json
        {
          "error": "Invalid Class ID. Please provide a valid class ID."
        }
        ```
    - **404 Not Found**: If the booking ID does not exist:
        ```json
        {
          "error": "Booking not found with ID: 1"
        }
        ```

## Database Configuration

The project uses H2 as an in-memory database for simplicity and ease of testing. Initial data is preloaded using `data.sql`, so some classes and bookings will already be present when the application starts.

## Constraints

- **Class Date Overlap**: A new class cannot overlap with the date range of an existing class.
- **Booking Date Validation**: The booking date must fall within the start and end date of the class it is associated with.

## Code_coverage
![Code_coverage](images/code_coverage.png)
## Future Improvements

- The feature of handling overbooking.
- Handle concurrency as multiple users may access the same resources at the same time.
- Use of isActive Flag: Introduce an isActive field for both Class and Booking entities. This allows for soft deletion, where records can be marked inactive instead of permanently deleted, aiding in better tracking and auditing.
- Booking Restrictions: Include an additional constraint in the Booking entity to ensure that the same user (identified by a unique key) cannot book the same class for the same date. This would prevent duplicate bookings and enhance booking logic.
- Integrate a database like PostgreSQL for production use.
- Implement pagination for retrieving large sets of classes or bookings.
- Add user authentication for secure access to APIs.


## License

This project is licensed under the MIT License.

---
