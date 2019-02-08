import {Component, Input, OnInit} from '@angular/core';
import {Bed} from "../../domain/bed";
import {Unit} from "../../domain/unit";

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

  formatDate(date: any) {
    return new Date(date).toLocaleDateString();
  }

}
