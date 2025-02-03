# DLQ Manager

## Project Description
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![RabbitMQ](https://img.shields.io/badge/rabbitmq-%23FF6600.svg?style=for-the-badge&logo=rabbitmq&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/postgresql-%23336791.svg?style=for-the-badge&logo=postgresql&logoColor=white)

**DLQ Manager** is a project developed to **study and deepen knowledge in RabbitMQ** by implementing a robust and practical system for managing Dead Letter Queues (DLQs). Built with **Java** and **Spring Boot**, this application facilitates the monitoring, reprocessing, and administration of failed messages sent to the DLQ, ensuring high reliability and traceability in message processing.

## Features

- **Message Sending:** Allows sending messages to a configured main queue.
- **Failure Handling:** Messages that cannot be processed are automatically sent to the DLQ.
- **DLQ Message Management:**
  - List messages with details such as failure reason and delivery attempts.
  - Reprocess messages from the DLQ to the main queue.
  - Discard unwanted messages.
- **Monitoring:**
  - Integration with Prometheus and Grafana for queue and message monitoring.
  - Metrics such as the number of messages in the DLQ, reprocessing rate, and average time of messages in the DLQ.
- **API Documentation:** Automatic API documentation with Swagger for easy use of REST APIs.

## Technologies Used

- **Language:** Java 21
- **Framework:** Spring Boot 3.4.2
- **Message Broker:** RabbitMQ
- **Database:** PostgreSQL
- **Monitoring:** Prometheus and Grafana
- **API Documentation:** SpringDoc OpenAPI (Swagger)
