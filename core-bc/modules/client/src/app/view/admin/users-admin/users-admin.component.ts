import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FormBuilder, FormGroup} from "@angular/forms";
import {User} from "../../../domain/user";
import {DropdownItem} from "vgr-komponentkartan";
import {Unit} from "../../../domain/unit";
import {DeleteModalComponent} from "../../../elements/delete-modal/delete-modal.component";

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

  @ViewChild(DeleteModalComponent) appDeleteModal: DeleteModalComponent;

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder) { }

  ngOnInit() {

    this.http.get<User[]>('/api/user')
      .subscribe((users: User[]) => {
        this.users = users;
      });

    this.http.get<Unit[]>('/api/unit').subscribe(units => {
      this.unitDropdownItems = units.map(unit => {
        this.unitMap.set(unit.id, unit);

        let clinicName = unit.clinic ? unit.clinic.name : 'N/A';

        return {displayName: clinicName + ' > ' + unit.name, value: unit.id}
      });
    });

    this.initForm(null);
  }

  private initForm(user: User) {
    if (!user) {
      user = new User();
    }

    if (this.userForm) {
      this.userForm.setValue({
        id: user.id ? user.id : null,
        name: user.name ? user.name : null,
        role: user.role ? user.role : null,
        units: user.units ? user.units.map(unit => unit.id) : null
      });
    } else {
      this.userForm = this.formBuilder.group({
        id: [user.id],
        name: [user.name],
        role: [user.role],
        units: [user.units ? user.units.map(unit => unit.id) : null]
      });
    }
  }

  setCurrentUser($event, user: User) {
    if ($event) {
      this.initForm(user);
    }
  }

  saveUser() {
    let user = new User();
    let userModel = this.userForm.value;

    user.id = userModel.id;
    user.name = userModel.name;
    user.role = userModel.role;
    user.units = userModel.units.map(unitId => {
      return this.unitMap.get(unitId);
    });

    this.http.put('/api/user', user)
      .subscribe(() => {
        this.ngOnInit();
      });
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
}
