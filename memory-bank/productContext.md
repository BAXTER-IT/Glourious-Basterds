# Product Context

## Why This Project Exists

The EURHUF Chart App is designed to provide users with real-time and historical exchange rate data between the Euro (EUR) and Hungarian Forint (HUF). This application serves several key purposes:

1. **Financial Monitoring**: Allows users to track the EUR/HUF exchange rate over time, which is crucial for businesses and individuals involved in transactions between Eurozone countries and Hungary.

2. **Investment Decision Support**: Provides historical data visualization to help investors identify trends and make informed decisions about currency trading or investments affected by EUR/HUF exchange rates.

3. **Economic Analysis**: Enables analysis of how the EUR/HUF exchange rate has changed over different time periods, which can be correlated with economic events or policy changes.

4. **Travel Planning**: Helps travelers planning trips between Eurozone countries and Hungary to monitor exchange rates and plan their currency exchanges at favorable times.

## Problems It Solves

1. **Data Accessibility**: Simplifies access to EUR/HUF exchange rate data that would otherwise require navigating complex financial websites or subscribing to paid services.

2. **Visual Representation**: Transforms raw numerical data into visual charts that make trends and patterns more easily identifiable.

3. **Historical Context**: Provides historical context for current exchange rates, allowing users to understand if the current rate is relatively high or low compared to past periods.

4. **Real-Time Updates**: Delivers regularly updated exchange rate information without requiring manual refreshes or searches.

5. **Consolidated Information**: Brings together current and historical data in one application, eliminating the need to consult multiple sources.

## How It Should Work

1. **Backend Operations**:
   - Fetch current EUR/HUF exchange rate data from the Frankfurter API
   - Retrieve historical exchange rate data for specified time periods
   - Process and store this data efficiently
   - Expose RESTful API endpoints for the frontend to consume
   - Update current exchange rate data automatically at regular intervals

2. **Frontend Experience**:
   - Display the current EUR/HUF exchange rate prominently
   - Provide interactive charts showing historical exchange rate data
   - Allow users to select different time periods (day, week, month, year)
   - Enable comparison of rates across different time periods
   - Provide a clean, intuitive interface that works well on both desktop and mobile devices

3. **Data Flow**:
   - Backend fetches data from external API
   - Data is processed, stored, and cached
   - Frontend requests data from backend API
   - Frontend renders data in user-friendly formats

## User Experience Goals

1. **Simplicity**: The application should be intuitive and easy to use, requiring no financial expertise to understand.

2. **Responsiveness**: The interface should be responsive and work well on various devices and screen sizes.

3. **Performance**: Charts and data should load quickly, with minimal waiting time for users.

4. **Clarity**: Data visualization should be clear and easy to interpret, with appropriate labeling and context.

5. **Reliability**: The application should provide consistent and accurate data, with appropriate error handling when external APIs are unavailable.

6. **Accessibility**: The application should be accessible to users with disabilities, following WCAG guidelines.

7. **Usefulness**: The information presented should be practical and actionable, helping users make informed decisions.