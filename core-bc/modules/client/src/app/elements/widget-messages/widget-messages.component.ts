import {Component, Input, OnInit} from '@angular/core';
import {Message} from "../../domain/message";

@Component({
  selector: 'app-widget-messages',
  templateUrl: './widget-messages.component.html',
  styleUrls: ['./widget-messages.component.scss']
})
export class WidgetMessagesComponent implements OnInit {

  @Input('messages') messages: Message[];
  @Input('hasEditUnitPermission') hasEditUnitPermission: boolean;

  constructor() {
  }

  ngOnInit() {
  }

}
