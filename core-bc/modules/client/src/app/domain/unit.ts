import {Patient} from "./patient";
import {Bed} from "./bed";
import {Clinic} from "./clinic";
import {Ssk} from "./ssk";
import {ServingClinic} from "./servingclinic";
import {Message} from "./message";
import {CleaningAlternative} from "./cleaning-alternative";
import {CareBurdenValue} from "./careburdenvalue";
import {CareBurdenCategory} from "./careBurdenCategory";
import {DietForMother} from "./dietformother";
import {DietForChild} from "./dietforchild";
import {DietForPatient} from "./dietforpatient";
import {UnitPlannedIn} from "./unit-planned-in";
import {SevenDaysPlaningUnit} from "./seven-days-planing-unit";
import { AllowedBedName } from "./allowedBedName";

export class Unit {
  id: string;
  name: string;
  beds: Bed[];
  clinic: Clinic;
  patients: Patient[];
  ssks: Ssk[];
  messages: Message[];
  servingClinics: ServingClinic[];
  /*dietForMothers: DietForMother[];
  dietForChildren: DietForChild[];
  dietForPatients: DietForPatient[];*/
  hasLeftDateFeature: boolean;
  hasAkutPatientFeature: boolean;
  has23oFeature: boolean;
  has24oFeature: boolean;
  hasVuxenPatientFeature: boolean;
  cleaningAlternatives: CleaningAlternative[];
  hasCleaningFeature: boolean;
 /* hasPalFeature: boolean;*/
  hasHendelseFeature: boolean;
  hasMorRondFeature: boolean;
  hasBarnRondFeature: boolean;
  hasRondFeature: boolean;
  hasAmningFeature: boolean;
  careBurdenCategories: CareBurdenCategory[];
  careBurdenValues: CareBurdenValue[];
  hasMorKostFeature: boolean;
  hasBarnKostFeature: boolean;
  hasKostFeature: boolean;
  hasMotherChildDietFeature: boolean;
  hasUnitPlannedInFeature: boolean;
  unitsPlannedIn: UnitPlannedIn[];
  sevenDaysPlaningUnits: SevenDaysPlaningUnit[];
  careBurden: string;
  // hasPatientNameColumn: boolean;
  hasPatientFromClinicFeature: boolean;
  hasGenderFeature: boolean;
  hasPatientWaitsFeature: boolean;
  hasBackToHomeAlternativFeature: boolean;
  hasDatedBackHomeFeature: boolean;
  allowedBedNames: AllowedBedName[];
  resetSskOnHasLeft: boolean;
  hasDetailedDietFeature: boolean;
}
