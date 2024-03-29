import { Action } from '@ngrx/store';
import { Notif } from 'src/app/interfaces/Notification';

export enum NotificationsActionType {
  SET_NOTIFICATIONS = 'SET_NOTIFICATIONS',
  SET_NOTIFICATION_TRUE = 'SET_NOTIFICATION_TRUE',
}

export class NotificationsAction implements Action {
  constructor(readonly type: string, public payload: Notif | Notif[]) {}
}
