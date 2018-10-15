import {Component, OnInit, ViewChild} from '@angular/core';
import {Unit} from "../../../domain/unit";
import {HttpClient} from "@angular/common/http";
import {DeleteModalComponent} from "../../../elements/delete-modal/delete-modal.component";
import {Clinic} from "../../../domain/clinic";

@Component({
  selector: 'app-units-admin',
  templateUrl: './units-admin.component.html',
  styleUrls: ['./units-admin.component.scss']
})
export class UnitsAdminComponent implements OnInit {

  units: Unit[];

  @ViewChild(DeleteModalComponent) appDeleteModal: DeleteModalComponent;

  unitForDeletion: Unit;
  clinics: Clinic[];

  constructor(private http: HttpClient) {}

  ngOnInit() {

    this.http.get<Unit[]>('/api/unit')
      .subscribe((units: Unit[]) => {
        this.units = units;
      });

    this.http.get<Clinic[]>('/api/clinic')
      .subscribe((clinics: Clinic[]) => {
        this.clinics = clinics;
      });
  }

  openDeleteModal(unit: Unit) {
    this.unitForDeletion = unit;
    this.appDeleteModal.open();
  }

  save() {
    this.ngOnInit();
  }

  confirmDelete() {
    this.http.delete('/api/unit/' + this.unitForDeletion.id)
      .subscribe(() => {
        this.ngOnInit();
      });
  }

}
