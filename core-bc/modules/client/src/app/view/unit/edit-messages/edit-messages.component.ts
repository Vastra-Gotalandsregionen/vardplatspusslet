import {Component, OnInit} from '@angular/core';
import {Clinic} from "../../../domain/clinic";
import {Unit} from "../../../domain/unit";
import {HttpClient} from "@angular/common/http";
import {FormBuilder} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {Message} from "../../../domain/message";

@Component({
  selector: 'app-edit-messages',
  templateUrl: './edit-messages.component.html',
  styleUrls: ['./edit-messages.component.scss']
})
export class EditMessagesComponent implements OnInit {

  unit: Unit;
  clinic: Clinic;

  messages: Message[] = [];
  editMessages: Message[] = [];

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {

      let clinicId = params.clinicId;
      let unitId = params.id;

      this.http.get<Clinic>('/api/clinic/' + clinicId).subscribe(clinic => {
        this.clinic = clinic;
      });

      this.http.get<Unit>('/api/unit/' + clinicId + '/' + params.id).subscribe(unit => {
        if (unit) {
          this.unit = unit;
        }
      });

      this.http.get<Message[]>('/api/message/' + unitId).subscribe(messages => {
        this.messages = messages;
      });

    });
  }

  editMessage(message: Message) {
    // Unit is lazily fetched and not set initially. So we set it here.
    message.unit = this.unit;

    if (this.editMessages.indexOf(message) === -1) {
      this.editMessages.push(message);
    }
  }

  delete(message: Message) {
    this.http.delete('/api/message/' + message.id).subscribe(() => this.ngOnInit());
  }

  save(message: Message) {
    this.ngOnInit();
    this.editMessages.splice(this.editMessages.indexOf(message), 1);
  }

  addNewMessage() {
    let newMessage = new Message();
    newMessage.unit = this.unit;
    this.messages.push(newMessage);
    this.editMessages.push(newMessage);
  }
}
