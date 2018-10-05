export class Patient {
  id: number;
  label: string;
  leaveStatus: LeaveStatus;
  gender: string;
  leftDate: Date;
}

enum LeaveStatus {
  PERMISSION, TEKNISK_PLATS
}
