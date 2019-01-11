import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User} from "../../../domain/user";
import {DropdownItem, ListItemComponent} from "vgr-komponentkartan";
import {Unit} from "../../../domain/unit";
import {DeleteModalComponent} from "../../../elements/delete-modal/delete-modal.component";
import {AuthService} from "../../../service/auth.service";
import {UserSaveRequest} from "../../../domain/user-save-request";

@Component({
  selector: 'app-users-admin',
  templateUrl: './users-admin.component.html',
  styleUrls: ['./users-admin.component.scss']
})
export class UsersAdminComponent implements OnInit {

  users: User[];

  userForm: FormGroup;
  unitDropdownItems: DropdownItem<string>[] = [];
  unitMap: Map<string, Unit> = new Map<string, Unit>();
  userForDeletion: User;
  private units: Unit[];

  @ViewChild(DeleteModalComponent) appDeleteModal: DeleteModalComponent;
  @ViewChild("addUserId") addUserId: ElementRef;

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder,
              private authService: AuthService) { }

  ngOnInit() {
    this.http.get<User[]>('/api/user')
      .subscribe((users: User[]) => {
        this.users = users;
      });

    this.http.get<Unit[]>('/api/unit').subscribe(units => {
      this.units = units;
      this.unitDropdownItems = units.map(unit => {
        this.unitMap.set(unit.id, unit);

        let clinicName = unit.clinic ? unit.clinic.name : 'N/A';

        return {displayName: clinicName + ' > ' + unit.name, value: unit.id}
      });

      this.initForm(null);
    });

  }

  private initForm(user: User) {
    if (!user) {
      user = new User();
    }

    let defaulSelection = this.authService.isAdmin() ? [] : this.units.map(unit => unit.id);

    if (this.userForm) {

      this.userForm.setValue({
        id: user.id ? user.id : null,
        name: user.name ? user.name : null,
        role: user.role ? user.role : (this.admin ? null: 'USER'),
        units: user.units ? user.units.map(unit => unit.id) : defaulSelection
      });
    } else {
      this.userForm = this.formBuilder.group({
        id: [user.id, Validators.required],
        name: [user.name],
        role: [user.role, Validators.required],
        units: [user.units ? user.units.map(unit => unit.id) : defaulSelection, Validators.required]
      });
    }
  }

  setCurrentUser($event, user: User) {
    if ($event) {
      this.initForm(user);
    }
  }

  saveUser(element: ListItemComponent) {
    let userSaveRequest = new UserSaveRequest();
    let userModel = this.userForm.value;

    userSaveRequest.id = userModel.id;
    userSaveRequest.role = userModel.role;
    userSaveRequest.unitIds = userModel.units;

    this.http.put('/api/user', userSaveRequest)
      .subscribe(() => {
        this.ngOnInit();
        this.addUserId.nativeElement.focus();
      });
    this.collapse(element);
  }

  openDeleteModal(user: User) {
    this.userForDeletion = user;
    this.appDeleteModal.open();
  }

  confirmDelete() {
    this.http.delete('/api/user/' + this.userForDeletion.id)
      .subscribe(() => {
        this.ngOnInit();
      });
  }

  toUnitString(units: Unit[]): string {
    return units.map(unit => unit.id).join(', ');
  }

  get admin(): boolean {
    return this.authService.isAdmin();
  }

  collapse(element: ListItemComponent) {
    this.userForm.reset();
    element.setExpandOrCollapsed();
  }
}
