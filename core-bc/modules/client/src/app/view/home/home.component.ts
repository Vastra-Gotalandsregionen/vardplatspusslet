import {Component, OnDestroy} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../service/auth.service";
import {Subscription} from "rxjs";
import {Management} from "../../domain/management";
import "rxjs-compat/add/operator/finally";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnDestroy {

  managements: Management[];

  subscription: Subscription;
  errorMessage: string;

  isFetchingManagements: boolean = false;

  constructor(private http: HttpClient,
              private authService: AuthService) {
    this.subscription = this.authService.isUserLoggedIn.subscribe(value => {
      this.fetchManagements();
    });
  }

  fetchManagements() {
    this.errorMessage = null;

    this.isFetchingManagements = true; // To avoid flickering of text stating that the user doesn't have permission to any clinics.
    this.http.get<Management[]>('/api/management')
      .finally(() => {
        this.isFetchingManagements = false;
      })
      .subscribe(managements => {
      this.managements = managements;

      if (!managements || managements.length === 0) {
        this.errorMessage = 'Du har inte behörighet till någon förvaltning.';
      } else {
        this.errorMessage = null;
      }
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
