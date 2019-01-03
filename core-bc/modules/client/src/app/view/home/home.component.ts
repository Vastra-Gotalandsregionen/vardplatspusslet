import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Clinic} from "../../domain/clinic";
import {AuthService} from "../../service/auth.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {

  clinics: Clinic[];

  userId: string;
  password: string;

  subscription: Subscription;

  constructor(private http: HttpClient,
              private authService: AuthService) {
    this.subscription = this.authService.isUserLoggedIn.subscribe(value => {
      this.ngOnInit();
    });
  }

  ngOnInit() {
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

  login() {
    this.http.post('/api/login', {username: this.userId, password: this.password}, {responseType: 'text'})
      .subscribe(response => {
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

}
