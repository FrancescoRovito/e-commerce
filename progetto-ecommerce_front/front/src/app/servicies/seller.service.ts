import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { API } from '../constants';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class SellerService {
  /**
   * isSellerLoggedIn è un oggetto di tipo BehaviorSubject<boolean>. 
   * Un BehaviorSubject è un tipo di oggetto osservabile che può essere usato per 
   * tenere traccia dello stato corrente e notificare i suoi osservatori 
   * quando lo stato cambia. In questo caso, viene inizializzato con il valore booleano false, 
   * il che potrebbe indicare che inizialmente il seller non è connesso.
   */
  isSellerLoggedIn=new BehaviorSubject<boolean>(false);
  /**
   * 
   * i parametri vengono iniettati automaticamente quando un'istanza di 
   * SellerService viene creata. Questo è un esempio di Dependency Injection
   */
  constructor(private apiService:ApiService, private router:Router) { }

  signup(data:object):void{
    /** ritorna un Observable al quale mi posso sottoiscrivere per ricevere una risposta */
    let result=this.apiService.makeRequest("post",API.user+API.registerAdmin,data)
    /**
     * Questo è tipico di un flusso di RxJS, il che significa che si sta aspettando 
     * una risposta asincrona dalla richiesta HTTP. Quando la risposta è disponibile, 
     * la funzione di callback specificata sarà eseguita.
     * Response rappresenta la risposta ricevuta dalla chiamata asincrona fatta con result, 
     * Quando la richiesta HTTP ha successo
     */
    result.subscribe((response)=>{
      //eventuali controlli da fare...

      // appena si sottoscrive la richiesta, e arriva la risposta e la risposta
      // ha un corpo, andiamo a salvare il corpo della risposta nella nostra
      // variabile seller. essendo un JSON lo trasformo in stringa
      localStorage.setItem('user',JSON.stringify(response))
      /**
       * Se la chiamata HTTP ha avuto successo, viene chiamato next(true) sull'oggetto isSellerLoggedIn, 
       * indicando che il seller è ora connesso (è registrato). Questo potrebbe attivare eventuali 
       * osservatori che ascoltano questo oggetto BehaviorSubject.
       */
      this.isSellerLoggedIn.next(true);
      /**
       * viene eseguito un reindirizzamento alla rotta 'seller-home' utilizzando il servizio di routing router
       */
      this.router.navigate(['seller-home']); 
    })
    console.log(result)
  }

  //in questo modo se cambio pagina rimangono salvati i dati acquisiti
  /**
   * Se esiste un valore associato alla chiave 'seller', imposta il valore di isSellerLoggedIn a 
   * true e naviga verso la rotta 'seller-home'. Questo indica che l'utente venditore è già 
   * autenticato e viene reindirizzato alla sua home page.
   */
  reloadSeller():void{
    if(localStorage.getItem('seller')){
      this.isSellerLoggedIn.next(true);
      this.router.navigate(['seller-home']);
    }
  }
}
