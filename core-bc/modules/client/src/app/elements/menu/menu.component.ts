import {Component, OnInit, ViewChild} from '@angular/core';
import {ModalService} from "vgr-komponentkartan";
import {AuthService} from "../../service/auth.service";
import {LoginModalComponent} from "../login-modal/login-modal.component";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  @ViewChild(LoginModalComponent) loginModal: LoginModalComponent;

  constructor(public authService: AuthService,
              public modalService: ModalService) { }

  ngOnInit() {
  }

  openLoginModal() {
    this.loginModal.openDialog();
  }

  logout() {
    this.authService.resetAuth();
  }
}
