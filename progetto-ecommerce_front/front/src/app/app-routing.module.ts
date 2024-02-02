import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { SellerAuthComponent } from './seller-auth/seller-auth.component';
import { LoginComponent } from './login/login.component';
import { CartComponent } from './cart/cart.component';
import { SellerHomeComponent } from './seller-home/seller-home.component';
import { authGuard } from './auth.guard';


/* routes è un array di oggetti in Angular utilizzato per definire le rotte dell'applicazione. 
   Ciascuna voce dell'array rappresenta una rotta e contiene informazioni su come la rotta 
   dovrebbe essere gestita; path è il percorso nell'url. Le pagine sono definite dai componenti */

   //PASSARE I PARAMETRI AL ROUTING
const routes: Routes = [
  {path:'', component:HomeComponent}, //'' vuold dire la home page
  {path:'seller-auth', component:SellerAuthComponent},
  {path:'login', component:LoginComponent},
  {path:'cart', component:CartComponent},
  {path:'seller-home', component:SellerHomeComponent},
  /**
   * path: 'seller-home': specifica il percorso dell'URL che attiverà il componente SellerHomeComponent. 
   * Quindi, quando l'URL dell'applicazione è 'seller-home', Angular caricherà il componente SellerHomeComponent.
   * 
   * Can activate accetta un array. canActivate: [authGuard]: indica che prima di permettere l'accesso 
   * al percorso 'seller-home', verrà eseguita un'operazione di attivazione. In questo caso, si 
   * sta utilizzando un servizio denominato authGuard come guardia di attivazione. Le guardie di 
   * attivazione sono utilizzate per controllare la navigazione in base a determinate condizioni. AuthGuard 
   * implementa l'interfaccia CanActivate, e Angular invoca il suo metodo canActivate 
   * per decidere se consentire o meno l'accesso al percorso 'seller-home se ritorna true'.
   */

  {path: 'seller-home', component:SellerHomeComponent, canActivate:[authGuard]}
];

//è UN DECORATORE
@NgModule({
  imports: [RouterModule.forRoot(routes)],  //routes sono le routes che io andrò a definire
  exports: [RouterModule]
})
export class AppRoutingModule { }
