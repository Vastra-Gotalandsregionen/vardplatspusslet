import {Injectable} from '@angular/core';
import {JwtHelper} from 'angular2-jwt/angular2-jwt';
import {Router} from '@angular/router';
import {Subscription} from "rxjs/Subscription";
import {interval} from "rxjs";
import "rxjs/add/operator/switchMap";
import "rxjs/add/operator/retry";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class AuthService {

  renewSubscription: Subscription;

  constructor(private jwtHelper: JwtHelper,
              private http: HttpClient,
              private router: Router) {

    const localStorageToken = localStorage.getItem('jwtToken');

    if (localStorageToken) {
      this.jwt = localStorageToken;
    }

    if (this.isTokenExpired()) {
      this.resetAuth();
    }

    interval(10000)
      .subscribe(_ => {
        if (this.isTokenExpired()) {
          this.resetAuth();
        }
      });
  }

  private startRenew() {
    if (this.renewSubscription) {
      this.renewSubscription.unsubscribe();
    }

    this.renewSubscription = interval(60000)
      .switchMap(() => this.http.post('/api/login/renew', this.jwt, {responseType: 'text'}))
      .retry(4)
      .subscribe(
        response => this.jwt = response,
        error => {
          this.jwt = null;
          this.renewSubscription.unsubscribe();
        }
      );
  }

  isTokenExpired() {
    const token = this.getToken();
    return token && (token.exp - new Date().getTime() / 1000 < 0);
  }

  getToken(): any {
    const jwtTokenString = this.jwt;
    return jwtTokenString ? this.jwtHelper.decodeToken(jwtTokenString) : null;
  }

  isAuthenticated(): boolean {
    return this.getToken() && !this.isTokenExpired();
  }

  get jwt(): string {
    return localStorage.getItem('jwtToken');
  }

  set jwt(value: string) {

    if (value) {

      localStorage.setItem('jwtToken', value);

      this.startRenew();
    } else if (this.getToken()) {
      // Logout

      this.router.navigate(['/']);
      localStorage.removeItem('jwtToken');
    }

  }

  resetAuth() {
    this.jwt = null;
  }

  getLoggedInUserId(): string {
    const token = this.getToken();
    return token ? token.sub : null;
  }

  getLoggedInDisplayName(): string {
    const token = this.getToken();
    return token ? token.displayName : null;
  }

  isAdmin() {
    const token = this.getToken();
    if (token) {
      const roles = <string[]>token.roles;
      return roles.indexOf('ADMIN') > -1;
    }

    return false;
  }

  canImpersonate() {
    const token = this.getToken();
    if (token) {
      const roles = <string[]>token.roles;
      return roles.indexOf('IMPERSONATE') > -1;
    }

    return false;
  }


}
