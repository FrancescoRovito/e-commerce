import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { HomeComponent } from './home/home.component';
import { SellerAuthComponent } from './seller-auth/seller-auth.component';
import { LoginComponent } from './login/login.component';
import { CartComponent } from './cart/cart.component';
import { FormsModule } from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import { SellerHomeComponent } from './seller-home/seller-home.component';
import { AddProductComponent } from './add-product/add-product.component'
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({ //vengono inseririti da angulare tutti i componenti creati
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    SellerAuthComponent,
    LoginComponent,
    CartComponent,
    SellerHomeComponent,
    AddProductComponent
  ],
  /* la proprietà imports all'interno del decoratore @NgModule è utilizzata per elencare 
  tutti i moduli che devono essere importati e utilizzati nel modulo corrente.*/
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [],  //vanno inseriti tutti i serivizi, se nel servizio abbiamo root si può evitare di inserirlo
  bootstrap: [AppComponent]
})
export class AppModule { }
