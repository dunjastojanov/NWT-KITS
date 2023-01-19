import {Component, Input, OnInit} from '@angular/core';
import {Notification} from "../../interfaces/Notification";

@Component({
  selector: 'notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {
  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;
  @Input('notification') notification!: Notification;

  constructor() { }

  ngOnInit(): void {
  }

  onAccept() {
    if (this.notification.type === "DRIVER_RIDE_REQUEST") {

    }
    if (this.notification.type === "PASSENGER_RIDE_REQUEST") {

    }
    if (this.notification.type === "UPDATE_DRIVER_REQUEST") {

    }

  }

  onDeny() {
    if (this.notification.type === "DRIVER_RIDE_REQUEST") {

    }
    if (this.notification.type === "PASSENGER_RIDE_REQUEST") {

    }
    if (this.notification.type === "UPDATE_DRIVER_REQUEST") {

    }
    this.closeFunc();
  }
}
