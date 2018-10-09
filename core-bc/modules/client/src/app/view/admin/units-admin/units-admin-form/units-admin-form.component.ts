import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FormArray, FormBuilder, FormGroup} from "@angular/forms";
import {Unit} from "../../../../domain/unit";
import {Clinic} from "../../../../domain/clinic";
import {Ssk} from "../../../../domain/ssk";

@Component({
  selector: 'app-units-admin-form',
  templateUrl: './units-admin-form.component.html',
  styleUrls: ['./units-admin-form.component.scss']
})
export class UnitsAdminFormComponent implements OnInit {

  @Input('showId') showId: boolean;
  @Input('showDelete') showDelete: boolean;
  @Input('unit') unit: Unit;

  @Output() openDeleteEvent: EventEmitter<any> = new EventEmitter();
  @Output() cancelEvent: EventEmitter<any> = new EventEmitter();
  @Output() saveEvent: EventEmitter<any> = new EventEmitter();

  clinics: Clinic[] = [];

  unitForm: FormGroup;

  clinicDropdownItems: { displayName: string; value: string }[] = [];

  editSsks: boolean;

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder) { }

  ngOnInit() {

    this.http.get<Clinic[]>('/api/clinic')
      .subscribe((clinics: Clinic[]) => {
        this.clinics = clinics;

        this.clinicDropdownItems = clinics.map((clinic) => {
          return {
            displayName: clinic.name,
            value: clinic.id
          }
        });

        this.initUnitForm(this.unit);
      });
  }

  private initUnitForm(unit: Unit) {
    if (!unit) {
      unit = new Unit();
      unit.clinic = new Clinic();
    }

    this.unitForm = this.formBuilder.group({
      id: [unit.id],
      name: [unit.name],
      clinic: [unit.clinic ? unit.clinic.id : null],
      ssks: this.formBuilder.array(this.toFormGroups(unit.ssks)),
      hasLeftDateFeature: [unit.hasLeftDateFeature]
    });

  }

  private reinitUnitForm(unit: Unit) {
    if (!unit) {
      unit = new Unit();
      unit.clinic = new Clinic();
    }

    this.unitForm.setValue({
      id: unit.id ? unit.id : null,
      name: unit.name ? unit.name : null,
      clinic: unit.clinic ? (unit.clinic.id ? unit.clinic.id : null) : null,
      ssks: this.formBuilder.array(this.toFormGroups(unit.ssks)),
      hasLeftDateFeature: unit.hasLeftDateFeature
    });

  }

  setCurrentUnit($event: any, unit: Unit) {
    if (event) {
      this.reinitUnitForm(unit);
    }
  }

  addSsk() {
    (this.unitForm.get('ssks') as FormArray).push(this.createSsk());
  }

  createSsk(): FormGroup {
    return this.formBuilder.group({
      id: null,
      label: null,
      color: null
    });
  }

  deleteSsk(index: number) {
    (this.unitForm.get('ssks') as FormArray).removeAt(index);
  }

  saveUnit() {
    let unit = new Unit();
    let unitModel = this.unitForm.value;

    unit.id = unitModel.id;
    unit.name = unitModel.name;
    unit.hasLeftDateFeature = unitModel.hasLeftDateFeature;

    if (unitModel.clinic) {
      unit.clinic = new Clinic();
      unit.clinic.id = unitModel.clinic;
    }

    unit.ssks = unitModel.ssks;

    this.http.put('/api/unit?keepBeds=true', unit)
      .subscribe(() => {
        this.saveEvent.emit();
      });
  }

  openDeleteModal(unit: Unit) {
    this.openDeleteEvent.emit(unit);
  }

  cancel() {
    this.cancelEvent.emit();
  }

  private toFormGroups(ssks: Ssk[]): FormGroup[] {

    if (!ssks || ssks.length === 0) {
      return [];
    }

    return ssks.map(ssk => {
      return this.formBuilder.group( {
        id: ssk.id,
        label: ssk.label,
        color: ssk.color
      })
    });
  }
}
