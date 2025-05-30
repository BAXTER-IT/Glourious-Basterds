import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { OrderbookService } from '../../services/orderbook.service';
import { OrderService } from '../../services/order.service';
import { AuthService } from '../../services/auth.service';
import { Orderbook, OrderbookEntry } from '../../models/orderbook.model';
import { Order } from '../../models/order.model';
import { EditOrderDialogComponent } from '../edit-order-dialog/edit-order-dialog.component';

@Component({
  selector: 'app-orderbook',
  templateUrl: './orderbook.component.html',
  styleUrls: ['./orderbook.component.scss']
})
export class OrderbookComponent implements OnInit, OnDestroy {
  orderbook: Orderbook | null = null;
  private subscription: Subscription | null = null;
  displayLimit = 5;

  constructor(
    private orderbookService: OrderbookService,
    private orderService: OrderService,
    public authService: AuthService,
    private dialog: MatDialog
  ) { }

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

  /**
   * Returns the top 5 asks sorted by price in descending order (highest first)
   */
  getSortedAsks(): OrderbookEntry[] {
    if (!this.orderbook || !this.orderbook.asks) {
      return [];
    }
    return [...this.orderbook.asks]
      .sort((a, b) => a.price - b.price) // Sort in ascending order (lowest first)
      .slice(0, this.displayLimit);
  }

  /**
   * Returns the top 5 bids sorted by price in descending order (highest first)
   */
  getSortedBids(): OrderbookEntry[] {
    if (!this.orderbook || !this.orderbook.bids) {
      return [];
    }
    return [...this.orderbook.bids]
      .sort((a, b) => b.price - a.price)
      .slice(0, this.displayLimit);
  }

  /**
   * Cancels a user order
   * @param orderId The ID of the order to cancel
   */
  cancelOrder(orderId: string): void {
    if (!orderId) {
      console.error('Cannot cancel order: No order ID provided');
      return;
    }

    this.orderService.cancelOrder(orderId).subscribe(
      () => {
        console.log(`Order ${orderId} canceled successfully`);
        // The OrderbookService will handle removing the order from the display
        this.orderbookService.removeUserOrder(orderId);
      },
      (error) => {
        console.error('Error canceling order:', error);
      }
    );
  }

  /**
   * Opens a dialog to edit a user order
   * @param orderId The ID of the order to edit
   * @param price The current price of the order
   * @param quantity The current quantity of the order
   */
  editOrder(orderId: string, price: number, quantity: number): void {
    if (!orderId) {
      console.error('Cannot edit order: No order ID provided');
      return;
    }

    // Create a temporary order object for the dialog
    const orderToEdit: Partial<Order> = {
      id: orderId,
      symbol: 'BTCUSDT',
      price: price,
      quantity: quantity
    };

    const dialogRef = this.dialog.open(EditOrderDialogComponent, {
      width: '400px',
      data: { order: orderToEdit }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // If dialog returns a result, update the order
        this.orderService.updateOrder(orderId, result).subscribe(
          (updatedOrder) => {
            console.log(`Order ${orderId} updated successfully`);
            // Update the order in the orderbook service
            this.orderbookService.updateUserOrder(orderId, updatedOrder);
          },
          (error) => {
            console.error('Error updating order:', error);
          }
        );
      }
    });
  }
}