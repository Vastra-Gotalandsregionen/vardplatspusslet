import {Component, Input, OnInit} from '@angular/core';
import {Unit} from "../../domain/unit";
import {Ssk} from "../../domain/ssk";
import {CareBurdenCategory} from "../../domain/careBurdenCategory";
import {CareBurdenValue} from "../../domain/careburdenvalue";

@Component({
  selector: 'app-widget-careburden-with-text-compact-alt',
  templateUrl: './widget-careburden-with-text-compact-alt.component.html',
  styleUrls: ['./widget-careburden-with-text-compact-alt.component.scss']
})
export class WidgetCareburdenWithTextCompactAltComponent implements OnInit {

  @Input('unit') unit: Unit;
  @Input('sskCategoryValueMatrix') sskCategoryValueMatrix: {};
  cbvs: CareBurdenValue[] = [];

  constructor() {
  }

  ngOnInit() {
    this.cbvs = this.unit.careBurdenValues.filter(cbv => cbv.countedIn);
  }

  getMatrixValue(ssk: Ssk, cbk: CareBurdenCategory, cbv: CareBurdenValue): string {
    if (this.sskCategoryValueMatrix && ssk && ssk.label && this.sskCategoryValueMatrix[ssk.label] && this.sskCategoryValueMatrix[ssk.label][cbk.name] && this.sskCategoryValueMatrix[ssk.label][cbk.name][cbv.name]) {
      return this.sskCategoryValueMatrix[ssk.label][cbk.name][cbv.name];
    } else {
      return '0';
    }
  }
}
