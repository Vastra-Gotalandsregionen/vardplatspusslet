export class Patient {
  id: number;
  label: string;
  leaveStatus: LeaveStatus;
  gender: string;
  leftDate: Date;
  plannedLeaveDate: Date;
}

enum LeaveStatus {
  PERMISSION, TEKNISK_PLATS
}
