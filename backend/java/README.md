# IELTS Platform Backend

Spring Boot 3 backend service for the IELTS training platform.

## 🚀 Quick Start

### Prerequisites
- Java 21
- Gradle 8.6+

### Build the Project
```bash
./gradlew build
```

### Run the Application
```bash
./gradlew bootRun
```

### Test the Application
```bash
./gradlew test
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/ryan/ieltsplatform/
│   │   └── IeltsPlatformApplication.java
│   └── resources/
│       └── application.yml
└── test/
    └── java/com/ryan/ieltsplatform/
        └── IeltsPlatformApplicationTests.java
```

## 🔧 Configuration

The application is configured via `application.yml` with the following key settings:

- **Server Port**: 8080
- **gRPC Port**: 9090
- **Actuator**: Available at `/actuator`
- **OpenAPI**: Available at `/swagger-ui`

## 📚 Dependencies

- Spring Boot 3.2.0
- Spring Boot Actuator
- SpringDoc OpenAPI
- gRPC Spring Boot Starter
- Logback with JSON encoding
- Lombok

## 🏗️ Architecture

This project is designed to support:
- REST API endpoints
- gRPC services
- OpenAPI documentation
- Health monitoring
- Structured logging
- Exception handling

## 📝 Development

This is the foundation project (ITP-36.1) that sets up the basic Spring Boot structure. Future tasks will add:
- REST controllers
- gRPC services
- Exception handling
- Logging configuration
- Integration tests
