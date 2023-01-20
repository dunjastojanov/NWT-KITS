import {Component, Input, OnInit} from '@angular/core';
import {Notification} from "../../../interfaces/Notification";
import {NotificationService} from "../../../services/notification/notification.service";

@Component({
  selector: 'notification-list-item',
  templateUrl: './notification-list-item.component.html',
  styleUrls: ['./notification-list-item.component.css']
})
export class NotificationListItemComponent implements OnInit {
  @Input('notification') notification!: Notification;
  showModal = false;
  constructor(private notificationService: NotificationService) { }

  ngOnInit(): void {
  }

  toggleShowModal(): void {
    this.showModal =!this.showModal;

    if (this.showModal) {
      this.notificationService.setRead(this.notification.id).then(result=> {
        console.log(result);
        this.notification.read = true;
      });
    }

  }

  getDate(): string {
    return `${this.notification.sent[2].toString().padStart(2, '0')}.${this.notification.sent[1].toString().padStart(2, '0')}.${this.notification.sent[0]}. ${this.notification.sent[3].toString().padStart(2, '0')}:${this.notification.sent[4].toString().padStart(2, '0')}`;
  }

}
