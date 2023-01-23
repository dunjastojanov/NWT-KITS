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

    default: {
      return state;
    }
  }
};
