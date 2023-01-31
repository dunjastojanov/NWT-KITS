import { ActiveVehicle } from 'src/app/interfaces/Vehicle';
import { ActiveVehiclesStateType } from '../types';
import {
  ActiveVehiclesAction,
  ActiveVehiclesActionType,
} from './active-vehicles.actions';

const initialState: ActiveVehiclesStateType = {
  activeVehicles: [],
};

export const ActiveVehiclesReducer = (
  state: ActiveVehiclesStateType = initialState,
  action: ActiveVehiclesAction
) => {
  switch (action.type) {
    case ActiveVehiclesActionType.SET_ACTIVE_VEHICLES:
      return {
        ...state,
        activeVehicles: <ActiveVehicle[]>action.payload,
      };
    case ActiveVehiclesActionType.UPDATE_ACTIVE_VEHICLE:
      let newActiveVehicles = [...state.activeVehicles];
      let act: ActiveVehicle = <ActiveVehicle>action.payload!;
      let finalActiveVehicles;
      if (act.longitude < 2 || act.latitude < 2) {
        finalActiveVehicles = newActiveVehicles.filter((v) => v.id !== act.id);
      } else {
        let vehicleExists = newActiveVehicles.filter((v) => v.id === act.id);
        if (vehicleExists.length === 0) {
          finalActiveVehicles = [...newActiveVehicles, act];
        } else {
          finalActiveVehicles = newActiveVehicles.map((vehicle) =>
            vehicle.id === act.id ? { ...vehicle, ...act } : vehicle
          );
        }
      }
      return {
        ...state,
        activeVehicles: finalActiveVehicles,
      };

    default: {
      return state;
    }
  }
};
