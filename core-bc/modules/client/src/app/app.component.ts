import {Component, Inject, ViewContainerRef} from '@angular/core';
import {AuthService} from "./service/auth.service";
import {ErrorDialogService} from "./service/error-dialog.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  title = 'vardplatspusslet';
  newVersionAvailable = false;
  compiledTimestamp;

  constructor(@Inject(ErrorDialogService) errorDialogService,
              @Inject(ViewContainerRef) viewContainerRef,
              protected authService: AuthService,
              private http: HttpClient) {
    errorDialogService.setRootViewContainerRef(viewContainerRef);

    setInterval(() => {
      this.http.get<string>('/api/appInfo/compiledTimestamp').subscribe(compiledTimestamp => {
        if (!this.compiledTimestamp) {
          this.compiledTimestamp = compiledTimestamp;
        } else if (this.compiledTimestamp !== compiledTimestamp) {
          this.newVersionAvailable = true;
        }
      });
    }, 60000);
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
