import {Patient} from "./patient";
import {Bed} from "./bed";
import {Clinic} from "./clinic";
import {Ssk} from "./ssk";
import {ServingClinic} from "./servingclinic";

export class Unit {
  id: string;
  name: string;
  beds: Bed[];
  clinic: Clinic;
  patients: Patient[];
  ssks: Ssk[];
  servingClinics: ServingClinic[];
  hasLeftDateFeature: boolean;
  hasCarePlan: boolean;
}
