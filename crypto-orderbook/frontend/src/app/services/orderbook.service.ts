import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject, BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';
import { Orderbook, OrderbookEntry } from '../models/orderbook.model';
import { Order, OrderSide, OrderStatus } from '../models/order.model';
import { environment } from '../../environments/environment';
import * as SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

@Injectable({
  providedIn: 'root'
})
export class OrderbookService {
  private apiUrl = `${environment.apiUrl}/orderbook`;
  private stompClient: Client | null = null;
  private orderbookSubject = new Subject<Orderbook>();
  public orderbook$ = this.orderbookSubject.asObservable();
  
  // Store user orders
  private userOrders: Order[] = [];
  private userOrdersSubject = new BehaviorSubject<Order[]>([]);
  public userOrders$ = this.userOrdersSubject.asObservable();

  constructor(private http: HttpClient) {
    this.connectWebSocket();
  }

  getOrderbook(): Observable<Orderbook> {
    return this.http.get<Orderbook>(this.apiUrl);
  }

  getNormalizedOrderbook(limit: number = 5): Observable<Orderbook> {
    return this.http.get<Orderbook>(`${this.apiUrl}/normalized?limit=${limit}`);
  }

  getAugmentedOrderbook(): Observable<Orderbook> {
    return this.http.get<Orderbook>(`${this.apiUrl}/augmented`);
  }

  private connectWebSocket(): void {
    this.stompClient = new Client({
      brokerURL: environment.wsUrl.replace('http://', 'ws://').replace('https://', 'wss://'),
      debug: (str) => {
        console.log(str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    });

    this.stompClient.onConnect = (frame) => {
      console.log('Connected to WebSocket: ' + frame);
      this.stompClient?.subscribe('/topic/orderbook', (message) => {
        if (message.body) {
          const orderbook = JSON.parse(message.body) as Orderbook;
          // Merge user orders with the fetched orderbook
          const mergedOrderbook = this.mergeUserOrders(orderbook);
          this.orderbookSubject.next(mergedOrderbook);
        }
      });
    };

    this.stompClient.onStompError = (frame) => {
      console.error('WebSocket error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    this.stompClient.activate();
  }

  disconnect(): void {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }
  
  /**
   * Add a user order to the list of user orders
   */
  addUserOrder(order: Order): void {
    // Only add open orders
    if (order.status !== OrderStatus.CANCELED && order.status !== OrderStatus.FILLED) {
      this.userOrders.push(order);
      this.userOrdersSubject.next([...this.userOrders]);
      
      // If we have an orderbook, merge the user order and emit the updated orderbook
      if (this.orderbookSubject) {
        const currentOrderbook = this.getLatestOrderbook();
        if (currentOrderbook) {
          const mergedOrderbook = this.mergeUserOrders(currentOrderbook);
          this.orderbookSubject.next(mergedOrderbook);
        }
      }
    }
  }
  
  /**
   * Remove a user order from the list of user orders
   */
  removeUserOrder(orderId: string): void {
    this.userOrders = this.userOrders.filter(order => order.id !== orderId);
    this.userOrdersSubject.next([...this.userOrders]);
    
    // Update the orderbook to remove the canceled order
    const currentOrderbook = this.getLatestOrderbook();
    if (currentOrderbook) {
      const mergedOrderbook = this.mergeUserOrders(currentOrderbook);
      this.orderbookSubject.next(mergedOrderbook);
    }
  }
  
  /**
   * Get the latest orderbook (without emitting an event)
   */
  private getLatestOrderbook(): Orderbook | null {
    let latestOrderbook: Orderbook | null = null;
    this.orderbook$.subscribe(orderbook => {
      latestOrderbook = orderbook;
    }).unsubscribe();
    return latestOrderbook;
  }
  
  /**
   * Update a user order in the list of user orders
   */
  updateUserOrder(orderId: string, updatedOrder: Order): void {
    // Find the order to update
    const index = this.userOrders.findIndex(order => order.id === orderId);
    if (index !== -1) {
      // Update the order
      this.userOrders[index] = {
        ...this.userOrders[index],
        ...updatedOrder,
        id: orderId // Ensure the ID remains the same
      };
      this.userOrdersSubject.next([...this.userOrders]);
      
      // Update the orderbook to reflect the updated order
      const currentOrderbook = this.getLatestOrderbook();
      if (currentOrderbook) {
        const mergedOrderbook = this.mergeUserOrders(currentOrderbook);
        this.orderbookSubject.next(mergedOrderbook);
      }
    }
  }
  
  /**
   * Merge user orders with the fetched orderbook
   */
  private mergeUserOrders(orderbook: Orderbook): Orderbook {
    if (!this.userOrders.length) {
      return orderbook;
    }
    
    // Create a deep copy of the orderbook to avoid modifying the original
    const mergedOrderbook: Orderbook = {
      bids: [...orderbook.bids],
      asks: [...orderbook.asks],
      lastUpdateId: orderbook.lastUpdateId,
      timestamp: orderbook.timestamp
    };
    
    // Add user orders to the appropriate side of the orderbook
    this.userOrders.forEach(order => {
      if (order.status === OrderStatus.OPEN) {
        const entry: OrderbookEntry = {
          price: order.price,
          quantity: order.quantity,
          isUserOrder: true,
          orderId: order.id
        };
        
        if (order.side === OrderSide.BUY) {
          // Add to bids
          mergedOrderbook.bids.push(entry);
        } else {
          // Add to asks
          mergedOrderbook.asks.push(entry);
        }
      }
    });
    
    // Sort bids by price in descending order (highest first)
    mergedOrderbook.bids.sort((a, b) => b.price - a.price);
    
    // Sort asks by price in ascending order (lowest first)
    mergedOrderbook.asks.sort((a, b) => a.price - b.price);
    
    // Limit to top 5 entries for each side
    const displayLimit = 5;
    mergedOrderbook.bids = mergedOrderbook.bids.slice(0, displayLimit);
    mergedOrderbook.asks = mergedOrderbook.asks.slice(0, displayLimit);
    
    return mergedOrderbook;
  }
}