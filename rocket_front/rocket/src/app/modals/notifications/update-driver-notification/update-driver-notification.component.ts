import {Component, Input, OnInit} from '@angular/core';
import {UpdateDriverNotification} from "../../../interfaces/Notification";

@Component({
  selector: 'update-driver-notification',
  templateUrl: './update-driver-notification.component.html',
  styleUrls: ['./update-driver-notification.component.css']
})
export class UpdateDriverNotificationComponent implements OnInit {

  @Input('notification') notification!: UpdateDriverNotification;
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
