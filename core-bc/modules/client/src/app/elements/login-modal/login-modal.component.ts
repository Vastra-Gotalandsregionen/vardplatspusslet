import { Component, OnInit } from '@angular/core';
import {ModalService} from "vgr-komponentkartan";
import {HttpClient} from "../../../../node_modules/@angular/common/http";
import {AuthService} from "../../service/auth.service";

@Component({
  selector: 'app-login-modal',
  templateUrl: './login-modal.component.html',
  styleUrls: ['./login-modal.component.scss']
})
export class LoginModalComponent implements OnInit {

  userId: string;
  password: string;

  constructor(protected http: HttpClient,
              public modalService: ModalService,
              protected authService: AuthService) { }

  ngOnInit() {
  }

  login() {
    this.http.post('/api/login', {username: this.userId, password: this.password}, {responseType: 'text'})
      .subscribe(response => {

        this.modalService.closeDialog("loginModal");

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

  openDialog() {
    this.modalService.openDialog('loginModal');
  }
}
