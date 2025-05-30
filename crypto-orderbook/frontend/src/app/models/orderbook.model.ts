export interface OrderbookEntry {
  price: number;
  quantity: number;
  isUserOrder: boolean;
  orderId?: string;  // Added to identify which order to cancel
}

export interface Orderbook {
  bids: OrderbookEntry[];
  asks: OrderbookEntry[];
  lastUpdateId: number;
  timestamp: number;
}