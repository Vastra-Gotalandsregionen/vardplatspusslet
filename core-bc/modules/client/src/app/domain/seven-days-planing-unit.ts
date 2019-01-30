import {UnitPlannedIn} from "./unit-planned-in";

export class SevenDaysPlaningUnit {
  id: number;
  date: Date;
  fromUnit: UnitPlannedIn;
  quantity: number;
  comment: string;
}
