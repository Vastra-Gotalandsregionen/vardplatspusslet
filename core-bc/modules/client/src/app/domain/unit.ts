import {Patient} from "./patient";
import {Bed} from "./bed";
import {Clinic} from "./clinic";
import {Ssk} from "./ssk";
import {ServingClinic} from "./servingclinic";
import {Message} from "./message";
import {CleaningAlternative} from "./cleaning-alternative";

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
  hasAkutPatientFeature: boolean;
  has23oFeature: boolean;
  has24oFeature: boolean;
  hasVuxenPatientFeature: boolean;
  hasSekretessFeature: boolean;
  hasInfekteradFeature: boolean;
  hasInfectionSensitiveFeature: boolean;
  hasSmittaFeature: boolean;
  cleaningAlternatives: CleaningAlternative[];
  hasCleaningFeature: boolean;
  hasPalFeature: boolean;
  hasHendelseFeature: boolean;
  hasMorRondFeature: boolean;
  hasBarnRondFeature: boolean;
  hasRondFeature: boolean;
  hasAmningFeature: boolean;

}
