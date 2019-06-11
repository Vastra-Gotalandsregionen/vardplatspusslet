import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../service/auth.service";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  constructor(private authService: AuthService) {
  }

  ngOnInit() {

  }

  get admin(): boolean {
    return this.authService.isAdmin();
  }

  get unitAdmin(): boolean {
    return this.authService.isUnitAdmin();
  }
}
