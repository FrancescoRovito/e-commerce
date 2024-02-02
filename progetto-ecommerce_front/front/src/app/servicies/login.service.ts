import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { API } from '../constants';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private apiService:ApiService) { }

  signin(data:object):void{
    let result=this.apiService.makeRequest("post",API.user+API.login,data).subscribe((response)=>{
      console.warn(response);
    })
  }
}
