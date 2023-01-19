import { Action } from '@ngrx/store';
import { Destination } from 'src/app/interfaces/Destination';
import { User } from 'src/app/interfaces/User';
import { RideInfo } from 'src/app/page/ride-request-page/data-info/ride-info.type';
import { Route } from '../utils/map/map/route.type';

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

export type StoreType = {
  loggedUser: LoggedUserStateType;
  destinations: DestinationsStateType;
  rideInfo: RideInfoStateType;
};
