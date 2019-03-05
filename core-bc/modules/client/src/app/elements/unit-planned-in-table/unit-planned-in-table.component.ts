import {Component, Input, OnInit} from '@angular/core';
import {Unit} from "../../domain/unit";
import {SevenDaysPlaningUnit} from "../../domain/seven-days-planing-unit";
import {DayAndDate} from "../../domain/dayAndDate";
import {UnitPlannedIn} from "../../domain/unit-planned-in";

@Component({
  selector: 'app-unit-planned-in-table',
  templateUrl: './unit-planned-in-table.component.html',
  styleUrls: ['./unit-planned-in-table.component.scss']
})
export class UnitPlannedInTableComponent implements OnInit {

  @Input('unit') unit: Unit;
  @Input('sevendaysplan') sevendaysplan:SevenDaysPlaningUnit[];
  daysAndMonths: DayAndDate[] = [];
  days: DayAndDate[] = [];
  sevendaysMatrix = {};
  sevenDaysSumArray: number[]= [0,0,0,0,0,0,0,0];


  constructor() { }


  ngOnInit() {

    this.daysAndMonths = [];
    this.CalculateDaysAndDate();
    this.filterSevenDays(this.unit.sevenDaysPlaningUnits);
    this.FillSevenDaysMatrix(this.unit);

  }

  private CalculateDaysAndDate()
  {
    this.daysAndMonths  =[];
    let WeekDay: string[]= ['Sön', 'Mån', 'Tis', 'Ons', 'Tors', 'Fred', 'Lör'];
    let veckodag: string[]= [];
    let today = new Date();
    let day = today.getDay();
    let dayDate = new DayAndDate();
    dayDate.dayName = WeekDay[day];
    dayDate.dayNumber= today.getDate();
    dayDate.monthNumber = today.getMonth() + 1;
    this.daysAndMonths.push(dayDate);
    for (var i=0; i< 7; i++)
    {
      let nextDay = new Date(today.setDate(today.getDate() + 1));
      day = nextDay.getDay();
      dayDate = new DayAndDate();
      dayDate.dayName = WeekDay[day];
      dayDate.dayNumber= nextDay.getDate();
      dayDate.monthNumber = nextDay.getMonth() + 1;
      this.daysAndMonths.push(dayDate);
    }
  }

  private  FillSevenDaysMatrix(unit: Unit)
  {
    this.sevendaysMatrix = [];
    this.sevenDaysSumArray = [0,0,0,0,0,0,0,0];
    let plannedIns: UnitPlannedIn[] =  unit.unitsPlannedIn;
    plannedIns.forEach( pi =>{
        this.sevendaysMatrix[pi.name] = [];
        for (var i=0; i<8; i++)
        {
          this.sevendaysMatrix[pi.name][i] = this.FindSevendaysMatrixValue(pi.name, i);
          this.sevenDaysSumArray[i] += this.sevendaysMatrix[pi.name][i];
        }
      }
    )
  }

  private FindSevendaysMatrixValue(name: string, i: number): number
  {

    let today = new Date();
    let currentDate= new Date( today.setDate(today.getDate() + i));
    currentDate.setHours(0, 0, 0, 0);
    let found = this.sevendaysplan.find(function (element) {
      let sourceDate = new Date(element.date);
      if (sourceDate.getDate() == currentDate.getDate())
      {
        return  element.fromUnit.name === name;
      }
    })
    return found != null ? found.quantity : null;
  }

  private filterSevenDays(sevendaysPlaningUnits: SevenDaysPlaningUnit[])  //actually 8 days
  {
    this.sevendaysplan = [];
    let today = new Date();
    let newdayTime = new Date();
    newdayTime.setHours(0);
    newdayTime.setMinutes(0,0, 0);
    let eightday = new Date(today.setDate(today.getDate() + 8));
    this.sevendaysplan = sevendaysPlaningUnits.sort( (a:SevenDaysPlaningUnit, b: SevenDaysPlaningUnit) =>
      (a.date > b.date ? 1 : -1));

    this.sevendaysplan = this.sevendaysplan.filter(x => (x.date <= eightday) && (x.date >= newdayTime) );
  }

}
