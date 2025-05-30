import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { OrderbookService } from '../../services/orderbook.service';
import { Orderbook, OrderbookEntry } from '../../models/orderbook.model';

@Component({
  selector: 'app-orderbook',
  templateUrl: './orderbook.component.html',
  styleUrls: ['./orderbook.component.scss']
})
export class OrderbookComponent implements OnInit, OnDestroy {
  orderbook: Orderbook | null = null;
  private subscription: Subscription | null = null;
  displayLimit = 5;

  constructor(private orderbookService: OrderbookService) { }

  ngOnInit(): void {
    this.loadOrderbook();
    this.subscribeToOrderbookUpdates();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  private loadOrderbook(): void {
    this.orderbookService.getNormalizedOrderbook(this.displayLimit)
      .subscribe(
        (data) => {
          this.orderbook = data;
        },
        (error) => {
          console.error('Error loading orderbook:', error);
        }
      );
  }

  private subscribeToOrderbookUpdates(): void {
    this.subscription = this.orderbookService.orderbook$.subscribe(
      (data) => {
        this.orderbook = data;
      },
      (error) => {
        console.error('Error in orderbook subscription:', error);
      }
    );
  }

  trackByPrice(index: number, item: OrderbookEntry): number {
    return item.price;
  }
}