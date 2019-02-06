import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../service/auth.service";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {

  userId: string;
  password: string;

  constructor(private http: HttpClient,
              private authService: AuthService) { }

  ngOnInit() {
  }

  login() {
    this.http.post('/api/login', {username: this.userId, password: this.password}, {responseType: 'text'})
      .subscribe(response => {
        this.authService.jwt = response;
      }, error => {
        /*if (Object.getPrototypeOf(error) === Object.getPrototypeOf(new TimeoutError())) {
          this.loginMessage = 'Tidsgränsen för anropet gick ut.'
        } else if (error.status && error.status >= 400 && error.status < 500) {
          this.loginMessage = 'Felaktiga inloggningsuppgifter';
        } else {
          this.loginMessage = 'Tekniskt fel';
        }*/

        console.log(error);
      });
  }

}
