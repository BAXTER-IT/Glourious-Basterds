export enum OrderSide {
  BUY = 'BUY',
  SELL = 'SELL'
}

export enum OrderStatus {
  OPEN = 'OPEN',
  FILLED = 'FILLED',
  CANCELED = 'CANCELED'
}

export interface Order {
  id?: string;
  userId?: string;
  symbol: string;
  side: OrderSide;
  price: number;
  quantity: number;
  timestamp?: number;
  status?: OrderStatus;
}