import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FormArray, FormBuilder, FormGroup} from "@angular/forms";
import {Unit} from "../../../../domain/unit";
import {Clinic} from "../../../../domain/clinic";
import {Ssk} from "../../../../domain/ssk";
import {ServingClinic} from "../../../../domain/servingclinic";

@Component({
  selector: 'app-units-admin-form',
  templateUrl: './units-admin-form.component.html',
  styleUrls: ['./units-admin-form.component.scss']
})
export class UnitsAdminFormComponent implements OnInit {

  @Input('showId') showId: boolean;
  @Input('showDelete') showDelete: boolean;
  @Input('unit') unit: Unit;
  @Input('clinics') clinics: Clinic[] = [];

  @Output() openDeleteEvent: EventEmitter<any> = new EventEmitter();
  @Output() cancelEvent: EventEmitter<any> = new EventEmitter();
  @Output() saveEvent: EventEmitter<any> = new EventEmitter();

  unitForm: FormGroup;

  clinicDropdownItems: { displayName: string; value: string }[] = [];

  editSsks: boolean;
  editKlinik: boolean;

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder) { }

  ngOnInit() {

    this.clinicDropdownItems = this.clinics.map((clinic) => {
      return {
        displayName: clinic.name,
        value: clinic.id
      }
    });

    this.initUnitForm(this.unit);
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
      hasLeftDateFeature: [unit.hasLeftDateFeature],
      hasCarePlan: unit.hasCarePlan,
      servingClinics: this.formBuilder.array(this.buildKlinikGroup(unit.servingClinics)),
      hasAkutPatientFeature: [unit.hasAkutPatientFeature],
      has23oFeature: [unit.has23oFeature],
      has24oFeature: [unit.has24oFeature],
      hasVuxenPatientFeature: [unit.hasVuxenPatientFeature],
      hasSekretessFeature: [unit.hasSekretessFeature],
      hasInfekteradFeature: [unit.hasInfekteradFeature],
      hasInfectionSensitiveFeature: [unit.hasInfectionSensitiveFeature],
      hasSmittaFeature: [unit.hasSmittaFeature]
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
      hasLeftDateFeature: unit.hasLeftDateFeature,
      hasCarePlan: unit.hasCarePlan,
      servingClinics: this.formBuilder.array(this.buildKlinikGroup(unit.servingClinics)),
      hasAkutPatientFeature: unit.hasAkutPatientFeature,
      has23oFeature: unit.has23oFeature,
      has24oFeature: unit.has24oFeature,
      hasVuxenPatientFeature: unit.hasVuxenPatientFeature,
      hasSekretessFeature: unit.hasSekretessFeature,
      hasInfekteradFeature: unit.hasInfekteradFeature,
      hasInfectionSensitiveFeature: unit.hasInfectionSensitiveFeature,
      hasSmittaFeature: unit.hasSmittaFeature
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
    unit.hasCarePlan = unitModel.hasCarePlan;
    unit.hasAkutPatientFeature = unitModel.hasAkutPatientFeature;
    unit.has23oFeature = unitModel.has23oFeature;
    unit.has24oFeature = unitModel.has24oFeature;
    unit.hasVuxenPatientFeature = unitModel.hasVuxenPatientFeature;
    unit.hasSekretessFeature = unitModel.hasSekretessFeature;
    unit.hasInfekteradFeature = unitModel.hasInfekteradFeature;
    unit.hasInfectionSensitiveFeature = unitModel.hasInfectionSensitiveFeature;
    unit.hasSmittaFeature = unitModel.hasSmittaFeature;

    if (unitModel.clinic) {
      unit.clinic = new Clinic();
      unit.clinic.id = unitModel.clinic;
    }

    unit.ssks = unitModel.ssks;
    unit.servingClinics = unitModel.servingClinics;

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

  addServingClinic(){
    this.servingClinics.push(this.CreateServingClinics());
    //(this.unitForm.get('servingClinics') as FormArray).push(this.CreateServingClinics());
  }

  CreateServingClinics(): FormGroup{
    return this.formBuilder.group({
      id: null,
      name: null
    });
  }

  private buildKlinikGroup(servingKliniks: ServingClinic[]): FormGroup[]
  {
    if (!servingKliniks || servingKliniks.length === 0) {
      return [];
    }
    return servingKliniks.map(servingklinik => {
      return this.formBuilder.group( {
        id: servingklinik.id,
        name: servingklinik.name
      })
    });
  }

  deleteServingClinic(index: number) {
    this.servingClinics.removeAt(index);
  }

  get servingClinics(): FormArray{
    return <FormArray>this.unitForm.get('servingClinics')
  }
}
