# DLQ Manager

## Project Description
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![RabbitMQ](https://img.shields.io/badge/rabbitmq-%23FF6600.svg?style=for-the-badge&logo=rabbitmq&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/postgresql-%23336791.svg?style=for-the-badge&logo=postgresql&logoColor=white)

**DLQ Manager** is a project developed to **study and deepen knowledge in RabbitMQ** by implementing a robust and practical system for managing Dead Letter Queues (DLQs). Built with **Java** and **Spring Boot**, this application facilitates the monitoring, reprocessing, and administration of failed messages sent to the DLQ, ensuring high reliability and traceability in message processing.

## Features

- **Automatic Failure Handling:** Messages that fail after multiple attempts are automatically sent to the DLQ.
- **DLQ Message Management:**
  - Store failed messages in the database.
  - List messages with failure details.
  - Reprocess messages individually, in bulk, or all at once.
  - Track reprocessing status (`PENDING`, `SUCCESS`, `FAILED`).
- **Monitoring & Metrics:**
  - Count total, successful, and failed reprocessed messages.
  - Calculate success and failure rates.

## Technologies Used

- **Language:** Java 21
- **Framework:** Spring Boot 3.4.2
- **Message Broker:** RabbitMQ
- **Database:** PostgreSQL
- **API Documentation:** SpringDoc OpenAPI (Swagger)
