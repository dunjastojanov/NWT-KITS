import {Destination} from 'src/app/interfaces/Destination';
import {Notif} from 'src/app/interfaces/Notification';
import {CurrentRide} from 'src/app/interfaces/Ride';
import {User} from 'src/app/interfaces/User';
import {RideInfo} from 'src/app/page/ride-request-page/data-info/ride-info.type';
import {Route} from '../utils/map/map/route.type';
import {MessageInfo} from "../../interfaces/MessageInfo";

export type LoggedUserStateType = {
  user: User | null;
};

export type DestinationsStateType = {
  destinations: Destination[];
  estimated_route_time: number;
  estimated_route_distance: number;
  estimated_price: number;
  routes: [Route, Route?][];
};

export type RideInfoStateType = {
  ride: RideInfo;
};

export type CurrentRideStateType = {
  currentRide: CurrentRide | null;
};

export type NotificationsStateType = {
  notifications: Notif[];
};

export type MessageStateType = {
  messages: MessageInfo[];
};

export type StoreType = {
  loggedUser: LoggedUserStateType;
  destinations: DestinationsStateType;
  rideInfo: RideInfoStateType;
  currentRide: CurrentRideStateType;
  notifications: NotificationsStateType;
  messages: MessageStateType;

};
