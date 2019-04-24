import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Clinic} from "../../../domain/clinic";
import {Unit} from "../../../domain/unit";
import {Subscription} from "rxjs";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {AuthService} from "../../../service/auth.service";
import {Management} from "../../../domain/management";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DeleteModalComponent} from "../../../elements/delete-modal/delete-modal.component";
import {ListItemComponent} from "vgr-komponentkartan";

@Component({
  selector: 'app-managements-admin',
  templateUrl: './managements-admin.component.html',
  styleUrls: ['./managements-admin.component.scss']
})
export class ManagementsAdminComponent implements OnInit {

  managements: Management[] = [];

  managementForm: FormGroup;
  managementForDeletion: Management;

  @ViewChild(DeleteModalComponent) appDeleteModal: DeleteModalComponent;
  @ViewChild("addManagementId") addManagementId: ElementRef;

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder) { }

  ngOnInit() {

    this.http.get<Management[]>('/api/management/')
      .subscribe((managements: Management[]) => {
        this.managements = managements;
      });
    this.initManagementForm(null);
  }

  private initManagementForm(management: Management) {
    if (!management) {
      management = new Management();
    }

    this.managementForm = this.formBuilder.group({
      id: [management.id, Validators.required],
      name: [management.name, Validators.required]
    });
  }

  setCurrentManagement($event: any, management: Management) {
    if (event) {
      this.initManagementForm(management);
    }
  }

  saveManagement(newManagement: boolean) {
    let management = new Management();
    let managementModel = this.managementForm.value;

    management.id = managementModel.id;
    management.name = managementModel.name;

    this.http.put('/api/management' + (newManagement ? '?newManagement=true' : ''), management)
      .subscribe(() => {
        this.ngOnInit();
        this.addManagementId.nativeElement.focus();
      });
  }

  openDeleteModal(management: Management) {
    this.managementForDeletion = management;
    this.appDeleteModal.open();
  }

  confirmDelete() {
    this.http.delete('/api/management/' + this.managementForDeletion.id)
      .subscribe(() => {
        this.ngOnInit();
      });
  }

  collapse(element: ListItemComponent) {
    this.managementForm.reset();
    element.toggleExpand();
  }

}
