import {Component, Input, OnInit} from '@angular/core';

import {Router} from "@angular/router";

import {Clinic} from "../../domain/clinic";
import {Management} from "../../domain/management";
import {Unit} from "../../domain/unit";

@Component({
  selector: 'app-breadcrumbs',
  templateUrl: './breadcrumbs.component.html',
  styleUrls: ['./breadcrumbs.component.scss']
})

export class BreadcrumbsComponent implements OnInit {

  @Input('current') current: any;
  @Input('type') type: string;

  pages : {'label': string, 'route': string, 'isCurrentPage': boolean} [] = [];

  constructor(protected router: Router) {
  }

  ngOnInit() {

    if(this.type == 'unit') {
      let unit = this.current as Unit;

      let clinic = unit.clinic;
      let management = clinic.management;

      let managementFriendlyURL = "/" + management.id;
      let clinicFriendlyURL = "/" + clinic.id;
      let unitFriendlyURL = "/" + unit.id;
      
      // Add management
      this.pages.push({
        'label': management.name,
        'route':  managementFriendlyURL,
        'isCurrentPage': false
      });

      // Add clinic
      this.pages.push({
        'label': clinic.name,
        'route':  managementFriendlyURL + clinicFriendlyURL,
        'isCurrentPage': false
      });

      // Add unit
      this.pages.push({
        'label': unit.name,
        'route':  managementFriendlyURL + clinicFriendlyURL + unitFriendlyURL,
        'isCurrentPage': true
      });

    }

    else if(this.type == 'clinic') {
      let clinic = this.current as Clinic;

      let management = clinic.management;

      let managementFriendlyURL = "/" + management.id;
      let clinicFriendlyURL = "/" + clinic.id;
      
      // Add management
      this.pages.push({
        'label': management.name,
        'route':  managementFriendlyURL,
        'isCurrentPage': false
      });

      // Add clinic
      this.pages.push({
        'label': clinic.name,
        'route':  managementFriendlyURL + clinicFriendlyURL,
        'isCurrentPage': true
      });

    }

    else if(this.type == 'management') {
      let management = this.current as Management;

      let managementFriendlyURL = "/" + management.id;
      
      // Add management
      this.pages.push({
        'label': management.name,
        'route':  managementFriendlyURL,
        'isCurrentPage': true
      });


    }    


  }

}


// export class DashboardMenuComponent implements OnInit {

//   @Input('unit') unit: Unit;
//   @Input('type') type: string;

//   unitDashboardData = new Map<string, string>([
//     ['/', 'Kombinerad'],
//     ['/bed-view', 'Sängar'],
//     ['/kpi-view', 'KPI']
//   ]);

//   unitDashboard = [{
//       'path': '',
//       'label': 'Kombinerad'
//     },{
//       'path': '/bed-view',
//       'label': 'Sängar'
//     },{ 
//       'path': '/kpi-view',
//       'label': 'KPI'
//   }];

//   dashboardPages : {'label': string, 'route': string, 'isCurrentPage': boolean} [] = [];

//   constructor(protected router: Router) {
//   }

//   ngOnInit() {

//     let currentURL = this.router.url;

//     if(this.type == 'unit-dashboard') {
//       let urlPrefix = '/' + this.unit.clinic.management.id + '/' + this.unit.clinic.id + '/' + this.unit.id;

//       this.unitDashboard.forEach((page) => {

//         let url = urlPrefix + page.path;
//         let isCurrentPage = false;

//         if(url == currentURL) {
//           isCurrentPage = true;
//         }

//         this.dashboardPages.push({
//           'label': page.label,
//           'route':  url,
//           'isCurrentPage': isCurrentPage
//         });

//       });
//     }

//   }

// }

