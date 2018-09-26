import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ClinicComponent} from "./view/clinic/clinic.component";
import {HomeComponent} from "./view/home/home.component";
import {UnitComponent} from "./view/unit/unit.component";

const routes: Routes = [
  {
    path: '',
    children: [
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
        component: HomeComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
