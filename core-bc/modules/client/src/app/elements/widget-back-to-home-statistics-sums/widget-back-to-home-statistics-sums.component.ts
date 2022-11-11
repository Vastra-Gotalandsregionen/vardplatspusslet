import {Component, Input, OnInit} from '@angular/core';
import {Unit} from "../../domain/unit";
import {Ssk} from "../../domain/ssk";

@Component({
  selector: 'app-widget-back-to-home-statistics-sums',
  templateUrl: './widget-back-to-home-statistics-sums.component.html',
  styleUrls: ['./widget-back-to-home-statistics-sums.component.scss']
})
export class WidgetBackToHomeStatisticsSumsComponent implements OnInit {

  @Input('unit') unit: Unit;

  constructor() {
  }

  ngOnInit() {
    console.log('Classes:');
    for (const allClass of this.getAllClasses()) {
      if (allClass.startsWith('fa-')) {
        console.log(allClass);
      }
    }
  }

  getBackToHomeStatistics(forSsk: Ssk): number {
    let result = 0;
    for (const bed of this.unit.beds) {
      if (bed && bed.ssk && forSsk && bed.ssk.id === forSsk.id) {
        const patient = bed.patient;
        if (patient) {
          const inf = patient.information;
          result += (!inf || inf === '5') ? 0 : 1;
        }
      }
    }
    return result;
  }

  getAllClasses(): string[] {
    const classSet = new Set();
    const allElements = document.getElementsByTagName("*");
    for (let i = 0, n = allElements.length; i < n; i++) {
      allElements[i].classList.forEach(cls => classSet.add(cls))
    }
    return Array.from(classSet);
  }

}
