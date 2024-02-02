import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../servicies/product.service';
import { Router } from '@angular/router';
import { Product } from '../dataTypes';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent implements OnInit{

  productForm:FormGroup;

  constructor(private router: Router, private productService: ProductService, private formBuilder:FormBuilder){
    
    /**
     * this.productForm: Crea un'istanza di FormGroup, che rappresenta un gruppo di controlli del form 
     * in Angular. This.formBuilder.group({ ... }): Utilizza il servizio formBuilder di Angular per 
     * creare un nuovo FormGroup. Questo servizio semplifica la creazione di FormGroup e FormControl 
     * in modo dichiarativo.
     */

    this.productForm=formBuilder.group({
      
      /* abbiamo l'elenco delle proprietà stabilite in html. Si usa '' in modo tale da 
       * essere vuoto e mostrare il placeholder.Il validatore inserito ci dice che il 
       * parametro è richiesto e non può essere vuoto!
       * 
       * name: Definisce il campo del form chiamato "name".
       * 
       * Validators.required: È un validatore che verifica se il campo è vuoto o 
       * non è stato compilato. Se il campo è vuoto, è considerato invalido.
       * 
       * Validators.pattern(...): È un validatore di pattern che verifica se il valore 
       * del campo segue il pattern specificato.
       */

      name:['',[Validators.required, Validators.pattern("^[A-Z][a-zA-Z]*(?:\\s[a-zA-Z]+)*$")]],
      type:['',[Validators.required, Validators.pattern("^[A-Z][a-zA-Z]*(?:\\s[a-zA-Z]+)*$")]],
      model:['',[Validators.required, Validators.pattern("^[A-Z][a-zA-Z0-9]*(?:\\s[a-zA-Z0-9]+)*$")]],
      code:['',[Validators.required, Validators.pattern("^[0-9]+$")]],
      price:['',[Validators.required, Validators.pattern("^[0-9]+(\\.[0-9]+)?$")]],
      quantity:['',[Validators.required, Validators.pattern("[0-9]+")]]
    })
  }

  ngOnInit(): void {
      
  }

  /*
  addProduct(data:object):void{
    console.log(this.productForm)
    console.warn(data)
    this.productService.addProduct(data);
    this.productForm.reset(); //SE C'E' UN'ECCEZIONE E NON VENGONO INSERITI I DATI VIENE FATTA UGUALMENTE LA RESET E NON VOGLIO
    // this.router.navigate(['']); // va alla home dopo aver aggiunto 
    alert("Prodotto inserito con successo")
  } */

  //Alternativa all'add di sopra
  addProduct():void{
    //associa gli attributi del form al Product in dataTypes e devono avere gli stessi nomi
    const product: Product=this.productForm.value;
    this.productService.addProduct(product).subscribe((res) =>{
      console.log(res)
      alert("Prodotto inserito con successo")
      this.productForm.reset();
    })
  }
}
