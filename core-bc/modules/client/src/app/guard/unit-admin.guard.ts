import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router} from '@angular/router';
import { Observable } from 'rxjs/Observable';
import {AuthService} from '../service/auth.service';

@Injectable()
export class UnitAdminGuard implements CanActivate {

  constructor(private authService: AuthService,
              private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    const admin = this.authService.isUnitAdmin() || this.authService.isAdmin();

    if (!admin) {
      this.router.navigate(['/']);
    }

    return admin;
  }
}
