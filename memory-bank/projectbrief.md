# Crypto Orderbook Project Brief

## Project Overview
The Crypto Orderbook is a web application that displays real-time cryptocurrency orderbook data from the Binance API. It allows users to view the current bids and asks for a cryptocurrency pair (BTC/USDT) and place their own orders.

## Core Requirements
1. Display real-time orderbook data from Binance API
2. Allow users to place buy and sell orders
3. Authenticate users before allowing them to place orders
4. Provide a clean, intuitive user interface for viewing orderbook data
5. Ensure real-time updates via WebSocket connections
6. Merge user orders into the displayed orderbook so they appear alongside fetched bids/asks

## Technical Stack
- **Backend**: Java Spring Boot
- **Frontend**: Angular
- **Real-time Communication**: WebSockets (STOMP over SockJS)
- **External API**: Binance API for orderbook data

## Key Features
1. **Real-time Orderbook Display**: Shows the top bids and asks from the Binance API
2. **User Authentication**: Secure login system for users
3. **Order Placement**: Interface for users to place buy and sell orders
4. **Integrated User Orders**: User orders appear in the orderbook alongside market data
5. **Responsive Design**: Works well on different screen sizes

## Project Scope
The application focuses on providing a simplified trading interface for cryptocurrency trading. It is not a full-featured trading platform but rather a specialized tool for viewing orderbook data and placing basic orders.

## Success Criteria
1. Users can view real-time orderbook data
2. Users can place orders that appear in the orderbook
3. The application maintains a stable connection to the Binance API
4. The user interface is intuitive and responsive
5. User orders are clearly distinguished from market orders in the display