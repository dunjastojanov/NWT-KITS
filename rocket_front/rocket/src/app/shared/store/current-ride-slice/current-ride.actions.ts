import { Action } from '@ngrx/store';
import { CurrentRide, LongitudeLatitude } from 'src/app/interfaces/Ride';

export enum CurrentRideActionType {
  SET = 'SET',
  REMOVE = 'REMOVE',
  UPDATE_VEHICLE_LOCATION = 'UPDATE_VEHICLE_LOCATION',
}

export class CurrentRideAction implements Action {
  constructor(
    readonly type: string,
    public payload?: CurrentRide | LongitudeLatitude
  ) {}
}
