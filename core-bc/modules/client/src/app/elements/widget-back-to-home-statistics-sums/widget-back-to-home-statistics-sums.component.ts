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
  }

  getAllClasses(): string[] {
    const classSet = new Set();
    const allElements = document.getElementsByTagName('*');
    for (let i = 0, n = allElements.length; i < n; i++) {
      allElements[i].classList.forEach(cls => classSet.add(cls));
    }
    return Array.from(classSet);
  }

}
