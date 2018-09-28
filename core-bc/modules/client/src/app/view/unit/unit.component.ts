import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {Unit} from "../../domain/unit";
import {ListItemComponent, ModalService} from "vgr-komponentkartan";
import {Bed} from "../../domain/bed";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Patient} from "../../domain/patient";
import {Clinic} from "../../domain/clinic";

@Component({
  selector: 'app-unit',
  templateUrl: './unit.component.html',
  styleUrls: ['./unit.component.scss']
})
export class UnitComponent implements OnInit {

  unit: Unit;
  clinic: Clinic;

  error: string;
  notFoundText = 'Oops. Inget fanns här...';

  bedForm: FormGroup;
  bedForDeletion: Bed;

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              protected modalService: ModalService) { }

  ngOnInit() {

    this.route.params.subscribe(params => {

      let clinicId = params.clinicId;

      this.http.get<Clinic>('/api/clinic/' + clinicId).subscribe(clinic => {
        this.clinic = clinic;
      });

      this.http.get<Unit>('/api/unit/' + clinicId + '/' + params.id).subscribe(unit => {
        if (unit) {
          this.unit = unit;
          this.initForm(null);
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
      label: [bed.label],
      patient: this.formBuilder.group({
        id: [bed.patient ? bed.patient.id : null],
        label: [bed.patient ? bed.patient.label : null]
      })
    });
  }

  setCurrentBed(event: any, bed: Bed) {
    if (event) {
      // Then the row is expanded and not collapsed.
      this.initForm(bed);
    }
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

    this.http.put('/api/bed/' + this.clinic.id + '/' + this.unit.id, bed)
      .subscribe(bed => {
        this.ngOnInit();
      });
  }

  collapse(element: ListItemComponent) {
    element.setExpandOrCollapsed();
  }

  openDeleteModel(bed: Bed) {
    this.bedForDeletion = bed;
    this.modalService.openDialog('deleteModal');
  }

  confirmDelete() {
    this.http.delete('/api/bed/'  + this.clinic.id + '/' + this.unit.id + '/' + this.bedForDeletion.id)
      .subscribe(() => {
        this.modalService.closeDialog('deleteModal');
        this.ngOnInit();
      }); // todo error handling
  }
}
