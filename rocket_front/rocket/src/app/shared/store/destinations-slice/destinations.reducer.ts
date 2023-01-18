import {Destination} from 'src/app/interfaces/Destination';
import {DestinationsStateType} from '../types';
import {
  DestinationsAction,
  DestinationsActionType,
  SwitchPayloadType,
  UpdatePayloadType,
} from './destinations.actions';

const initialState: DestinationsStateType = {
  destinations: [
    {index: 0, address: ''},
    {index: 1, address: ''},
  ],
  estimated_route_distance: 0,
  estimated_route_time: 0,
};

export const destinationsReducer = (
  state: DestinationsStateType = initialState,
  action: DestinationsAction
) => {
  switch (action.type) {
    case DestinationsActionType.ADD:
      let newStateAdd = [...state.destinations];
      let destination: Destination = {
        index: newStateAdd.length,
        address: <string>action.payload,
      };
      newStateAdd.push(destination);
      return {
        ...state,
        destinations: newStateAdd,
      };

    case DestinationsActionType.REMOVE:
      if (state.destinations.length < 3) return state;
      let newStateRemove = [...state.destinations];
      newStateRemove.splice(<number>action.payload, 1);
      let ind = 0;
      let finalStateRemove = newStateRemove.map((dest) => {
        return {...dest, index: ind++};
      });

      return {
        ...state,
        destinations: finalStateRemove,
      };
    case DestinationsActionType.UPDATE:
      let newStateUpdate = [...state.destinations];
      let act: UpdatePayloadType = <UpdatePayloadType>action.payload;
      let finalStateUpdate = newStateUpdate.map((dest) =>
        act.index === dest.index ? {...dest, address: act.address} : dest
      );
      return {
        ...state,
        destinations: finalStateUpdate,
      };
    case DestinationsActionType.SWITCH:
      let newStateSwitch = [...state.destinations];
      let {firstIndex, secondIndex}: SwitchPayloadType = <SwitchPayloadType>(
        action.payload
      );
      const finalStateSwitch = newStateSwitch.map((element, index) => {
        if (index === firstIndex) {
          let item = newStateSwitch[secondIndex];
          return {index: firstIndex, address: item.address};
        }
        if (index === secondIndex) {
          let item = newStateSwitch[firstIndex];
          return {index: secondIndex, address: item.address};
        }
        return element;
      });
      return {
        ...state,
        destinations: finalStateSwitch,
      };
    case DestinationsActionType.ADD_ROUTE_DISTANCE:
      return {
        ...state,
        estimated_route_distance: <number>action.payload,
      };
    case DestinationsActionType.ADD_ROUTE_TIME:
      return {
        ...state,
        estimated_route_time: <number>action.payload,
      };
    default: {
      return state;
    }
  }
};
