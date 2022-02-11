import {Patientexamination} from "./patientexamination";
import {PatientEvent} from "./patient-event";
import {CareBurdenChoice} from "./careburdenchoice";
import {DietForMother} from "./dietformother";
import {DietForChild} from "./dietforchild";
import {DietForPatient} from "./dietforpatient";
import {ServingClinic} from "./servingclinic";
import { Mothersdiet } from "./mothersdiet";
import { Childrensdiet } from "./childrensdiet";
import { Diet } from "./diet";

export class Patient {

  constructor() {
    this.patientEvents = [];
    this.patientExaminations = [];
    this.careBurdenChoices = [];
  }

  id: number;
  label: string;
  leaveStatus: LeaveStatus;
  gender: string;
  leftDate: Date;
  plannedLeaveDate: Date;
  carePlan: string;
  interpreter: boolean;
  interpretInfo: string;
  interpretDate: Date;
  patientExaminations: Patientexamination[];
  akutPatient: boolean;
  electiv23O: boolean;
  electiv24O: boolean;
  fiktivPlats: boolean;
  vuxenPatient: boolean;
  /*pal: string;*/
  patientEvents: PatientEvent[];
  morRond: boolean;
  barnRond: boolean;
  rond: boolean;
  amning: string;
  information: string;
  kommentar: string;
  careBurdenChoices: CareBurdenChoice[];
  relatedInformation: string;
  fromClinic: ServingClinic;
  specialDietChild : boolean;
  specialDietMother : boolean;
  specialDiet : boolean;
  mothersDiet: Mothersdiet;
  childrensDiet: Childrensdiet;
  allergier: string;
  detailedDiet: Diet;
}

enum LeaveStatus {
  PERMISSION, TEKNISK_PLATS
}
