import {Patient} from "./patient";

export class Bed {
  id: number;
  label: string;
  occupied: boolean;
  patient: Patient;
}
