import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {Unit} from "../../domain/unit";
import {Clinic} from "../../domain/clinic";
import {DropdownItem} from "../../domain/DropdownItem";
import {ListItemComponent, SelectableItem} from "vgr-komponentkartan";
import {Bed} from "../../domain/bed";
import {DeleteModalComponent} from "../../elements/delete-modal/delete-modal.component";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {SevenDaysPlaningUnit} from "../../domain/seven-days-planing-unit";
import {CareBurdenChoice} from "../../domain/careburdenchoice";
import {CareBurdenCategory} from "../../domain/careBurdenCategory";

@Component({
  selector: 'app-bed-table',
  templateUrl: './bed-table.component.html',
  styleUrls: ['./bed-table.component.scss', '../../view/unit/unit.component.scss']
})
export class BedTableComponent implements OnInit {

  @ViewChild(DeleteModalComponent) appDeleteModal: DeleteModalComponent;

  @Input('unit') unit: Unit;
  @Input('clinic') clinic: Clinic;

  @Output('save') saveEvent = new EventEmitter();

  addBedForm: FormGroup;
  addSevenDaysPlaningUnitForm: FormGroup;

  bedForDeletion: Bed;

  careBurdenValuesOptions: DropdownItem<number>[];
  amningOptions: SelectableItem<number>[];
  genderDropdownItems: SelectableItem<string>[];
  informationOptions: SelectableItem<number>[];
  dietMotherDropdownItems: DropdownItem<number>[];
  dietChildDropdownItems: DropdownItem<number>[];
  dietDropdownItems: DropdownItem<number>[];
  leaveStatusesDropdownItems: DropdownItem<string>[];
  sskDropdownItems: DropdownItem<number>[];
  servingKlinikerDropdownItems: DropdownItem<number>[];
  cleaningAlternativesDropdownItems: DropdownItem<number>[];
  plannedInDropdownUnits: DropdownItem<number>[];

  expandedRows: Array<boolean>;
  delayedExpandedRows: Array<boolean>;

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder) { }

  ngOnInit() {

    this.initThings();

  }

  openDeleteModal(bed: Bed) {
    this.bedForDeletion = bed;
    this.appDeleteModal.open();
  }

  toggleRow(index) {
    let newState = !this.expandedRows[index];
    this.expandedRows = new Array<boolean>();
    this.delayedExpandedRows = new Array<boolean>();
    this.expandedRows[index] = newState;
    setTimeout(() => {
      this.delayedExpandedRows[index] = newState;
    });
  }

  collapse(index) {
    this.expandedRows[index] = false;
  }

  save() {
    this.saveEvent.emit();
  }

  saveAddBed() {
    let bed = new Bed();
    let bedModel = this.addBedForm.value;

    bed.label = bedModel.label;

    this.http.put('/api/bed/' + this.clinic.id + '/' + this.unit.id, bed)
      .subscribe(bed => {
        this.initAddBedForm();
        this.save();
      });
  }

  confirmDelete() {
    this.http.delete('/api/bed/' + this.clinic.id + '/' + this.unit.id + '/' + this.bedForDeletion.id)
      .subscribe(() => {
        this.expandedRows = new Array<boolean>();
        this.save();
      });
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

  get sevenDaysPlaningUnits(): FormArray {
    return <FormArray> (this.addSevenDaysPlaningUnitForm ? this.addSevenDaysPlaningUnitForm.get('sevenDaysPlaningUnits') : []);
  }

  deleteSevenDaysPlaningUnit(index: number) {
    this.sevenDaysPlaningUnits.removeAt(index);
  }

  addPlannedInUnit() {
    this.sevenDaysPlaningUnits.push(this.CreateSevenDaysPlaningUnit());
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
        this.save();
      });
  }

  getDatepickerValidation(index: number)
  {
    return (this.addSevenDaysPlaningUnitForm.get('sevenDaysPlaningUnits') as FormArray).at(index).get('date').touched;
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

  CreateSevenDaysPlaningUnit(): FormGroup {
    return this.formBuilder.group({
      id: null,
      date: [null, Validators.required],
      fromUnit: [null, Validators.required],
      quantity: [null, [Validators.required, Validators.pattern("^[0-9]*$")]],
      comment: null
    });
  }

  initThings() {
    this.expandedRows = new Array(this.unit.beds.length);
    this.delayedExpandedRows = new Array(this.unit.beds.length);

    this.addSevenDaysPlaningUnitForm = this.formBuilder.group({
      sevenDaysPlaningUnits: this.formBuilder.array(this.buildSevenDaysPlaningGroup(this.unit.sevenDaysPlaningUnits))
    });

    this.initAddBedForm();

    this.careBurdenValuesOptions = [{displayName: 'Välj', value: null}].concat(this.unit.careBurdenValues.map(cbv => {
      return {displayName: cbv.name, value: cbv.id};
    }));

    this.amningOptions = [
      {displayName: 'Normal amning', value: 1},
      {displayName: 'Amningshjälp', value: 2},
      {displayName: 'Amningsmottagning ', value: 3}
    ];

    this.genderDropdownItems = [
      {displayName: 'Välj', value: null},
      {displayName: 'Kvinna', value: 'KVINNA'},
      {displayName: 'Man', value: 'MAN'},
      {displayName: 'Barn', value: 'BARN'}
    ];

    this.informationOptions = [
      {displayName: 'THG', value: 1},
      {displayName: 'THG/Barn', value: 2},
      {displayName: 'Föräldrarum', value: 3}
    ];

    this.dietMotherDropdownItems = [{displayName: 'Välj', value: null}].concat(this.unit.dietForMothers.map(diet => {
      return {displayName: diet.name, value: diet.id};
    }));

    this.dietChildDropdownItems = [{displayName: 'Välj', value: null}].concat(this.unit.dietForChildren.map(diet => {
      return {displayName: diet.name, value: diet.id};
    }));

    this.dietDropdownItems = [{displayName: 'Välj', value: null}].concat(this.unit.dietForPatients.map(diet => {
      return {displayName: diet.name, value: diet.id};
    }));

    this.leaveStatusesDropdownItems = [
      {displayName: 'Välj', value: null},
      {displayName: 'Permission', value: 'PERMISSION'},
      {displayName: 'Teknisk plats', value: 'TEKNISK_PLATS'}
    ];

    this.sskDropdownItems = [{displayName: 'Välj', value: null}].concat(this.unit.ssks.map(ssk => {
      return {displayName: ssk.label, value: ssk.id};
    }));

    this.servingKlinikerDropdownItems = [{displayName: 'Välj', value: null}].concat(this.unit.servingClinics.map(klinik => {
      return {displayName: klinik.name, value: klinik.id};
    }));

    this.cleaningAlternativesDropdownItems = [{displayName: 'Välj', value: null}].concat(this.unit.cleaningAlternatives.map(cg => {
      return {displayName: cg.description, value: cg.id};
    }));

    this.plannedInDropdownUnits = [{displayName: 'Välj', value: null}].concat(this.unit.unitsPlannedIn.map(unitplannedIn => {
      return {displayName: unitplannedIn.name, value: unitplannedIn.id};
    }));
  }

  private initAddBedForm() {
    this.addBedForm = this.formBuilder.group({
      id: null,
      label: [null, Validators.required]
    });
  }

  getCareBurdenValueName(cbc: CareBurdenCategory, careBurdenChoices: CareBurdenChoice[]) {
    if (!careBurdenChoices) {
      return null;
    }

    let careBurdenChoice = careBurdenChoices.find(choice => choice.careBurdenCategory.id === cbc.id);

    if (!careBurdenChoice) {
      return null;
    }

    let careBurdenValue = careBurdenChoice.careBurdenValue;
    return careBurdenValue ? careBurdenValue.name : null;
  }
}
