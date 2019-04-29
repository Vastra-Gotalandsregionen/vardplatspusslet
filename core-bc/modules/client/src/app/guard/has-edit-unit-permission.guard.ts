import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs/Observable';
import {AuthService} from "../service/auth.service";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class HasEditUnitPermissionGuard implements CanActivate {

  constructor(private authService: AuthService,
              private http: HttpClient,
              private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    let hasEditUnitPermission = this.authService.hasEditUnitPermission(next.params.id);

    if (hasEditUnitPermission) {
      return true;
    } else {
      this.router.navigate(['/'], { queryParams: { returnUrl: state.url }});
      return false;
    }
  }
}
