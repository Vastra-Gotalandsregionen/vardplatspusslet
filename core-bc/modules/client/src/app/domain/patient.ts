import {Patientexamination} from "./patientexamination";

export class Patient {
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
  vuxenPatient: boolean;
  sekretess: boolean;
  sekretessInfo: string;
}

enum LeaveStatus {
  PERMISSION, TEKNISK_PLATS
}
