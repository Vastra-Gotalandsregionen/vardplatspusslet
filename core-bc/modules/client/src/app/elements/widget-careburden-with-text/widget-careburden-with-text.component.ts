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

  constructor() {
  }

  ngOnInit() {
  }


  getMatrixValue(ssk: Ssk, cbk: CareBurdenCategory, cbv: CareBurdenValue): string {
    if (this.sskCategoryValueMatrix[ssk.label] && this.sskCategoryValueMatrix[ssk.label][cbk.name] && this.sskCategoryValueMatrix[ssk.label][cbk.name][cbv.name]) {
      return this.sskCategoryValueMatrix[ssk.label][cbk.name][cbv.name];
    } else {
      return '0';
    }
  }
}
