import { Component } from '@angular/core';

@Component({
  selector: 'app-root', //da qua parte tutto. Il file index.html ha il tag <app-root>, va richiamato lì
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'] //è un array, ce ne può essere più di uno
})
export class AppComponent {
  title = 'front';
}
