import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {Unit} from "../../domain/unit";
import {DropdownItem, ListItemComponent} from "vgr-komponentkartan";
import {Bed} from "../../domain/bed";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Patient} from "../../domain/patient";
import {Clinic} from "../../domain/clinic";
import {DeleteModalComponent} from "../../elements/delete-modal/delete-modal.component";

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

  vacantBeds: DropdownItem<number>[];

  @ViewChild(DeleteModalComponent) appDeleteModal: DeleteModalComponent;

  chosenVacantBedId: number;

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute) { }

  ngOnInit() {

    this.route.params.subscribe(params => {

      let clinicId = params.clinicId;

      this.http.get<Clinic>('/api/clinic/' + clinicId).subscribe(clinic => {
        this.clinic = clinic;
      });

      this.http.get<Unit>('/api/unit/' + clinicId + '/' + params.id).subscribe(unit => {
        if (unit) {
          this.unit = unit;

          this.vacantBeds = unit.beds
            .filter(bed => !bed.occupied)
            .map(bed => {
              return {
                displayName: bed.label,
                value: bed.id
              }
            });

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
        label: [bed.patient ? bed.patient.label : null],
        leaveStatus: [bed.patient ? bed.patient.leaveStatus : null]
      })
    });

  }

  private reinitForm(bed: Bed) {
    if (!bed) {
      bed = new Bed();
    }

    this.bedForm.setValue({
      id: bed.id ? bed.id : null,
      occupied: bed.occupied ? bed.occupied : null,
      label: bed.label ? bed.label : null,
      patient: {
        id: bed.patient ? bed.patient.id : null,
        label: bed.patient ? bed.patient.label : null,
        leaveStatus: bed.patient ? bed.patient.leaveStatus : null
      }
    });
  }

  setCurrentBed(event: any, bed: Bed) {
    if (event) {
      // Then the row is expanded and not collapsed.
      this.reinitForm(bed);
    }
  }

  save() {
    let bed = new Bed();
    let bedModel = this.bedForm.value;

    bed.id = bedModel.id;
    bed.label = bedModel.label;
    bed.occupied = !!bedModel.occupied;

    if (bedModel.patient.label) {
      bed.patient = new Patient();
      bed.patient.id = bedModel.patient.id;
      bed.patient.label = bedModel.patient.label;
      bed.patient.leaveStatus = bedModel.patient.leaveStatus;
    } else {
      bed.patient = null;
    }

    this.http.put('/api/bed/' + this.clinic.id + '/' + this.unit.id, bed)
      .subscribe(bed => {
        this.ngOnInit();
      });
  }

  collapse(element: ListItemComponent) {
    element.setExpandOrCollapsed();
  }

  openDeleteModal(bed: Bed) {
    this.bedForDeletion = bed;
    this.appDeleteModal.open();
  }

  confirmDelete() {
    this.http.delete('/api/bed/'  + this.clinic.id + '/' + this.unit.id + '/' + this.bedForDeletion.id)
      .subscribe(() => {
        this.ngOnInit();
      }); // todo error handling
  }

  chooseBedForLeavePatient(patient) {
    patient.leaveStatus = null;

    let bed = this.unit.beds.find(bed => bed.id === this.chosenVacantBedId);
    bed.patient = patient;
    bed.occupied = true;
    this.http.put('/api/bed/' + this.clinic.id + '/' + this.unit.id, bed)
      .subscribe(bed => {
        this.ngOnInit();
      });

  }
}
