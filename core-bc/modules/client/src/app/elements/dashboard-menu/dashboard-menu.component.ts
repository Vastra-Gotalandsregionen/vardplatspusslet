import {Component, Input, OnInit} from '@angular/core';

import {Unit} from "../../domain/unit";

@Component({
  selector: 'app-dashboard-menu',
  templateUrl: './dashboard-menu.component.html',
  styleUrls: ['./dashboard-menu.component.scss']
})
export class DashboardMenuComponent implements OnInit {

  @Input('unit') unit: Unit;
  @Input('type') type: string;

  constructor() {
  }

  ngOnInit() {

    console.log('type is: ' + this.type);

    console.log('unit is: ' + this.unit);

    //path: ':managementId/:clinicId/:id/kpi-view',

    if(this.type == 'unit-dashboard') {
      // let urlPrefix = '/' + this.unit.clinic.management.id + '/' + this.unit.clinic.id + '/' + this.unit.id + '/';

      // console.log('urlPrefix: ' + urlPrefix);
    }

    //this.unit.
    //managementId
    //clinicId
    //unitId


  }

}
