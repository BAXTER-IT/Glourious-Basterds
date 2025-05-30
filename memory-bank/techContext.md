# Technical Context

## Technologies Used

### Backend
- **Java 8**: Programming language for the backend
- **Spring Boot 2.7.18**: Framework for creating stand-alone, production-grade Spring-based applications
- **Spring WebFlux**: Reactive web framework for building non-blocking, asynchronous applications
- **Spring Cache**: For caching frequently accessed data
- **Spring Scheduled Tasks**: For periodic execution of tasks
- **Maven**: Build and dependency management tool
- **Lombok**: Reduces boilerplate code for model/data objects

### Frontend (Planned)
- **Angular**: Framework for building client-side applications
- **TypeScript**: Typed superset of JavaScript
- **Chart.js/D3.js**: For visualizing exchange rate data
- **Angular Material**: UI component library

### APIs
- **Frankfurter API** (https://api.frankfurter.app): Free API for current and historical foreign exchange rates
  - Endpoints:
    - Latest rates: `/latest?from=EUR&to=HUF`
    - Historical rates: `/YYYY-MM-DD?from=EUR&to=HUF`
    - Time series: `/YYYY-MM-DD..YYYY-MM-DD?from=EUR&to=HUF`

## Development Setup
- **IDE**: Any Java IDE (IntelliJ IDEA, Eclipse, VS Code)
- **JDK 8**: Java Development Kit
- **Maven**: For dependency management and building
- **Git**: Version control
- **Node.js & npm**: For Angular frontend development (planned)

## Technical Constraints
- **Java 8 Compatibility**: The application must be compatible with Java 8
- **RESTful API Design**: The backend must expose RESTful endpoints
- **Responsive Design**: The frontend must be responsive and work on different devices
- **API Rate Limits**: The Frankfurter API may have rate limits that need to be respected

## Dependencies
- **spring-boot-starter-web**: For building web applications
- **spring-boot-starter-webflux**: For reactive programming
- **spring-boot-starter-cache**: For caching support
- **spring-boot-devtools**: For development tools
- **lombok**: For reducing boilerplate code
- **spring-boot-starter-test**: For testing

## Tool Usage Patterns
- **Repository Pattern**: For data access
- **Service Layer**: For business logic
- **Controller Layer**: For API endpoints
- **Dependency Injection**: For loose coupling
- **Caching**: For performance optimization
- **Scheduled Tasks**: For real-time updates
- **WebClient**: For making HTTP requests to external APIs