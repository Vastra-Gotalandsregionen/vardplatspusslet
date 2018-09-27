import {Clinic} from "./clinic";
import {Patient} from "./patient";

export class Bed {
  id: number;
  label: string;
  occupied: boolean;
  clinic: Clinic;
  patient: Patient;
}
