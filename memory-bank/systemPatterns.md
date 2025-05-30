# System Patterns

## System Architecture

The EURHUF Chart App follows a 2-Tier architecture:

1. **Backend Tier (Spring Boot)**
   - Responsible for fetching, processing, and storing exchange rate data
   - Exposes RESTful API endpoints for the frontend
   - Handles caching and scheduled updates

2. **Frontend Tier (Angular - Planned)**
   - User interface for displaying exchange rate data
   - Charts for visualizing historical data
   - Responsive design for different devices

## Key Technical Decisions

1. **Using Frankfurter API for Exchange Rate Data**
   - Free and reliable API for current and historical exchange rates
   - Simple API structure with good documentation
   - No authentication required

2. **In-Memory Data Storage**
   - Exchange rate data stored in memory for fast access
   - Suitable for the limited amount of data needed for this application
   - Can be easily replaced with a database if needed in the future

3. **Caching Strategy**
   - Current exchange rate cached with a short TTL (5 minutes)
   - Historical data cached with a longer TTL (1 day)
   - Reduces API calls and improves performance

4. **Scheduled Tasks for Real-Time Updates**
   - Current exchange rate updated automatically every 5 minutes
   - Ensures data is always up-to-date without manual refresh

## Design Patterns in Use

1. **Repository Pattern**
   - `ExchangeRateRepository` interface defines data access methods
   - `InMemoryExchangeRateRepository` implements the interface for in-memory storage
   - Allows for easy switching to different storage implementations

2. **Service Layer Pattern**
   - `ExchangeRateService` contains business logic
   - `ExchangeRateApiService` handles API communication
   - `CacheService` manages caching
   - Separation of concerns for better maintainability

3. **Dependency Injection**
   - Spring's DI container manages component lifecycle
   - Components declare dependencies through constructor injection
   - Promotes loose coupling and testability

4. **Controller-Service-Repository Pattern**
   - `ExchangeRateController` handles HTTP requests and responses
   - `ExchangeRateService` contains business logic
   - `ExchangeRateRepository` handles data access
   - Clear separation of responsibilities

5. **Adapter Pattern**
   - `ExchangeRateApiService` adapts the external API to the internal model
   - Isolates the application from changes in the external API

6. **Strategy Pattern**
   - Different API services can be used interchangeably
   - Allows for easy switching between different exchange rate data providers

## Component Relationships

```
┌─────────────────┐      ┌─────────────────┐      ┌─────────────────┐
│                 │      │                 │      │                 │
│ Controller Layer│◄─────┤  Service Layer  │◄─────┤Repository Layer │
│                 │      │                 │      │                 │
└────────┬────────┘      └────────┬────────┘      └────────┬────────┘
         │                        │                        │
         │                        │                        │
         ▼                        ▼                        ▼
┌─────────────────┐      ┌─────────────────┐      ┌─────────────────┐
│                 │      │                 │      │                 │
│  REST Endpoints │      │ Business Logic  │      │   Data Access   │
│                 │      │                 │      │                 │
└─────────────────┘      └────────┬────────┘      └─────────────────┘
                                  │
                                  │
                                  ▼
                         ┌─────────────────┐
                         │                 │
                         │  External API   │
                         │                 │
                         └─────────────────┘
```

## Critical Implementation Paths

1. **Exchange Rate Data Flow**
   - Frontend requests data from backend controller
   - Controller delegates to service layer
   - Service checks cache for data
   - If not in cache, service requests data from repository
   - If not in repository, service fetches data from external API
   - Data is stored in repository and cache
   - Data is returned to frontend

2. **Scheduled Update Flow**
   - Scheduler triggers update method in service
   - Service fetches latest data from external API
   - Cache is updated with new data
   - Repository is updated with new data

3. **Error Handling Flow**
   - External API errors are caught and logged
   - Appropriate HTTP status codes are returned to frontend
   - Fallback mechanisms are used when possible