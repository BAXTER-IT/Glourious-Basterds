# Active Context

## Current Focus
We're implementing a 2-Tier EURHUF Chart App with a Spring Boot backend and Angular frontend. The backend is responsible for fetching exchange rate data from external APIs, processing it, and providing it to the frontend through RESTful endpoints.

## Recent Changes
- Successfully switched from Yahoo Finance API to Frankfurter API (https://api.frankfurter.app) for exchange rate data
- Updated the ExchangeRateApiService to use the correct endpoint format for Frankfurter API
- Fixed parameter names in API requests (from "base"/"symbols" to "from"/"to")
- Fixed the historical data endpoint by correcting the API URL format
- Implemented proper error handling and logging for API requests
- Added caching to improve performance and reduce API calls

## Next Steps
1. ✅ Test the historical data endpoint to ensure it works correctly with the Frankfurter API
2. Implement the Angular frontend to display the exchange rate data
3. Add unit tests for the backend components
4. Implement error handling and fallback mechanisms for API failures
5. Complete the integration between frontend and backend

## Active Decisions
- Using Frankfurter API as the data source for exchange rates after encountering issues with Yahoo Finance API
- Implementing in-memory storage for exchange rate data
- Using caching to improve performance and reduce API calls
- Using scheduled tasks for real-time updates of exchange rate data

## Important Patterns
- Repository pattern for data access
- Service layer for business logic
- Controller layer for API endpoints
- Dependency injection for loose coupling
- Caching for performance optimization
- Scheduled tasks for real-time updates

## Learnings and Insights
- External APIs can change or have authentication issues, so it's important to have fallback mechanisms
- Proper error handling and logging are essential for debugging API integration issues
- Caching can significantly improve performance for frequently accessed data
- The Frankfurter API provides a simple and reliable way to access exchange rate data