import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Unit} from "../../../domain/unit";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.scss']
})
export class StatisticsComponent implements OnInit {

  units: Unit[] = [];

  fromDate = new FormControl();
  toDate = new FormControl();

  maxDate = new Date('2099-12-31');
  minDate = new Date('2000-01-01');

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.http.get<Unit[]>('/api/unit')
      .subscribe((units: Unit[]) => {
        this.units = units;
      });

    let firstInMonth = new Date();
    firstInMonth.setMilliseconds(0);
    firstInMonth.setSeconds(0);
    firstInMonth.setMinutes(0);
    firstInMonth.setHours(0);
    firstInMonth.setDate(1);
    firstInMonth.setMonth(firstInMonth.getMonth() - 1);

    this.fromDate.setValue(firstInMonth);

    const lastInMonth = new Date(firstInMonth.getFullYear(), firstInMonth.getMonth() + 1, 0);

    this.toDate.setValue(lastInMonth);
  }

  toString(date: Date) : string {
    let clonedDate = new Date(date.valueOf());
    clonedDate.setMinutes(clonedDate.getMinutes() - clonedDate.getTimezoneOffset());
    return clonedDate.toISOString().slice(0, 10);
  }

}
