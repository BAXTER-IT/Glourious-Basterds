import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Order } from '../../models/order.model';

@Component({
  selector: 'app-edit-order-dialog',
  templateUrl: './edit-order-dialog.component.html',
  styleUrls: ['./edit-order-dialog.component.scss']
})
export class EditOrderDialogComponent implements OnInit {
  editForm: FormGroup;
  loading = false;
  submitted = false;
  error = '';

  constructor(
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<EditOrderDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { order: Order }
  ) {
    this.editForm = this.formBuilder.group({
      price: [data.order.price, [Validators.required, Validators.min(0.01)]],
      quantity: [data.order.quantity, [Validators.required, Validators.min(0.00000001)]]
    });
  }

  ngOnInit(): void {
  }

  // Convenience getter for easy access to form fields
  get f() { return this.editForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';

    // Stop here if form is invalid
    if (this.editForm.invalid) {
      return;
    }

    const updatedOrder: Partial<Order> = {
      price: this.f['price'].value,
      quantity: this.f['quantity'].value
    };

    this.dialogRef.close(updatedOrder);
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}