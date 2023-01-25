import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { AxiosResponse } from 'axios';
import { Notif } from 'src/app/interfaces/Notification';
import {
  NotificationsAction,
  NotificationsActionType,
} from 'src/app/shared/store/notifications-slice/notifications.actions';

import { StoreType } from 'src/app/shared/store/types';
import { http } from '../../shared/api/axios-wrapper';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  constructor(private store: Store<StoreType>) {}

  async getNotification(): Promise<any> {
    let result: AxiosResponse = await http.get('/api/notification');
    return result.data;
  }

  async setRead(id: string) {
    let result: AxiosResponse = await http.put('/api/notification/' + id);
    return result.data;
  }

  async loadNotifications() {
    const notifications: Notif[] = await this.getNotification();
    this.store.dispatch(
      new NotificationsAction(
        NotificationsActionType.SET_NOTIFICATIONS,
        notifications
      )
    );
  }
}
