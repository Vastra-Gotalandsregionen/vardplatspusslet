import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {Clinic} from "../../domain/clinic";
import {Unit} from "../../domain/unit";
import {AuthService} from "../../service/auth.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-clinic',
  templateUrl: './clinic.component.html',
  styleUrls: ['./clinic.component.scss']
})
export class ClinicComponent implements OnDestroy {

  clinic: Clinic;
  units: Unit[];
  error: string;
  management: string;

  isFetchingUnits: boolean = false;

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
      this.http.get<Clinic>('/api/clinic/' + params.id).subscribe(clinic => {
        if (clinic) {
          this.clinic = clinic;
          this.management = clinic.management.name;
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
        this.isFetchingUnits = false;
      });

      this.isFetchingUnits = true; // To avoid flickering of text stating that the user doesn't have permission to any units.
      this.http.get<Unit[]>('/api/unit?clinic=' + params.id).subscribe(units => {
        this.units = units;
        this.isFetchingUnits = false;
      }, (error1 => {
        this.isFetchingUnits = false;
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
