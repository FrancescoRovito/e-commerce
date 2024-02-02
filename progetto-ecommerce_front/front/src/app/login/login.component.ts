import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ApiService } from '../servicies/api.service';
import { API } from '../constants';
import { LoginService } from '../servicies/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  constructor(private loginService:LoginService){}
  
  ngOnInit(): void {}

  signin(data:object):void{
    console.warn(data)
    this.loginService.signin(data);
    
    //PRIMA DI CREARE IL LOGIN SERVICE DIVIDENDO I COMPITI
    //this.apiService.makeRequest('post',API.user+API.login,data).subscribe((response)=>{
    //console.warn(response);
    //})
  }
}
