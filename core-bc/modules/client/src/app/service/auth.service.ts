import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Subscription} from "rxjs/Subscription";
import {BehaviorSubject, interval} from "rxjs";
import "rxjs/add/operator/switchMap";
import "rxjs/add/operator/retry";
import {HttpClient} from "@angular/common/http";
import {JwtHelperService} from "@auth0/angular-jwt";
import {TokenResponse} from '../domain/token-response';

@Injectable()
export class AuthService {

  renewSubscription: Subscription;
  jwtHelper = new JwtHelperService();

  public isUserLoggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient,
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
      .switchMap(() => this.http.post('/api/login/renew', this.jwt))
      .retry(4)
      .subscribe(
        (response: TokenResponse) => this.jwt = response.token,
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

    this.isUserLoggedIn.next(this.isAuthenticated());
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

  getUnitIds(): string[] {
    const token = this.getToken();
    if (token) {
      return <string[]>token.unitIds;
    } else {
      return [];
    }
  }

  hasEditUnitPermission(unitId: string): boolean {
    return this.isAdmin() || this.getUnitIds().indexOf(unitId) > -1;
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
