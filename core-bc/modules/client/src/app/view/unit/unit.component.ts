import {Component, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';
import {Unit} from '../../domain/unit';
import {Bed} from '../../domain/bed';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Patient} from '../../domain/patient';
import {Clinic} from '../../domain/clinic';
import {Message} from '../../domain/message';
import {interval, Subscription} from 'rxjs';
import {AuthService} from '../../service/auth.service';
import {CareBurdenChoice} from '../../domain/careburdenchoice';
import {CareBurdenValue} from '../../domain/careburdenvalue';
import {DropdownItem} from '../../domain/DropdownItem';
import 'rxjs-compat/add/operator/do';
import {ListItemComponent} from 'vgr-komponentkartan';
import {SevenDaysPlaningUnit} from '../../domain/seven-days-planing-unit';
import {UnitPlannedInTableComponent} from '../../elements/unit-planned-in-table/unit-planned-in-table.component';
import {UnitPlannedInItemsComponent} from '../../elements/unit-planned-in-items/unit-planned-in-items.component';

@Component({
  selector: 'app-unit',
  templateUrl: './unit.component.html',
  styleUrls: ['./unit.component.scss']
})
export class UnitComponent implements OnInit, OnDestroy {

  @ViewChildren(UnitPlannedInTableComponent) unitsOnSameClinicTables: QueryList<UnitPlannedInTableComponent>;
  @ViewChild('thisUnitPlannedInTable') thisUnitPlannedInTable: UnitPlannedInTableComponent;
  @ViewChild('unitPlannedInItems') unitPlannedInItems: UnitPlannedInItemsComponent;
  @ViewChild('unitPlannedInItemsOld') unitPlannedInItemsOld: UnitPlannedInItemsComponent;

  addSevenDaysPlaningUnitForm: FormGroup;
  management: string;
  unit: Unit;
  allUnitsOnSameClinic: Unit[];
  clinic: Clinic;
  sevendaysplan: SevenDaysPlaningUnit[] = [];
  plannedInDropdownUnits: DropdownItem<number>[];
  error: string;
  notFoundText = 'Oops. Inget fanns här...';
  vacantBeds: DropdownItem<number>[];
  chosenVacantBedId: number;
  showChangeBedOrder = false;
  bedsOrder: Bed[] = [];
  inited: boolean;
  cbvs: CareBurdenValue[] = [];
  messages: Message[] = [];
  private timerSubscription: Subscription;
  sskCategoryValueMatrix = {};

  constructor(protected http: HttpClient,
              protected formBuilder: FormBuilder,
              protected route: ActivatedRoute,
              protected authService: AuthService) {

    this.timerSubscription = interval(20000).subscribe(() => this.checkForChanges());
  }


  protected updateView(clinicId, unitId)
  {
    this.http.get<Clinic>('/api/clinic/' + clinicId).subscribe(clinic => {
      this.clinic = clinic;
      this.management = clinic.management.name;
      this.http.get<Unit>('/api/unit/' + clinicId + '/' + unitId)
        .do(unit => {
          if (unit) {
            this.unit = unit;
            this.cbvs = this.unit.careBurdenValues.filter(cbv => cbv.countedIn);
            this.http.get<Unit[]>('/api/unit?clinic=' + clinicId).subscribe(unitArray => {
              this.allUnitsOnSameClinic = unitArray.filter(x => x.name !== this.unit.name);
            });

            this.plannedInDropdownUnits = [{displayName: 'Välj', value: null}].concat(this.unit.unitsPlannedIn.map(unitplannedIn => {
              return {displayName: unitplannedIn.name, value: unitplannedIn.id};
            }));

            this.updateSskCategoryValueMatrix(unit);
            this.updateVacants(unit);
            this.inited = true;
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
      .filter(bed => bed.bedStatus != 'OCCUPIED')
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

          //thisBed.occupied = incomingBed.occupied;
          thisBed.bedStatus = incomingBed.bedStatus;
          thisBed.label = incomingBed.label;
          thisBed.patient = incomingBed.patient;
          thisBed.ssk = incomingBed.ssk;
          thisBed.cleaningAlternative = incomingBed.cleaningAlternative;
          thisBed.cleaningalternativeExists = incomingBed.cleaningalternativeExists;
          thisBed.cleaningInfo = incomingBed.cleaningInfo;
          thisBed.patientWaits = incomingBed.patientWaits;
          thisBed.servingClinic = incomingBed.servingClinic;
          // thisBed.
          //thisBed.relatedInformation = incomingBed.relatedInformation;
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
          thisPatient.relatedInformation = incomingPatient.relatedInformation;
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
      this.updateMessages(unit);

      this.updateUnitPlannedIns(unit, clinicId);
    });
  }

  private updateUnitPlannedIns(unit, clinicId) {
    if (this.unitPlannedInItemsOld) {
      this.unitPlannedInItemsOld.update(unit);
    }
    this.http.get<Unit[]>('/api/unit?clinic=' + clinicId).subscribe(unitArray => {

      // We also include this unit, so we don't filter.
      for (const incomingUnitOnSameClinic of unitArray) {

        this.unitsOnSameClinicTables
          .filter(table => table.unit.id === incomingUnitOnSameClinic.id)
          .forEach(table => {
            table.unit = incomingUnitOnSameClinic;
            table.update();
          });
      }
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
        .filter(z => z.bedStatus === 'OCCUPIED' && z.patient).map(x => x.patient);

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
              if (this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name][choice.careBurdenValue.name] &&
                choice.careBurdenValue.countedIn) {
                this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name][choice.careBurdenValue.name]++;
              } else {
                if (choice.careBurdenValue.countedIn)
                {
                  this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name][choice.careBurdenValue.name] = 1;
                }
              }
            } else {
              if (choice.careBurdenValue.countedIn)
              {
                this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name] = {};
                this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name][choice.careBurdenValue.name] = 1;
              }
            }
          }
          else
          {
            this.sskCategoryValueMatrix[ssk.label] = {};
            this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name] = {};
            if (choice.careBurdenValue.countedIn)
            {
              this.sskCategoryValueMatrix[ssk.label][choice.careBurdenCategory.name][choice.careBurdenValue.name] = 1;
            }
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
    //bed.occupied = true;
    bed.bedStatus = 'OCCUPIED';
    this.http.put('/api/bed/' + this.clinic.id + '/' + this.unit.id, bed)
      .subscribe(bed => {
        this.ngOnInit();
      });
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

    collapse(element: ListItemComponent) {
    element.toggleExpand();
    this.ngOnInit();
  }

  onUnitPlannedInItemsSave() {
    this.checkForChanges();
  }


  private updateMessages(unit: Unit) {
    this.http.get<Message[]>('/api/message/' + unit.id + '/today')
      .subscribe((messages: Message[]) => {
        this.messages = messages;
      });
  }
}



