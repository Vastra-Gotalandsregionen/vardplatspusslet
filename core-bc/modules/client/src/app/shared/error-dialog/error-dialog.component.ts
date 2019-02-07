import {Component, Inject, OnInit} from '@angular/core';
import {ErrorDialogService} from "../../service/error-dialog.service";

@Component({
  selector: 'app-error-dialog',
  templateUrl: './error-dialog.component.html',
  styleUrls: ['./error-dialog.component.scss']
})
export class ErrorDialogComponent implements OnInit {

  errorMessage: string;
  closeCallback: () => void;

  constructor() { }

  ngOnInit() {
  }

  setErrorMessage(errorMessage: string): void {
    this.errorMessage = errorMessage;
  }

  closeErrorDialog() {
    this.closeCallback();
  }

  setCloseCallback(closeCallback: () => void) {
    this.closeCallback = closeCallback;
  }

}
