import { Action } from '@ngrx/store';
import { Destination } from 'src/app/interfaces/Destination';
import { Route } from '../../utils/map/map/route.type';

export enum DestinationsActionType {
  ADD = 'ADD',
  REMOVE = 'REMOVE',
  UPDATE = 'UPDATE',
  SWITCH = 'SWITCH',
  ADD_ROUTE_TIME = 'ADD_ROUTE_TIME',
  ADD_ROUTE_DISTANCE = 'ADD_ROUTE_DISTANCE',
  ADD_PRICE = 'ADD_PRICE',
  UPDATE_ROUTES = 'UPDATE_ROUTES',
  ADD_ROUTE = 'ADD_ROUTE',
  SWITCH_ALTERNATIVE = 'SWITCH_ALTERNATIVE',
  RESET = 'RESET',
}

export type UpdatePayloadType = Destination;
export type SwitchPayloadType = { firstIndex: number; secondIndex: number };
export type UpdateRoutes = [Route, Route?][];

export class DestinationsAction implements Action {
  constructor(
    readonly type: string,
    public payload:
      | string
      | number
      | UpdatePayloadType
      | SwitchPayloadType
      | UpdateRoutes
      | [Route, Route?]
  ) {}
}
