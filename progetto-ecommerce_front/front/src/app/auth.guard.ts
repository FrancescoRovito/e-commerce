import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { SellerService } from './servicies/seller.service';

/**
 * Injectable indica che authGuard è un servizio che può essere iniettato 
 * in altri componenti, servizi o moduli. La configurazione providedIn: 'root' 
 * specifica che il servizio deve essere fornito a livello di applicazione, 
 * rendendolo accessibile ovunque nell'applicazione.
 */
@Injectable({
  providedIn: 'root'
})

/**
 * implementa l'interfaccia CanActivate. Questo significa che AuthGuard sarà utilizzato come una 
 * guardia di attivazione per le route di Angular.
 */
export class authGuard implements CanActivate{
  constructor(private sellerService:SellerService){}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree{
      //una volta registrato questo mi permette di entrare nella seller-home se ci accedo
      //direttamente dall'url. Se non metto l'if dall'url non entro nella seller-home.
      //verifica se il seller è registrato, e quindi loggato
      if(localStorage.getItem('seller')){
        return true;
      }
      
      /**
       * Il metodo canActivate restituisce direttamente l'Observable<boolean> isSellerLoggedIn 
       * del SellerService. Questo significa che la navigazione sarà consentita solo se 
       * l'Observable emette un valore true, e verrà bloccata se emette false o non emette alcun valore.
       */
      return this.sellerService.isSellerLoggedIn;
    }
}

/* QUESTO ME LO HA CREATO LUI SENZA @INJECTABLE
export const authGuard: CanActivateFn = (route, state) => {
  return true;
}; */
