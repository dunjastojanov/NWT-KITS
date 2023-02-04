import { Action } from '@ngrx/store';
import { ActiveVehicle } from '../../../interfaces/Vehicle';

export enum ActiveVehiclesActionType {
  SET_ACTIVE_VEHICLES = 'SET_ACTIVE_VEHICLES',
  UPDATE_ACTIVE_VEHICLE = 'UPDATE_ACTIVE_VEHICLE',
}

export class ActiveVehiclesAction implements Action {
  constructor(
    readonly type: string,
    public payload: ActiveVehicle[] | ActiveVehicle
  ) {}
}
