import {Component, Input, OnInit} from '@angular/core';
import {Unit} from "../../domain/unit";
import {Ssk} from "../../domain/ssk";

@Component({
  selector: 'app-widget-ssk-leave-for-home-sum',
  templateUrl: './widget-ssk-leave-for-home-sum.component.html',
  styleUrls: ['./widget-ssk-leave-for-home-sum.component.scss']
})
export class WidgetSskLeaveForHomeSumComponent implements OnInit {

  @Input()
  unit: Unit;

  @Input()
  ssk: Ssk;

  result: number;
  constructor() {

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

  ngOnInit() {
    this.result = this.getBackToHomeStatistics(this.ssk);
  }

}
