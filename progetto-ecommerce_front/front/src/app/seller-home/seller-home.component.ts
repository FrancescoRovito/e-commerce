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

  productList:undefined | Product[]
  constructor(private sellerService:SellerService, private productService:ProductService){}

  ngOnInit(): void {
    this.productService.getAllProduct().subscribe((result)=>{
      if(result){
        this.productList=result;
      }
    })
  }
}
