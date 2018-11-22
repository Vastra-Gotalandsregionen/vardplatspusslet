import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {Unit} from "../../domain/unit";
import {DropdownItem, ListItemComponent, SelectableItem} from "vgr-komponentkartan";
import {Bed} from "../../domain/bed";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Patient} from "../../domain/patient";
import {Clinic} from "../../domain/clinic";
import {DeleteModalComponent} from "../../elements/delete-modal/delete-modal.component";
import {Ssk} from "../../domain/ssk";
import {Message} from "../../domain/message";
import "rxjs-compat/add/operator/do";

@Component({
  selector: 'app-unit',
  templateUrl: './unit.component.html',
  styleUrls: ['./unit.component.scss']
})
export class UnitComponent implements OnInit {

  @ViewChild(DeleteModalComponent) appDeleteModal: DeleteModalComponent;

  unit: Unit;
  clinic: Clinic;

  amningOptions: SelectableItem<number>[];
  informationOptions: SelectableItem<number>[];
  genderDropdownItems: DropdownItem<string>[];
  servingKlinikerDropdownItems: DropdownItem<number>[];
  sskDropdownItems: DropdownItem<number>[];
  cleaningAlternativesDropdownItems: DropdownItem<number>[];
  leaveStatusesDropdownItems = [
    {
      displayName: 'Permission', value: 'PERMISSION'
    },
    {
      displayName: 'Teknisk plats', value: 'TEKNISK_PLATS'
    }];

  error: string;
  notFoundText = 'Oops. Inget fanns här...';

  addBedForm: FormGroup;
  bedForDeletion: Bed;

  vacantBeds: DropdownItem<number>[];

  chosenVacantBedId: number;

  showChangeBedOrder = false;
  bedsOrder: Bed[] = [];
  inited: boolean;

  messages: Message[] = [];

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute) {

    this.genderDropdownItems = [
      {displayName: 'Kvinna', value: 'KVINNA'},
      {displayName: 'Man', value: 'MAN'},
      {displayName: 'Barn', value: 'BARN'}
    ];

    this.amningOptions = [
      {displayName: 'Normal amning', value: 1},
      {displayName: 'Amningshjälp', value: 2},
      {displayName: 'AmningsMottagning ', value: 3}
    ];

    this.informationOptions = [
      {displayName: 'THG', value: 1},
      {displayName: 'THG/Barn', value: 2},
      {displayName: 'Föräldrarum', value: 3}
    ];

    window.setInterval(() => this.checkForChanges(), 10000);
  }

  ngOnInit() {
    this.route.params.subscribe(params => {

      let clinicId = params.clinicId;

      this.http.get<Clinic>('/api/clinic/' + clinicId).subscribe(clinic => {
        this.clinic = clinic;
      });

      this.http.get<Unit>('/api/unit/' + clinicId + '/' + params.id)
        .do(unit => {
          if (unit) {
            this.unit = unit;

            this.sskDropdownItems = [{displayName: 'Välj', value: null}].concat(unit.ssks.map(ssk => {
              return {displayName: ssk.label, value: ssk.id};
            }));

            this.servingKlinikerDropdownItems = unit.servingClinics.map(klinik => {
              return {displayName: klinik.name, value: klinik.id};
            });
            this.cleaningAlternativesDropdownItems = unit.cleaningAlternatives.map(cg => {
              return {displayName: cg.description, value: cg.id};
            });

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
        console.log(error1);
        if (error1.status === 404) {
          this.error = this.notFoundText;
        } else {
          this.error = error1.message;
        }
      });

      this.addBedForm = this.formBuilder.group({
        id: null,
        label: null
      })
    });
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

      this.updateVacants(unit);
    });
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

  saveAddBed() {
    let bed = new Bed();
    let bedModel = this.addBedForm.value;

    bed.label = bedModel.label;

    this.http.put('/api/bed/' + this.clinic.id + '/' + this.unit.id, bed)
      .subscribe(bed => {
        this.ngOnInit();
      });
  }

  collapse(element: ListItemComponent) {
    element.setExpandOrCollapsed();
  }

  openDeleteModal(bed: Bed) {
    this.bedForDeletion = bed;
    this.appDeleteModal.open();
  }

  confirmDelete() {
    this.http.delete('/api/bed/' + this.clinic.id + '/' + this.unit.id + '/' + this.bedForDeletion.id)
      .subscribe(() => {
        this.ngOnInit();
      }); // todo error handling
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
}
