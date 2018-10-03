export class Patient {
  id: number;
  label: string;
  leaveStatus: LeaveStatus;
}

enum LeaveStatus {
  PERMISSION, TEKNISK_PLATS
}
