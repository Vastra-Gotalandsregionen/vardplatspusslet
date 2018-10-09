import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Unit} from "../../../../domain/unit";
import {Clinic} from "../../../../domain/clinic";

@Component({
  selector: 'app-units-admin-form',
  templateUrl: './units-admin-form.component.html',
  styleUrls: ['./units-admin-form.component.scss']
})
export class UnitsAdminFormComponent implements OnInit {

  @Input('showId') showId: boolean;
  @Input('showDelete') showDelete: boolean;
  @Input('unit') unit: boolean;

  @Output() openDeleteEvent: EventEmitter<any> = new EventEmitter();
  @Output() cancelEvent: EventEmitter<any> = new EventEmitter();
  @Output() saveEvent: EventEmitter<any> = new EventEmitter();

  clinics: Clinic[] = [];

  unitForm: FormGroup;

  clinicDropdownItems: { displayName: string; value: string }[] = [];

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
      });

    this.initUnitForm(null);
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
      hasLeftDateFeature: unit.hasLeftDateFeature
    });

  }

  setCurrentUnit($event: any, unit: Unit) {
    if (event) {
      this.reinitUnitForm(unit);
    }
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

    this.http.put('/api/unit?keepBeds=true', unit)
      .subscribe(() => {
        this.saveEvent.emit();
      });
  }

  openDeleteModal(unit: Unit) {
    this.openDeleteEvent.emit(unit);
  }

  // confirmDelete() {
  //   this.deleteEvent.emit();
  // }

  cancel() {
    this.cancelEvent.emit();
  }

}
