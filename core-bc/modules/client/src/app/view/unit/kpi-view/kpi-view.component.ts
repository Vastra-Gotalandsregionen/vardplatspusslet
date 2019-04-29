import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {FormBuilder} from "@angular/forms";
import {AuthService} from "../../../service/auth.service";
import "rxjs-compat/add/operator/do";
import {UnitComponent} from "../unit.component";

@Component({
  selector: 'app-unit',
  templateUrl: './kpi-view.component.html',
  styleUrls: ['./kpi-view.component.scss']
})
export class KpiViewComponent extends UnitComponent implements OnInit {

  constructor(protected http: HttpClient,
      protected formBuilder: FormBuilder,
      protected route: ActivatedRoute,
      protected authService: AuthService) {

    super(http, formBuilder, route, authService);
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      let clinicId = params.clinicId;
      let unitId = params.id;
      this.updateView(clinicId, unitId);
    });
  }
}



