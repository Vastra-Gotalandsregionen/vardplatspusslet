export class Patient {
  id: number;
  label: string;
  leaveStatus: LeaveStatus;
  gender: string;
}

enum LeaveStatus {
  PERMISSION, TEKNISK_PLATS
}
