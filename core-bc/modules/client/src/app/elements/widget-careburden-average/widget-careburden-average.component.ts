import {Component, Input, OnInit} from '@angular/core';

import {Bed} from "../../domain/bed";
import {Patient} from "../../domain/patient";
import {Ssk} from "../../domain/ssk";
import {Unit} from "../../domain/unit";

@Component({
  selector: 'app-widget-careburden-average',
  templateUrl: './widget-careburden-average.component.html',
  styleUrls: ['./widget-careburden-average.component.scss']
})
export class WidgetCareBurdenAverageComponent implements OnInit {

  @Input('unit') unit: Unit;
  @Input('bedsExpanded') bedsExpanded: boolean;

  constructor() {
  }

  ngOnInit() {
  }


  BedHasNoPatientWithCareBurden(patient: Patient) {
    if ( patient != null && patient.careBurdenChoices && patient.careBurdenChoices.map(x => x.careBurdenValue)
      .filter(z => z!= null && z.id).length > 0)
      return false;
    else return true;
 }

 patientCareBurden(patientChoices, burdenCategoriId) {
  if (patientChoices!= null && patientChoices.length > 0){
    let x =  patientChoices.find(x => x.careBurdenCategory.id === burdenCategoriId);
    return x && x.careBurdenValue  && x.careBurdenValue.countedIn ? x.careBurdenValue.name: null;

  }
  else{
    return "";
  }
} 

CalculateAverage(burdenCategoriId) {
  let burdenval = 0;
  let antal = 0;
  for (let bed of this.unit.beds)
  {
    if (bed.patient)
    {
      let x = bed.patient.careBurdenChoices.find(x => x.careBurdenCategory.id === burdenCategoriId);
      if (x && x.careBurdenValue && x.careBurdenValue.countedIn){
        antal++;
        burdenval +=  +x.careBurdenValue.name;
      }
    }
  }
  if (antal === 0) return 0;
  return (burdenval/antal).toFixed(2);
}

}
