import {Patient} from "./patient";
import {Bed} from "./bed";
import {Clinic} from "./clinic";

export class Unit {
  id: string;
  name: string;
  beds: Bed[];
  clinic: Clinic;
  patients: Patient[];
  hasLeftDateFeature: boolean;
}
