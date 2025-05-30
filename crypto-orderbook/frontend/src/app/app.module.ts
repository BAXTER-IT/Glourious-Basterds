import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { OrderbookComponent } from './components/orderbook/orderbook.component';
import { OrderFormComponent } from './components/order-form/order-form.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: '', redirectTo: '/orderbook', pathMatch: 'full' },
  { path: 'orderbook', component: OrderbookComponent },
  { path: 'login', component: LoginComponent },
  { path: 'place-order', component: OrderFormComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '/orderbook' }
];

@NgModule({
  declarations: [
    AppComponent,
    OrderbookComponent,
    OrderFormComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(routes)
  ],
  providers: [AuthGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }