import { CurrentRide, LongitudeLatitude } from 'src/app/interfaces/Ride';
import { CurrentRideStateType } from '../types';
import {
  CurrentRideAction,
  CurrentRideActionType,
} from './current-ride.actions';

const initialState: CurrentRideStateType = {
  currentRide: null,
  timeForVehicleToArive: 0,
};

export const CurrentRideReducer = (
  state: CurrentRideStateType = initialState,
  action: CurrentRideAction
) => {
  switch (action.type) {
    case CurrentRideActionType.SET:
      return {
        ...state,
        currentRide: <CurrentRide>action.payload!,
      };

    case CurrentRideActionType.REMOVE:
      return {
        ...state,
        currentRide: null,
      };
    case CurrentRideActionType.SET_TIME_FOR_VEHICLE_TO_COME:
      return {
        ...state,
        timeForVehicleToArive: <number>action.payload!,
      };
    case CurrentRideActionType.UPDATE_VEHICLE_LOCATION:
      const longLat = <LongitudeLatitude>action.payload!;
      return {
        ...state,
        currentRide: {
          ...state.currentRide!,
          vehicle: {
            type: state.currentRide!.vehicle!.type,
            longitude: <number>longLat.longitude,
            latitude: <number>longLat.latitude,
          },
        },
      };
    default: {
      return state;
    }
  }
};
