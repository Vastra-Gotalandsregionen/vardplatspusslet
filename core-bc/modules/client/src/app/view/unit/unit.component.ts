import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {Unit} from "../../domain/unit";

@Component({
  selector: 'app-unit',
  templateUrl: './unit.component.html',
  styleUrls: ['./unit.component.scss']
})
export class UnitComponent implements OnInit {

  unit: Unit;

  error: string;
  notFoundText = 'Oops. Inget fanns här...';

  constructor(private http: HttpClient,
              private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params.subscribe(params => {

      this.http.get<Unit>('/api/unit/' + params.clinicId + '/' + params.id).subscribe(unit => {
        if (unit) {
          this.unit = unit;
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

    });
  }

}
