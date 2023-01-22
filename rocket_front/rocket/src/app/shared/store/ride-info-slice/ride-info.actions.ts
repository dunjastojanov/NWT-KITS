import { Action } from '@ngrx/store';
import { sideUser } from 'src/app/interfaces/User';
import { VehicleType } from 'src/app/interfaces/VehicleType';
import { RideInfo } from 'src/app/page/ride-request-page/data-info/ride-info.type';

export enum RideInfoActionType {
  UPDATE_IS_NOW = 'UPDATE_IS_NOW',
  UPDATE_TIME = 'UPDATE_TIME',
  UPDATE_FRIENDS = 'UPDATE_FRIENDS',
  UPDATE_SPLIT = 'UPDATE_SPLIT',
  UPDATE_FEATURES = 'UPDATE_FEATURES',
  UPDATE_VEHICLE_TYPE = 'UPDATE_VEHICLE_TYPE',
}

export class RideInfoAction implements Action {
  constructor(
    public readonly type: string,
    public payload: string | boolean | string[] | VehicleType | sideUser[]
  ) {}
}
