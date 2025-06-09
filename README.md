# Gusgo - Person

A project that implements a CRUD (Create, Read, Update, Delete) system to manage information about people.

## Description

This project allows users to manage a list of people, including their personal information such as name, nickname, address, and other relevant data. The goal is to provide a simple application to understand the concepts of CRUD and the use of frameworks like Spring Boot.

## Technologies Used

- Java 21
- Spring Boot 3.5.0
- Spring Data JPA
- PostgreSQL
- Lombok
- MapStruct
- Gradle
- JUnit 5 (for testing)

## Features

- Add a new person
- List all people
- Update information of an existing person
- Remove a person
- Upload xls list people

## Prerequisites

Before you begin, make sure you have the following installed on your machine:

- JDK 21
- PostgreSQL
- Gradle
- docker-compose

## Installation on dev

1. Clone the repository:
   ```bash
   git clone https://github.com/gustavoborges25/gusgo-person
   cd gusgo-person
   
2. Set up the PostgreSQL database:
   ```bash
   docker-compose up -d 
   ## docker-compose will spin up an instance of the PostgreSQL database. If you have any questions, look at the docker-compose.yml file in the project root.
3. Compile the project:
   ```bash
   ./gradlew build

## Contribution
Contributions are welcome! Feel free to open an issue or submit a pull request.

## Contact
For any questions, feel free to reach out:

- Email: gustavoborges25@gmail.com
- GitHub: gustavoborges25