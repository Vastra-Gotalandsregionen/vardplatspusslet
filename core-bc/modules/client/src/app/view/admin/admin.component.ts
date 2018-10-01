import {Component, OnInit} from '@angular/core';
import {Clinic} from "../../domain/clinic";
import {HttpClient} from "../../../../node_modules/@angular/common/http";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Unit} from "../../domain/unit";
import {DropdownItem} from "vgr-komponentkartan";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  clinics: Clinic[] = [];

  clinicForm: FormGroup;
  unitForm: FormGroup;
  units: Unit[];
  clinicsDropdownItems: DropdownItem<string>[];

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder,
  ) {
  }

  ngOnInit() {

    this.http.get<Clinic[]>('/api/clinic/')
      .subscribe((clinics: Clinic[]) => {
        this.clinics = clinics;

        this.clinicsDropdownItems = this.clinics.map(clinic => {
          return {displayName: clinic.name, value: clinic.id};
        });

      });

    this.http.get<Unit[]>('/api/unit')
      .subscribe((units: Unit[]) => {
        this.units = units;
      });

    this.initClinicForm(null);
    this.initUnitForm(null);

  }

  private initClinicForm(clinic: Clinic) {
    if (!clinic) {
      clinic = new Clinic();
    }

    this.clinicForm = this.formBuilder.group({
      id: [clinic.id],
      name: [clinic.name],
      units: [clinic.units]
    });
  }

  private initUnitForm(unit: Unit) {
    if (!unit) {
      unit = new Unit();
    }

    this.unitForm = this.formBuilder.group({
      id: [unit.id],
      name: [unit.name]
    });
  }

  setCurrentClinic($event: any, clinic: Clinic) {
    this.initClinicForm(clinic);
  }

  setCurrentUnit($event: any, unit: Unit) {

  }

  saveClinic() {
    let clinic = new Clinic();
    let clinicModel = this.clinicForm.value;

    clinic.id = clinicModel.id;
    clinic.name = clinicModel.name;
    clinic.units = clinicModel.units;

    this.http.put('/api/clinic', clinic)
      .subscribe(() => {
        this.ngOnInit();
      });
  }

}
