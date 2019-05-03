import { Component, OnInit } from '@angular/core';
import {ModalService} from "vgr-komponentkartan";
import {HttpClient} from "../../../../node_modules/@angular/common/http";
import {AuthService} from "../../service/auth.service";
import {TokenResponse} from '../../domain/token-response';

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
    this.http.post('/api/login', {username: this.userId, password: this.password})
      .subscribe((response: TokenResponse) => {

        this.modalService.closeDialog("loginModal");

        this.authService.jwt = response.token;
      });
  }

  openDialog() {
    this.modalService.openDialog('loginModal');
  }
}
