# Crypto Orderbook System Patterns

## Architecture Overview
The Crypto Orderbook application follows a client-server architecture with a clear separation between the frontend and backend components:

```
┌─────────────┐     WebSocket     ┌─────────────┐     HTTP     ┌─────────────┐
│   Angular   │◄───(STOMP/SJS)───►│ Spring Boot │◄────REST────►│  Binance    │
│  Frontend   │                   │   Backend   │              │     API     │
└─────────────┘                   └─────────────┘              └─────────────┘
```

## Key Design Patterns

### 1. Observer Pattern
- **Implementation**: WebSocket subscription model
- **Purpose**: Allows the frontend to observe changes in the orderbook data without polling
- **Components**: 
  - `OrderbookService` (frontend) subscribes to WebSocket updates
  - `OrderbookController` (backend) publishes updates to WebSocket topics

### 2. Repository Pattern
- **Implementation**: Service classes that abstract data access
- **Purpose**: Encapsulates the logic for retrieving and storing data
- **Components**:
  - `BinanceApiService` abstracts access to the Binance API
  - `OrderbookService` provides a clean interface for orderbook data

### 3. Model-View-Controller (MVC)
- **Implementation**: Angular components and services
- **Purpose**: Separates concerns for better maintainability
- **Components**:
  - Models: `Orderbook`, `OrderbookEntry`, `Order`
  - Views: Component templates (HTML/CSS)
  - Controllers: Component classes and services

### 4. Dependency Injection
- **Implementation**: Angular and Spring Boot DI systems
- **Purpose**: Loose coupling between components
- **Components**:
  - Services injected into components (Angular)
  - Services and controllers injected into each other (Spring)

## Critical Implementation Paths

### 1. Orderbook Data Flow
```
Binance API → BinanceApiService → OrderbookController → WebSocket → OrderbookService → OrderbookComponent
```

### 2. User Order Flow
```
OrderFormComponent → OrderService → OrderController → (Backend processing) → OrderbookService → OrderbookComponent
```

### 3. User Order Integration Flow
```
OrderFormComponent → OrderService → OrderbookService.addUserOrder() → mergeUserOrders() → OrderbookComponent
```

## Component Relationships

### Frontend Components
- **OrderbookComponent**: Displays the orderbook data
- **OrderFormComponent**: Allows users to place orders
- **LoginComponent**: Handles user authentication

### Frontend Services
- **OrderbookService**: Manages orderbook data and WebSocket connection
- **OrderService**: Handles order creation and management
- **AuthService**: Manages user authentication

### Backend Components
- **OrderbookController**: Exposes orderbook data endpoints
- **OrderController**: Handles order creation requests
- **AuthController**: Manages authentication
- **BinanceApiService**: Fetches data from Binance API

## Data Models

### Orderbook
- Contains lists of bids and asks
- Includes metadata like timestamp and lastUpdateId

### OrderbookEntry
- Represents a single bid or ask
- Contains price, quantity, and isUserOrder flag

### Order
- Represents a user's order
- Contains details like side (buy/sell), price, quantity, and status

## Integration Patterns

### 1. WebSocket for Real-time Updates
- Uses STOMP over SockJS for compatibility
- Subscribes to topics for push-based updates
- Maintains a persistent connection for low-latency updates

### 2. REST for Command Operations
- Uses HTTP endpoints for operations like placing orders
- Follows RESTful principles for resource management
- Returns appropriate status codes and response bodies

### 3. User Order Integration
- Merges user orders with market orders in the frontend
- Uses the `isUserOrder` flag to visually distinguish user orders
- Maintains separate lists but presents a unified view