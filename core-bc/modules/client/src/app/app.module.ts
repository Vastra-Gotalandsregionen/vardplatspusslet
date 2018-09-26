import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ClinicComponent} from './view/clinic/clinic.component';
import {HomeComponent} from './view/home/home.component';
import {HttpClientModule} from "@angular/common/http";
import { UnitComponent } from './view/unit/unit.component';

@NgModule({
  declarations: [
    AppComponent,
    ClinicComponent,
    HomeComponent,
    UnitComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
