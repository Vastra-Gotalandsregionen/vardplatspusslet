import {Component, Inject, ViewContainerRef} from '@angular/core';
import {AuthService} from "./service/auth.service";
import {ErrorDialogService} from "./service/error-dialog.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  title = 'vardplatspusslet';

  constructor(@Inject(ErrorDialogService) errorDialogService,
              @Inject(ViewContainerRef) viewContainerRef,
              protected authService: AuthService) {
    errorDialogService.setRootViewContainerRef(viewContainerRef);
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
}
