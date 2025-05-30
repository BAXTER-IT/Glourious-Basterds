# EURHUF Chart App - Backend

This is the backend component of the EURHUF Chart App, a Spring Boot application that fetches EURHUF exchange rate data from Yahoo Finance API and provides endpoints for the frontend to consume.

## Features

- Fetches real-time EURHUF exchange rate data from Yahoo Finance API
- Provides historical data for the last 3 months
- Implements caching to minimize external API calls
- Exposes RESTful API endpoints for the frontend

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd eurhuf-backend
```

### Build the Application

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The application will start on port 8080 by default. You can access the API at `http://localhost:8080/api/v1`.

### Run with Development Profile

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## API Endpoints

### Get Current Exchange Rate

```
GET /api/v1/eurhuf/current
```

Response:

```json
{
  "timestamp": "2025-05-30T09:00:00",
  "rate": 385.42,
  "change": 0.15,
  "changePercent": 0.04
}
```

### Get Historical Data

```
GET /api/v1/eurhuf/historical?from={from}&to={to}&interval={interval}
```

Parameters:
- `from`: Start date (ISO format, e.g., "2025-05-01T00:00:00")
- `to`: End date (ISO format, e.g., "2025-05-30T23:59:59")
- `interval`: Data interval (e.g., "1h", "1d", "1w")

Response:

```json
{
  "data": [
    {
      "timestamp": "2025-05-30T09:00:00",
      "rate": 385.42
    },
    {
      "timestamp": "2025-05-29T09:00:00",
      "rate": 385.27
    }
  ],
  "meta": {
    "interval": "1d",
    "count": 30
  }
}
```

### Get Data for a Specific Time Range

```
GET /api/v1/eurhuf/range/{range}
```

Parameters:
- `range`: Predefined time range (e.g., "1d", "1w", "1m", "3m")

Response: Same as historical data endpoint.

## Configuration

The application can be configured through the `application.properties` file. Key configuration properties include:

- `server.port`: The port the server runs on (default: 8080)
- `yahoo.finance.symbol`: The Yahoo Finance symbol for EURHUF (default: EURHUF=X)
- `yahoo.finance.update.interval`: The interval for updating the current exchange rate in milliseconds (default: 60000)

## Development

### Project Structure

```
eurhuf-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── eurhufapp/
│   │   │           ├── EurhufBackendApplication.java
│   │   │           ├── config/
│   │   │           ├── controller/
│   │   │           ├── service/
│   │   │           ├── model/
│   │   │           ├── repository/
│   │   │           ├── exception/
│   │   │           └── util/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties
│   └── test/
│       └── java/
│           └── com/
│               └── eurhufapp/
├── pom.xml
└── README.md
```

### Adding Tests

The project uses JUnit and Mockito for testing. To add tests, create test classes in the `src/test/java` directory.

### Building for Production

```bash
mvn clean package -P prod
```

This will create a JAR file in the `target` directory that can be deployed to a production environment.

## License

This project is licensed under the MIT License - see the LICENSE file for details.