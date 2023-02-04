import { Notif } from 'src/app/interfaces/Notification';
import { NotificationsStateType } from '../types';
import {
  NotificationsAction,
  NotificationsActionType,
} from './notifications.actions';

const initialState: NotificationsStateType = {
  notifications: [],
};

export const NotificationsReducer = (
  state: NotificationsStateType = initialState,
  action: NotificationsAction
) => {
  switch (action.type) {
    case NotificationsActionType.SET_NOTIFICATIONS:
      return {
        ...state,
        notifications: <Notif[]>action.payload!,
      };
    case NotificationsActionType.SET_NOTIFICATION_TRUE:
      let notifs = [...state.notifications];
      let updateNotif = <Notif>action.payload;
      let finalNotifs = notifs.map((notif) =>
        notif.id === updateNotif.id ? { ...notif, read: true } : notif
      );
      return {
        ...state,
        notifications: finalNotifs,
      };

    default: {
      return state;
    }
  }
};
