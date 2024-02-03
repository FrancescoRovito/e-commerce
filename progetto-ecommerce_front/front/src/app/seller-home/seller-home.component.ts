import { Component, OnInit } from '@angular/core';
import { SellerService } from '../servicies/seller.service';
import { ProductService } from '../servicies/product.service';
import { Product } from '../dataTypes';

@Component({
  selector: 'app-seller-home',
  templateUrl: './seller-home.component.html',
  styleUrls: ['./seller-home.component.css']
})
export class SellerHomeComponent implements OnInit{

  //productList:undefined|Product[]; //versione precedente
  constructor(private sellerService:SellerService, public productService:ProductService){}

  ngOnInit(): void {
    /** versione precedente 
    this.productService.getAllProduct().subscribe((result)=>{
      if(result){
        console.log(result)
        this.productList=result 
      }
    })*/
  }
}
