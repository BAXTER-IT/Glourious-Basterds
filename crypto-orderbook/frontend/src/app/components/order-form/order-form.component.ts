import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { Order, OrderSide } from '../../models/order.model';

@Component({
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  styleUrls: ['./order-form.component.scss']
})
export class OrderFormComponent implements OnInit {
  orderForm: FormGroup;
  loading = false;
  submitted = false;
  error = '';
  success = '';
  OrderSide = OrderSide; // Make enum available to template

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private orderService: OrderService
  ) {
    this.orderForm = this.formBuilder.group({
      symbol: ['BTCUSDT', Validators.required],
      side: [OrderSide.BUY, Validators.required],
      price: ['', [Validators.required, Validators.min(0.01)]],
      quantity: ['', [Validators.required, Validators.min(0.00000001)]]
    });
  }

  ngOnInit(): void {
  }

  // Convenience getter for easy access to form fields
  get f() { return this.orderForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';
    this.success = '';

    // Stop here if form is invalid
    if (this.orderForm.invalid) {
      return;
    }

    this.loading = true;
    
    const order: Order = {
      symbol: this.f['symbol'].value,
      side: this.f['side'].value,
      price: this.f['price'].value,
      quantity: this.f['quantity'].value
    };

    this.orderService.createOrder(order)
      .subscribe(
        response => {
          this.success = 'Order placed successfully!';
          this.loading = false;
          this.orderForm.reset({
            symbol: 'BTCUSDT',
            side: OrderSide.BUY
          });
          this.submitted = false;
        },
        error => {
          this.error = 'Failed to place order. Please try again.';
          this.loading = false;
        }
      );
  }
}