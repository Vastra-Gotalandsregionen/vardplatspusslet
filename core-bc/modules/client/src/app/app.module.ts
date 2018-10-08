import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ClinicComponent} from './view/clinic/clinic.component';
import {HomeComponent} from './view/home/home.component';
import {HttpClientModule} from "@angular/common/http";
import {UnitComponent} from './view/unit/unit.component';
import {KomponentkartanModule} from "vgr-komponentkartan";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import {ChevronRightComponent} from './elements/chevron-right/chevron-right.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AuthService} from "./service/auth.service";
import {AdminComponent} from './view/admin/admin.component';
import {ClinicsAdminComponent} from './view/admin/clinics-admin/clinics-admin.component';
import {UnitsAdminComponent} from './view/admin/units-admin/units-admin.component';
import {DeleteModalComponent} from './elements/delete-modal/delete-modal.component';
import {JwtModule} from "@auth0/angular-jwt";
import {DragulaModule} from "ng2-dragula";
import {UsersAdminComponent} from './view/admin/users-admin/users-admin.component';

@NgModule({
  declarations: [
    AppComponent,
    ClinicComponent,
    HomeComponent,
    UnitComponent,
    ChevronRightComponent,
    AdminComponent,
    ClinicsAdminComponent,
    UnitsAdminComponent,
    DeleteModalComponent,
    UsersAdminComponent
  ],
  imports: [
    BrowserModule,
    NoopAnimationsModule,
    HttpClientModule,
    JwtModule,
    KomponentkartanModule,
    FormsModule,
    ReactiveFormsModule,
    DragulaModule.forRoot(),
    AppRoutingModule
  ],
  providers: [
    AuthService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
