import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Bed} from "../../../domain/bed";
import {Patient} from "../../../domain/patient";
import {Unit} from "../../../domain/unit";
import {HttpClient} from "../../../../../node_modules/@angular/common/http";
import {FormBuilder, FormGroup} from "@angular/forms";
import {DropdownItem} from "vgr-komponentkartan";

@Component({
  selector: 'app-bed-form',
  templateUrl: './bed-form.component.html',
  styleUrls: ['./bed-form.component.scss']
})
export class BedFormComponent implements OnInit {

  @Input('clinicId') clinicId;
  @Input('unit') unit: Unit;
  @Input('bed') bed;

  @Output('collapse') collapseEvent = new EventEmitter();
  @Output('openDeleteModal') openDeleteModalEvent = new EventEmitter();
  @Output('save') saveEvent = new EventEmitter();

  bedForm: FormGroup;

  @Input('genderDropdownItems') genderDropdownItems: DropdownItem<string>[];
  @Input('sskDropdownItems') sskDropdownItems: DropdownItem<number>[];
  @Input('leaveStatusesDropdownItems') leaveStatusesDropdownItems = [
    {
      displayName: 'Permission', value: 'PERMISSION'
    },
    {
      displayName: 'Teknisk plats', value: 'TEKNISK_PLATS'
    },
    {
      displayName: 'Tillfällig hemgång', value: 'TILLFÄLLIG_HEMGÅNG'
    },
    {
      displayName: 'Bebis???', value: 'BEBIS'
    },
    {
      displayName: 'Föräldrarum', value: 'FÖRÄLDRARUM'
    }];

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {

    /*this.http.get<Unit>('/api/unit/' + this.clinicId + '/' + this.unitId).subscribe(unit => {
      if (unit) {*/
    // this.unit = unit;

    /*this.sskDropdownItems = this.unit.ssks.map(ssk => {
      return {displayName: ssk.label, value: ssk.id};
    });*/

    // this.updateVacants(unit);

    this.initForm(this.bed);
    // } else {
    // this.error = this.notFoundText;
    // }
    /*}, (error1: HttpErrorResponse) => {
      console.log(error1);
      /!*if (error1.status === 404) {
        this.error = this.notFoundText;
      } else {
        this.error = error1.message;
      }*!/
    });*/
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
        leaveStatus: [bed.patient ? bed.patient.leaveStatus : null],
        gender: [bed.patient ? bed.patient.gender : null],
        leftDate: [bed.patient ? bed.patient.leftDate : null],
        plannedLeaveDate: [bed.patient ? bed.patient.plannedLeaveDate : null],
        carePlan: [bed.patient ? bed.patient.carePlan : null]
      }),
      ssk: bed.ssk ? bed.ssk.id : null
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
        leaveStatus: bed.patient ? bed.patient.leaveStatus : null,
        gender: bed.patient ? bed.patient.gender : null,
        leftDate: bed.patient ? bed.patient.leftDate : null,
        plannedLeaveDate: bed.patient ? bed.patient.plannedLeaveDate : null,
        carePlan: [bed.patient ? bed.patient.carePlan : null]
      },
      ssk: bed.ssk ? bed.ssk.id : null
    });
  }

  /*changeBedOrder() {
    this.bedsOrder = this.unit.beds;
    this.showChangeBedOrder = true;
  }

  hideChangeBedOrder() {
    this.showChangeBedOrder = false;
  }

  saveBedOrder() {
    this.unit.beds = this.bedsOrder;
    this.showChangeBedOrder = false;
    this.http.put('/api/unit', this.unit)
      .subscribe(unit => {

      });
  }*/

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
      bed.patient.leftDate = bedModel.patient.leftDate;
      bed.patient.gender = bedModel.patient.gender ? bedModel.patient.gender : null;
      bed.patient.plannedLeaveDate = bedModel.patient.plannedLeaveDate ? bedModel.patient.plannedLeaveDate : null;
      bed.patient.carePlan = bedModel.patient.carePlan ? bedModel.patient.carePlan : null;
    } else {
      bed.patient = null;
    }

    if (bedModel.ssk) {
      bed.ssk = this.unit.ssks.find(ssk => ssk.id === bedModel.ssk);
    }

    this.http.put('/api/bed/' + this.clinicId + '/' + this.unit.id, bed)
      .subscribe(bed => {
        // this.ngOnInit();
        this.saveEvent.emit();
      });
  }

  cancel() {
    this.collapseEvent.emit();
  }

  openDeleteModal(bed: Bed) {
    this.openDeleteModalEvent.emit();
  }
}
