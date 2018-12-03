import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AuthService} from "../service/auth.service";

@Injectable()
export class JwtHttpInterceptor implements HttpInterceptor{

  constructor(private authService: AuthService) {
  }


  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const token = this.authService.jwt;

    if (this.authService.isTokenExpired()) {
      // this.location.go('/'); TODO
      // this.authService.resetAuth();
      // return Observable.of();
    }

    if (token) {
      const authReq = req.clone({setHeaders: {Authorization: `Bearer ${token}`}});
      return next.handle(authReq);
    } else {
      return next.handle(req);
    }

/*      if (typeof url === 'string') { // meaning we have to add the token to the options, not in url
        if (!options) {
          // let's make option object
          options = {headers: new Headers()};
        }
        options.headers.set('Authorization', `Bearer ${token}`);
      } else {
        // we have to add the token to the url object
        url.headers.set('Authorization', `Bearer ${token}`);
      }
    }

    const timerSubscription: Subscription = Observable.timer(250) // Delay when progress indicator is shown.
      .take(1)
      .subscribe(undefined, undefined, () => {
        this.stateService.startShowProgress();
      });

    return super.request(url, options)
      .catch(error => {
        this.errorHandler.notifyError(error);
        return Observable.throw(error);
      })
      .finally(() => {
        timerSubscription.unsubscribe();
        this.stateService.stopShowProgress()
      });*/


  }
}
