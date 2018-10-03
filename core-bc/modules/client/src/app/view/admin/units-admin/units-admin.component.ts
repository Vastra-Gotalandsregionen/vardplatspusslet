import {Component, OnInit, ViewChild} from '@angular/core';
import {Unit} from "../../../domain/unit";
import {HttpClient} from "../../../../../node_modules/@angular/common/http";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Clinic} from "../../../domain/clinic";
import {DeleteModalComponent} from "../../../elements/delete-modal/delete-modal.component";

@Component({
  selector: 'app-units-admin',
  templateUrl: './units-admin.component.html',
  styleUrls: ['./units-admin.component.scss']
})
export class UnitsAdminComponent implements OnInit {

  clinics: Clinic[] = [];

  unitForm: FormGroup;
  units: Unit[];

  @ViewChild(DeleteModalComponent) appDeleteModal: DeleteModalComponent;

  unitForDeletion: Unit;

  clinicDropdownItems: { displayName: string; value: string }[];

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder) { }

  ngOnInit() {

    this.http.get<Unit[]>('/api/unit')
      .subscribe((units: Unit[]) => {
        this.units = units;
      });

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
      clinic: [unit.clinic ? unit.clinic.id : null]
    });

  }

  private reinitUnitForm(unit: Unit) {
    if (!unit) {
      unit = new Unit();
      unit.clinic = new Clinic();
    }

    this.unitForm.setValue({
      id: unit.id,
      name: unit.name,
      clinic: unit.clinic ? unit.clinic.id : null
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

    if (unitModel.clinic) {
      unit.clinic = new Clinic();
      unit.clinic.id = unitModel.clinic;
    }

    this.http.put('/api/unit', unit)
      .subscribe(() => {
        this.ngOnInit();
      });
  }

  openDeleteModal(unit: Unit) {
    this.unitForDeletion = unit;
    this.appDeleteModal.open();
  }

  confirmDelete() {
    this.http.delete('/api/unit/' + this.unitForDeletion.id)
      .subscribe(() => {
        this.ngOnInit();
      });
  }

}
