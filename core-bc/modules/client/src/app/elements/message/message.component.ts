import {Component, Input, OnInit} from '@angular/core';
import {Message} from "../../domain/message";

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss']
})
export class MessageComponent implements OnInit {

  @Input('message') message: Message;

  dayOfWeekLabel;

  allDaysOfWeekMap = new Map<string, string>([
    ['MONDAY', 'Måndag'],
    ['TUESDAY', 'Tisdag'],
    ['WEDNESDAY', 'Onsdag'],
    ['THURSDAY', 'Torsdag'],
    ['FRIDAY', 'Fredag'],
    ['SATURDAY', 'Lördag'],
    ['SUNDAY', 'Söndag'],
  ]);

  constructor() {
  }

  ngOnInit() {
    this.dayOfWeekLabel = this.allDaysOfWeekMap.get(this.message.dayOfWeek);
  }

}
