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
}

enum LeaveStatus {
  PERMISSION, TEKNISK_PLATS
}
