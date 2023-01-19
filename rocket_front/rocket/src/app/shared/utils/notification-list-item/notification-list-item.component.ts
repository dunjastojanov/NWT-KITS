import {Component, Input, OnInit} from '@angular/core';
import {Notification} from "../../../interfaces/Notification";

@Component({
  selector: 'notification-list-item',
  templateUrl: './notification-list-item.component.html',
  styleUrls: ['./notification-list-item.component.css']
})
export class NotificationListItemComponent implements OnInit {
  @Input('notification') notification!: Notification;
  showModal = false;
  constructor() { }

  ngOnInit(): void {
  }

  toggleShowModal(): void {
    this.showModal =!this.showModal;
    console.log(this.showModal);
  }

}
