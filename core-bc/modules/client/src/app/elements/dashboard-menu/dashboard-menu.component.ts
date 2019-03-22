import {Component, Input, OnInit} from '@angular/core';

import {Router} from "@angular/router";

import {Unit} from "../../domain/unit";

@Component({
  selector: 'app-dashboard-menu',
  templateUrl: './dashboard-menu.component.html',
  styleUrls: ['./dashboard-menu.component.scss']
})


export class DashboardMenuComponent implements OnInit {

  @Input('unit') unit: Unit;
  @Input('type') type: string;

  unitDashboardData = new Map<string, string>([
    ['/', 'Kombinerad'],
    ['/bed-view', 'Sängar'],
    ['/kpi-view', 'KPI']
  ]);

  unitDashboard = [{
      'path': '',
      'label': 'Kombinerad'
    },{
      'path': '/bed-view',
      'label': 'Sängar'
    },{ 
      'path': '/kpi-view',
      'label': 'KPI'
  }];

  dashboardPages : {'label': string, 'route': string, 'isCurrentPage': boolean} [] = [];

  constructor(protected router: Router) {
  }

  ngOnInit() {

    let currentURL = this.router.url;

    if(this.type == 'unit-dashboard') {
      let urlPrefix = '/' + this.unit.clinic.management.id + '/' + this.unit.clinic.id + '/' + this.unit.id;

      this.unitDashboard.forEach((page) => {

        let url = urlPrefix + page.path;
        let isCurrentPage = false;

        if(url == currentURL) {
          isCurrentPage = true;
        }

        this.dashboardPages.push({
          'label': page.label,
          'route':  url,
          'isCurrentPage': isCurrentPage
        });

      });
    }

  }

}

