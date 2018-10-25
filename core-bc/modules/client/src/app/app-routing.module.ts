import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ClinicComponent} from "./view/clinic/clinic.component";
import {HomeComponent} from "./view/home/home.component";
import {UnitComponent} from "./view/unit/unit.component";
import {AdminComponent} from "./view/admin/admin.component";
import {ClinicsAdminComponent} from "./view/admin/clinics-admin/clinics-admin.component";
import {UnitsAdminComponent} from "./view/admin/units-admin/units-admin.component";
import {UsersAdminComponent} from "./view/admin/users-admin/users-admin.component";
import {EditMessagesComponent} from "./view/unit/edit-messages/edit-messages.component";

const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'admin',
    component: AdminComponent
  },
  {
    path: 'admin/clinics',
    component: ClinicsAdminComponent
  },
  {
    path: 'admin/units',
    component: UnitsAdminComponent
  },
  {
    path: 'admin/users',
    component: UsersAdminComponent
  },
  {
    path: ':id',
    component: ClinicComponent
  },
  {
    path: ':clinicId/:id',
    component: UnitComponent
  },
  {
    path: ':clinicId/:id/editMessages',
    component: EditMessagesComponent
  },
  {
    path: '**',
    redirectTo: '/'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
