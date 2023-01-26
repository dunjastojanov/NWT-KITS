import { CurrentRide, LongitudeLatitude } from 'src/app/interfaces/Ride';
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
    case CurrentRideActionType.UPDATE_VEHICLE_LOCATION:
      const longLat = <LongitudeLatitude>action.payload!;
      return {
        ...state,
        currentRide: {
          ...state.currentRide,
          vehicle: {
            ...state.currentRide?.vehicle,
            longitude: longLat.longitude,
            latitude: longLat.latitude,
          },
        },
      };
    default: {
      return state;
    }
  }
};
