import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Unit} from "../../../../domain/unit";
import {Clinic} from "../../../../domain/clinic";
import {Ssk} from "../../../../domain/ssk";
import {ServingClinic} from "../../../../domain/servingclinic";
import {CleaningAlternative} from "../../../../domain/cleaning-alternative";
import {CareBurdenKategori} from "../../../../domain/careburdenkategori";
import {CareBurdenValue} from "../../../../domain/careburdenvalue";

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
  editCleanGroup: boolean;
  editKategoriGroup: boolean;
  editBurdenValueGroup: boolean;

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
      servingClinics: this.formBuilder.array(this.buildKlinikGroup(unit.servingClinics)),
      careBurdenKategories: this.formBuilder.array(this.buildBurdenKategoriGroups(unit.burdenKategories)),
      careBurdenValues: this.formBuilder.array(this.buildBurdenValueGroups(unit.burdenValues)),
      hasLeftDateFeature: [unit.hasLeftDateFeature],
      hasCarePlan: unit.hasCarePlan,
      cleaningAlternatives: this.formBuilder.array(this.buildCleaningGroups(unit.cleaningAlternatives)),
      hasAkutPatientFeature: [unit.hasAkutPatientFeature],
      has23oFeature: [unit.has23oFeature],
      has24oFeature: [unit.has24oFeature],
      hasVuxenPatientFeature: [unit.hasVuxenPatientFeature],
      hasSekretessFeature: [unit.hasSekretessFeature],
      hasInfekteradFeature: [unit.hasInfekteradFeature],
      hasInfectionSensitiveFeature: [unit.hasInfectionSensitiveFeature],
      hasSmittaFeature: [unit.hasSmittaFeature],
      hasCleaningFeature: [unit.hasCleaningFeature],
      hasPalFeature: [unit.hasPalFeature],
      hasHendelseFeature: [unit.hasHendelseFeature],
      hasMorRondFeature: [unit.hasMorRondFeature],
      hasBarnRondFeature: [unit.hasBarnRondFeature],
      hasRondFeature: [unit.hasRondFeature],
      hasAmningFeature: [unit.hasAmningFeature],
      hasInfoFeature: [unit.hasInfoFeature],
      hasCareBurdenFeature: [unit.hasCareBurdenFeature]
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
      careBurdenKategories: this.formBuilder.array(this.buildBurdenKategoriGroups(unit.burdenKategories)),
      careBurdenValues: this.formBuilder.array(this.buildBurdenValueGroups(unit.burdenValues)),
      hasLeftDateFeature: unit.hasLeftDateFeature,
      hasCarePlan: unit.hasCarePlan,
      servingClinics: this.formBuilder.array(this.buildKlinikGroup(unit.servingClinics)),
      cleaningAlternatives: this.formBuilder.array(this.buildCleaningGroups(unit.cleaningAlternatives)),
      hasAkutPatientFeature: unit.hasAkutPatientFeature,
      has23oFeature: unit.has23oFeature,
      has24oFeature: unit.has24oFeature,
      hasVuxenPatientFeature: unit.hasVuxenPatientFeature,
      hasSekretessFeature: unit.hasSekretessFeature,
      hasInfekteradFeature: unit.hasInfekteradFeature,
      hasInfectionSensitiveFeature: unit.hasInfectionSensitiveFeature,
      hasSmittaFeature: unit.hasSmittaFeature,
      hasCleaningFeature: unit.hasCleaningFeature,
      hasPalFeature: unit.hasPalFeature,
      hasHendelseFeature: unit.hasHendelseFeature,
      hasMorRondFeature: unit.hasMorRondFeature,
      hasBarnRondFeature: unit.hasBarnRondFeature,
      hasRondFeature: unit.hasRondFeature,
      hasAmningFeature: unit.hasAmningFeature,
      hasInfoFeature: unit.hasInfoFeature,
      hasCareBurdenFeature: unit.hasCareBurdenFeature

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
    unit.hasCleaningFeature = unitModel.hasCleaningFeature;
    unit.hasPalFeature = unitModel.hasPalFeature;
    unit.hasHendelseFeature = unitModel.hasHendelseFeature;
    unit.hasMorRondFeature = unitModel.hasMorRondFeature;
    unit.hasBarnRondFeature = unitModel.hasBarnRondFeature;
    unit.hasRondFeature = unitModel.hasRondFeature;
    unit.hasAmningFeature = unitModel.hasAmningFeature;
    unit.hasInfoFeature = unitModel.hasInfoFeature;
    unit.hasCareBurdenFeature = unitModel.hasCareBurdenFeature;


    if (unitModel.clinic) {
      unit.clinic = new Clinic();
      unit.clinic.id = unitModel.clinic;
    }
    unit.ssks = unitModel.ssks;
    unit.servingClinics = unitModel.servingClinics;
    unit.burdenKategories = unitModel.careBurdenKategories;
    unit.burdenValues = unitModel.careBurdenValues;
    unit.cleaningAlternatives = unitModel.cleaningAlternatives;

    this.http.put('/api/unit?keepBeds=true', unit)
      .subscribe(() => {
        this.saveEvent.emit();
        this.unitForm.reset();
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

  get servingClinics(): FormArray{
    return <FormArray>this.unitForm.get('servingClinics')
  }

  addServingClinic(){
    this.servingClinics.push(this.CreateServingClinics());
  }

  deleteServingClinic(index: number) {
    this.servingClinics.removeAt(index);
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


  get cleaningAlternatives(): FormArray{
    return <FormArray>this.unitForm.get('cleaningAlternatives');
  }

  addCleaningAlternative(){
    this.cleaningAlternatives.push(this.CreateCleaningAlternative());
  }

  deleteCleaningGroup(index: number) {
    this.cleaningAlternatives.removeAt(index);
  }

  CreateCleaningAlternative(): FormGroup{
    return this.formBuilder.group({
      id: [null,Validators.required],
      description: [null, Validators.required]
    });
  }

  private buildCleaningGroups(cleanningalternatives: CleaningAlternative[]): FormGroup[]
  {
    if (!cleanningalternatives || cleanningalternatives.length === 0) {
      return [];
    }
    return cleanningalternatives.map(cleaningalternative => {
      return this.formBuilder.group( {
        id: cleaningalternative.id,
        description: cleaningalternative.description
      })
    });
  }



  get careBurdenKategories(): FormArray{
    return <FormArray>this.unitForm.get('careBurdenKategories');
  }

  addCareBurdenKategories(){
    this.careBurdenKategories.push(this.CreateCareBurdenKategories());
  }

  deleteCareBurdenKategories(index: number) {
    this.careBurdenKategories.removeAt(index);
  }

  CreateCareBurdenKategories(): FormGroup{
    return this.formBuilder.group({
      id: [null,Validators.required],
      kategori: [null, Validators.required]
    });
  }

  private buildBurdenKategoriGroups(careburdenkategories: CareBurdenKategori[]): FormGroup[]
  {
    if (!careburdenkategories || careburdenkategories.length === 0) {
      return [];
    }
    return careburdenkategories.map(careburdenkategori => {
      return this.formBuilder.group( {
        id: careburdenkategori.id,
        kategori: careburdenkategori.kategori
      })
    });
  }


  get careBurdenValues(): FormArray{
    return <FormArray>this.unitForm.get('careBurdenValues');
  }

  addCareBurdenValues(){
    this.careBurdenValues.push(this.CreateCareBurdenValues());
  }

  deleteCareBurdenValues(index: number) {
    this.careBurdenValues.removeAt(index);
  }

  CreateCareBurdenValues(): FormGroup{
    return this.formBuilder.group({
      id: [null,Validators.required],
      burdenValue: [null, Validators.required]
    });
  }

  private buildBurdenValueGroups(careburdenvalues: CareBurdenValue[]): FormGroup[]
  {
    if (!careburdenvalues || careburdenvalues.length === 0) {
      return [];
    }
    return careburdenvalues.map(careburdenvalue => {
      return this.formBuilder.group( {
        id: careburdenvalue.id,
        burdenValue: careburdenvalue.burdenValue
      })
    });
  }

}
