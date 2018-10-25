import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Message} from "../../domain/message";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-edit-message',
  templateUrl: './edit-message.component.html',
  styleUrls: ['./edit-message.component.scss']
})
export class EditMessageComponent implements OnInit {

  @Input('message') message: Message;

  @Output('save') saveEvent: EventEmitter<Message> = new EventEmitter<Message>();
  @Output('cancel') cancelEvent: EventEmitter<void> = new EventEmitter<void>();

  allWeekdays = [
    {displayName: 'Måndag', value: 'MONDAY'},
    {displayName: 'Tisdag', value: 'TUESDAY'},
    {displayName: 'Onsdag', value: 'WEDNESDAY'},
    {displayName: 'Torsdag', value: 'THURSDAY'},
    {displayName: 'Fredag', value: 'FRIDAY'},
    {displayName: 'Lördag', value: 'SATURDAY'},
    {displayName: 'Söndag', value: 'SUNDAY'}
    ];

  constructor(private http: HttpClient) { }

  ngOnInit() {
  }

  save() {
    this.http.put('/api/message', this.message)
      .subscribe(() => {
        this.saveEvent.emit(this.message);
      });
  }

  cancel() {
    this.cancelEvent.emit();
  }

  toDate(): Date {
    if (!this.message.date) {
      return null;
    }
    let d: Date;
    d = new Date(this.message.date);
    return d;
  }
}
