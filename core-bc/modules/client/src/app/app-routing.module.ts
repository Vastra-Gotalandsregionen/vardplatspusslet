import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ClinicComponent} from "./view/clinic/clinic.component";
import {HomeComponent} from "./view/home/home.component";
import {UnitComponent} from "./view/unit/unit.component";
import {BedViewComponent} from "./view/unit/bed-view/bed-view.component";
import {KpiViewComponent} from "./view/unit/kpi-view/kpi-view.component";
import {AdminComponent} from "./view/admin/admin.component";
import {ClinicsAdminComponent} from "./view/admin/clinics-admin/clinics-admin.component";
import {UnitsAdminComponent} from "./view/admin/units-admin/units-admin.component";
import {UsersAdminComponent} from "./view/admin/users-admin/users-admin.component";
import {EditMessagesComponent} from "./view/unit/edit-messages/edit-messages.component";
import {UserLoggedInGuard} from "./guard/user-logged-in.guard";
import {AdminGuard} from "./guard/admin.guard";
import {HasEditUnitPermissionGuard} from "./guard/has-edit-unit-permission.guard";
import {StatisticsComponent} from "./view/admin/statistics/statistics.component";
import {ManagementsAdminComponent} from "./view/admin/managements-admin/managements-admin.component";
import {ManagementComponent} from "./view/management/management.component";

const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'admin/managements',
    component: ManagementsAdminComponent,
    canActivate: [AdminGuard]
  },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [UserLoggedInGuard]
  },
  {
    path: 'admin/clinics',
    component: ClinicsAdminComponent,
    canActivate: [AdminGuard]
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
    path: 'admin/statistics',
    component: StatisticsComponent
  },
  {
    path: ':managementId/:id',
    component: ClinicComponent
  },
  {
    path: ':id',
    component: ManagementComponent
  },
  {
    path: ':managementId/:clinicId/:id',
    component: UnitComponent
  },
  {
    path: ':managementId/:clinicId/:id/bed-view',
    component: BedViewComponent
  },
  {
    path: ':managementId/:clinicId/:id/kpi-view',
    component: KpiViewComponent
  },
  {
    path: ':managementId/:clinicId/:id/editMessages',
    component: EditMessagesComponent,
    canActivate: [HasEditUnitPermissionGuard]
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
