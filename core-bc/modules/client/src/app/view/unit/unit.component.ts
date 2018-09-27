import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {Unit} from "../../domain/unit";
import {ListItemComponent} from "vgr-komponentkartan";
import {Bed} from "../../domain/bed";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {Patient} from "../../domain/patient";

@Component({
  selector: 'app-unit',
  templateUrl: './unit.component.html',
  styleUrls: ['./unit.component.scss']
})
export class UnitComponent implements OnInit {

  unit: Unit;

  error: string;
  notFoundText = 'Oops. Inget fanns här...';

  bedForm: FormGroup;

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute) { }

  ngOnInit() {
    this.initForm(null);

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

  private initForm(bed: Bed) {
    if (!bed) {
      bed = new Bed();
    }

    this.bedForm = this.formBuilder.group({
      id: [bed.id],
      occupied: [bed.occupied],
      clinic: [bed.clinic],
      label: [bed.label],
      patient: this.formBuilder.group({
        id: [bed.patient ? bed.patient.id : null],
        label: [bed.patient ? bed.patient.label : null]
      })
    });
  }

  setCurrentBed(event: any, bed: Bed) {
    this.initForm(bed);
  }

  save() {
    let bed = new Bed();
    let bedModel = this.bedForm.value;

    bed.id = bedModel.id;
    bed.label = bedModel.label;
    bed.occupied = bedModel.occupied;

    bed.patient = new Patient();
    bed.patient.id = bedModel.patient.id;
    bed.patient.label = bedModel.patient.label;

    this.http.put('/api/bed', bed)
      .subscribe(bed => {
        this.ngOnInit();
      });
  }

  collapse(element: ListItemComponent) {
    element.setExpandOrCollapsed();
  }

}
