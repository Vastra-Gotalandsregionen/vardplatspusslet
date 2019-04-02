import {Component, Input, OnInit} from '@angular/core';
import {Ssk} from "../../domain/ssk";

@Component({
  selector: 'app-ssk-icon',
  templateUrl: './ssk-icon.component.html',
  styleUrls: ['./ssk-icon.component.scss']
})
export class SskIconComponent implements OnInit {

  @Input('ssk') ssk: Ssk;

  constructor() { }

  ngOnInit() {
  }

}
