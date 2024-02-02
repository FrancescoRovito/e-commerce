import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';  //è la libreria fulcro di Angular
//aggiungere il nostro servizio creato
import { ApiService } from '../servicies/api.service';
import { API } from '../constants';
import { Route, Router } from '@angular/router';
import { SellerService } from '../servicies/seller.service';

@Component({  //E' un decoratore
  selector: 'app-seller-auth',
  templateUrl: './seller-auth.component.html',
  styleUrls: ['./seller-auth.component.css']
})
export class SellerAuthComponent implements OnInit{ //On è un evento, Init sta per initializzation
  /* PRIMA VERSIONE SENZA SERVIZI
     httpClient è un modulo di Angular che fornisce un'interfaccia per effettuare richieste HTTP. 
     Consente al tuo componente di comunicare con un server per recuperare o inviare dati.
     Questo è un esempio di iniezione delle dipendenze in Angular.

  constructor(private http:HttpClient){} */

  constructor(private sellerService:SellerService, private router: Router){}

  // è un nome del metodo creato da me
  isSellerAuthPage(): boolean {
    console.log(this.router.url)
    return this.router.url === '/seller-auth';
  }
  
  /* Il metodo ngOnInit() è un metodo dell'interfaccia OnInit e viene chiamato automaticamente 
    quando un componente viene inizializzato. Fornisce un punto di ingresso per eseguire logica di
    inizializzazione quando un componente è pronto per essere utilizzato.
    > Angular chiama automaticamente il metodo ngOnInit() quando il componente viene creato.
    > È comunemente utilizzato per eseguire operazioni di inizializzazione necessarie per il componente. 
      Ad esempio, potresti voler recuperare dati iniziali da un servizio, inizializzare variabili, o 
      fare altre operazioni necessarie prima che il componente venga visualizzato.
    > ngOnInit() viene chiamato solo una volta durante il ciclo di vita del componente, 
      immediatamente dopo la creazione del componente. */ 
  ngOnInit(): void {
      this.sellerService.reloadSeller()
  }

  //qui faremo una richiesta REST al nostro server
  signup(data:object):void{
    //Stampa i dati ricevuti sulla console usando console.warn(data), warn sta per warning
    console.warn(data)
    //siamo nel componente che chiama la funzione, la funzione chiama il servizio
    this.sellerService.signup(data);
    //i parametri li dobbiamo passare nella richiesta ma nella richiesta non si può passare un dizionario
    //ma un ogg di tipo Httpparams e lo creiamo nel servizio

    // ERA UN ESEMPIO PER CAPIRE let params={'nome':'simone'};

    /** SENZA LA STRUTTURA DI SERVIZI 
     * Chiama il metodo makeRequest del servizio apiService. Questo sembra essere un wrapper 
     * attorno alle chiamate HTTP per gestire le richieste in modo più centralizzato.
     * 
     * data sono i dati che si vogliono inviare con la richiesta post
     * 
     * subscribe((response) => { console.warn(response); }): Si sottoscrive all'observable 
     * restituito dalla chiamata HTTP. Quando la risposta viene ricevuta, la funzione di callback 
     * viene chiamata con la risposta ricevuta. In questo caso, la risposta viene stampata 
     * sulla console di avvertimento del browser.
     
    # this.apiService.makeRequest('post',API.user+API.registerUser,data).subscribe((result)=>{ #
      /**
       * In questo modo la rotta è libera,cioè se un utente nell'url scrive a mano
       * http://localhost:4200/seller-home anche se non si logga, entra lo stesso.
       * Sono necessari dei protocolli di sicurezza. Ci viene in aiuto il guard poichè 
       * se l'utente scrive a mano l'url dato che non passa dall'autenticazione viene bloccato.
       * Andiamo nell'auth.guard.ts
       
      # if(result){ #
        # this.router.navigate(['seller-home']); #
      }
      //console.warn(result);
    # }) #*/

    /* PRIMA VERSIONE SENZA SERVIZI
       > this.http fa riferimento all'istanza di HttpClient iniettata nel costruttore.
         post prende l'URL di destinazione e i dati da inviare nella richiesta (data).
         In data avremo il request body che riceve il metodo registerUser
       > subscribe() è un metodo che permette di "ascoltare" le risposte della richiesta HTTP
         che restituiscono un oggetto Observable. Prende una funzione di callback come argomento.
         La funzione di callback, in questo caso, è scritta come una lambda function (response) => (console.warn(response)).
       > response rappresenta la risposta ricevuta dal server dopo l'esecuzione della richiesta 

    this.http.post("http://localhost:8080/user/registerUser", data).subscribe((response)=>(console.warn(response))) */


  }
}
