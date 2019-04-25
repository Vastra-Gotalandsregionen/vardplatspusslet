import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AuthService} from "../service/auth.service";
import "rxjs-compat/add/observable/of";
import {Router} from "@angular/router";

@Injectable()
export class JwtHttpInterceptor implements HttpInterceptor{

  constructor(private authService: AuthService,
              private router: Router) {
  }


  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const token = this.authService.jwt;

    if (this.authService.isTokenExpired()) {
      this.router.navigate(['/']);
      this.authService.resetAuth();
      return Observable.of();
    }

    if (token) {
      const authReq = req.clone({setHeaders: {Authorization: `Bearer ${token}`}});
      return next.handle(authReq);
    } else {
      return next.handle(req);
    }
  }
}
