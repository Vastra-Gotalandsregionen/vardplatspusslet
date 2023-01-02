import {Component, Input, OnInit} from '@angular/core';

import {CareBurdenCategory} from "../../domain/careBurdenCategory";
import {CareBurdenValue} from "../../domain/careburdenvalue";
import {Ssk} from "../../domain/ssk";
import {Unit} from "../../domain/unit";

@Component({
  selector: 'app-widget-careburden-with-text',
  templateUrl: './widget-careburden-with-text.component.html',
  styleUrls: ['./widget-careburden-with-text.component.scss']
})
export class WidgetCareBurdenWithTextComponent implements OnInit {

  @Input('unit') unit: Unit;
  @Input('sskCategoryValueMatrix') sskCategoryValueMatrix: {};
  @Input('cbvs') cbvs: CareBurdenValue[];


  constructor() {
  }

  ngOnInit() {
    //this.cbvs = this.cbvs.filter(cbv => cbv.countedIn);
  }


  getMatrixValue(ssk: Ssk, cbk: CareBurdenCategory, cbv: CareBurdenValue): string {
    if (this.sskCategoryValueMatrix[ssk.label] && this.sskCategoryValueMatrix[ssk.label][cbk.name] && this.sskCategoryValueMatrix[ssk.label][cbk.name][cbv.name]) {
      return this.sskCategoryValueMatrix[ssk.label][cbk.name][cbv.name];
    } else {
      return '0';
    }
  }

  justCareBurdenItems(ssks: Ssk[]): Ssk[] {
    const result: Ssk[] = [];
    for (const ssk of ssks) {
      if (ssk.showCareBurden) {
        result.push(ssk);
      }
    }
    return result;
  }

}
