import {Component, Input, OnInit} from '@angular/core';
import {Bed} from "../../domain/bed";
import {Unit} from "../../domain/unit";
import {Patient} from "../../domain/patient";

@Component({
  selector: 'app-the-other-bed-icons',
  templateUrl: './the-other-bed-icons.component.html',
  styleUrls: ['./the-other-bed-icons.component.scss', '../../view/unit/unit.component.scss']
})
export class TheOtherBedIconsComponent implements OnInit {

  @Input('bed') bed: Bed;
  @Input('unit') unit: Unit;

  constructor() { }

  ngOnInit() {
  }

  toInterpreterString(patient: Patient): string {
    let text = 'Tolk: ';

    if (patient.interpretDate) {
      text += this.formatDate(patient.interpretDate);
    }

    if (patient.interpretDate && patient.interpretInfo) {
      text += ', ';
    }

    if (patient.interpretInfo) {
      text += patient.interpretInfo;
    }

    return text;
  }

  formatDate(date: any) {
    return new Date(date).toLocaleDateString();
  }

}
