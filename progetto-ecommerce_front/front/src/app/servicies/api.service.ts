import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API } from '../constants';
import { Observable } from 'rxjs';

/**
 * E' un decoratore! Consente di iniettare il servizio in altri 
 * componenti o servizi all'interno dell'applicazione Angular.
 * 
 * root qualsiasi componente ha accesso a questo service, chiunque può chiamarlo!
 * E' disponibile in tutta l'applicazione
 */
@Injectable({
  providedIn: 'root'
})

/**
 * La classe ApiService in TypeScript è un servizio Angular che fornisce 
 * metodi per effettuare richieste HTTP mediante l'utilizzo del modulo HttpClient
 */
export class ApiService {

  /**
   *  riceve il servizio HttpClient tramite l'iniezione delle dipendenze.
   */
  constructor(private http:HttpClient) { }

  /**
   * Questo metodo è progettato per effettuare richieste HTTP generiche. I parametri 
   * includono il tipo di richiesta, l'URL della richiesta, il corpo opzionale e i parametri opzionali.
   * 
   * ? dopo un nome di parametro indica che quel parametro è opzionale
   * 
   * any è un tipo denominato "any". Questo tipo è speciale perché consente di assegnare
   * qualsiasi valore a una variabile o a un parametro di funzione, ignorando la verifica 
   * del tipo durante la compilazione.
   * 
   * Observable è una classe che rappresenta una sequenza di valori asincroni. 
   * Viene spesso utilizzata per gestire flussi di dati asincroni, come ad esempio 
   * le risposte di una chiamata HTTP.
   * 
   */
  makeRequest(type:string, url:string, body?:any, params?:any): Observable<any>{
    /** let serve a dichiarare una variabile */
    let baseUrl=API.baseUrl
    url=baseUrl+url

    /**
     * Viene inizializzata una variabile options come oggetto vuoto. Questo 
     * oggetto sarà utilizzato per configurare le opzioni della richiesta HTTP, 
     * come i parametri, il corpo, ecc.
     */
    let options={}

    //supponiamo di passare un dizionario come è definito in seller-auth.component.ts
    //ma deve essere creato come un http params. Lo devo mappare e per ogni chiave del
    // dizionario mi aggiungo nell'http params chiave:valore

    //nei params posso anche creare un header e passare il parametro di autorizzazione,
    //e quindi un bearer token. FARLO!!!! MINUTO 16 DEL VIDEO


    if(params){
      /**
       * offre un modo di gestire i parametri di una richiesta HTTP in modo flessibile.
       */
      let httpParams=new HttpParams()
      /**
       * Vengono iterati tutti i parametri presenti nell'oggetto params (chiavi e valori), 
       * e vengono aggiunti all'oggetto httpParams utilizzando il metodo append. Questo è 
       * utile quando si trattano i parametri di una richiesta GET.
       * 
       * Object.keys(params): Restituisce un array contenente le chiavi dell'oggetto params.
       * Su questo array restituito si applica forEach((key) => { ... }): Itera su ciascun elemento 
       * dell'array (le chiavi) e esegue la funzione specificata all'interno delle parentesi graffe. 
       * La funzione all'interno di forEach è una funzione di callback che viene chiamata per ogni chiave 
       * presente in params e il suo scopo è quello di aggiungere ogni coppia chiave-valore (key, params[key])
       * come parametro alla classe HttpParams utilizzando il metodo append.
       */
      Object.keys(params).forEach((key) =>{
        httpParams=httpParams.append(key,params[key])
      })

      /**
       * L'oggetto options viene poi aggiornato con una nuova proprietà params, 
       * che contiene l'oggetto httpParams creato. In questo modo, se ci sono parametri, 
       * vengono inclusi nelle opzioni della richiesta HTTP. Le opzioni di una richiesta HTTP 
       * si riferiscono a parametri aggiuntivi o configurazioni che possono essere specificati 
       * quando si effettua una chiamata HTTP.
       */
      options={params:httpParams}
    }

    // Aggiungi l'header "Authorization" solo se il token è presente in localStorage
    const storedUser = localStorage.getItem('user');

    if (storedUser) {
      const tokenObject = JSON.parse(storedUser);
      const token = tokenObject.token;
      console.log(token)
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      options = { ...options, headers };
    }

    /**
     * Qui possiamo fare le nostre chiamate REST da qualsiasi componente
     */
    if(body){
      /*
       * operatore spread: copia tutto quello che è contenuto in options più body  
      */
      options={...options,body}

      //se non abbiamo un body e facciamo una put, il body essendo null darà problemi.
      //lo risolviamo inserendo post e put qua.
      //PROBABILMENTE SI PUO' RISOLVERE MEGLIO
      if(type.toLocaleLowerCase()=='put'){
        return this.http.put(url,body,options)
      }
      else if(type.toLocaleLowerCase()=='post'){
        return this.http.post(url,body,options)
      }
    }

    //SE FACCIAMO UNA POST E NON GLI PASSIAMO IL BODY DARA' PROBLEMI PERCHE'
    //NON ESEGUE QUESTO IF MA ESEGUIRA' IL RETURN CHE E' UNA GET E AVREMO PROBLEMI NEL SERVER
    //SE USIAMO PERO' LA GIUSTA LOGICA NON CI SONO PROBLEMI

    //STIAMO DEFINENDO NOI MALE LE RICHIESTE, DOVREMMO SISTEMARLE
    if(type.toLocaleLowerCase()=='delete'){
      return this.http.delete(url, options)
    }
    return this.http.get(url,options);
  }
}
