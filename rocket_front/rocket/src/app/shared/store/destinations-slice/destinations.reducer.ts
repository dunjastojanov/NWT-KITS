import { Destination } from "src/app/interfaces/Destination";
import { DestinationsStateType } from "../types";
import { DestinationsAction, DestinationsActionType, UpdatePayloadType } from "./destinations.actions";

const initialState: DestinationsStateType = {
    destinations: [
        {index: 0, address:''},
        {index: 1, address:''}
    ],
   
}

export const destinationsReducer = (state: DestinationsStateType = initialState, action: DestinationsAction) => {
    switch (action.type) {
        case DestinationsActionType.ADD:
            let newStateAdd = [...state.destinations]
            let destination: Destination = {index: newStateAdd.length, address:<string>action.payload}
            newStateAdd.push(destination)
            return {
                ...state,
                destinations: newStateAdd
            };

        case DestinationsActionType.REMOVE:
            if (state.destinations.length < 3)
                return state;
            let newStateRemove = [...state.destinations]
            newStateRemove.splice(<number>action.payload, 1);
            let ind = 0;
            let finalStateRemove = newStateRemove.map(dest => {return {...dest, index: ind++}})
          
            return {
                ...state,
                destinations: finalStateRemove
            };
        case DestinationsActionType.UPDATE:
            let newStateUpdate = [...state.destinations]
            let act: UpdatePayloadType = <UpdatePayloadType>action.payload
            let finalStateUpdate = newStateUpdate.map(dest => act.index === dest.index ? {...dest, address:act.address} : dest)
            return {
                ...state,
                destinations: finalStateUpdate
            };
        default: {
            return state;
        }
    }
}