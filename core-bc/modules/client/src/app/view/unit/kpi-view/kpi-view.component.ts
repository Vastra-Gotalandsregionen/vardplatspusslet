import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {AbstractControl, FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {interval, Subscription} from "rxjs";
import {AuthService} from "../../../service/auth.service";
import "rxjs-compat/add/operator/do";
import {ListItemComponent} from "vgr-komponentkartan";
import {UnitComponent} from "../unit.component";

@Component({
  selector: 'app-unit',
  templateUrl: './kpi-view.component.html',
  styleUrls: ['./kpi-view.component.scss']
})
export class KpiViewComponent extends UnitComponent {

  constructor(protected http: HttpClient,
      protected formBuilder: FormBuilder,
      protected route: ActivatedRoute,
      protected authService: AuthService) {

    super(http, formBuilder, route, authService);
  }

  
}



