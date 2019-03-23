import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Unit} from "../../../../domain/unit";
import {Clinic} from "../../../../domain/clinic";
import {Ssk} from "../../../../domain/ssk";
import {ServingClinic} from "../../../../domain/servingclinic";
import {CleaningAlternative} from "../../../../domain/cleaning-alternative";
import {CareBurdenCategory} from "../../../../domain/careBurdenCategory";
import {CareBurdenValue} from "../../../../domain/careburdenvalue";
import {DietForMother} from "../../../../domain/dietformother";
import {DietForChild} from "../../../../domain/dietforchild";
import {DietForPatient} from "../../../../domain/dietforpatient";
import {UnitPlannedIn} from "../../../../domain/unit-planned-in";
import {DropdownItem} from "../../../../domain/DropdownItem";
import {Management} from "../../../../domain/management";
import {SelectableItem} from "vgr-komponentkartan";

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
  @Input('managementId') managementId: string;
  @Input('newUnit') newUnit:boolean;
  managementDropdownItems: { displayName: string; value: string }[] = [];

  @Output() openDeleteEvent: EventEmitter<any> = new EventEmitter();
  @Output() cancelEvent: EventEmitter<any> = new EventEmitter();
  @Output() saveEvent: EventEmitter<any> = new EventEmitter();

  managementClinics:Clinic[] = [];

  unitForm: FormGroup;

  clinicDropdownItems: { displayName: string; value: string }[] = [];
  colorDropdownItems: DropdownItem<string>[];


  editSsks: boolean;
  editKlinik: boolean;
  editCleanGroup: boolean;
  editCategoryGroup: boolean;
  editBurdenValueGroup: boolean;
  editDietForMotherGroup: boolean;
  editDietForChildGroup: boolean;
  editDietForPatientGroup: boolean;
  editUnitPlannedIn: boolean;
  managements: Management[] = [];
  careBurdenOptions: SelectableItem<string>[];
  careBurdenValueIsNumber: boolean = false;


  constructor(private http: HttpClient,
              private formBuilder: FormBuilder) {
    this.colorDropdownItems = [
      {displayName: 'Blå', value: 'BLUE'},
      {displayName: 'Grön', value: 'GREEN'},
      {displayName: 'Gul', value: 'YELLOW'},
      {displayName: 'Lila', value: 'PURPLE'},
      {displayName: 'Rosa', value: 'PINK'},
      {displayName: 'Röd', value: 'RED'},
      {displayName: 'Vit', value: 'WHITE'}
    ];
  }

  ngOnInit() {

    this.http.get<Management[]>('/api/management/').subscribe((managements: Management[]) => {
      this.managements = managements;
      this.managementDropdownItems = [{displayName: 'Välj', value: null}].concat(this.managements.map(management => {
        return {
          displayName: management.name,
          value: management.id
        }
      }));
      this.http.get<Clinic[]>('/api/clinic/management?management=' + this.managementId).subscribe(clinics => {
        this.managementClinics = clinics;
        this.clinicDropdownItems = this.managementClinics.map((clinic) => {
          return {
            displayName: clinic.name,
            value: clinic.id
          }
        });
        this.initUnitForm(this.unit);

        this.unitForm.get('careBurden').valueChanges
          .subscribe(value => this.changeCareBurden(value));
      });
    });
    this.careBurdenOptions = [
      {displayName: 'Text', value: "text"},
      {displayName: 'Tal', value: "tal"},
      {displayName: 'Inget', value: "inget"}
    ];

  }

  private initUnitForm(unit: Unit) {
    if (!unit) {
      unit = new Unit();
      unit.clinic = new Clinic();
    }
    this.unitForm = this.formBuilder.group({
      id: [unit.id, Validators.required],
      name: [unit.name, Validators.required],
      clinic: [unit.clinic ? unit.clinic.id : null, Validators.required],
      management: [unit.clinic.management ? unit.clinic.management.id : null, Validators.required],
      ssks: this.formBuilder.array(this.toFormGroups(unit.ssks)),
      dietForMothers: this.formBuilder.array(this.buildDietGroupForMother(unit.dietForMothers)),
      dietForChildren: this.formBuilder.array(this.buildDietGroupForChildren(unit.dietForChildren)),
      dietForPatients: this.formBuilder.array(this.buildDietGroupForPatient(unit.dietForPatients)),
      unitsPlannedIn: this.formBuilder.array(this.buildPlannedInUnits(unit.unitsPlannedIn)),
      servingClinics: this.formBuilder.array(this.buildKlinikGroup(unit.servingClinics)),
      careBurdenCategories: this.formBuilder.array(this.buildBurdenCategoryGroups(unit.careBurdenCategories)),
      careBurdenValues: this.formBuilder.array(this.buildBurdenValueGroups(unit.careBurdenValues)),
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
      hasMorKostFeature : [unit.hasMorKostFeature],
      hasBarnKostFeature : [unit.hasBarnKostFeature],
      hasKostFeature : [unit.hasKostFeature],
      hasMotherChildDietFeature: [unit.hasMotherChildDietFeature],
      hasUnitPlannedInFeature: [unit.hasUnitPlannedInFeature],
      careBurden: [unit.careBurden? unit.careBurden: "inget"]
    });
    this.unitForm.get('management').valueChanges.subscribe((mgId: string)=>{
      if (mgId != null)
      {
        this.unitForm.get('clinic').setValue(null);
        this.http.get<Clinic[]>('/api/clinic/management?management=' + mgId).subscribe(clinics => {
          this.clinicDropdownItems = clinics.map((clinic) => {
            return {
              displayName: clinic.name,
              value: clinic.id
            }
          });
        });
      }
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
      dietForMothers: this.formBuilder.array(this.buildDietGroupForMother(unit.dietForMothers)),
      dietForChildren: this.formBuilder.array(this.buildDietGroupForChildren(unit.dietForChildren)),
      dietForPatients: this.formBuilder.array(this.buildDietGroupForPatient(unit.dietForPatients)),
      unitsPlannedIn: this.formBuilder.array(this.buildPlannedInUnits(unit.unitsPlannedIn)),
      careBurdenCategories: this.formBuilder.array(this.buildBurdenCategoryGroups(unit.careBurdenCategories)),
      careBurdenValues: this.formBuilder.array(this.buildBurdenValueGroups(unit.careBurdenValues)),
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
      hasMorKostFeature : unit.hasMorKostFeature,
      hasBarnKostFeature : unit.hasBarnKostFeature,
      hasKostFeature : unit.hasKostFeature,
      hasMotherChildDietFeature: unit.hasMotherChildDietFeature,
      hasUnitPlannedInFeature: unit.hasUnitPlannedInFeature,
      careBurden: unit.careBurden

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
    unit.hasMorKostFeature = unitModel.hasMorKostFeature;
    unit.hasBarnKostFeature = unitModel.hasBarnKostFeature;
    unit.hasKostFeature = unitModel.hasKostFeature;
    unit.hasMotherChildDietFeature = unitModel.hasMotherChildDietFeature;
    unit.hasUnitPlannedInFeature = unitModel.hasUnitPlannedInFeature;
    if (unitModel.clinic) {
      unit.clinic = new Clinic();
      unit.clinic.id = unitModel.clinic;
      unit.clinic.management = new Management();
      unit.clinic.management.id = unitModel.management;
    }
    unit.ssks = unitModel.ssks;
    unit.servingClinics = unitModel.servingClinics;
    unit.dietForPatients = unitModel.dietForPatients;
    unit.dietForChildren = unitModel.dietForChildren;
    unit.dietForMothers = unitModel.dietForMothers;
    unit.careBurdenCategories = unitModel.careBurdenCategories;
    unit.careBurdenValues = unitModel.careBurdenValues;
    unit.cleaningAlternatives = unitModel.cleaningAlternatives;
    unit.unitsPlannedIn = unitModel.unitsPlannedIn;
    unit.careBurden = unitModel.careBurden;

    this.http.put('/api/unit?keepBeds=true', unit)
      .subscribe(() => {
        this.saveEvent.emit();
        this.unitForm.reset();
      });
  }

  openDeleteModal(unit: Unit) {
    this.openDeleteEvent.emit(unit);
  }

  changeCareBurden(value : string): void{
    if(value == "text")
    {
      this.unit.careBurden = "text";
      this.careBurdenValues.controls.map(i => i.get('name').clearValidators());
      this.careBurdenValues.controls.map(i => i.get('name').setValidators(Validators.required));
      this.careBurdenValues.controls.map(i => i.get('name').updateValueAndValidity());
    }
    else if (value == "tal")
    {
      this.unit.careBurden == "tal";
      this.careBurdenValueIsNumber = true;
      this.careBurdenValues.controls.map(i => i.get('name').setValidators(Validators.pattern(/^[0-9]+$/)));
      this.careBurdenValues.controls.map(i => i.get('name').updateValueAndValidity());
    }
    else
    {
      this.unit.careBurden = "inget";
      this.careBurdenValues.controls.map(i => i.get('name').clearValidators());
      this.careBurdenValues.controls.map(i => i.get('name').setValidators(Validators.required));
      this.careBurdenValues.controls.map(i => i.get('name').updateValueAndValidity());
    }
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

  addSsk() {
    (this.unitForm.get('ssks') as FormArray).push(this.createSsk());
  }
  createSsk(): FormGroup {
    return this.formBuilder.group({
      id: null,
      label: [null, Validators.required],
      color: [null,Validators.required]
    });
  }

  deleteSsk(index: number) {
    (this.unitForm.get('ssks') as FormArray).removeAt(index);
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
      name: [null, Validators.required]
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
      id: null,
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



  get careBurdenCategories(): FormArray{
    return <FormArray>this.unitForm.get('careBurdenCategories');
  }

  addCareBurdenCategories(){
    this.careBurdenCategories.push(this.CreateCareBurdenCategories());
  }

  deleteCareBurdenCategories(index: number) {
    this.careBurdenCategories.removeAt(index);
  }

  CreateCareBurdenCategories(): FormGroup{
    return this.formBuilder.group({
      id: null,
      name: [null, Validators.required]
    });
  }

  private buildBurdenCategoryGroups(careBurdenCategories: CareBurdenCategory[]): FormGroup[]
  {
    if (!careBurdenCategories || careBurdenCategories.length === 0) {
      return [];
    }
    return careBurdenCategories.map(category => {
      return this.formBuilder.group( {
        id: category.id,
        name: category.name
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
      id: [null],
      name: [null],
      color: [null]
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
        name: careburdenvalue.name,
        color: careburdenvalue.color
      })
    });
  }


  get dietForMothers(): FormArray{
    return <FormArray>this.unitForm.get('dietForMothers');
  }
  private buildDietGroupForMother(diets: DietForMother[]): FormGroup[] {
    if (!diets || diets.length === 0) {
      return [];
    }

    return diets.map(diet => {
      return this.formBuilder.group( {
        id: diet.id,
        name: diet.name
      })
    });
  }

  addDietForMother(){
    this.dietForMothers.push(this.CreateDietForMother());
  }

  deleteDietForMother(index: number) {
    this.dietForMothers.removeAt(index);
  }

  CreateDietForMother(): FormGroup{
    return this.formBuilder.group({
      id: null,
      name: [null, Validators.required]
    });
  }


  get dietForChildren(): FormArray{
    return <FormArray>this.unitForm.get('dietForChildren');
  }
  private buildDietGroupForChildren(diets: DietForChild[]): FormGroup[] {
    if (!diets || diets.length === 0) {
      return [];
    }

    return diets.map(diet => {
      return this.formBuilder.group( {
        id: diet.id,
        name: diet.name
      })
    });
  }

  addDietForChild(){
    this.dietForChildren.push(this.CreateDietForChild());
  }

  deleteDietForChild(index: number) {
    this.dietForChildren.removeAt(index);
  }

  CreateDietForChild(): FormGroup{
    return this.formBuilder.group({
      id: null,
      name: [null, Validators.required]
    });
  }

  get dietForPatients(): FormArray{
    return <FormArray>this.unitForm.get('dietForPatients');
  }
  private buildDietGroupForPatient(diets: DietForPatient[]): FormGroup[] {
    if (!diets || diets.length === 0) {
      return [];
    }

    return diets.map(diet => {
      return this.formBuilder.group( {
        id: diet.id,
        name: diet.name
      })
    });
  }

  addDietForPatient(){
    this.dietForPatients.push(this.CreateDietForPatient());
  }

  deleteDietForPatient(index: number) {
    this.dietForPatients.removeAt(index);
  }

  CreateDietForPatient(): FormGroup{
    return this.formBuilder.group({
      id: null,
      name: [null, Validators.required]
    });
  }

  private buildPlannedInUnits (plannedUnits: UnitPlannedIn[]): FormGroup[] {
    if (!plannedUnits || plannedUnits.length === 0) {
      return [];
    }
    return plannedUnits.map(plannedUnit => {
      return this.formBuilder.group( {
        id: plannedUnit.id,
        name: plannedUnit.name
      })
    });
  }

  CreatePlannedUnit(): FormGroup{
    return this.formBuilder.group({
      id: null,
      name: [null, Validators.required]
    });
  }

  get unitsPlannedIn(): FormArray{
    return <FormArray>this.unitForm.get('unitsPlannedIn');
  }

  addPlannedUnit()
  {
    this.unitsPlannedIn.push(this.CreatePlannedUnit());
  }

  deletePlannedUnit(index: number)
  {
    this.unitsPlannedIn.removeAt(index);
  }

  getCareTeamValidation(index: number): boolean
  {
    return (this.unitForm.get('ssks') as FormArray).at(index).get('label').touched;
  }

  getSskValidation(index: number): boolean
  {
    return (this.unitForm.get('ssks') as FormArray).at(index).get('color').touched;
  }

  getServingClinicValidation(index: number): boolean
  {
    return (this.unitForm.get('servingClinics') as FormArray).at(index).get('name').touched;
  }

  getCleaniningValidation(index: number)
  {
    return (this.unitForm.get('cleaningAlternatives') as FormArray).at(index).get('description').touched;
  }

  getCareBurdenCategoriValidation(index: number)
  {
    return (this.unitForm.get('careBurdenCategories') as FormArray).at(index).get('name').touched;
  }

  getCareBurdenValuesValidation(index: number)
  {
    return (this.unitForm.get('careBurdenValues') as FormArray).at(index).get('name').touched;
  }

  getcareBurdenValidation(index: number)
  {
    return (this.unitForm.get('careBurdenValues') as FormArray).at(index).get('name').hasError('pattern');
  }
  getMotherDietValidation(index: number)
  {return (this.unitForm.get('dietForMothers') as FormArray).at(index).get('name').touched;
  }

  getChildDietValidation(index: number)
  {return (this.unitForm.get('dietForChildren') as FormArray).at(index).get('name').touched;
  }

  getPatientDietValidation(index: number)
  {return (this.unitForm.get('dietForPatients') as FormArray).at(index).get('name').touched;
  }

  getPlannedUnitValidation(index: number)
  {
    return (this.unitForm.get('unitsPlannedIn') as FormArray).at(index).get('name').touched;
  }

  checkCareBurdenFor(index: number)
  {
    if (this.careBurdenValueIsNumber || this.unit.careBurden == 'tal')
    {
      (this.unitForm.get('careBurdenValues') as FormArray).at(index).get('name').setValidators(Validators.pattern(/^[0-9]+$/));
      (this.unitForm.get('careBurdenValues') as FormArray).at(index).get('name').updateValueAndValidity();
    }
  }
}
