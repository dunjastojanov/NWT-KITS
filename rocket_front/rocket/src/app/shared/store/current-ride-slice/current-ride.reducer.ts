import { CurrentRide } from 'src/app/interfaces/Ride';
import { CurrentRideStateType } from '../types';
import {
  CurrentRideAction,
  CurrentRideActionType,
} from './current-ride.actions';

const initialState: CurrentRideStateType = {
  currentRide: null,
};

export const CurrentRideReducer = (
  state: CurrentRideStateType = initialState,
  action: CurrentRideAction
) => {
  switch (action.type) {
    case CurrentRideActionType.SET:
      return {
        ...state,
        currentRide: action.payload!,
      };

    case CurrentRideActionType.REMOVE:
      return {
        ...state,
        currentRide: null,
      };
    default: {
      return state;
    }
  }
};
