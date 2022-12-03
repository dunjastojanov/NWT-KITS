import {CurrentRide} from "./Ride";
import {User} from "./User";

export enum NotificationType {
  ScheduledRide,
  UpdateDriver
}

export interface Notification {
  type: NotificationType,
  text: string;
  imageLink: string;
  date: Date;
  read: boolean;
}

export interface ScheduledRideNotification extends Notification {
  ride: CurrentRide;
}

export interface UpdateDriverNotification extends Notification {
  driver: User;
}
