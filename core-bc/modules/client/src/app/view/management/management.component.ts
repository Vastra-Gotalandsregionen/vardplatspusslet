import {Component, OnDestroy, OnInit} from '@angular/core';
import {Clinic} from "../../domain/clinic";
import {Subscription} from "rxjs";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {AuthService} from "../../service/auth.service";
import {Management} from "../../domain/management";

@Component({
  selector: 'app-management',
  templateUrl: './management.component.html',
  styleUrls: ['./management.component.scss']
})
export class ManagementComponent implements OnDestroy {

  management: Management;
  clinics: Clinic[] = [];
  error: string;

  isFetchingClinics: boolean = false;

  notFoundText = 'Oops. Inget fanns här...';

  subscription: Subscription;

  constructor(private http: HttpClient,
              private route: ActivatedRoute,
              private authService: AuthService) {
    this.subscription = this.authService.isUserLoggedIn.subscribe(value => {
      this.fetch();
    });
  }

  fetch() {
    this.route.params.subscribe(params => {
      this.http.get<Management>('/api/management/' + params.id).subscribe(management => {
        if (management) {
          this.management = management;
        } else {
          this.error = this.notFoundText;
        }
      }, (error1: HttpErrorResponse) => {
        console.log(error1);
        if (error1.status === 404) {
          this.error = this.notFoundText;
        } else {
          this.error = error1.message;
        }
      });

      this.isFetchingClinics = true; // To avoid flickering of text stating that the user doesn't have permission to any clinics.
      this.http.get<Clinic[]>('/api/clinic/management?management=' + params.id).subscribe(clinics => {
        this.clinics = clinics;
        this.isFetchingClinics = false;
      }, (error1 => {
        this.isFetchingClinics = false;
      }));

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
