import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { API } from '../constants';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';
import { Product } from '../dataTypes';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  isSellerLoggedIn=new BehaviorSubject<boolean>(false);

  constructor(private apiService:ApiService, private router:Router) { }

  /*addProduct(data:object):void{
    let result=this.apiService.makeRequest("post",API.product+API.addProduct,data)
    result.subscribe((response)=>{
      //poi la tolgo il local storage non è necessario salvare
      localStorage.setItem('product',JSON.stringify(response))
      //this.isSellerLoggedIn.next(true); POTREBBE NON SERVIRE
      //COME DEVO AZZERARE I CAMPI PER UN NUOVO INSERIMENTO?
    })
    console.log(result) //stampa l'observable
    console.warn("PRODOTTO INSERITO CON SUCCESSO!")
  } */

  //Alternativa all'add di sopra
  addProduct(product:Product):Observable<Product>{
    return this.apiService.makeRequest("post",API.product+API.addProduct,product)
  }

  getAllProduct():Observable<Product[]>{
    return this.apiService.makeRequest("get",API.product+API.getAllProduct)
  }
}
