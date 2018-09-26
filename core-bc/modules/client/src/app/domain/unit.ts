import {Patient} from "./patient";
import {Bed} from "./bed";

export class Unit {
  id: string;
  name: string;
  beds: Bed[];
  patients: Patient[];
  hasLeftDateFeature: boolean;
}
