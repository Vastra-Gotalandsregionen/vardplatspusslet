import {Component} from '@angular/core';
import {ModalService} from "vgr-komponentkartan";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./service/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  title = 'vardplatspusslet';

  userId: string;
  password: string;

  constructor(protected modalService: ModalService,
              protected http: HttpClient,
              protected authService: AuthService) {}

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

  getLoggedInDisplayName() {
    return this.authService.getLoggedInDisplayName();
  }

  isLoggedIn() {
    return this.authService.isAuthenticated();
  }

  get admin() {
    return this.authService.isAdmin();
  }

  logout() {
    this.authService.resetAuth();
  }
}
