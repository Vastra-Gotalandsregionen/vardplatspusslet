import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Clinic} from "../../../domain/clinic";
import {HttpClient} from "../../../../../node_modules/@angular/common/http";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DeleteModalComponent} from "../../../elements/delete-modal/delete-modal.component";
import {ListItemComponent} from "vgr-komponentkartan";
import {Management} from "../../../domain/management";

@Component({
  selector: 'app-clinics-admin',
  templateUrl: './clinics-admin.component.html',
  styleUrls: ['./clinics-admin.component.scss']
})
export class ClinicsAdminComponent implements OnInit {
  clinics: Clinic[] = [];

  clinicForm: FormGroup;
  clinicForDeletion: Clinic;
  managements: Management[];
  managementDropdownItems: { displayName: string; value: string }[] = [];

  @ViewChild(DeleteModalComponent) appDeleteModal: DeleteModalComponent;
  @ViewChild("addClinicId") addClinicId: ElementRef;

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.http.get<Management[]>('/api/management/').subscribe((managements: Management[]) => {
      this.managements = managements;
      this.managementDropdownItems = this.managements.map(management =>{
        return{
          displayName: management.name,
          value: management.id
        }
      });
      this.http.get<Clinic[]>('/api/clinic/')
        .subscribe((clinics: Clinic[]) => {
          this.clinics = clinics;
        });
      this.initClinicForm(null);
    });
  }

  private initClinicForm(clinic: Clinic) {
    if (!clinic) {
      clinic = new Clinic();
    }

    this.clinicForm = this.formBuilder.group({
      id: [clinic.id, Validators.required],
      name: [clinic.name, Validators.required],
      management: [clinic.management ? clinic.management.id : null, Validators.required]
    });
  }

  setCurrentClinic($event: any, clinic: Clinic) {
    if (event) {
      this.initClinicForm(clinic);
    }
  }

  saveClinic() {
    let clinic = new Clinic();
    let clinicModel = this.clinicForm.value;

    clinic.id = clinicModel.id;
    clinic.name = clinicModel.name;
    if(clinicModel.management)
    {
      clinic.management = new Management();
      clinic.management.id= clinicModel.management;
    }
    this.http.put('/api/clinic', clinic)
      .subscribe(() => {
        this.ngOnInit();
        this.addClinicId.nativeElement.focus();
      });
  }

  openDeleteModal(clinic: Clinic) {
    this.clinicForDeletion = clinic;
    this.appDeleteModal.open();
  }

  confirmDelete() {
    this.http.delete('/api/clinic/' + this.clinicForDeletion.id)
      .subscribe(() => {
        this.ngOnInit();
      });
  }

  collapse(element: ListItemComponent) {
    this.clinicForm.reset();
    element.toggleExpand();
  }

}
