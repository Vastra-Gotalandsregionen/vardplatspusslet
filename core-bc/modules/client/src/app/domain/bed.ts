import {Patient} from "./patient";
import {Ssk} from "./ssk";
import {ServingClinic} from "./servingclinic";

export class Bed {
  id: number;
  label: string;
  occupied: boolean;
  patient: Patient;
  ssk: Ssk;
  servingClinic: ServingClinic;
  patientWaits: boolean;
}
