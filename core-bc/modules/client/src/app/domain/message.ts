import {Unit} from "./unit";

export class Message {
  id: number;
  heading: string;
  text: string;
  date: Date;
  dayOfWeek: string;
  unit: Unit;
  pinned: boolean;
}
