import {Patient} from "./patient";
import {Ssk} from "./ssk";

export class Bed {
  id: number;
  label: string;
  occupied: boolean;
  patient: Patient;
  ssk: Ssk;
}
