import {Patient} from "./patient";
import {Bed} from "./bed";
import {Clinic} from "./clinic";
import {Ssk} from "./ssk";
import {ServingClinic} from "./servingclinic";
import {Message} from "./message";

export class Unit {
  id: string;
  name: string;
  beds: Bed[];
  clinic: Clinic;
  patients: Patient[];
  ssks: Ssk[];
  messages: Message[];
  servingClinics: ServingClinic[];
  hasLeftDateFeature: boolean;
  hasCarePlan: boolean;
}
