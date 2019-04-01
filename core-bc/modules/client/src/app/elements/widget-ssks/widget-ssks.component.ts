import {Component, Input, OnInit} from '@angular/core';

import {Ssk} from "../../domain/ssk";
import {Unit} from "../../domain/unit";

@Component({
  selector: 'app-widget-ssks',
  templateUrl: './widget-ssks.component.html',
  styleUrls: ['./widget-ssks.component.scss']
})
export class WidgetSsksComponent implements OnInit {

  @Input('unit') unit: Unit;

  constructor() {
  }

  ngOnInit() {
  }

  countOccupiedBeds(sskArg: Ssk) {
    // Map to ssk, then filter out those with the same id and count the result.
    return this.unit.beds.filter(bed => bed.occupied).map(bed => bed.ssk).filter(ssk => ssk ? ssk.id === sskArg.id : false).length;
  }  

}
