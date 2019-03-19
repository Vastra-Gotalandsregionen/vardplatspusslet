import {Component, OnDestroy} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Clinic} from "../../domain/clinic";
import {AuthService} from "../../service/auth.service";
import {Subscription} from "rxjs";
import {Management} from "../../domain/management";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnDestroy {

  managements: Management[];

  subscription: Subscription;

  constructor(private http: HttpClient,
              private authService: AuthService) {
    this.subscription = this.authService.isUserLoggedIn.subscribe(value => {
      this.fetchManagements();
    });
  }

  fetchManagements() {
    this.http.get<Management[]>('/api/management').subscribe(managements => {
      this.managements = managements;
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  get loggedIn(): boolean {
    return this.authService.isAuthenticated();
  }

}
