import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ModalService} from "vgr-komponentkartan";

@Component({
  selector: 'app-delete-modal',
  templateUrl: './delete-modal.component.html',
  styleUrls: ['./delete-modal.component.scss']
})
export class DeleteModalComponent implements OnInit {

  @Input('itemLabel') label: string;

  @Output() confirmDelete = new EventEmitter();

  constructor(public modalService: ModalService) { }

  ngOnInit() {
  }

  open() {
    this.modalService.openDialog('deleteModal');
  }

  confirm() {
    this.modalService.closeDialog('deleteModal');
    this.confirmDelete.emit();
  }

}
