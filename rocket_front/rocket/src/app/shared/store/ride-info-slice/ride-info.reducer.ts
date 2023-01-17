import { RideInfo } from 'src/app/page/ride-request-page/data-info/ride-info.type';
import { RideInfoStateType } from '../types';
import { RideInfoAction, RideInfoActionType } from './ride-info.actions';

const initialState: RideInfoStateType = {
  ride: new RideInfo(),
};

export const RideInfoReducer = (
  state: RideInfoStateType = initialState,
  action: RideInfoAction
) => {
  switch (action.type) {
    case RideInfoActionType.UPDATE_IS_NOW:
      return {
        ...state,
        ride: {
          ...state.ride,
          isNow: <boolean>action.payload,
        },
      };
    case RideInfoActionType.UPDATE_TIME:
      return {
        ...state,
        ride: {
          ...state.ride,
          time: <string>action.payload,
        },
      };
    case RideInfoActionType.UPDATE_FRIENDS:
      return {
        ...state,
        ride: {
          ...state.ride,
          friends: <string[]>action.payload,
        },
      };
    case RideInfoActionType.UPDATE_SPLIT:
      return {
        ...state,
        ride: {
          ...state.ride,
          split: <boolean>action.payload,
        },
      };
    case RideInfoActionType.UPDATE_FEATURES:
      return {
        ...state,
        ride: {
          ...state.ride,
          features: <string[]>action.payload,
        },
      };
    case RideInfoActionType.UPDATE_VEHICLE_TYPE:
      return {
        ...state,
        ride: {
          ...state.ride,
          vehicle: <string>action.payload,
        },
      };
    default: {
      return state;
    }
  }
};
