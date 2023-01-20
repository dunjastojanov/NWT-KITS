import { Action } from '@ngrx/store';
import { CurrentRide } from 'src/app/interfaces/Ride';

export enum CurrentRideActionType {
  SET = 'SET',
  REMOVE = 'REMOVE',
}

export class CurrentRideAction implements Action {
  constructor(readonly type: string, public payload?: CurrentRide) {}
}
