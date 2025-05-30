import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { map } from 'rxjs/operators';
import { Orderbook } from '../models/orderbook.model';
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
    const socket = new SockJS(environment.wsUrl);
    this.stompClient = new Client({
      webSocketFactory: () => socket,
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
          this.orderbookSubject.next(orderbook);
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
}