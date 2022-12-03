import {Component, Input, OnInit} from '@angular/core';
import {getFullDateAndTimeString} from "../../shared/helperFunctions/dateHelperFunctions";
import {ScheduledRideNotification, UpdateDriverNotification} from "../../interfaces/Notification";

@Component({
  selector: 'notification-item',
  templateUrl: './notification-item.component.html',
  styleUrls: ['./notification-item.component.css']
})
export class NotificationItemComponent implements OnInit {

  @Input('openFunc') openFunc!: () => void;
  @Input('notification') notification!: ScheduledRideNotification | UpdateDriverNotification;

  dateString: string = "";
  open: boolean = false;
  constructor() {

  }

  ngOnInit(): void {
    this.dateString = getFullDateAndTimeString(this.notification.date);
  }


}


