# Project Brief: EURHUF Chart App

## Project Overview

The EURHUF Chart App is a web application designed to display real-time and historical exchange rate data between the Euro (EUR) and Hungarian Forint (HUF). The application follows a 2-Tier architecture with a Spring Boot backend and an Angular frontend.

## Core Requirements

1. **Real-Time Exchange Rate Data**
   - Display the current EUR/HUF exchange rate
   - Update the exchange rate automatically at regular intervals
   - Show the percentage change from the previous day

2. **Historical Data Visualization**
   - Display historical exchange rate data in interactive charts
   - Support different time periods (day, week, month, year)
   - Allow users to zoom in/out and pan through the data

3. **User Interface**
   - Clean, intuitive, and responsive design
   - Support for both desktop and mobile devices
   - Clear visualization of data with appropriate context

4. **Performance**
   - Fast loading times for both current and historical data
   - Efficient caching to reduce API calls
   - Smooth interaction with charts and UI elements

5. **Reliability**
   - Proper error handling for API failures
   - Fallback mechanisms when data is unavailable
   - Consistent and accurate data presentation

## Technical Requirements

1. **Backend**
   - Spring Boot application with RESTful API endpoints
   - Integration with external exchange rate API (Frankfurter API)
   - Caching mechanism for improved performance
   - Scheduled tasks for automatic updates

2. **Frontend**
   - Angular application with responsive design
   - Interactive charts for data visualization
   - Intuitive user interface with clear navigation

3. **Data Management**
   - Efficient storage and retrieval of exchange rate data
   - Proper handling of different time periods and date formats
   - Caching strategy to reduce API calls and improve performance

## Project Scope

### In Scope
- Development of Spring Boot backend with RESTful API endpoints
- Integration with Frankfurter API for exchange rate data
- Implementation of caching and scheduled updates
- Development of Angular frontend with interactive charts
- Support for current and historical exchange rate data
- Responsive design for desktop and mobile devices

### Out of Scope
- User authentication and authorization
- Support for currencies other than EUR/HUF
- Advanced financial analysis tools
- Integration with trading platforms
- Mobile app development (native iOS/Android)

## Success Criteria
- The application successfully displays the current EUR/HUF exchange rate
- Historical data is correctly visualized in interactive charts
- The UI is responsive and works well on different devices
- Data is updated automatically at regular intervals
- The application handles API failures gracefully
- Performance is optimized with proper caching

## Timeline
- Phase 1: Backend Development (Current)
  - Set up Spring Boot project
  - Implement model classes and repository
  - Integrate with exchange rate API
  - Implement caching and scheduled updates
  - Create RESTful API endpoints

- Phase 2: Frontend Development (Next)
  - Set up Angular project
  - Implement UI components
  - Create interactive charts
  - Connect to backend API
  - Implement responsive design

- Phase 3: Testing and Deployment
  - Unit and integration testing
  - Performance optimization
  - Bug fixing
  - Deployment to production environment

## Current Status
The backend development is in progress. We have successfully integrated with the Frankfurter API for exchange rate data and implemented the core functionality for fetching, processing, and caching the data. The next step is to complete the backend testing and then move on to frontend development.