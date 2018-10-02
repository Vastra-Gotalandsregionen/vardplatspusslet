import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ClinicComponent} from "./view/clinic/clinic.component";
import {HomeComponent} from "./view/home/home.component";
import {UnitComponent} from "./view/unit/unit.component";
import {AdminComponent} from "./view/admin/admin.component";
import {ClinicsAdminComponent} from "./view/admin/clinics-admin/clinics-admin.component";
import {UnitsAdminComponent} from "./view/admin/units-admin/units-admin.component";

const routes: Routes = [
  {
    path: '',
    children: [
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
        path: ':id',
        component: ClinicComponent
      },
      {
        path: ':clinicId/:id',
        component: UnitComponent
      },
      {
        path: '**',
        redirectTo: '/'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
