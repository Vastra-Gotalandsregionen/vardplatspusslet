import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Inject, Injectable} from "@angular/core";
import {Observable, throwError} from "rxjs";
import "rxjs-compat/add/operator/catch";
import {ErrorDialogService} from "../service/error-dialog.service";

@Injectable()
export class HttpErrorHandlerInterceptor implements HttpInterceptor {

  constructor(@Inject(ErrorDialogService) private errorDialogService: ErrorDialogService) {
  }


  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    return next.handle(req)
      .catch(error => {
        this.errorDialogService.showErrorDialog(error);
        return throwError(error);
      });
  }

}
