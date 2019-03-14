import {Component, Input, OnInit} from '@angular/core';

import {Clinic} from "../../domain/clinic";
import {Unit} from "../../domain/unit";

@Component({
  selector: 'app-widget-diet-list',
  templateUrl: './widget-diet-list.component.html',
  styleUrls: ['./widget-diet-list.component.scss']
})
export class WidgetDietListComponent implements OnInit {

  @Input('clinic') clinic: Clinic;
  @Input('unit') unit: Unit;

  constructor() {
  }

  ngOnInit() {
  }

}
