export interface OrderbookEntry {
  price: number;
  quantity: number;
  isUserOrder: boolean;
}

export interface Orderbook {
  bids: OrderbookEntry[];
  asks: OrderbookEntry[];
  lastUpdateId: number;
  timestamp: number;
}