import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Notif } from '../../../interfaces/Notification';
import { NotificationService } from '../../../services/notification/notification.service';
import {
  NotificationsAction,
  NotificationsActionType,
} from '../../store/notifications-slice/notifications.actions';
import { StoreType } from '../../store/types';

@Component({
  selector: 'notification-list-item',
  templateUrl: './notification-list-item.component.html',
  styleUrls: ['./notification-list-item.component.css'],
})
export class NotificationListItemComponent implements OnInit {
  @Input('notification') notification!: Notif;
  showModal = false;
  constructor(
    private notificationService: NotificationService,
    private store: Store<StoreType>
  ) {}

  ngOnInit(): void {}

  toggleShowModal(): void {
    this.showModal = !this.showModal;

    if (this.showModal) {
      this.notificationService.setRead(this.notification.id).then((result) => {
        //this.notification.read = true;
        /*this.store.dispatch(
          new NotificationsAction(
            NotificationsActionType.SET_NOTIFICATION_TRUE,
            this.notification
          )
        );*/
      });
    }
  }

  getDate(): string {
    if (this.notification.sent instanceof Date) {
      const convertedDate = this.notification.sent as Date;
      return `${convertedDate.getFullYear()}-${
        convertedDate.getMonth() + 1
      }-${convertedDate.getDate()} ${convertedDate.getHours()}:${convertedDate.getMinutes()}`;
    } else {
      return `${this.notification.sent[2]
        .toString()
        .padStart(2, '0')}.${this.notification.sent[1]
        .toString()
        .padStart(2, '0')}.${
        this.notification.sent[0]
      }. ${this.notification.sent[3]
        .toString()
        .padStart(2, '0')}:${this.notification.sent[4]
        .toString()
        .padStart(2, '0')}`;
    }
  }
}
