import {Component, OnDestroy} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Clinic} from "../../domain/clinic";
import {AuthService} from "../../service/auth.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnDestroy {

  clinics: Clinic[];

  subscription: Subscription;

  constructor(private http: HttpClient,
              private authService: AuthService) {
    this.subscription = this.authService.isUserLoggedIn.subscribe(value => {
      this.fetchClinics();
    });
  }

  fetchClinics() {
    this.http.get<Clinic[]>('/api/clinic').subscribe(clinics => {
      this.clinics = clinics;
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
