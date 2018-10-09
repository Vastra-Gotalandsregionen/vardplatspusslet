import {Patient} from "./patient";
import {Bed} from "./bed";
import {Clinic} from "./clinic";
import {Ssk} from "./ssk";

export class Unit {
  id: string;
  name: string;
  beds: Bed[];
  clinic: Clinic;
  patients: Patient[];
  ssks: Ssk[];
  hasLeftDateFeature: boolean;
}
