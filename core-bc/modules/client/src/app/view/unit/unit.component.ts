import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {Unit} from "../../domain/unit";
import {Bed} from "../../domain/bed";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Patient} from "../../domain/patient";
import {Clinic} from "../../domain/clinic";
import {Ssk} from "../../domain/ssk";
import {Message} from "../../domain/message";
import {interval, Subscription} from "rxjs";
import {AuthService} from "../../service/auth.service";
import {CareBurdenChoice} from "../../domain/careburdenchoice";
import {CareBurdenCategory} from "../../domain/careBurdenCategory";
import {CareBurdenValue} from "../../domain/careburdenvalue";
import {DropdownItem} from "../../domain/DropdownItem";
import "rxjs-compat/add/operator/do";
import {ListItemComponent} from "vgr-komponentkartan";
import {SevenDaysPlaningUnit} from "../../domain/seven-days-planing-unit";
import {UnitPlannedInTableComponent} from "../../elements/unit-planned-in-table/unit-planned-in-table.component";
import {UnitPlannedInItemsComponent} from "../../elements/unit-planned-in-items/unit-planned-in-items.component";

@Component({
  selector: 'app-unit',
  templateUrl: './unit.component.html',
  styleUrls: ['./unit.component.scss']
})
export class UnitComponent implements OnInit, OnDestroy {

  @ViewChild('thisUnitPlannedInTable') thisUnitPlannedInTable: UnitPlannedInTableComponent;
  @ViewChild('unitPlannedInItems') unitPlannedInItems: UnitPlannedInItemsComponent;
  @ViewChild('unitPlannedInItemsOld') unitPlannedInItemsOld: UnitPlannedInItemsComponent;

  addSevenDaysPlaningUnitForm: FormGroup;
  management: string;
  unit: Unit;
  units: Unit[];
  clinic: Clinic;
  showRow: boolean = true;
  burdenvals: string;
  sevendaysplan: SevenDaysPlaningUnit[] = [];
  plannedInDropdownUnits: DropdownItem<number>[];
  error: string;
  notFoundText = 'Oops. Inget fanns här...';
  vacantBeds: DropdownItem<number>[];
  chosenVacantBedId: number;
  showChangeBedOrder = false;
  bedsOrder: Bed[] = [];
  inited: boolean;

  messages: Message[] = [];
  private timerSubscription: Subscription;
  sskCategoryValueMatrix = {};


  constructor(protected http: HttpClient,
              protected formBuilder: FormBuilder,
              protected route: ActivatedRoute,
              protected authService: AuthService) {

    this.timerSubscription = interval(10000).subscribe(() => this.checkForChanges());
    this.management = this.route.snapshot.queryParams['managementName'];
  }


  private updateView(clinicId, unitId)
  {
    this.http.get<Clinic>('/api/clinic/' + clinicId).subscribe(clinic => {
      this.clinic = clinic;

      this.http.get<Unit[]>('/api/unit?clinic=' + clinicId).subscribe(unitArray => {
        this.units = unitArray.filter(x => x.name !== this.unit.name);
      });
    });
    this.http.get<Unit>('/api/unit/' + clinicId + '/' + unitId)
      .do(unit => {
        if (unit) {
          this.unit = unit;

          this.plannedInDropdownUnits = [{displayName: 'Välj', value: null}].concat(this.unit.unitsPlannedIn.map(unitplannedIn => {
            return {displayName: unitplannedIn.name, value: unitplannedIn.id};
          }));

          this.burdenvals = this.unit.careBurdenValues.map(x => x.name).join(' - ');
          this.updateSskCategoryValueMatrix(unit);
          this.updateVacants(unit);
          this.inited = true;

          this.http.get<Unit[]>('/api/unit?clinic=' + clinicId).subscribe(unitArray => {
            this.units = unitArray.filter(x => x.name !== this.unit.name);
          });

          if (this.thisUnitPlannedInTable) {
            this.thisUnitPlannedInTable.update(unit);
          }

          if (this.unitPlannedInItems) {
            this.unitPlannedInItems.update(unit);
          }

          if (this.unitPlannedInItemsOld) {
            this.unitPlannedInItemsOld.update(unit);
          }
        } else {
          this.error = this.notFoundText;
        }
      })
      .switchMap((unit: Unit) => this.http.get<Message[]>('/api/message/' + unit.id + '/today'))
      .subscribe((messages: Message[]) => {
        this.messages = messages;
      }, (error1: HttpErrorResponse) => {
        console.error(error1);
        if (error1.status === 404) {
          this.error = this.notFoundText;
        } else {
          this.error = error1.message;
        }
      });
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      let clinicId = params.clinicId;
      let unitId = params.id;
      this.updateView(clinicId, unitId);
    });
  }
  ngOnDestroy(): void {
    if (this.timerSubscription) {
      this.timerSubscription.unsubscribe();
    }
  }

  private updateVacants(unit) {
    this.vacantBeds = unit.beds
      .filter(bed => !bed.occupied)
      .map(bed => {
        return {
          displayName: bed.label,
          value: bed.id
        }
      });
  }

  private checkForChanges() {
    let params = this.route.snapshot.params;

    let clinicId = params.clinicId;

    this.http.get<Clinic>('/api/clinic/' + clinicId).subscribe(clinic => {
      this.clinic = clinic;
    });

    this.http.get<Unit>('/api/unit/' + clinicId + '/' + params.id).subscribe(unit => {
      for (let incomingBed of unit.beds) {
        let thisBed = null as Bed;

        let found = this.unit.beds.find(bed => bed.id === incomingBed.id);
        if (found) {
          thisBed = found;

          thisBed.occupied = incomingBed.occupied;
          thisBed.label = incomingBed.label;
          thisBed.patient = incomingBed.patient;
          thisBed.relatedInformation = incomingBed.relatedInformation;
        }
      }

      for (let incomingPatient of unit.patients) {
        let thisPatient = null as Patient;

        let found = this.unit.patients.find(patient => patient.id === incomingPatient.id);
        if (found) {
          thisPatient = found;

          thisPatient.leaveStatus = incomingPatient.leaveStatus;
          thisPatient.label = incomingPatient.label;
          thisPatient.gender = incomingPatient.gender;
        } else {
          // Not found means it is a new patient
          this.unit.patients.push(incomingPatient);
        }
      }

      // We also need to look for patients which should not belong to the unit anymore.
      for (let thisPatient of this.unit.patients) {
        let found = unit.patients.find(patient => patient.id === thisPatient.id);
        if (!found) {
          this.unit.patients.splice(this.unit.patients.indexOf(thisPatient), 1);
        }
      }

      this.updateSskCategoryValueMatrix(unit);
      this.updateVacants(unit);
    });
  }

  private updateSskCategoryValueMatrix(unit) {
    this.sskCategoryValueMatrix = {};
   // loopa över unit.ssks
   // this.sskCategoryValueMatrix()
    let unitSsks = unit.ssks;
    let sskPatientsChoices;
    let sskPatients;

    unitSsks.forEach(ssk => {
      sskPatients = this.unit.beds.filter(bed => bed.ssk && bed.ssk.id === ssk.id)
        .filter(z => z.occupied === true && z.patient).map(x => x.patient);

      if (sskPatients.length === 0) return;
      sskPatientsChoices = <CareBurdenChoice[]>sskPatients.map(x => x.careBurdenChoices).reduce(((previousValue, currentValue) => {
        if (!previousValue) {
          previousValue = [];
        }
        previousValue = previousValue.concat(currentValue);
        return previousValue;
      }));

      sskPatientsChoices
        .filter(choice => {
          return choice.careBurdenValue && choice.careBurdenCategory;
        })
        .forEach(choice => {
          if (this.sskCategoryValueMatrix[ssk.label]){
            if (this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name]) {
              if (this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name][choice.careBurdenValue.name]) {
                this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name][choice.careBurdenValue.name]++;
              } else {
                this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name][choice.careBurdenValue.name] = 1;
              }
            } else {
              this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name] = {};
              this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name][choice.careBurdenValue.name] = 1;
            }
          }
          else
          {
            this.sskCategoryValueMatrix[ssk.label] = {};
            this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name] = {};
            this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name][choice.careBurdenValue.name] = 1;
          }
        });
    })
  }

  changeBedOrder() {
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
  }

  chooseBedForLeavePatient(patient) {
    if (!this.chosenVacantBedId) {
      return;
    }

    patient.leaveStatus = null;

    let bed = this.unit.beds.find(bed => bed.id === this.chosenVacantBedId);
    bed.patient = patient;
    bed.occupied = true;
    this.http.put('/api/bed/' + this.clinic.id + '/' + this.unit.id, bed)
      .subscribe(bed => {
        this.ngOnInit();
      });
  }

  countBeds(sskArg: Ssk) {
    // Map to ssk, then filter out those with the same id and count the result.
    return this.unit.beds.map(bed => bed.ssk).filter(ssk => ssk ? ssk.id === sskArg.id : false).length;
  }

  // To initial capital letter and lower case after first letter. Also replace underscore with space.
  format(input: string) {
    if (!input) {
      return null;
    }

    if (input.length === 0) {
      return '';
    }

    return input.substr(0, 1).toUpperCase() + input.substr(1, input.length - 1).toLowerCase().replace('_', ' ');
  }

  hasEditUnitPermission() {
    if (this.unit) {
      return this.authService.hasEditUnitPermission(this.unit.id);
    } else {
      return false;
    }
  }

  patientCareBurden(patientChoices, burdenCategoriId) {
    if (patientChoices!= null && patientChoices.length > 0){
      let x =  patientChoices.find(x => x.careBurdenCategory.id === burdenCategoriId);
      return x && x.careBurdenValue ? x.careBurdenValue.name: null;

    }
    else{
      this.showRow = false;
      return null;
    }
  }

  CalculateAverage(burdenCategoriId) {
    let burdenval = 0;
    let antal = 0;
    for (let bed of this.unit.beds)
    {
      if (bed.patient)
      {
        let x = bed.patient.careBurdenChoices.find(x => x.careBurdenCategory.id === burdenCategoriId);
        if (x && x.careBurdenValue){
          antal++;
          burdenval +=  +x.careBurdenValue.name;
        }
      }
    }
    if (antal === 0) return 0;
    return (burdenval/antal).toFixed(2);
  }

  getMatrixValue(ssk: Ssk, cbk: CareBurdenCategory, cbv: CareBurdenValue): string {
    if (this.sskCategoryValueMatrix[ssk.label] && this.sskCategoryValueMatrix[ssk.label][cbk.name] && this.sskCategoryValueMatrix[ssk.label][cbk.name][cbv.name]) {
      return this.sskCategoryValueMatrix[ssk.label][cbk.name][cbv.name];
    } else {
      return '0';
    }
  }

  BedHasNoPatientWithCareBurden(patient: Patient) {
     if ( patient != null && patient.careBurdenChoices && patient.careBurdenChoices.map(x => x.careBurdenValue)
       .filter(z => z!= null && z.id).length > 0)
       return false;
     else return true;
  }

  collapse(element: ListItemComponent) {
    element.toggleExpand();
    this.ngOnInit();
  }

  onUnitPlannedInItemsSave() {
    this.updateView(this.clinic.id, this.unit.id);
    //this.ngOnInit();
     //this.thisUnitPlannedInTable.ngOnInit();
  }


}



