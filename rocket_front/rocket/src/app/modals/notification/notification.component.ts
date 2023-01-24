import { Component, Input, OnInit } from '@angular/core';
import { Notif } from '../../interfaces/Notification';

@Component({
  selector: 'notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css'],
})
export class NotificationComponent implements OnInit {
  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;
  @Input('notification') notification!: Notif;

  showReviewModal = false;

  toggleReviewModal() {
    this.showReviewModal =!this.showReviewModal;
  }

  constructor() { }

  ngOnInit(): void {}

  isBasic(type: string) {
    return type === "RIDE_CANCELED" || type === "RIDE_CONFIRMED" || type === "RIDE_SCHEDULED" || type === "USER_BLOCKED";
  }

  onAccept() {
    if (this.notification.type === 'DRIVER_RIDE_REQUEST') {
      //this.notification ima ride id koji se zove resourceId i koji user je prihvation/odbio i zove se userId
    }
    if (this.notification.type === 'PASSENGER_RIDE_REQUEST') {
    }
    if (this.notification.type === 'UPDATE_DRIVER_REQUEST') {
    }
    if (this.notification.type === "RIDE_REVIEW") {
    }
  }

  onDeny() {
    if (this.notification.type === 'DRIVER_RIDE_REQUEST') {
    }
    if (this.notification.type === 'PASSENGER_RIDE_REQUEST') {
    }
    if (this.notification.type === 'UPDATE_DRIVER_REQUEST') {
    }
    this.closeFunc();
  }
}
