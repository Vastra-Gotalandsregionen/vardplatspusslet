import {Patient} from "./patient";
import {Ssk} from "./ssk";
import {ServingClinic} from "./servingclinic";
import {CleaningAlternative} from "./cleaning-alternative";

export class Bed {
  id: number;
  label: string;
  occupied: boolean;
  patient: Patient;
  ssk: Ssk;
  servingClinic: ServingClinic;
  patientWaits: boolean;
  cleaningalternativeExists: boolean;
  cleaningInfo: string;
  cleaningAlternative: CleaningAlternative;


}
