import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-link-icon',
  templateUrl: './link-icon.component.html',
  styleUrls: ['./link-icon.component.scss']
})
export class LinkIconComponent implements OnInit {

  @Input('url') url: string;
  @Input('icon') icon: string;
  @Input('label') label: string;

  constructor() { }

  ngOnInit() {
  }

}
