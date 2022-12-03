import {Component, Input, OnInit} from '@angular/core';
import {ScheduledRideNotification} from "../../../interfaces/Notification";

@Component({
  selector: 'scheduled-ride-notification',
  templateUrl: './scheduled-ride-notification.component.html',
  styleUrls: ['./scheduled-ride-notification.component.css']
})
export class ScheduledRideNotificationComponent implements OnInit {
  @Input('notification') notification!: ScheduledRideNotification;
  open: boolean = false;

  constructor() {
  }

  ngOnInit(): void {
  }

  toggleOpen = () => {
    this.open = !this.open;
    this.notification.read = true;
  }

}
