import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AbstractControl, FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Unit} from "../../domain/unit";
import {HttpClient} from "@angular/common/http";
import {SevenDaysPlaningUnit} from "../../domain/seven-days-planing-unit";
import {Clinic} from "../../domain/clinic";
import {DropdownItem} from "../../domain/DropdownItem";

@Component({
  selector: 'app-unit-planned-in-items',
  templateUrl: './unit-planned-in-items.component.html',
  styleUrls: ['./unit-planned-in-items.component.scss']
})
export class UnitPlannedInItemsComponent implements OnInit {

  sevendaysplans: SevenDaysPlaningUnit[] = [];
  plannedInDropdownUnits: DropdownItem<number>[];
  addSevenDaysPlaningUnitForm: FormGroup;
  @Input('showPlus') showPlus: boolean
  @Input('oldPost') oldPost: boolean;
  @Input('clinic') clinic: Clinic;
  @Input('unit') unit: Unit;
  @Output('savePlaningUnitEvent') savePlaningUnitEvent = new EventEmitter();
  @Output('cancelPlaningUnitEvent') cancelPlaningUnitEvent = new EventEmitter();

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder) { }

  ngOnInit() {
    const formGroup = this.calculateFormGroup();
    this.addSevenDaysPlaningUnitForm = formGroup;
  }

  public update(unit: Unit) {
    this.unit = unit;
    const formGroup = this.calculateFormGroup();
    this.addSevenDaysPlaningUnitForm.patchValue(formGroup.value);
  }

  private calculateFormGroup() {
    this.plannedInDropdownUnits = this.unit.unitsPlannedIn.map(unitplannedIn => {
      return {displayName: unitplannedIn.name, value: unitplannedIn.id};
    });
    this.plannedInDropdownUnits = [{displayName: 'Välj', value: null}].concat(this.plannedInDropdownUnits);
    this.unit.sevenDaysPlaningUnits = this.unit.sevenDaysPlaningUnits.sort((a: SevenDaysPlaningUnit, b: SevenDaysPlaningUnit) =>
      (a.date > b.date ? -1 : 1));

    let today = new Date();
    today.setHours(0, 0, 0, 0);
    let idag = today.getTime();
    if (this.oldPost) {
      this.sevendaysplans = this.unit.sevenDaysPlaningUnits.filter(x => (new Date(x.date)).getTime() < idag);
    } else {
      this.sevendaysplans = this.unit.sevenDaysPlaningUnits.filter(x => (new Date(x.date)).getTime() >= idag);
    }
    // this.oldPost = false;
    const controlsConfig = this.buildSevenDaysPlaningGroup(this.sevendaysplans);

    let formGroup = this.formBuilder.group({
      sevenDaysPlaningUnits: this.formBuilder.array(controlsConfig, [this.SevenDaysArrayCompare.bind(this)])
    });
    return formGroup;
  }

  private buildSevenDaysPlaningGroup(sevenDaysPlaningUnits:SevenDaysPlaningUnit[]): FormGroup[] {
    if (!sevenDaysPlaningUnits|| sevenDaysPlaningUnits.length === 0) {
      return [];
    }
    return sevenDaysPlaningUnits.map(sevenDaysPlaningUnit => {
      return this.formBuilder.group({
        id: sevenDaysPlaningUnit.id,
        date: sevenDaysPlaningUnit.date,
        fromUnit: sevenDaysPlaningUnit.fromUnit.id,
        quantity: sevenDaysPlaningUnit.quantity,
        comment:  sevenDaysPlaningUnit.comment
      })
    });
  }

  SevenDaysArrayCompare(c: AbstractControl):{[key: string]: any} | null{
    for (let element of c.value )
    {
      if(element.date !== null)
      {
        element.date = new Date(element.date).getTime();
      }
    }
    let array1 = c.value;
    //array1 = array1.filter(x => x.id !== null);
    let duplicatesArray = [];
    array1.forEach((e1, index) => array1.forEach(e2 => {if (e1.date === e2.date && e1.fromUnit === e2.fromUnit && e1.id !== e2.id) {duplicatesArray[index] = true}}));
    if (duplicatesArray.length > 0) {
      return {'duplicatesExist': duplicatesArray }
    }
    else
    {
      return null;
    }
  }

  get sevenDaysPlaningUnits(): FormArray {
    return <FormArray> (this.addSevenDaysPlaningUnitForm ? this.addSevenDaysPlaningUnitForm.get('sevenDaysPlaningUnits') : []);
  }

  deleteSevenDaysPlaningUnit(id: number, index: number) {
    this.sevenDaysPlaningUnits.removeAt(index);
    if (id !== null){
      this.http.delete('/api/unit/' + this.clinic.id + '/' + this.unit.id + '/' + id)
        .subscribe(() => {
        });
    }
  }

  addPlannedInUnit() {
    this.sevenDaysPlaningUnits.push(this.CreateSevenDaysPlaningUnit());
  }

  CreateSevenDaysPlaningUnit(): FormGroup {
    return this.formBuilder.group({
      id: null,
      date: [null, Validators.required],
      fromUnit: [null, Validators.required],
      quantity: [null, [Validators.required, Validators.pattern("^[0-9]*$")]],
      comment: null
    });
  }

  isDuplicate(duplicates: string[], id: string) {
    return !!(duplicates && duplicates.indexOf(id) > -1);
  }

  getDropdownValidation(index : number)
  {
    return (this.addSevenDaysPlaningUnitForm.get('sevenDaysPlaningUnits') as FormArray).at(index).get('fromUnit').touched
      && !(this.addSevenDaysPlaningUnitForm.get('sevenDaysPlaningUnits') as FormArray).at(index).get('fromUnit').valid ;
  }

  getQuantityValidation(index: number)
  {
    return (this.addSevenDaysPlaningUnitForm.get('sevenDaysPlaningUnits') as FormArray).at(index).get('quantity').touched
      && !(this.addSevenDaysPlaningUnitForm.get('sevenDaysPlaningUnits') as FormArray).at(index).get('quantity').valid ;
  }

  getDatepickerValidation(index: number)
  {
    return (this.addSevenDaysPlaningUnitForm.get('sevenDaysPlaningUnits') as FormArray).at(index).get('date').touched;
  }
  saveFromEnhet() {
    let sevenDaysPlaningUnits = <SevenDaysPlaningUnit[]>[];
    let sevenDaysPlaningUnitsModel = this.addSevenDaysPlaningUnitForm.value;
    sevenDaysPlaningUnits = sevenDaysPlaningUnitsModel.sevenDaysPlaningUnits.map(term => {
      return {
        id: term.id ? term.id : null,
        date: term.date,
        fromUnit: this.unit.unitsPlannedIn.find(plannedIn => plannedIn.id === term.fromUnit),
        quantity: term.quantity,
        comment: term.comment
      }
    });
    this.http.put('/api/unit/' + this.clinic.id + '/' + this.unit.id + '/' + 'sevenDaysPlaningUnit', sevenDaysPlaningUnits)
      .subscribe(unit => {
        this.savePlaningUnitEvent.emit();
      });
  }

  cancle(){
    this.cancelPlaningUnitEvent.emit();
  }

}
